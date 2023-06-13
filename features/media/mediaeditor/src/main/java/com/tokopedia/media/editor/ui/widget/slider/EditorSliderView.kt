package com.tokopedia.media.editor.ui.widget.slider

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.media.editor.databinding.MediaEditorSliderLayoutBinding
import kotlin.math.round

class EditorSliderView(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {

    private var binding: MediaEditorSliderLayoutBinding =
        MediaEditorSliderLayoutBinding.inflate(LayoutInflater.from(context), this, true)

    private val sliderThumb: View = binding.sliderThumb
    private val sliderTrack: View = binding.sliderTrack
    private val trackViewActiveSliderView: EditorTrackSliderView = binding.sliderTrackActive
    private val sliderCenterIndicator: View = binding.sliderCenterIndicator
    private val sliderText: AppCompatTextView = binding.sliderText

    private var sliderWidth = 0f
    private var thumbWidth = 0f

    private var sliderHandler: Handler? = null

    var listener: Listener? = null

    var isValueUpdateDelay = false
    var delayTime = 500L

    private var delayFlag = false

    private var sliderStepNumber = DEFAULT_SLIDER_STEP
        set(value) {
            field = if (value % 2 == 1) {
                value + 1
            } else {
                value
            }
        }
    private var sliderStartValue = DEFAULT_START_VALUE
    private var sliderStepValue = DEFAULT_STEP_VALUE

    private var sliderInitialPosition = DEFAULT_START_VALUE

    init {
        updateSliderTextValue()
    }

    fun setRangeSliderValue(
        startValue: Int,
        stepNumber: Int,
        stepValue: Int,
        initialPosition: Int? = null
    ) {
        sliderStartValue = startValue
        sliderStepNumber = stepNumber
        sliderStepValue = stepValue
        sliderInitialPosition = initialPosition ?: startValue

        post {
            initSliderValue()
            moveThumb()
            initSliderTouchListener()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (hasWindowFocus) {
            initSliderTouchListener()
        } else {
            sliderThumb.setOnTouchListener(null)
        }
    }

    private fun moveThumb() {
        // got step size on px
        val stepSize = (sliderWidth - thumbWidth) / sliderStepNumber

        // got difference value between move target and center start position value
        val diffTargetWithCenter =
            if (sliderInitialPosition < sliderStartValue) sliderStartValue - sliderInitialPosition
            else sliderInitialPosition - sliderStartValue

        // got how many step needed to move from center
        val targetStep = diffTargetWithCenter / sliderStepValue

        val targetX = targetStep * stepSize

        sliderThumb.x = if (sliderInitialPosition > sliderStartValue) {
            targetX + sliderThumb.x
        } else {
            sliderThumb.x - targetX
        }

        trackViewActiveSliderView.update(
            sliderCenterIndicator.x,
            sliderThumb.x,
            thumbWidth / 2
        )

        sliderText.text = sliderInitialPosition.toString()
    }

    private fun updateSliderTextValue() {
        sliderText.text = sliderStartValue.toString()
    }

    private fun initSliderValue() {
        sliderWidth = sliderTrack.width.toFloat()
        thumbWidth = sliderThumb.width.toFloat()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initSliderTouchListener() {
        val thumbMinX = 0f
        val thumbMaxX = sliderWidth - thumbWidth
        val centerX = sliderCenterIndicator.x
        val halfThumbWidth = thumbWidth / 2

        sliderThumb.setOnTouchListener { view, motionEvent ->
            val newXPosition = (view.x + motionEvent.x) - (view.width / 2)
            when {
                newXPosition < thumbMinX -> {
                    view.x = thumbMinX
                }
                newXPosition > thumbMaxX -> {
                    view.x = thumbMaxX
                }
                else -> {
                    view.x = newXPosition
                }
            }

            trackViewActiveSliderView.update(
                centerX,
                newXPosition,
                halfThumbWidth
            )

            val stepIndex = xToStep(view.x)
            val value = stepToValue(stepIndex).toInt().toString()
            if (sliderText.text != value) {
                sliderText.text = value

                if (!isValueUpdateDelay && !delayFlag) {
                    listener?.valueUpdated(stepIndex, value.toFloat())
                } else if (!delayFlag) {
                    checkIsThumbStop(value.toFloat(), stepIndex, 0f)
                    delayFlag = true
                }
            }

            true
        }
    }

    private fun checkIsThumbStop(
        currentValue: Float,
        currentStepIndex: Int,
        previousValue: Float
    ) {
        if (sliderHandler == null) sliderHandler = Handler()
        sliderHandler?.removeCallbacksAndMessages(null)
        sliderHandler?.postDelayed({
            if (currentValue == previousValue) {
                listener?.valueUpdated(currentStepIndex, currentValue)
                delayFlag = false
            } else {
                val stepIndex = xToStep(sliderThumb.x)
                val value = stepToValue(stepIndex).toInt().toString()
                checkIsThumbStop(value.toFloat(), stepIndex, currentValue)
            }
        }, delayTime)
    }

    private fun stepToValue(stepIndex: Int): Float {
        val standardizeStep = (stepIndex - (sliderStepNumber / 2))
        return ((standardizeStep * sliderStepValue) + sliderStartValue).toFloat()
    }

    private fun xToStep(xCoordinate: Float): Int {
        val stepSize = (sliderWidth - thumbWidth) / sliderStepNumber
        return (round(xCoordinate / stepSize).toInt())
    }

    companion object {
        private const val DEFAULT_SLIDER_STEP = 10
        private const val DEFAULT_START_VALUE = 0
        private const val DEFAULT_STEP_VALUE = 1
    }

    interface Listener {
        fun valueUpdated(step: Int, value: Float)
    }

}