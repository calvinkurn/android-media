package com.tokopedia.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

open class ScanModuleToPublishTask : DefaultTask() {
    var moduleToPublishList = mutableSetOf<String>()

    val PROPERTY_KEY = "moduleToPublish"
    var successScan = false

    @TaskAction
    fun run() {
        if (project.hasProperty(PROPERTY_KEY)) {
            val pathString: String = project.properties[PROPERTY_KEY].toString()
            val file = File(pathString)
            if (file.exists()) {
                println("Read File $pathString")
                file.forEachLine { line ->
                    if (line.isNotEmpty() && !line.startsWith("//")) {
                        moduleToPublishList.add(line)
                    }
                }
                if (moduleToPublishList.isEmpty()) {
                    println("File module To Publish is empty.")
                    successScan = false
                } else {
                    successScan = true
                }
            } else {
                println("File $pathString does not exist")
                successScan = false
            }
        } else {
            println("Please add property -P$PROPERTY_KEY=[Module To Publish File path]")
            successScan = false
        }
    }
}