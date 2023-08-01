package com.tokopedia.editor.ui.gesture

import android.view.View
import com.tokopedia.editor.ui.model.AddTextModel

data class MultiTouchData(
    var isScaleEnabled: Boolean = false,
    var isRotateEnabled: Boolean = false,
    var isTranslateEnabled: Boolean = false,
    var minimumScale: Float = 0f,
    var maximumScale: Float = 0f,
    var isTextPinchZoomable: Boolean = false,
)

interface MultiTouchListener {
    val data: MultiTouchData
    fun move(view: View, model: AddTextModel)
}

class MultiTouchImpl : MultiTouchListener {

    override val data: MultiTouchData
        get() = MultiTouchData()

    override fun move(view: View, model: AddTextModel) {

    }

}
