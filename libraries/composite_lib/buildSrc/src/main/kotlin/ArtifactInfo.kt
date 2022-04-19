package com.tokopedia.plugin

data class ArtifactInfo(
        val projectName: String,
        val groupId: String,
        val artifactId: String,
        val artifactName: String,
        val versionName: String,
        val isAndroidProject: Boolean,
        var maxCurrentVersionName: String = "",
        var increaseVersionString: String = "") {
}