package com.tokopedia.mediauploader.util

import com.tokopedia.picker.common.utils.fileExtension
import com.tokopedia.picker.common.utils.isVideoFormat
import io.mockk.mockkStatic

open class BaseUploaderTest {

    init {
        mockkStatic(::fileExtension)
        mockkStatic(::isVideoFormat)
    }
}
