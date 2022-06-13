package com.tokopedia.mediauploader.video.data.params

data class ChunkCheckerParam(
    var uploadId: String,
    var partNumber: String,
    var fileName: String,
)