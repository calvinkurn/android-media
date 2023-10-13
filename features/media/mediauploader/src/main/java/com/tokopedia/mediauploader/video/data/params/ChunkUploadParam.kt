package com.tokopedia.mediauploader.video.data.params

import com.tokopedia.mediauploader.common.util.network.byteBody

data class ChunkUploadParam(
    var sourceId: String,
    var uploadId: String,
    var partNumber: String,
    var fileName: String,
    var byteArray: ByteArray,
    var timeOut: String,
) {

    fun fileBody() = byteArray.byteBody(
        fileName = fileName,
        type = TYPE_FILE,
        bodyName = BODY_FILE
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ChunkUploadParam

        if (sourceId != other.sourceId) return false
        if (uploadId != other.uploadId) return false
        if (partNumber != other.partNumber) return false
        if (!byteArray.contentEquals(other.byteArray)) return false
        if (timeOut != other.timeOut) return false

        return true
    }

    override fun hashCode(): Int {
        var result = sourceId.hashCode()
        result = 31 * result + uploadId.hashCode()
        result = 31 * result + partNumber.hashCode()
        result = 31 * result + byteArray.contentHashCode()
        result = 31 * result + timeOut.hashCode()
        return result
    }

    companion object {
        private const val BODY_FILE = "file"
        private const val TYPE_FILE = "video/*"
    }
}
