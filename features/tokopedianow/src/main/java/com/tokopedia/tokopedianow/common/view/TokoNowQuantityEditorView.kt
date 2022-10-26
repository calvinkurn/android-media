package com.tokopedia.tokopedianow.common.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.searchbar.navigation_component.util.getActivityFromContext
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.util.ViewUtil.getDpFromDimen
import com.tokopedia.tokopedianow.common.util.ViewUtil.setDimenAsTextSize
import com.tokopedia.tokopedianow.databinding.LayoutTokopedianowQuantityEditorViewBinding
import com.tokopedia.unifycomponents.BaseCustomView
import java.util.Timer
import java.util.TimerTask

@SuppressLint("ClickableViewAccessibility")
class TokoNowQuantityEditorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : BaseCustomView(context, attrs) {

    companion object {
        private const val DELAY_TIME_BACK_TO_START_STATE = 2000L
        private const val MIN_NUMBER = 1
        private const val DEFAULT_NUMBER = 0
        private const val DEFAULT_DP = 0
    }

    private var binding: LayoutTokopedianowQuantityEditorViewBinding

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null
    private var counter = DEFAULT_NUMBER
    private var text: String = ""

    var maxQuantity: Int = Int.MAX_VALUE
        set(value) {
            field = value
            if (text.isNotBlank()) {
                binding.setCounter()
            }
        }

    var minQuantity: Int = MIN_NUMBER
        set(value) {
            field = value
            if (text.isNotBlank()) {
                binding.setCounter()
            }
        }

    var onClickListener: (counter: Int) -> Unit = {}
    var onClickVariantListener: () -> Unit = {}

    init {
        binding = LayoutTokopedianowQuantityEditorViewBinding.inflate(LayoutInflater.from(context),this, true).apply {
            setupAddButton()
            setupSubButton()
            setupEditText()

            root.setTransitionListener(object : MotionLayout.TransitionListener {
                    override fun onTransitionCompleted(motionLayout: MotionLayout, p1: Int) = onAnimationFinished(
                        currentState = motionLayout.currentState
                    )

                    override fun onTransitionStarted(motionLayout: MotionLayout, p1: Int, p2: Int) { /* nothing to do */ }

                    override fun onTransitionChange(motionLayout: MotionLayout?, p1: Int, p2: Int, p3: Float) { /* nothing to do */ }

                    override fun onTransitionTrigger(motionLayout: MotionLayout?, p1: Int, p2: Boolean, p3: Float) { /* nothing to do */ }
                }
            )
        }

        obtainAttributes(attrs)
    }

    private fun obtainAttributes(attrs: AttributeSet?) {
        context.obtainStyledAttributes(
            attrs,
            R.styleable.TokoNowQuantityEditorView
        ).apply {
            try {
                setQuantity(getInt(R.styleable.TokoNowQuantityEditorView_quantity, DEFAULT_NUMBER))
                maxQuantity = getInt(R.styleable.TokoNowQuantityEditorView_maxQuantity, maxQuantity)
                minQuantity = getInt(R.styleable.TokoNowQuantityEditorView_minQuantity, minQuantity)
            } finally {
                recycle()
            }
        }
    }

    private fun startTimer() {
        timer = Timer()
        initializeTimerTask()
        timer?.schedule(
            timerTask,
            DELAY_TIME_BACK_TO_START_STATE
        )
    }

    private fun initializeTimerTask() {
        timerTask = object : TimerTask() {
            override fun run() {
                context.getActivityFromContext()?.runOnUiThread {
                    binding.editText.clearFocus()
                }
            }
        }
    }

    private fun cancelTimer() {
        if (timer != null) {
            timer?.cancel()
            timerTask?.cancel()
            timerTask =  null
            timer = null
        }
    }

    private fun getEnabledColor(isEnabled: Boolean): Int = if (isEnabled) getResourceColor(R.color.tokopedianow_product_card_dms_button_stroke_collapsed_color) else getResourceColor(R.color.tokopedianow_product_card_dms_button_stroke_expanded_color)

    private fun getResourceColor(id: Int): Int = ContextCompat.getColor(context, id)

