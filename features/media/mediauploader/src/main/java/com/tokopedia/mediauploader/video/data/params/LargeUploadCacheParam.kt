package com.tokopedia.mediauploader.video.data.params

data class LargeUploadCacheParam(
    var filePath: String,
    var uploadId: String,
    var partDone: Map<Int, Boolean>,
    var initTimeInMillis: Long,
)
