/*
 * Copyright (c) 2014, Inversoft Inc., All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package org.savantbuild.dep.maven;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.savantbuild.dep.domain.Artifact;
import org.savantbuild.dep.domain.Dependencies;
import org.savantbuild.dep.domain.DependencyGroup;
import org.savantbuild.dep.domain.ReifiedArtifact;

/**
 * Maven artifact.
 *
 * @author Brian Pontarelli
 */
public class MavenArtifact {
  public String classifier;

  public List<MavenArtifact> dependencies = new ArrayList<>();

  public List<MavenArtifact> dependencyTemplates = new ArrayList<>();

  public String group;

  public String id;

  public String optional;

  public ReifiedArtifact savantArtifact;

  public String scope;

  public String type;

  public String version;

  public MavenArtifact() {
  }

  public MavenArtifact(String group, String id, String version) {
    this.group = group;
    this.id = id;
    this.version = version;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof MavenArtifact)) return false;
    final MavenArtifact that = (MavenArtifact) o;
    return Objects.equals(group, that.group) &&
        Objects.equals(id, that.id) &&
        Objects.equals(type, that.type) &&
        Objects.equals(version, that.version) &&
        Objects.equals(classifier, that.classifier);
  }

  public String getArtifactName() {
    return id + (StringUtils.isNotBlank(classifier) ? "-" + classifier : "");
  }

  public String getMainFile() {
    return id + "-" + version + (StringUtils.isNotBlank(classifier) ? "-" + classifier : "") + "." + (type == null ? "jar" : type);
  }

  public String getPOM() {
    return id + "-" + version + ".pom";
  }

  public Dependencies getSavantDependencies() {
    Dependencies savantDependencies = new Dependencies();
    dependencies.forEach((dependency) -> {
      String groupName = dependency.scope;
      DependencyGroup savantDependencyGroup = savantDependencies.groups.get(groupName);
      if (savantDependencyGroup == null) {
        savantDependencyGroup = new DependencyGroup(groupName, true);
        savantDependencies.groups.put(groupName, savantDependencyGroup);
      }

      savantDependencyGroup.dependencies.add(new Artifact(dependency.savantArtifact.id, dependency.savantArtifact.version, false));
    });

    return savantDependencies;
  }

  public String getSourceFile() {
    return id + "-" + version + (StringUtils.isNotBlank(classifier) ? "-" + classifier : "") + "-sources." + (type == null ? "jar" : type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(group, id, type, version, classifier);
  }

  public String toString() {
    if (StringUtils.isNotBlank(classifier)) {
      return group + ":" + id + ":" + id + "-" + classifier + ":" + version + ":" + (type == null ? "jar" : type);
    }
    return group + ":" + id + ":" + version + ":" + (type == null ? "jar" : type);
  }
}
