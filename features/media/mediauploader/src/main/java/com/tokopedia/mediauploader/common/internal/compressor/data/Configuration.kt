package com.tokopedia.mediauploader.common.internal.compressor.data

data class Configuration(
    var videoBitrate: Int? = null,
    var disableAudio: Boolean = false,
    var videoHeight: Double? = null,
    var videoWidth: Double? = null,
)
