package com.tokopedia.editor.faker

import android.graphics.Bitmap
import com.tokopedia.editor.data.repository.VideoFlattenRepository
import kotlinx.coroutines.flow.MutableSharedFlow

class FakeVideoFlattenRepository : VideoFlattenRepository {

    private val flow = MutableSharedFlow<String>()
    suspend fun emit(value: String) = flow.emit(value)

    override fun flatten(videoPath: String, canvasText: Bitmap) = flow
}
