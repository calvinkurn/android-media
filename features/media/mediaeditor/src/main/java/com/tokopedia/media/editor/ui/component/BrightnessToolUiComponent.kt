package com.tokopedia.media.editor.ui.component

import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.editor.R
import com.tokopedia.media.editor.ui.widget.slider.EditorSliderView
import com.tokopedia.picker.common.basecomponent.UiComponent

class BrightnessToolUiComponent constructor(
    viewGroup: ViewGroup,
    private val listener: Listener
) : UiComponent(viewGroup, R.id.uc_tool_brightness), EditorSliderView.Listener {

    private val brightnessSliderViewView: EditorSliderView = findViewById(R.id.slider_brightness)

    fun setupView(sliderInitValue: Float) {
        container().show()

        brightnessSliderViewView.setRangeSliderValue(
            BRIGHTNESS_SLIDER_START_VALUE,
            BRIGHTNESS_SLIDER_RANGE,
            BRIGHTNESS_SLIDER_STEP_VALUE,
            sliderInitValue.toInt()
        )
        brightnessSliderViewView.listener = this
    }

    override fun valueUpdated(step: Int, value: Float) {
        listener.onBrightnessValueChanged(value)
    }

    interface Listener {
        fun onBrightnessValueChanged(value: Float)
    }

    companion object {
        private const val BRIGHTNESS_SLIDER_RANGE = 200
        private const val BRIGHTNESS_SLIDER_STEP_VALUE = 1
        private const val BRIGHTNESS_SLIDER_START_VALUE = 0
        private const val BRIGHTNESS_MAX_REAL_VALUE = 255

        /**
         * convert slider value -100...100 to brightness value -255...255
         */
        fun sliderValueToBrightness(value: Float): Float {
            return (value / (BRIGHTNESS_SLIDER_RANGE / 2)) * BRIGHTNESS_MAX_REAL_VALUE
        }
    }

}