    private fun LayoutTokopedianowQuantityEditorViewBinding.onAnimationFinished(
        currentState: Int
    ) {
        if (currentState == R.id.end) {
            editText.requestFocus()
            editText.setOnTouchListener(null)
            editText.setSelection(text.length)
            cancelTimer()
            startTimer()
        } else if (currentState == R.id.startWithValue) {
            setEditTextPadding()
        }
    }

    private fun LayoutTokopedianowQuantityEditorViewBinding.setCounter() {
        val currentCounter = text.toIntOrZero()
        counter = if (currentCounter >= maxQuantity) {
            editText.setText(maxQuantity.toString())
            editText.setSelection(text.length)
            addButton.setColorFilter(getEnabledColor(false))
            maxQuantity
        }   else {
            addButton.setColorFilter(getEnabledColor(true))
            currentCounter
        }
    }

    private fun LayoutTokopedianowQuantityEditorViewBinding.setupAddButton() {
        addButton.setOnClickListener {
            if (root.currentState == R.id.start) {
                expandAnimationWhenStartingWithoutValue()
                editText.setText(minQuantity.toString())
            } else if (root.currentState == R.id.startWithValue) {
                expandAnimationWhenStartingWithValue()
            } else if (counter < maxQuantity) {
                counter++
                editText.setText(counter.toString())
            }
        }
    }

    private fun LayoutTokopedianowQuantityEditorViewBinding.setupSubButton() {
        subButton.setOnClickListener {
            counter--
            if (counter < minQuantity) {
                editText.clearFocus()
            } else {
                editText.setText(counter.toString())
            }
        }
    }

    private fun LayoutTokopedianowQuantityEditorViewBinding.setupEditText() {
        editText.focusChangedListener = { isFocused ->
            if (!isFocused) backToTheStartState()
        }

        editText.afterTextChanged {
            if (text != it) {
                text = it
                editText.setSelection(text.length)
                changeViewDisplayAndRestartTimer()
            }
        }
    }

    private fun LayoutTokopedianowQuantityEditorViewBinding.changeViewDisplayAndRestartTimer() {
        setCounter()
        cancelTimer()
        startTimer()
    }

    private fun LayoutTokopedianowQuantityEditorViewBinding.backToTheStartState() {
        editText.setDimenAsTextSize(R.dimen.tokopedianow_quantity_editor_text_size_start_with_value)
        if (counter < minQuantity) {
            binding.editText.setText("")
            root.setTransition(R.id.end, R.id.start)
        } else {
            editText.setText(counter.toString())
            root.setTransition(R.id.end, R.id.startWithValue)
        }
        root.transitionToEnd()
        cancelTimer()
        onClickListener(counter)
    }

    private fun LayoutTokopedianowQuantityEditorViewBinding.setEditTextPadding() {
        editText.clearAnimation()
        editText.setPadding(
            getDpFromDimen(context, R.dimen.tokopedianow_quantity_editor_padding_horizontal).toInt(),
            DEFAULT_DP,
            getDpFromDimen(context, R.dimen.tokopedianow_quantity_editor_padding_horizontal).toInt(),
            DEFAULT_DP
        )
    }

    private fun LayoutTokopedianowQuantityEditorViewBinding.expandAnimationWhenStartingWithoutValue() {
        editText.setDimenAsTextSize(R.dimen.tokopedianow_quantity_editor_text_size_end_with_value)
        root.setTransition(R.id.start, R.id.end)
        root.transitionToEnd()
    }

    private fun LayoutTokopedianowQuantityEditorViewBinding.expandAnimationWhenStartingWithValue() {
        editText.setDimenAsTextSize(R.dimen.tokopedianow_quantity_editor_text_size_end_with_value)
        root.setTransition(R.id.startWithValue, R.id.end)
        root.transitionToEnd()
        setOnTouchListener(null)
    }

    fun setQuantity(quantity: Int) {
        binding.apply {
            if (quantity > DEFAULT_NUMBER) {
                counter = quantity
                root.setTransition(R.id.startWithValue, R.id.end)
                editText.setDimenAsTextSize(R.dimen.tokopedianow_quantity_editor_text_size_start_with_value)
                editText.setText(counter.toString())
                setEditTextPadding()
            }
        }
    }

    fun getQuantity(): Int = counter
}
