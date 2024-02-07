package com.tokopedia.editor.faker

import com.tokopedia.editor.data.repository.FlattenParam
import com.tokopedia.editor.data.repository.VideoFlattenRepository
import kotlinx.coroutines.flow.MutableSharedFlow

class FakeVideoFlattenRepository : VideoFlattenRepository {

    private val flow = MutableSharedFlow<String>()

    private var isFlattenOngoing = false

    suspend fun emit(value: String) = flow.emit(value)

    fun setFlattenStatus(value: Boolean) {
        isFlattenOngoing = value
    }

    override fun flatten(param: FlattenParam, fileNameAppendix: String) = flow

    override fun isFlattenOngoing() = isFlattenOngoing

    override fun cancel() = Unit
}
