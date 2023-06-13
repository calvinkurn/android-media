package com.tokopedia.media.picker.common.data.repository

import com.tokopedia.media.picker.data.repository.BitmapConverterRepository

class TestBitmapConverterRepository : BitmapConverterRepository {
    override suspend fun convert(url: String) = ""
}
