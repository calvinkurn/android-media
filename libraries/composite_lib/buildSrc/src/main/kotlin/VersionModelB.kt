package com.tokopedia.plugin

data class VersionModelB(
    val projectName: String,
    val groupId: String,
    val artifactId: String,
    val artifactName: String,
    val versionName: String) {

    var versionInt: Int = 0
    var versionNameFuture: String = ""
    var versionIntFuture: Int = 0

}