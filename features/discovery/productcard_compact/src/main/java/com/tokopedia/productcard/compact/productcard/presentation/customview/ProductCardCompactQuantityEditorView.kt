package com.tokopedia.productcard.compact.productcard.presentation.customview

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.productcard.compact.R
import com.tokopedia.productcard.compact.common.util.ContextUtil.getActivityFromContext
import com.tokopedia.productcard.compact.common.util.ViewUtil.getDpFromDimen
import com.tokopedia.productcard.compact.common.util.ViewUtil.setDimenAsTextSize
import com.tokopedia.productcard.compact.databinding.LayoutProductCardCompactQuantityEditorViewBinding
import com.tokopedia.productcard.compact.productcard.helper.MotionLayoutTransitionImpl
import com.tokopedia.productcard.compact.productcard.helper.TextWatcherImpl
import com.tokopedia.productcard.compact.productcard.helper.TimerTaskImpl
import com.tokopedia.unifycomponents.BaseCustomView
import java.util.Timer
import java.util.TimerTask

@SuppressLint("ClickableViewAccessibility")
class ProductCardCompactQuantityEditorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : BaseCustomView(context, attrs) {

    companion object {
        private const val DELAY_TIME_BACK_TO_START_STATE = 2000L
        private const val DELAY_TIME_BEFORE_CHANGING_QUANTITY = 500L
        private const val MIN_NUMBER = 1
        private const val DEFAULT_NUMBER = 0
        private const val DEFAULT_DP = 0
        private const val NO_PROGRESS = 0F
        private const val HIDE_SOFT_INPUT_KEYBOARD_FLAG = 0
        private const val MAX_ALPHA = 1f
        private const val MIN_ALPHA = 0f
    }

    private var binding: LayoutProductCardCompactQuantityEditorViewBinding

    private var timer: Timer? = null
    private var timerTaskChangingQuantity: TimerTask? = null
    private var timerTaskCollapsingWidget: TimerTask? = null
    private var isAnimationRunning: Boolean = false
    private var counter: Int = DEFAULT_NUMBER
    private var text: String = ""
    private var executeTimerAfterTextChanged: Boolean = false

    var maxQuantity: Int = Int.MAX_VALUE
        set(value) {
            if (value != field) {
                field = value
                binding.setCounter()
            }
        }

    var minQuantity: Int = MIN_NUMBER
        set(value) {
            if (value != field) {
                field = value
                binding.setCounter()
            }
        }

    var enableQuantityEditor: Boolean = true
        set(value) {
            field = value
            loadLayoutDescription()
        }

    var isVariant: Boolean = false
    var hasBlockedAddToCart: Boolean = false

    // Cart quantity listener for non variant product
    var onQuantityChangedListener: ((counter: Int) -> Unit)? = null

    // Add to cart listener for variant product
    var onClickAddVariantListener: ((counter: Int) -> Unit)? = null
    var onBlockAddToCartListener: (() -> Unit)? = null
    var orderQuantity: Int = DEFAULT_NUMBER

    init {
        binding = LayoutProductCardCompactQuantityEditorViewBinding.inflate(LayoutInflater.from(context), this, true).apply {
            setupAddButton()
            setupSubButton()

            if (enableQuantityEditor) {
                setupEditText()
                safeRunBlock {
                    root.setTransitionListener(
                        MotionLayoutTransitionImpl(
                            onTransitionCompleted = {
                                onTransitionCompleted(it)
                            }
                        )
                    )
                }
            }
        }

        obtainAttributes(attrs)
    }

    private fun obtainAttributes(attrs: AttributeSet?) {
        val styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.ProductCardCompactQuantityEditorView)

