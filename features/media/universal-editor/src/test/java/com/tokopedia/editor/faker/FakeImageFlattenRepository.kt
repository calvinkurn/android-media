package com.tokopedia.editor.faker

import android.graphics.Bitmap
import com.tokopedia.editor.data.repository.ImageFlattenRepository
import kotlinx.coroutines.flow.MutableSharedFlow

class FakeImageFlattenRepository : ImageFlattenRepository {

    private val flow = MutableSharedFlow<String>()

    suspend fun emit(value: String) = flow.emit(value)

    override fun flattenImage(imageBitmap: Bitmap, textBitmap: Bitmap?) = flow
}
