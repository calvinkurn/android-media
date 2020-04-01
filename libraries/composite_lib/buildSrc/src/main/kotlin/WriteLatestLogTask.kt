package com.tokopedia.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import java.io.File

open class WriteLatestLogTask : DefaultTask() {
    val file = File("tools/version/module_latest_log.txt")

    @TaskAction
    fun run() {
        if (file.exists()) {
            file.delete()
        }
        val queue = mutableListOf<Project>(project)
        //BFS to get all projects
        while (queue.isNotEmpty()) {
            val projectItem = queue.removeAt(0)
            queue.addAll(projectItem.childProjects.values)

            if (projectItem == project) {
                // this is to ignore the root project
                continue
            }

            val projectName = projectItem.name
            val gitLog = "git log -1 --pretty=\'%ad\' --date=format:\'%Y-%m-%d,%H:%M:%S\' ${projectName}"
            val moduleLatestChangeDateString = gitLog.runCommand(project.projectDir.absoluteFile)?.trimSpecial()
                ?: ""
            file.appendText("$projectName#$moduleLatestChangeDateString\n")
        }
    }
}