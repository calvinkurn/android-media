package com.tokopedia.tokopedianow.common.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.searchbar.navigation_component.util.getActivityFromContext
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.util.ViewUtil.getDpFromDimen
import com.tokopedia.tokopedianow.common.util.ViewUtil.setDimenAsTextSize
import com.tokopedia.tokopedianow.databinding.LayoutTokopedianowQuantityEditorCustomViewBinding
import com.tokopedia.unifycomponents.BaseCustomView
import java.util.Timer
import java.util.TimerTask

@SuppressLint("ClickableViewAccessibility")
class TokoNowQuantityEditorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    companion object {
        private const val DELAY_TIME_BACK_TO_START_STATE = 2000L
        private const val MIN_NUMBER = 1
        private const val DEFAULT_NUMBER = 0
        private const val DEFAULT_DP = 0
        private const val NO_PROGRESS_ANIMATION = 0F
    }

    private var binding: LayoutTokopedianowQuantityEditorCustomViewBinding

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

    init {
        binding = LayoutTokopedianowQuantityEditorCustomViewBinding.inflate(LayoutInflater.from(context),this, true).apply {
            setupAddButton()
            setupSubButton()
            setupEditText()

            root.setTransitionListener(object : MotionLayout.TransitionListener {
                    override fun onTransitionStarted(motionLayout: MotionLayout, p1: Int, p2: Int) = onAnimationStarted(
                        currentState = motionLayout.currentState
                    )

                    override fun onTransitionCompleted(motionLayout: MotionLayout, p1: Int) = onAnimationFinished(
                        currentState = motionLayout.currentState
                    )

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
                maxQuantity = getInt(R.styleable.TokoNowQuantityEditorView_maxQuantity, Int.MAX_VALUE)
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

    @SuppressLint("Method Call Prohibited")
    private fun String.getCounterOrDefaultValue(): Int {
        return try {
            if (isBlank()) minQuantity else toInt()
        } catch (e: Exception) {
            maxQuantity
        }
    }

    private fun getEnabledColor(isEnabled: Boolean): Int = if (isEnabled) getResourceColor(com.tokopedia.unifyprinciples.R.color.Unify_GN500) else getResourceColor(com.tokopedia.unifyprinciples.R.color.Unify_NN300)

    private fun getResourceColor(id: Int): Int = ContextCompat.getColor(context, id)

    private fun LayoutTokopedianowQuantityEditorCustomViewBinding.onAnimationStarted(
        currentState: Int
    ) {
        if (currentState == R.id.startWithValue) {
            setEditTextWhenStartingWithValueAnimation()
        }
    }

    private fun LayoutTokopedianowQuantityEditorCustomViewBinding.onAnimationFinished(
        currentState: Int
    ) {
        if (currentState == R.id.end) {
            editText.requestFocus()
            editText.setOnTouchListener(null)
            editText.setSelection(text.length)
            cancelTimer()
            startTimer()
        }
    }

    private fun LayoutTokopedianowQuantityEditorCustomViewBinding.setCounter() {
        val currentCounter = text.getCounterOrDefaultValue()
        counter = if (text.isBlank()) {
            editText.text?.clear()
            quantityEditorSubButton.setColorFilter(getEnabledColor(false))
            currentCounter
        } else if (maxQuantity == minQuantity) {
            editText.setText(minQuantity.toString())
            quantityEditorAddButton.setColorFilter(getEnabledColor(false))
            quantityEditorSubButton.setColorFilter(getEnabledColor(false))
            minQuantity
        } else if (currentCounter >= maxQuantity) {
            editText.setText(maxQuantity.toString())
            quantityEditorAddButton.setColorFilter(getEnabledColor(false))
            quantityEditorSubButton.setColorFilter(getEnabledColor(true))
            maxQuantity
        } else if (currentCounter <= minQuantity) {
            if (currentCounter < minQuantity) editText.setText(minQuantity.toString())
            quantityEditorAddButton.setColorFilter(getEnabledColor(true))
            quantityEditorSubButton.setColorFilter(getEnabledColor(false))
            minQuantity
        }  else {
            quantityEditorAddButton.setColorFilter(getEnabledColor(true))
            quantityEditorSubButton.setColorFilter(getEnabledColor(true))
            currentCounter
        }
    }

    private fun LayoutTokopedianowQuantityEditorCustomViewBinding.setupAddButton() {
        quantityEditorAddButton.setOnClickListener {
            if (counter < maxQuantity) {
                if (root.progress == NO_PROGRESS_ANIMATION) {
                    root.setTransition(R.id.start, R.id.end)
                    root.transitionToEnd()
                    editText.setText(counter.toString())
                } else {
                    counter++
                    editText.setText(counter.toString())
                }
            }
        }
    }

    private fun LayoutTokopedianowQuantityEditorCustomViewBinding.setupSubButton() {
        quantityEditorSubButton.setOnClickListener {
            if (counter > minQuantity) {
                counter--
                editText.setText(counter.toString())
            }
        }
    }

    private fun LayoutTokopedianowQuantityEditorCustomViewBinding.setupEditText() {
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

    private fun LayoutTokopedianowQuantityEditorCustomViewBinding.changeViewDisplayAndRestartTimer() {
        setCounter()
        cancelTimer()
        startTimer()
    }

    private fun LayoutTokopedianowQuantityEditorCustomViewBinding.backToTheStartState() {
        if (text.isBlank() && counter == DEFAULT_NUMBER) {
            root.setTransition(R.id.end, R.id.start)
        } else {
            if (text.isBlank()) editText.setText(counter.toString())
            root.setTransition(R.id.end, R.id.startWithValue)
        }
        root.transitionToEnd()
        cancelTimer()
    }

    private fun LayoutTokopedianowQuantityEditorCustomViewBinding.setEditTextWhenStartingWithValueAnimation() {
        editText.apply {
            layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
            setDimenAsTextSize(R.dimen.tokopedianow_quantity_editor_text_size_start_with_value)
            setTextColor(getEnabledColor(true))
            setPadding(
                getDpFromDimen(context, R.dimen.tokopedianow_quantity_editor_padding_horizontal).toInt(),
                DEFAULT_DP,
                getDpFromDimen(context, R.dimen.tokopedianow_quantity_editor_padding_horizontal).toInt(),
                DEFAULT_DP
            )

            setOnTouchListener { _, motionEvent ->
                if (motionEvent.action == KeyEvent.ACTION_DOWN) {
                    root.setTransition(R.id.startWithValue, R.id.end)
                    root.transitionToEnd()
                    setDimenAsTextSize(R.dimen.tokopedianow_quantity_editor_text_size_end_with_value)
                    setTextColor(getResourceColor(com.tokopedia.unifyprinciples.R.color.Unify_NN950))
                    setOnTouchListener(null)
                }
                true
            }
        }
    }

    fun setQuantity(quantity: Int) {
        binding.apply {
            if (quantity > DEFAULT_NUMBER) {
                counter = quantity
                editText.setText(counter.toString())
                binding.root.setTransition(R.id.startWithValue, R.id.end)
                setEditTextWhenStartingWithValueAnimation()
            }
        }
    }

    fun getQuantity(): Int = counter
}
