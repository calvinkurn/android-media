package com.tokopedia.mediauploader

import com.tokopedia.mediauploader.common.util.isVideoFormat
import com.tokopedia.picker.common.utils.fileExtension
import io.mockk.mockkStatic

open class CompressionTest {

    init {
        mockkStatic(::fileExtension)
        mockkStatic(::isVideoFormat)
    }
}
