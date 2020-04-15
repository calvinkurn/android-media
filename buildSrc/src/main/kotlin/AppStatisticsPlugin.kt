package com.tokopedia.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

open class AppStatisticsPlugin : Plugin<Project> {
    override fun apply(project: Project) {

        project.tasks.register( "appStatisticsTask", AppStatisticsTask::class.java)
    }
}
