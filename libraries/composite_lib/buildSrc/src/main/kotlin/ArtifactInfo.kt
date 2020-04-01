package com.tokopedia.plugin

data class ArtifactInfo(
    val projectName: String,
    val groupId: String,
    val artifactId: String,
    val artifactName: String,
    val versionName: String) {

    var maxCurrentVersionName:String = ""
    var increaseVersionString:String = ""
}