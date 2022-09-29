package com.tokopedia.media.editor.ui.component

import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.editor.R
import com.tokopedia.media.editor.ui.widget.slider.EditorSliderView
import com.tokopedia.picker.common.basecomponent.UiComponent

class ContrastToolsUiComponent constructor(
    viewGroup: ViewGroup,
    private val listener: Listener
) : UiComponent(viewGroup, R.id.uc_tool_contrast), EditorSliderView.Listener {

    private val contrastSliderViewView: EditorSliderView = findViewById(R.id.slider_contrast)

    fun setupView(sliderInitValue: Float) {
        container().show()

        contrastSliderViewView.setRangeSliderValue(
            CONTRAST_SLIDER_START_VALUE,
            CONTRAST_SLIDER_RANGE * 2,
            CONTRAST_SLIDER_STEP_VALUE,
            sliderInitValue.toInt()
        )
        contrastSliderViewView.listener = this
        contrastSliderViewView.isValueUpdateDelay = true
        contrastSliderViewView.delayTime = CONTRAST_SLIDER_DELAY_TIME
    }

    override fun valueUpdated(step: Int, value: Float) {
        listener.onContrastValueChanged(value)
    }

    interface Listener {
        fun onContrastValueChanged(value: Float)
    }

    companion object {
        // contrast can be adjust from 0...10, current product requirement need to cap it on scale 3
        private const val CONTRAST_REAL_POSITIVE_VALUE = 2

        private const val CONTRAST_SLIDER_RANGE = 100
        private const val CONTRAST_SLIDER_STEP_VALUE = 1
        private const val CONTRAST_SLIDER_START_VALUE = 0
        private const val CONTRAST_SLIDER_DELAY_TIME = 100L

        /**
         * convert slider value -100...100 to contrast value 0...3
         * -100...1 => 0,01...1
         * 0...100 => 1...3
         */
        fun contrastRawToStdValue(rawStorageValue: Float): Float {
            return if (rawStorageValue >= 0) {
                // 1...3
                ((rawStorageValue / CONTRAST_SLIDER_RANGE) * CONTRAST_REAL_POSITIVE_VALUE) + 1
            } else {
                // 0...0.99
                ((rawStorageValue + CONTRAST_SLIDER_RANGE) / CONTRAST_SLIDER_RANGE) + 0.01f
            }
        }
    }
}