package com.tokopedia.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

open class ScanReleaseDateTask : DefaultTask() {

    var listReleaseDate = mutableListOf<String>()

    @InputFile
    val file = File ("tools/version/release_date.txt")

    @TaskAction
    fun run() {
        println("[Start Scan Release Date Task]")
        file.forEachLine {
            listReleaseDate.add(it)
        }
        println(listReleaseDate)
    }
}