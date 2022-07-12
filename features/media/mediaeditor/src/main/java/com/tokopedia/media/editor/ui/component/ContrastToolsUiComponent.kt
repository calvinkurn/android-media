package com.tokopedia.media.editor.ui.component

import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.editor.R
import com.tokopedia.media.editor.ui.component.slider.MediaEditorSlider
import com.tokopedia.picker.common.basecomponent.UiComponent

class ContrastToolsUiComponent constructor(
    viewGroup: ViewGroup,
    private val listener: Listener
) : UiComponent(viewGroup, R.id.uc_tool_contrast), MediaEditorSlider.Listener {

    private val contrastSlider: MediaEditorSlider = findViewById(R.id.slider_contrast)

    fun setupView(sliderInitValue: Float) {
        container().show()

        contrastSlider.setRangeSliderValue(
            50,
            100,
            1,
            sliderInitValue.toInt()
        )
        contrastSlider.listener = this
    }

    override fun valueUpdated(step: Int, value: Float) {
        listener.onContrastValueChanged(value / CONTRAST_SLIDER_VALUE_DIVIDER)
    }

    interface Listener {
        fun onContrastValueChanged(value: Float)
    }

    companion object {
        const val CONTRAST_SLIDER_VALUE_DIVIDER = 10
    }
}