package com.tokopedia.tokopedianow.common.view.productcard

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.orZero
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
        private const val DELAY_TIME_BEFORE_CHANGING_QUANTITY = 1000L
        private const val MIN_NUMBER = 1
        private const val DEFAULT_NUMBER = 0
        private const val DEFAULT_DP = 0
        private const val NO_PROGRESS = 0F
        private const val HIDE_SOFT_INPUT_KEYBOARD_FLAG = 0
        private const val MAX_ALPHA = 1f
    }

    private var binding: LayoutTokopedianowQuantityEditorViewBinding

    private var timer: Timer? = null
    private var timerTaskChangingQuantity: TimerTask? = null
    private var timerTaskCollapsingWidget: TimerTask? = null
    private var isAnimationRunning = false
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

    var isVariant: Boolean = false
    var onClickListener: (counter: Int) -> Unit = {}
    var onClickVariantListener: (counter: Int) -> Unit = {}

    init {
        binding = LayoutTokopedianowQuantityEditorViewBinding.inflate(LayoutInflater.from(context), this, true).apply {
            setupAddButton()
            setupSubButton()
            setupEditText()

            safeRunBlock {
                root.setTransitionListener(
                    object : MotionLayout.TransitionListener {
                        override fun onTransitionCompleted(motionLayout: MotionLayout, p1: Int) = onTransitionCompleted(
                            currentState = motionLayout.currentState
                        )

                        override fun onTransitionStarted(motionLayout: MotionLayout, p1: Int, p2: Int) { /* nothing to do */ }

                        override fun onTransitionChange(motionLayout: MotionLayout?, p1: Int, p2: Int, p3: Float) { /* nothing to do */ }

                        override fun onTransitionTrigger(motionLayout: MotionLayout?, p1: Int, p2: Boolean, p3: Float) { /* nothing to do */ }
                    }
                )
            }
        }

        obtainAttributes(attrs)
    }

    private fun obtainAttributes(attrs: AttributeSet?) {
        val styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.TokoNowQuantityEditorView)

        try {
            setQuantity(styledAttrs.getInt(R.styleable.TokoNowQuantityEditorView_quantity, DEFAULT_NUMBER))
            maxQuantity = styledAttrs.getInt(R.styleable.TokoNowQuantityEditorView_maxQuantity, maxQuantity)
            minQuantity = styledAttrs.getInt(R.styleable.TokoNowQuantityEditorView_minQuantity, minQuantity)
        } finally {
            styledAttrs.recycle()
        }
    }

    private fun startTimer() {
        timer = Timer()
        initializeTimerTask()
        timer?.schedule(
            timerTaskChangingQuantity,
            DELAY_TIME_BEFORE_CHANGING_QUANTITY
        )
        timer?.schedule(
            timerTaskCollapsingWidget,
            DELAY_TIME_BACK_TO_START_STATE
        )
    }

    private fun initializeTimerTask() {
        timerTaskChangingQuantity = object : TimerTask() {
            override fun run() {
                onClickListener(counter)
            }
        }
        timerTaskCollapsingWidget = object : TimerTask() {
            override fun run() {
                context.getActivityFromContext()?.runOnUiThread {
                    binding.collapseAnimation()
                }
            }
        }
    }

    private fun cancelTimer() {
        if (timer != null) {
            timer?.cancel()
            timer?.purge()
            timer = null
            timerTaskChangingQuantity?.cancel()
            timerTaskChangingQuantity = null
            timerTaskCollapsingWidget?.cancel()
            timerTaskCollapsingWidget = null
        }
    }

    private fun getEnabledColor(isEnabled: Boolean): Int = if (isEnabled) getResourceColor(R.color.tokopedianow_product_card_dms_button_stroke_collapsed_color) else getResourceColor(R.color.tokopedianow_product_card_dms_button_stroke_expanded_color)

    private fun getResourceColor(id: Int): Int = ContextCompat.getColor(context, id)

    private fun LayoutTokopedianowQuantityEditorViewBinding.safeRunBlock(block: () -> Unit) {
        if (ViewCompat.isLaidOut(root)) {
            block()
        } else {
            root.post(block)
        }
    }

    private fun LayoutTokopedianowQuantityEditorViewBinding.onTransitionCompleted(
        currentState: Int
    ) {
        when (currentState) {
            R.id.end -> executeTimer()
            R.id.startWithValue -> {
                setEditTextPadding()
                isAnimationRunning = false
            }
            R.id.start -> {
                isAnimationRunning = false
            }
        }
    }

    private fun LayoutTokopedianowQuantityEditorViewBinding.setCounter() {
        val currentCounter = text.toIntOrZero()
        counter = if (currentCounter >= maxQuantity) {
            editText.setText(maxQuantity.toString())
            editText.setSelection(text.length)
            addButton.setColorFilter(
                getEnabledColor(
                    isEnabled = false
                )
            )
            maxQuantity
        } else {
            addButton.setColorFilter(
                getEnabledColor(
                    isEnabled = true
                )
            )
            setupAddBtnLayout()
            currentCounter
        }
    }

    private fun LayoutTokopedianowQuantityEditorViewBinding.setupAddBtnLayout() {
        val size = context?.resources?.getDimensionPixelSize(
            R.dimen.tokopedianow_product_card_add_button_size
        ).orZero()

        val layoutParams = addButton.layoutParams
        layoutParams.height = size
        layoutParams.width = size

        addButton.layoutParams = layoutParams
        addButton.alpha = MAX_ALPHA
    }

    private fun LayoutTokopedianowQuantityEditorViewBinding.setupAddButton() {
        addButton.setOnClickListener {
            if (isVariant) {
                onClickVariantListener(if (counter > minQuantity) counter else minQuantity)
            } else {
                onClickAddNonVariant()
            }
        }
    }

    private fun LayoutTokopedianowQuantityEditorViewBinding.onClickAddNonVariant() {
        val currentState = root.currentState
        when {
            currentState == R.id.start -> {
                expandAnimation(
                    startId = R.id.start,
                    endId = R.id.end
                )
                editText.setText(minQuantity.toString())
            }
            currentState == R.id.startWithValue -> {
                expandAnimation(
                    startId = R.id.startWithValue,
                    endId = R.id.end
                )
            }
            counter < maxQuantity -> {
                counter++
                editText.setText(counter.toString())
            }
        }
        isAnimationRunning = true
        executeTimer()
    }

    private fun LayoutTokopedianowQuantityEditorViewBinding.setupSubButton() {
        subButton.setOnClickListener {
            counter--
            if (counter < minQuantity) {
                onClickListener(counter)
                collapseAnimation()
            } else {
                editText.setText(counter.toString())
                executeTimer()
            }
            isAnimationRunning = true
        }
    }

    private fun LayoutTokopedianowQuantityEditorViewBinding.setupEditText() {
        editText.focusChangedListener = { isFocused ->
            if (isFocused) executeTimer()
        }

        editText.afterTextChanged {
            if (text != it) {
                text = it
                editText.setSelection(text.length)
                setCounter()
            }
        }

        editText.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                onClickListener(counter)
                collapseAnimation()
                true
            } else if (keyEvent.action == KeyEvent.ACTION_UP) {
                executeTimer()
                true
            } else {
                false
            }
        }
    }

    private fun LayoutTokopedianowQuantityEditorViewBinding.collapseAnimation() {
        editText.clearFocus()
        editText.setDimenAsTextSize(R.dimen.tokopedianow_quantity_editor_text_size_start_with_value)
        hideKeyboardFrom(context, editText)

        if (counter < minQuantity || binding.editText.text?.isBlank() == true) {
            counter = 0
            editText.text?.clear()
            root.setTransition(R.id.end, R.id.start)
        } else {
            editText.setText(counter.toString())
            root.setTransition(R.id.end, R.id.startWithValue)
        }

        root.transitionToEnd()
        cancelTimer()
    }

    private fun LayoutTokopedianowQuantityEditorViewBinding.setEditTextPadding() {
        editText.setPadding(
            getDpFromDimen(context, R.dimen.tokopedianow_quantity_editor_padding_horizontal).toInt(),
            DEFAULT_DP,
            getDpFromDimen(context, R.dimen.tokopedianow_quantity_editor_padding_horizontal).toInt(),
            DEFAULT_DP
        )
    }

    private fun LayoutTokopedianowQuantityEditorViewBinding.expandAnimation(
        startId: Int,
        endId: Int
    ) {
        editText.setDimenAsTextSize(R.dimen.tokopedianow_quantity_editor_text_size_end_with_value)
        root.setTransition(startId, endId)
        root.transitionToEnd()
        setOnTouchListener(null)
    }

    private fun LayoutTokopedianowQuantityEditorViewBinding.setStartTransition() {
        root.setTransition(
            R.id.start,
            R.id.end
        )
        root.progress = NO_PROGRESS

        editText.text?.clear()
        editText.clearFocus()
    }

    private fun LayoutTokopedianowQuantityEditorViewBinding.setStartTransitionWithValue() {
        root.setTransition(
            R.id.startWithValue,
            R.id.end
        )
        root.progress = NO_PROGRESS

        editText.apply {
            setDimenAsTextSize(R.dimen.tokopedianow_quantity_editor_text_size_start_with_value)
            setText(counter.toString())
            clearFocus()
            setEditTextPadding()
        }
    }

    private fun executeTimer() {
        cancelTimer()
        startTimer()
    }

    private fun hideKeyboardFrom(context: Context, view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, HIDE_SOFT_INPUT_KEYBOARD_FLAG)
    }

    fun setQuantity(quantity: Int) {
        binding.apply {
            if (isAnimationRunning || counter == quantity) return@apply

            if (quantity > DEFAULT_NUMBER) {
                counter = quantity
                setStartTransitionWithValue()
            } else {
                setStartTransition()
            }
        }
    }
}
