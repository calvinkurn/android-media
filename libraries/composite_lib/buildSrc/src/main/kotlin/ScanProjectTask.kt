package com.tokopedia.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class ScanProjectTask : DefaultTask() {

    val listModule = mutableListOf<String>()

    @TaskAction
    fun run() {
        println("[Start Scan Project Task]")
        for (subproject in project.subprojects) {
            listModule.add(subproject.path.substring(1))
        }
        println(listModule)
    }
}