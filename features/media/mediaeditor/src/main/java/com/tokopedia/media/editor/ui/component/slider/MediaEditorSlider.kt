package com.tokopedia.media.editor.ui.component.slider

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.tokopedia.media.editor.R
import com.tokopedia.media.editor.databinding.MediaEditorSliderLayoutBinding
import kotlin.math.round
import com.tokopedia.unifyprinciples.R as PrincipleR

class MediaEditorSlider(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {

    private var binding: MediaEditorSliderLayoutBinding =
        MediaEditorSliderLayoutBinding.inflate(LayoutInflater.from(context), this, true)

    private val sliderThumb: View = binding.sliderThumb
    private val sliderTrack: View = binding.sliderTrack
    private val sliderTrackActive: MediaEditorSliderTrack = binding.sliderTrackActive
    private val sliderWrapper: ConstraintLayout = binding.sliderWrapper
    private val sliderCenterIndicator: View = binding.sliderCenterIndicator
    private val sliderText: AppCompatTextView = binding.sliderText

    private var sliderWidth = 0f
    private var thumbWidth = 0f

    private val trackActiveColor: Int = ContextCompat.getColor(context, PrincipleR.color.Unify_GN400)

    var listener: Listener? = null

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

        initSliderTrack()
        updateSliderTextValue()

        post {
            initSliderRef()
            moveThumb()
            initSliderTouchListener()
        }
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

        resetSlider()
    }

    private fun moveThumb() {
        if (sliderInitialPosition == sliderStartValue) return
        val stepSize = (sliderWidth - thumbWidth) / sliderStepNumber

        val targetStep = sliderInitialPosition / sliderStepValue

        val targetX = targetStep * stepSize

        sliderThumb.x = if (sliderInitialPosition > sliderStartValue) {
            targetX + sliderThumb.x
        } else {
            sliderThumb.x - targetX
        }

        sliderTrackActive.update(
            sliderCenterIndicator.x,
            sliderThumb.x,
            thumbWidth / 2
        )

        sliderText.text = sliderInitialPosition.toString()
    }

    private fun resetSlider() {
        updateSliderTextValue()
        sliderThumb.x = 0f
    }

    private fun updateSliderTextValue() {
        sliderText.text = sliderStartValue.toString()
    }

    private fun initSliderRef() {
        sliderWidth = sliderTrack.width.toFloat()
        thumbWidth = sliderThumb.width.toFloat()

        val constraintSet = ConstraintSet()
        constraintSet.clone(sliderWrapper)
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

            sliderTrackActive.update(
                centerX,
                newXPosition,
                halfThumbWidth
            )

            val stepIndex = xToStep(view.x)
            val value = stepToValue(stepIndex).toInt().toString()
            if (sliderText.text != value) {
                sliderText.text = value
                listener?.valueUpdated(stepIndex, value.toFloat())
            }

            true
        }
    }

    private fun stepToValue(stepIndex: Int): Float {
        val standarizeStep = (stepIndex - (sliderStepNumber / 2))
        return ((standarizeStep * sliderStepValue) + sliderStartValue).toFloat()
    }

    private fun xToStep(xKoor: Float): Int {
        val stepSize = (sliderWidth - thumbWidth) / sliderStepNumber
        return (round(xKoor / stepSize).toInt())
    }

    private fun initSliderTrack() {
        sliderTrackActive.setLine(
            resources.getDimension(R.dimen.media_editor_slider_height),
            trackActiveColor
        )
    }

    companion object {
        private const val DEFAULT_SLIDER_STEP = 10
        private const val DEFAULT_START_VALUE = 5
        private const val DEFAULT_STEP_VALUE = 1
    }

    interface Listener {
        fun valueUpdated(step: Int, value: Float)
    }

}