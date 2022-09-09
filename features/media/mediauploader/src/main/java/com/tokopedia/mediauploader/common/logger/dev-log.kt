package com.tokopedia.mediauploader.common.logger

import com.tokopedia.config.GlobalConfig
import com.tokopedia.mediauploader.common.data.entity.SourcePolicy
import timber.log.Timber

data class DebugLog(
    val sourceId: String,
    val sourceFile: String,
    val url: String = "",
    val uploadId: String,
    val sourcePolicy: SourcePolicy?
)

fun onShowDebugLogcat(param: DebugLog) {
    // the logcat debug only shown on app debug
    if (!GlobalConfig.isAllowDebuggingTools()) return

    val moduleName = "DevMediaUploader"

    val message = """
        sourceId: ${param.sourceId} |
        file: ${param.sourceFile} |
        resultUrl: ${param.url} |
        uploadId: ${param.uploadId} |
        policy: ${param.sourcePolicy}
    """

    Timber.d("$moduleName: $message")
}