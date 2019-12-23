package com.tokopedia.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File

@CacheableTask
open class ScanReleaseDateTask : DefaultTask() {
    @Input
    val listReleaseDate = mutableListOf<String>()

    @Input
    val file = File ("tools/version/release_date.txt")

    @TaskAction
    fun run() {
        println("[Start Scan Project Task]")
        file.forEachLine {
            listReleaseDate.add(it)
        }
        println(listReleaseDate)
    }
}