package com.tokopedia.mediauploader.video.data.params

data class LargeUploadCacheParam(
    var filePath: String,
    var uploadId: String,
    var partNumber: Int,
    var initTimeInMillis: Long,
)