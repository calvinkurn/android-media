package com.tokopedia.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashSet

open class ScanProjectTask : DefaultTask() {

    var latestReleaseDate: Date = Date()
    val candidateModuleListToUpdate = hashSetOf<String>()
    val dependenciesHashSet = HashSet<Pair<String, String>>()
    val dateFormatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    val versionProjectToArtifactList = hashMapOf<String, VersionModelB>()
    val versionArtifactToProjectList = hashMapOf<String, String>()
    var versionConfigMap = mutableMapOf<String, Int>()

    companion object {
        const val TOKOPEDIA = "tokopedia"
        const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
        const val LIBRARIES_PATH = "../../buildconfig/dependencies/dependency-libraries.gradle"
    }

    @TaskAction
    fun run() {

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

        // this is to update all possible candidate version increase
        // for example, the change of global config will also
        // increase the version of the project that dependent on it
        calibratingCandidateList(dependenciesHashSetTemp)

        // artifact version calibrating
        // dependency-libraries version and version defined in project extension might be different
        // if it is different, take the maximum between those 2
        calibratingVersion()
    }

    // this function is get the correct current version between
    // 1. version in dependencies-libraries, or
    // 2. version in project.ext for each module
    // and take the maximum between 2
    private fun calibratingVersion(){
        try {
            val file = File(LIBRARIES_PATH)
            file.forEachLine {
                val str = it.trim().replace("\\s".toRegex(), "")
                if (!str.startsWith("//")) {
                    val strSplit = str.split(":".toPattern(), 2)
                    if (strSplit.size == 2) {
                        val strProject = strSplit[1].substring(strSplit[1].indexOf("\""),strSplit[1].lastIndexOf("\""))
                        val projectSplit = strProject.split(":".toPattern(), 3)
                        if (projectSplit.size == 3 && projectSplit[0].contains(TOKOPEDIA)){
                            val artifactId = projectSplit[1]
                            val version = projectSplit[2]
                            versionProjectToArtifactList.forEach { artifactItem ->
//                                if (artifactItem.value.artifactId == artifactId) {
//                                    artifactItem.value.versionName = if (version.toIntegerTruncated() > )
//                                }
                            }
                            println("$artifactId $version")
                        }
                    }
                }
            }
        } catch (ignored:Exception){
            // do nothing, assumed no calibration for version.
        }
    }

    // dependencies hash set is collection of dependencies [graphql-config;network-config;...]
    // this function is to look up for the hashset to update the candidate list
    // for example, the change of global config will also
    // increase the version of the project that dependent on it
    private fun calibratingCandidateList(dependenciesHashSetTemp: HashSet<Pair<String, String>>){
        val cloneDepHashSet: HashSet<Pair<String, String>> = hashSetOf()
        dependenciesHashSetTemp.forEach {
            val projectDependant = versionArtifactToProjectList[it.second] ?: ""
            dependenciesHashSet.add(it.first to projectDependant)
            cloneDepHashSet.add(it.first to projectDependant)
        }

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