package com.tokopedia.media.editor.ui.component

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.editor.R
import com.tokopedia.media.editor.ui.widget.slider.EditorSliderView
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.picker.common.basecomponent.UiComponent

class RotateToolUiComponent(viewGroup: ViewGroup, val listener: Listener) :
    UiComponent(viewGroup, R.id.uc_tool_rotate),
    EditorSliderView.Listener {

    private val rotateSlider = findViewById<EditorSliderView>(R.id.slider_rotate)
    private val flipBtn = findViewById<IconUnify>(R.id.flip_btn)
    private val rotateBtn = findViewById<IconUnify>(R.id.rotate_btn)

    private var scaleX = 1f
    private var scaleY = 1f
    private val scaleNormalizeValue get() = scaleX * scaleY

    var sliderValue = 0f

    @SuppressLint("ClickableViewAccessibility")
    fun setupView(paramData: EditorDetailUiModel) {
        container().show()

        rotateSlider.setRangeSliderValue(
            ROTATE_SLIDER_START_VALUE,
            ROTATE_SLIDER_STEP_NUMBER,
            ROTATE_SLIDER_STEP_VALUE,
            paramData.cropRotateValue.rotateDegree.toInt()
        )

        rotateSlider.listener = this

        flipBtn.apply {
            setImageDrawable(
                ContextCompat.getDrawable(context, R.drawable.editor_icon_rotate_flip)
            )
            setOnClickListener {
                listener.onImageMirror()
            }
        }

        rotateBtn.setOnClickListener {
            listener.onImageRotate(ROTATE_BTN_DEGREE)
        }
    }

    private fun updateRotation() {
        listener.onRotateValueChanged(
            sliderValue * scaleNormalizeValue
        )
    }

    override fun valueUpdated(step: Int, value: Float) {
        sliderValue = value
        updateRotation()
    }

    interface Listener {
        fun onRotateValueChanged(rotateValue: Float)
        fun onImageMirror()
        fun onImageRotate(rotateDegree: Float)
    }

    companion object {
        const val ROTATE_BTN_DEGREE = 90f
        const val ROTATE_SLIDER_START_VALUE = 0
        const val ROTATE_SLIDER_STEP_NUMBER = 180
        const val ROTATE_SLIDER_STEP_VALUE = 1
    }
}