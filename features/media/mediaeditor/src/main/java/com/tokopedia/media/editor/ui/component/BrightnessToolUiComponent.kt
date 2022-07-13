package com.tokopedia.media.editor.ui.component

import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.editor.R
import com.tokopedia.media.editor.ui.component.slider.MediaEditorSlider
import com.tokopedia.picker.common.basecomponent.UiComponent

class BrightnessToolUiComponent constructor(
    viewGroup: ViewGroup,
    private val listener: Listener
) : UiComponent(viewGroup, R.id.uc_tool_brightness), MediaEditorSlider.Listener {

    private val brightnessSlider: MediaEditorSlider = findViewById(R.id.slider_brightness)

    fun setupView(sliderInitValue: Float) {
        container().show()

        brightnessSlider.setRangeSliderValue(0, 200, 1, sliderInitValue.toInt())
        brightnessSlider.listener = this
    }

    override fun valueUpdated(step: Int, value: Float) {
        listener.onBrightnessValueChanged(value)
    }

    interface Listener {
        fun onBrightnessValueChanged(value: Float)
    }

}