        try {
            val quantityAttribute = styledAttrs.getInt(R.styleable.ProductCardCompactQuantityEditorView_quantity, DEFAULT_NUMBER)
            val maxAttribute = styledAttrs.getInt(R.styleable.ProductCardCompactQuantityEditorView_maxQuantity, maxQuantity)
            val minAttribute = styledAttrs.getInt(R.styleable.ProductCardCompactQuantityEditorView_minQuantity, minQuantity)

            if (quantityAttribute != DEFAULT_NUMBER) setQuantity(quantityAttribute)
            if (maxAttribute != maxQuantity) maxQuantity = maxAttribute
            if (minAttribute != minQuantity) minQuantity = minAttribute
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
        timerTaskChangingQuantity = TimerTaskImpl {
            onQuantityChangedListener?.invoke(counter)
        }
        timerTaskCollapsingWidget = TimerTaskImpl {
            binding.collapseAnimation()
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

    private fun getEnabledColor(isEnabled: Boolean): Int = if (isEnabled) getResourceColor(R.color.product_card_compact_dms_button_stroke_collapsed_color) else getResourceColor(R.color.product_card_compact_dms_button_stroke_expanded_color)

    private fun getResourceColor(id: Int): Int = ContextCompat.getColor(context, id)

    private fun LayoutProductCardCompactQuantityEditorViewBinding.safeRunBlock(block: () -> Unit) {
        if (ViewCompat.isLaidOut(root)) {
            block()
        } else {
            root.post(block)
        }
    }

    private fun LayoutProductCardCompactQuantityEditorViewBinding.onTransitionCompleted(
        currentState: Int?
    ) {
        executeTimerAfterTextChanged = false
        when (currentState) {
            R.id.end -> {
                executeTimer()
            }
            R.id.startWithValue -> {
                setEditTextPadding()
                isAnimationRunning = false
            }
            R.id.start -> {
                isAnimationRunning = false
            }
        }
    }

    private fun LayoutProductCardCompactQuantityEditorViewBinding.setCounter() {
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
            currentCounter
        }
    }

    private fun LayoutProductCardCompactQuantityEditorViewBinding.setupAddBtnLayout() {
        val size = context?.resources?.getDimensionPixelSize(
            R.dimen.product_card_compact_product_card_add_button_size
        ).orZero()

        val layoutParams = addButton.layoutParams
        layoutParams.height = size
        layoutParams.width = size

        addButton.layoutParams = layoutParams
        addButton.alpha = MAX_ALPHA
        addButton.show()
    }

    private fun LayoutProductCardCompactQuantityEditorViewBinding.setupAddButton() {
        addButton.setOnClickListener {
            if (hasBlockedAddToCart) {
                onBlockAddToCartListener?.invoke()
                return@setOnClickListener
            }

            if (isVariant) {
                onClickAddVariantListener?.invoke(if (counter > minQuantity) counter else minQuantity)
            } else {
                onClickAddNonVariant()
            }
        }
    }

    private fun loadLayoutDescription() {
        val animScene = R.xml.layout_product_card_compact_quantity_editor_custom_view_scene

        if (enableQuantityEditor) {
            binding.root.loadLayoutDescription(animScene)
        } else {
            val scene = R.xml.layout_product_card_compact_quantity_editor_no_anim_view_scene
            binding.root.getTransition(animScene)?.setEnable(false)
            binding.root.loadLayoutDescription(scene)
        }
    }

    private fun LayoutProductCardCompactQuantityEditorViewBinding.onClickAddNonVariant() {
        if (enableQuantityEditor) {
            onClickAddToCartWithAnimation()
        } else {
            onClickAddToCartWithoutAnimation()
        }
    }

    private fun LayoutProductCardCompactQuantityEditorViewBinding.onClickAddToCartWithAnimation() {
        executeTimerAfterTextChanged = false
        val currentState = root.currentState
        when {
            currentState == R.id.start -> {
                editText.setText(minQuantity.toString())
                expandAnimation(
                    startId = R.id.start,
                    endId = R.id.end
                )
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

    private fun LayoutProductCardCompactQuantityEditorViewBinding.onClickAddToCartWithoutAnimation() {
        root.setTransition(R.id.end, R.id.end)
        root.transitionToEnd()

        addButton.alpha = MIN_ALPHA
        addButton.gone()

        addToCartBtnShimmer.alpha = MAX_ALPHA
        addToCartBtnShimmer.show()

        onQuantityChangedListener?.invoke(minQuantity)
    }

    private fun LayoutProductCardCompactQuantityEditorViewBinding.setupSubButton() {
        subButton.setOnClickListener {
            counter--
            if (counter < minQuantity) {
                onQuantityChangedListener?.invoke(DEFAULT_NUMBER)
                collapseAnimation()
            } else {
                executeTimerAfterTextChanged = false
                editText.setText(counter.toString())
                executeTimer()
            }
            isAnimationRunning = true
        }
    }

    private fun LayoutProductCardCompactQuantityEditorViewBinding.setupEditText() {
        editText.onFocusChangedListener = { isFocused ->
            if (isFocused) {
                executeTimerAfterTextChanged = false
                executeTimer()
            }
        }

        editText.addTextChangedListener(
            TextWatcherImpl(
                onTextChanged = {
                    executeTimerAfterTextChanged = if (executeTimerAfterTextChanged) {
                        executeTimer()
                        false
                    } else {
                        timer != null
                    }
                },
                afterTextChanged = { editable ->
                    if (text != editable.toString()) {
                        text = editable.toString()
                        editText.setSelection(text.length)
                        setCounter()
                    }
                }
            )
        )

        editText.onEnterClickedListener = {
            if (hasBlockedAddToCart) {
                onBlockAddToCartListener?.invoke()
            } else {
                onQuantityChangedListener?.invoke(counter)
                collapseAnimation()
            }
        }

        editText.onAnyKeysClickedListener = {
            executeTimer()
        }
    }

    private fun LayoutProductCardCompactQuantityEditorViewBinding.collapseAnimation() {
        context.getActivityFromContext()?.runOnUiThread {
            editText.setDimenAsTextSize(
                R.dimen.product_card_compact_quantity_editor_text_size_start_with_value
            )
            editText.clearFocus()
            hideKeyboardFrom(context, editText)
            executeTimerAfterTextChanged = false

            if (counter < minQuantity || binding.editText.text?.isBlank() == true) {
                editText.text?.clear()
                root.setTransition(R.id.end, R.id.start)
            } else {
                root.setTransition(R.id.end, R.id.startWithValue)
            }

            root.transitionToEnd()
            cancelTimer()
        }
    }

    private fun LayoutProductCardCompactQuantityEditorViewBinding.setEditTextPadding() {
        editText.setPadding(
            getDpFromDimen(context, R.dimen.product_card_compact_quantity_editor_padding_horizontal).toInt(),
            DEFAULT_DP,
            getDpFromDimen(context, R.dimen.product_card_compact_quantity_editor_padding_horizontal).toInt(),
            DEFAULT_DP
        )
    }

    private fun LayoutProductCardCompactQuantityEditorViewBinding.expandAnimation(
        startId: Int,
        endId: Int
    ) {
        editText.setDimenAsTextSize(R.dimen.product_card_compact_quantity_editor_text_size_end_with_value)
        root.setTransition(startId, endId)
        root.transitionToEnd()
        setOnTouchListener(null)
    }

    private fun LayoutProductCardCompactQuantityEditorViewBinding.setStartTransition() {
        root.setTransition(
            R.id.start,
            R.id.end
        )
        root.progress = NO_PROGRESS

        editText.text?.clear()
        editText.clearFocus()
        setupAddBtnLayout()
    }

    private fun LayoutProductCardCompactQuantityEditorViewBinding.setStartTransitionWithValue() {
        root.setTransition(
            R.id.startWithValue,
            R.id.end
        )
        root.progress = NO_PROGRESS

        editText.apply {
            setDimenAsTextSize(R.dimen.product_card_compact_quantity_editor_text_size_start_with_value)
            setText(counter.toString())
            clearFocus()
            setEditTextPadding()
        }

        addButton.alpha = MIN_ALPHA
        addButton.gone()
    }

    private fun executeTimer() {
        cancelTimer()
        startTimer()
    }

    private fun hideKeyboardFrom(context: Context, view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, HIDE_SOFT_INPUT_KEYBOARD_FLAG)
    }

    private fun LayoutProductCardCompactQuantityEditorViewBinding.onSetQuantityWithAnimation(quantity: Int) {
        if (isAnimationRunning || counter == quantity) return
        counter = quantity

        if (quantity > DEFAULT_NUMBER) {
            setStartTransitionWithValue()
        } else {
            setStartTransition()
        }
    }

    private fun LayoutProductCardCompactQuantityEditorViewBinding.onSetQuantityWithoutAnimation(quantity: Int) {
        counter = quantity
        if (quantity > DEFAULT_NUMBER) {
            showCheckMarkIcon()
        } else {
            showAddButton()
        }
    }

    private fun LayoutProductCardCompactQuantityEditorViewBinding.showCheckMarkIcon() {
        root.setTransition(R.id.startWithValue, R.id.startWithValue)
        root.progress = NO_PROGRESS

        addToCartBtnShimmer.alpha = MIN_ALPHA
        addToCartBtnShimmer.gone()

        addButton.alpha = MIN_ALPHA
        addButton.gone()
    }

    private fun LayoutProductCardCompactQuantityEditorViewBinding.showAddButton() {
        root.setTransition(R.id.start, R.id.start)
        root.progress = NO_PROGRESS

        addToCartBtnShimmer.alpha = MIN_ALPHA
        addToCartBtnShimmer.gone()

        addButton.alpha = MAX_ALPHA
        addButton.show()
    }

    fun setQuantity(quantity: Int) {
        binding.apply {
            if (enableQuantityEditor) {
                onSetQuantityWithAnimation(quantity)
            } else {
                onSetQuantityWithoutAnimation(quantity)
            }
        }
    }

    fun getQuantity(): Int = counter
}
