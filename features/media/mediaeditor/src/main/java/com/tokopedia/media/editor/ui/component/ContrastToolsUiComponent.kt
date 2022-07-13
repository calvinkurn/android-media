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
        contrastSlider.isValueUpdateDelay = true
        contrastSlider.delayTime = 250L
    }

    override fun valueUpdated(step: Int, value: Float) {
        listener.onContrastValueChanged(value)
    }

    interface Listener {
        fun onContrastValueChanged(value: Float)
    }

    companion object {
        private const val CONTRAST_SLIDER_VALUE_DIVIDER = 10

        // convert raw value (storage value & slider value is raw value) to contrast range [0..10]
        fun contrastRawToStdValue(rawStorageValue: Float): Float {
            return rawStorageValue / CONTRAST_SLIDER_VALUE_DIVIDER
        }
    }
}