package com.tokopedia.tokopedianow.common.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.LayoutTokopedianowQuantityEditorCustomViewBinding
import com.tokopedia.unifycomponents.BaseCustomView
import java.util.*


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
    }

    private var binding: LayoutTokopedianowQuantityEditorCustomViewBinding

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null
    private var counter = 1
    private var textLength = 0

    var maxNumber: Int = 100
        set(value) {
            field = value
        }

    var text: String = ""

    init {
        binding = LayoutTokopedianowQuantityEditorCustomViewBinding.inflate(LayoutInflater.from(context),this, true).apply {
            quantityEditorAddButton.setOnClickListener {
                if (counter < maxNumber) {
                    if (!editText.isFocused) {
                        root.setTransition(R.id.start, R.id.end)
                        root.transitionToEnd()
                        editText.setText(counter.toString())
                    } else {
                        counter++
                        editText.setText(counter.toString())
                    }
                }
            }

            quantityEditorSubButton.setOnClickListener {
                if (counter > MIN_NUMBER) {
                    counter--
                    editText.setText(counter.toString())
                }
            }

            editText.focusChangedListener = { isFocused ->
                if (!isFocused) {
                    startTimer()
                }
            }

            editText.afterTextChanged {
                if (text != it) {
                    text = it
                    val currentCounter = text.toIntSafely()
                    counter = if (currentCounter >= maxNumber) {
                        editText.setText(maxNumber.toString())
                        quantityEditorAddButton.setColorFilter(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN300))
                        maxNumber
                    } else if (currentCounter <= MIN_NUMBER) {
                        editText.setText(MIN_NUMBER.toString())
                        quantityEditorSubButton.setColorFilter(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN300))
                        MIN_NUMBER
                    } else {
                        quantityEditorAddButton.setColorFilter(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
                        quantityEditorSubButton.setColorFilter(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
                        editText.setText(currentCounter.toString())
                        currentCounter
                    }
                }
            }

            root.setTransitionListener(object : MotionLayout.TransitionListener {
                    override fun onTransitionStarted(motionLayout: MotionLayout, p1: Int, p2: Int) {
                        if (motionLayout.currentState == R.id.startWithValue) {
                            editText.apply {
                                setPadding(
                                    getDpFromDimen(R.dimen.tokopedianow_quantity_editor_padding_horizontal).toInt(),
                                    DEFAULT_DP,
                                    getDpFromDimen(R.dimen.tokopedianow_quantity_editor_padding_horizontal).toInt(),
                                    DEFAULT_DP
                                )
                                setDimenAsTextSize(R.dimen.tokopedianow_quantity_editor_text_size_start_with_value)
                                setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
                                setOnTouchListener { _, motionEvent ->
                                    if (motionEvent.action == KeyEvent.ACTION_DOWN) {
                                        root.setTransition(R.id.startWithValue, R.id.end)
                                        root.transitionToEnd()
                                    }
                                    true
                                }
                                layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                            }
                        } else if (motionLayout.currentState == R.id.end) {
                            editText.apply {
                                setDimenAsTextSize(R.dimen.tokopedianow_quantity_editor_text_size_end_with_value)
                                setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950))
                            }
                        }
                    }

                    override fun onTransitionChange(motionLayout: MotionLayout?, p1: Int, p2: Int, p3: Float) { /* nothing to do */ }

                    override fun onTransitionCompleted(motionLayout: MotionLayout?, p1: Int) {
                        if (motionLayout?.currentState == R.id.end) {
                            editText.requestFocus()
                            editText.setOnTouchListener(null)
                            editText.setSelection(editText.text?.length.orZero())
                        } else {
                            cancelTask()
                        }
                    }

                    override fun onTransitionTrigger(motionLayout: MotionLayout?, p1: Int, p2: Boolean, p3: Float) { /* nothing to do */ }
                }
            )
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
                binding.apply {
                    if (editText.text.isNullOrBlank()) {
                        root.setTransition(R.id.end, R.id.start)
                    } else {
                        root.setTransition(R.id.end, R.id.startWithValue)
                    }
                    root.transitionToEnd()
                }
            }
        }
    }

    private fun cancelTask() {
        if (timer != null) {
            timer?.cancel()
            timerTask?.cancel()
            timerTask =  null
            timer = null
        }
    }

    fun TextView.setDimenAsTextSize(id: Int) {
        setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            context.resources.getDimension(id)
        )
    }

    fun getDpFromDimen(id: Int): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PX,
            context.resources.getDimension(id),
            resources.displayMetrics
        )
    }
}
