package com.tokopedia.plugin

data class VersionModel(
    val projectName: String,
    val incrementCount: Float,
    val groupId: String,
    val artifactId: String,
    val artifactName: String)