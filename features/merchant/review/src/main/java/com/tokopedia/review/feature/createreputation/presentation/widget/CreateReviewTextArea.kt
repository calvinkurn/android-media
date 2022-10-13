package com.tokopedia.review.feature.createreputation.presentation.widget

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.R
import com.tokopedia.review.databinding.WidgetCreateReviewTextAreaBinding
import com.tokopedia.review.feature.createreputation.presentation.uimodel.CreateReviewTextAreaTextUiModel
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewTextAreaUiState
import com.tokopedia.reviewcommon.uimodel.StringRes
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

class CreateReviewTextArea @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = Int.ZERO
) : BaseReviewCustomView<WidgetCreateReviewTextAreaBinding>(context, attrs, defStyleAttr) {

    companion object {
        const val PADDING_BOTTOM = 8
    }

    private val transitionHandler = TransitionHandler()
    private val textAreaListener = TextAreaListener()

    private var sourceName: CreateReviewTextAreaTextUiModel.Source? = null

    override val binding = WidgetCreateReviewTextAreaBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        binding.layoutTextArea.root.editText.addTextChangedListener(textAreaListener)
        binding.layoutTextArea.root.editText.onFocusChangeListener = textAreaListener
        binding.layoutTextArea.root.icon2.setOnClickListener(textAreaListener)
        binding.root.setOnClickListener(textAreaListener)
        binding.layoutTextArea.root.labelText.gone()
    }

    override fun shouldRequestClearFocusOnClick(event: MotionEvent): Boolean {
        val viewCoordinate = getExpandIconCoordinate()
        val inX = event.x.toInt() in viewCoordinate.first().first..viewCoordinate.first().second
        val inY = event.y.toInt() in viewCoordinate.last().first..viewCoordinate.last().second
        return inX && inY
    }

    override fun shouldRequestFocusOnClick(event: MotionEvent): Boolean {
        val viewCoordinate = getExpandIconCoordinate()
        val notInX = event.x.toInt() !in viewCoordinate.first().first..viewCoordinate.first().second
        val notInY = event.y.toInt() !in viewCoordinate.last().first..viewCoordinate.last().second
        return notInX && notInY
    }

    private fun showLoading() = transitionHandler.transitionToShowLoading()

    private fun WidgetCreateReviewTextAreaBinding.showTextArea(
        uiState: CreateReviewTextAreaUiState.Showing
    ) {
        transitionHandler.transitionToShowTextArea()
        setupTextArea(uiState)
    }

    private fun WidgetCreateReviewTextAreaBinding.setupTextArea(
        uiState: CreateReviewTextAreaUiState.Showing
    ) {
        setupExpandButton()
        setupText(uiState.reviewTextAreaTextUiModel)
        setupHint(uiState.hint)
        setupHelper(uiState.helper)
        setupFocus(uiState.hasFocus)
    }

    private fun WidgetCreateReviewTextAreaBinding.setupExpandButton() {
        if (sourceName == CreateReviewTextAreaTextUiModel.Source.CREATE_REVIEW_TEXT_AREA) {
            layoutTextArea.root.setSecondIcon(R.drawable.ic_expand)
        }
    }

    private fun WidgetCreateReviewTextAreaBinding.setupText(
        reviewTextAreaTextUiModel: CreateReviewTextAreaTextUiModel
    ) {
        with(layoutTextArea.root.editText) {
            if (reviewTextAreaTextUiModel.source != sourceName) {
                tag = reviewTextAreaTextUiModel.source
                setText(reviewTextAreaTextUiModel.text)
                tag = null
                setSelection(reviewTextAreaTextUiModel.text.length)
            }
        }
    }

    private fun WidgetCreateReviewTextAreaBinding.setupHint(hint: StringRes) {
        layoutTextArea.root.setPlaceholder(hint.getStringValueWithDefaultParam(context))
    }

    private fun WidgetCreateReviewTextAreaBinding.setupHelper(helper: StringRes) {
        if (sourceName == CreateReviewTextAreaTextUiModel.Source.CREATE_REVIEW_TEXT_AREA) {
            layoutTextArea.root.setMessage(helper.getStringValue(context))
        }
    }

    private fun WidgetCreateReviewTextAreaBinding.setupFocus(hasFocus: Boolean) {
        if (hasFocus) {
            layoutTextArea.root.requestFocus()
            layoutTextArea.root.editText.showKeyboard()
        } else {
            layoutTextArea.root.clearFocus()
            layoutTextArea.root.editText.hideKeyboard()
        }
    }

    private fun EditText.showKeyboard() {
        val imm = context.getSystemService(
            Context.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        imm.showSoftInput(this, 0)
    }

    private fun EditText.hideKeyboard() {
        val imm = context.getSystemService(
            Context.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun View.makeMatchParent() {
        val layoutParamsCopy = layoutParams
        layoutParamsCopy.width = ViewGroup.LayoutParams.MATCH_PARENT
        layoutParamsCopy.height = ViewGroup.LayoutParams.MATCH_PARENT
        layoutParams = layoutParamsCopy
    }

    private fun isChangedByUser(): Boolean {
        return binding.layoutTextArea.root.editText.tag == null
    }

    private fun getExpandIconCoordinate(): List<Pair<Int, Int>> {
        val viewCoordinate = IntArray(2).also { binding.layoutTextArea.root.icon2.getLocationOnScreen(it) }
        val viewStartX = viewCoordinate.first() - binding.layoutTextArea.root.icon2.left
        val viewEndX = viewStartX + binding.layoutTextArea.root.icon2.width + binding.layoutTextArea.root.icon2.right
        val viewStartY = viewCoordinate.last() - binding.layoutTextArea.root.icon2.top
        val viewEndY = viewStartY + binding.layoutTextArea.root.icon2.height + binding.layoutTextArea.root.icon2.bottom
        return listOf(
            viewStartX to viewEndX,
            viewStartY to viewEndY
        )
    }

    fun updateUi(
        uiState: CreateReviewTextAreaUiState,
        source: CreateReviewTextAreaTextUiModel.Source,
        continuation: Continuation<Unit>
    ) {
        this.sourceName = source
        when(uiState) {
            is CreateReviewTextAreaUiState.Loading -> showLoading()
            is CreateReviewTextAreaUiState.Showing -> binding.showTextArea(uiState)
        }
        if (sourceName == CreateReviewTextAreaTextUiModel.Source.CREATE_REVIEW_TEXT_AREA) {
            animateShow(onAnimationEnd = {
                continuation.resume(Unit)
            })
        } else {
            binding.root.makeMatchParent()
            binding.layoutTextArea.root.makeMatchParent()
            continuation.resume(Unit)
        }
    }

    fun setListener(newTextAreaListener: Listener) {
        textAreaListener.listener = newTextAreaListener
    }

    fun setMinLine(minLine: Int) {
        binding.layoutTextArea.root.minLine = minLine
    }

    fun setMaxLine(maxLine: Int) {
        binding.layoutTextArea.root.maxLine = maxLine
    }

    fun removeBorder() {
        binding.layoutTextArea.root.textInputLayout.boxStrokeWidth = Int.ZERO
        binding.layoutTextArea.root.textInputLayout.boxStrokeWidthFocused = Int.ZERO
    }

    private inner class TransitionHandler {
        private val fadeTransition by lazy(LazyThreadSafetyMode.NONE) {
            Fade().apply {
                duration = ANIMATION_DURATION
                addTarget(binding.layoutTextArea.root)
                addTarget(binding.layoutTextAreaLoading.root)
                interpolator = AccelerateInterpolator()
            }
        }

        private fun WidgetCreateReviewTextAreaBinding.showLoadingLayout() {
            layoutTextAreaLoading.root.show()
        }

        private fun WidgetCreateReviewTextAreaBinding.hideLoadingLayout() {
            layoutTextAreaLoading.root.gone()
        }

        private fun WidgetCreateReviewTextAreaBinding.showTextAreaLayout() {
            layoutTextArea.root.show()
        }

        private fun WidgetCreateReviewTextAreaBinding.hideTextAreaLayout() {
            layoutTextArea.root.gone()
        }

        private fun WidgetCreateReviewTextAreaBinding.beginDelayedTransition() {
            TransitionManager.beginDelayedTransition(root, fadeTransition)
        }

        fun transitionToShowTextArea() {
            with(binding) {
                beginDelayedTransition()
                hideLoadingLayout()
                showTextAreaLayout()
            }
        }

        fun transitionToShowLoading() {
            with(binding) {
                beginDelayedTransition()
                hideTextAreaLayout()
                showLoadingLayout()
            }
        }
    }

    private inner class TextAreaListener: OnFocusChangeListener, TextWatcher, OnClickListener {
        var listener: Listener? = null

        override fun onFocusChange(v: View?, hasFocus: Boolean) {
            listener?.onFocusChanged(hasFocus)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            if (isChangedByUser()) {
                sourceName?.let { listener?.onTextChanged(s?.toString().orEmpty(), it) }
            }
        }

        override fun onClick(v: View?) {
            when (v?.id) {
                binding.layoutTextArea.root.icon2.id -> {
                    listener?.onExpandButtonClicked()
                }
                binding.root.id -> {
                    if (sourceName == CreateReviewTextAreaTextUiModel.Source.CREATE_REVIEW_EXPANDED_TEXT_AREA) {
                        binding.layoutTextArea.root.requestFocus()
                        binding.layoutTextArea.root.editText.showKeyboard()
                    }
                }
            }
        }
    }

    interface Listener {
        fun onTextChanged(text: String, source: CreateReviewTextAreaTextUiModel.Source)
        fun onFocusChanged(hasFocus: Boolean)
        fun onExpandButtonClicked()
    }
}