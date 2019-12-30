package com.tokopedia.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashSet

open class VersionTask : DefaultTask() {

    var latestReleaseDateString: String = ""
    val candidateModuleListToUpdate = hashSetOf<String>()
    val dependenciesHashSet = HashSet<Pair<String, String>>()
    val dateFormatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    val versionProjectToArtifactList = hashMapOf<String, VersionModelB>()
    val versionArtifactToProjectList = hashMapOf<String, String>()

    lateinit var latestReleaseDate:Date

    companion object {
        const val TOKOPEDIA = "tokopedia"
        const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
    }

    @TaskAction
    fun run() {
        latestReleaseDate = dateFormatter.parse(latestReleaseDateString)

        val flattenedAllSubProjects = mutableListOf<Project>()
        val queue = mutableListOf<Project>(project)

        val dependenciesHashSetTemp = HashSet<Pair<String, String>>()

        //BFS to get all projects
        while (queue.isNotEmpty()) {
            val projectItem = queue.removeAt(0)
            queue.addAll(projectItem.childProjects.values)

            if (projectItem == project) {
                // this is to ignore the root project
                continue
            }

            flattenedAllSubProjects.add(projectItem)

            val projectName = projectItem.name
            println(projectName)

            val artifactId = projectItem.properties["artifactId"].toString()
            if (artifactId.isNotEmpty()) {
                val groupId = projectItem.properties["groupId"].toString()
                val artifactName = projectItem.properties["artifactName"].toString()
                val versionName = projectItem.properties["versionName"].toString()
                versionProjectToArtifactList[projectName] = VersionModelB(projectName, groupId, artifactId, artifactName, versionName)
                versionArtifactToProjectList[artifactId] = projectName
            }

            projectItem.configurations.all { conf ->
                conf.dependencies.forEach {
                    // this is to make hashCode of combination of libraries and those version
                    // If there are version changes in the libraries, the module will become a candidate for version increase.
                    val depGroup = it.group
                    if (depGroup?.contains(TOKOPEDIA) == true) {
                        val artifactName = it.name
                        // create list tree
                        dependenciesHashSetTemp.add(projectName to artifactName)
                    }
                }
            }

            // check candidate for version update
            val moduleHasNewChanges = moduleHasNewChanges(projectName)
            if (moduleHasNewChanges) {
                candidateModuleListToUpdate.add(projectName)
            }
        }

        val cloneDepHashSet: HashSet<Pair<String, String>> = hashSetOf()
        dependenciesHashSetTemp.forEach {
            val projectDependant = versionArtifactToProjectList[it.second] ?: ""
            dependenciesHashSet.add(it.first to projectDependant)
            cloneDepHashSet.add(it.first to projectDependant)
        }

        // this is to update all possible candidate version increase
        // for example, the change of global config will also
        // increase the version of the project that dependent on it
        var isDepCandidateChanged = true
        while (isDepCandidateChanged && cloneDepHashSet.isNotEmpty()) {
            isDepCandidateChanged = false
            val toRemoveList : MutableList<Pair<String, String>> = mutableListOf()
            cloneDepHashSet.forEach {
                val projectName = it.first
                val dependencyProjectName = it.second
                if (candidateModuleListToUpdate.contains(projectName)) {
                    // this is to efficiency. No need to check if it already candidate (for next loop)
                    toRemoveList.add(Pair(projectName,dependencyProjectName))
                } else if (candidateModuleListToUpdate.contains(dependencyProjectName)){
                    println("Add Candidate to Update $projectName")
                    candidateModuleListToUpdate.add(projectName)
                    isDepCandidateChanged = true
                }
            }
            toRemoveList.forEach {
                cloneDepHashSet.remove(it)
            }
        }
    }

    private fun moduleHasNewChanges(module: String): Boolean {
        val gitLog = "git log -1 --pretty=\'%ad\' --date=format:\'%Y-%m-%d,%H:%M:%S\' $module"
        val moduleLatestChangeDateString = gitLog.runCommand(project.projectDir.absoluteFile)?.trimSpecial() ?: ""
        val moduleLatestChangeDate = dateFormatter.parse(moduleLatestChangeDateString)
        if (moduleLatestChangeDate > latestReleaseDate) {
            println("$module latest date $moduleLatestChangeDateString")
            return true
        } else {
            println("$module already uptodate")
        }
        return false
    }
}