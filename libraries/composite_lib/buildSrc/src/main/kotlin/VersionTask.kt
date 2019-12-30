package com.tokopedia.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashSet

open class VersionTask : DefaultTask() {

    var latestReleaseDateString: String = ""
    val candidateModuleListToUpdate = mutableListOf<String>()
    val dependenciesHashSet = HashSet<Pair<String, String>>()
    val dateFormatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    val versionList = hashMapOf<String, VersionModelB>()

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
                versionList[projectName] = VersionModelB(projectName, groupId, artifactId, artifactName, versionName)
            }

            projectItem.configurations.all { conf ->
                conf.dependencies.forEach {
                    // this is to make hashCode of combination of libraries and those version
                    // If there are version changes in the libraries, the module will become a candidate for version increase.
                    val depGroup = it.group
                    if (depGroup?.contains(TOKOPEDIA) == true) {
                        val depName = it.name
                        // create list tree
                        println("add dependency: $projectName to $depName")
                        dependenciesHashSet.add(projectName to depName)
                    }
                }
            }

            // check candidate for version update
            val moduleHasNewChanges = moduleHasNewChanges(projectName)
            if (moduleHasNewChanges) {
                candidateModuleListToUpdate.add(projectName)
            }
        }
        println("hashSet $dependenciesHashSet")
        println("candidateModuleListToUpdate $candidateModuleListToUpdate")
        println("versionList $versionList")
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