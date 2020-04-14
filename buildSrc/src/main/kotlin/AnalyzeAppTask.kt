package com.tokopedia.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction

open class AnalyzeAppTask : DefaultTask() {

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

            projectItem.configurations.all { conf ->
                conf.dependencies.forEach {
                    // this is to make hashCode of combination of libraries and those version
                    // If there are version changes in the libraries, the module will become a candidate for version increase.
                    val artifactName = it.name
                    // create list tree
                    dependenciesHashSetTemp.add(projectName to artifactName)
                }
            }
        }

        println(dependenciesHashSetTemp)
    }

}