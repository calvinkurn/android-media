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
        private const val DELAY_EXECUTION_TIME = 1000L
        private const val MIN_NUMBER = 1
        private const val DEFAULT_DP = 0
        private const val NO_PROGRESS_ANIMATION = 0F
    }

    private var binding: LayoutTokopedianowQuantityEditorCustomViewBinding

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null
    private var counter = MIN_NUMBER
    private var text: String = ""

    var maxNumber: Int = Int.MAX_VALUE
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
                        currentState = motionLayout.currentState,
                        binding = this@apply
                    )

                    override fun onTransitionCompleted(motionLayout: MotionLayout, p1: Int) = onAnimationFinished(
                        currentState = motionLayout.currentState,
                        binding = this@apply
                    )

                    override fun onTransitionChange(motionLayout: MotionLayout?, p1: Int, p2: Int, p3: Float) { /* nothing to do */ }

                    override fun onTransitionTrigger(motionLayout: MotionLayout?, p1: Int, p2: Boolean, p3: Float) { /* nothing to do */ }
                }
            )
        }
    }

    private fun onAnimationStarted(currentState: Int, binding: LayoutTokopedianowQuantityEditorCustomViewBinding) {
        if (currentState == R.id.startWithValue) {
            binding.setEditTextWhenStartingWithValueAnimation()
        }
    }

    private fun onAnimationFinished(currentState: Int, binding: LayoutTokopedianowQuantityEditorCustomViewBinding) {
        if (currentState == R.id.end) {
            binding.editText.requestFocus()
            binding.editText.setOnTouchListener(null)
            cancelTimer()
            startTimer()
        }
    }

    private fun startTimer() {
        timer = Timer()
        initializeTimerTask()
        timer?.schedule(
            timerTask,
            DELAY_EXECUTION_TIME
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
            if (isBlank()) MIN_NUMBER else toInt()
        } catch (e: Exception) {
            maxNumber
        }
    }

    private fun getEnabledColor(isEnabled: Boolean): Int = if (isEnabled) getResourceColor(com.tokopedia.unifyprinciples.R.color.Unify_GN500) else getResourceColor(com.tokopedia.unifyprinciples.R.color.Unify_NN300)

    private fun getResourceColor(id: Int): Int = ContextCompat.getColor(context, id)

    private fun LayoutTokopedianowQuantityEditorCustomViewBinding.setCounter() {
        val currentCounter = text.getCounterOrDefaultValue()
        counter = if (maxNumber == MIN_NUMBER) {
            editText.setText(MIN_NUMBER.toString())
            quantityEditorAddButton.setColorFilter(getEnabledColor(false))
            quantityEditorSubButton.setColorFilter(getEnabledColor(false))
            MIN_NUMBER
        } else if (currentCounter >= maxNumber) {
            editText.setText(maxNumber.toString())
            quantityEditorAddButton.setColorFilter(getEnabledColor(false))
            quantityEditorSubButton.setColorFilter(getEnabledColor(true))
            maxNumber
        } else if (currentCounter <= MIN_NUMBER) {
            editText.setText(MIN_NUMBER.toString())
            quantityEditorAddButton.setColorFilter(getEnabledColor(true))
            quantityEditorSubButton.setColorFilter(getEnabledColor(false))
            MIN_NUMBER
        }  else {
            quantityEditorAddButton.setColorFilter(getEnabledColor(true))
            quantityEditorSubButton.setColorFilter(getEnabledColor(true))
            currentCounter
        }
    }

    private fun LayoutTokopedianowQuantityEditorCustomViewBinding.setupAddButton() {
        quantityEditorAddButton.setOnClickListener {
            if (counter < maxNumber) {
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
            if (counter > MIN_NUMBER) {
                counter--
                editText.setText(counter.toString())
            }
        }
    }

    private fun LayoutTokopedianowQuantityEditorCustomViewBinding.setupEditText() {
        editText.focusChangedListener = { isFocused ->
            if (!isFocused) {
                backToTheStartState()
            }
        }

        editText.afterTextChanged {
            if (text != it) {
                text = it
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
        if (editText.text.isNullOrBlank()) {
            root.setTransition(R.id.end, R.id.start)
        } else {
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

    fun setText(text: String) {
        binding.apply {
            if (text.isNotBlank()) {
                counter = text.getCounterOrDefaultValue()
                editText.setText(counter.toString())
                binding.root.setTransition(R.id.startWithValue, R.id.end)
                setEditTextWhenStartingWithValueAnimation()
            }
        }
    }

    fun getText(): String = text
}
