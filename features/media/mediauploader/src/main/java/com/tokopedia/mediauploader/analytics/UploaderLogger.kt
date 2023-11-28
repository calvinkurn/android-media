package com.tokopedia.mediauploader.analytics

import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.mediauploader.BaseParam
import com.tokopedia.mediauploader.common.data.entity.UploaderTracker
import com.tokopedia.mediauploader.common.state.UploadResult

object UploaderLogger {

    private val tags = mapOf(
        TagType.Common to Pair("MEDIA_UPLOADER_ERROR", Priority.P1),
        TagType.Compression to Pair("MEDIA_UPLOADER_COMPRESSOR", Priority.P2),
    )

    fun commonWithoutReqIdError(param: BaseParam, message: String) {
        val err = UploadResult.Error(message)
        commonError(param, err)
    }

    fun commonError(param: BaseParam, message: String, reqId: String) {
        val err = UploadResult.Error(message, reqId)
        commonError(param, err)
    }

    fun commonError(param: BaseParam, error: UploadResult.Error) {
        if (error.message.isEmpty()) return

        serverLogger(
            type = TagType.Common,
            data = mapOf(
                "file" to param.file.path,
                "sourceId" to param.sourceId,
                "reqId" to error.requestId,
                "message" to error.message
            )
        )
    }

    fun compressionError(data: UploaderTracker?, message: String) {
        serverLogger(
            type = TagType.Compression,
            data = mapOf(
                "err" to message,
                "data" to data.toString(),
            )
        )
    }

    private fun serverLogger(
        type: TagType,
        data: Map<String, String>
    ) {
        val (tag, priority) = tags[type] ?: return
        ServerLogger.log(priority, tag, data)
    }

    sealed interface TagType {
        object Common : TagType
        object Compression : TagType
    }
}
