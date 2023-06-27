package com.tokopedia.mediauploader.util

import com.tokopedia.mediauploader.common.util.isVideoFormat
import com.tokopedia.picker.common.utils.fileExtension
import io.mockk.mockkStatic

open class BaseUploaderTest {

    init {
        mockkStatic(::fileExtension)
        mockkStatic(::isVideoFormat)
    }
}
