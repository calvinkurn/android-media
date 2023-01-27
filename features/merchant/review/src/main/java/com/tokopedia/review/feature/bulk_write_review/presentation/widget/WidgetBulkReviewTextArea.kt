package com.tokopedia.review.feature.bulk_write_review.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.R
import com.tokopedia.review.databinding.WidgetBulkReviewTextAreaBinding
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewTextAreaUiState
import com.tokopedia.review.feature.createreputation.presentation.widget.BaseReviewCustomView

class WidgetBulkReviewTextArea(
    context: Context,
    attrs: AttributeSet?
) : BaseReviewCustomView<WidgetBulkReviewTextAreaBinding>(context, attrs) {

    companion object {
        private const val MAX_LINES = 3
        private const val MIN_LINES = 3
    }

    override val binding = WidgetBulkReviewTextAreaBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )
    private var listener: Listener? = null

    init {
        setupTextArea()
        setupListener()
    }

    fun updateUiState(uiState: BulkReviewTextAreaUiState, animate: Boolean) {
        when (uiState) {
            is BulkReviewTextAreaUiState.Hidden -> {
                animateHide(animate = animate, onAnimationEnd = { gone() })
            }
            is BulkReviewTextAreaUiState.Showing -> {
                if (binding.root.editText.text.toString() != uiState.text) {
                    binding.root.editText.setText(uiState.text)
                }
                binding.root.setPlaceholder(uiState.hint.getStringValue(context))
                animateShow(animate = animate, onAnimationStart = {
                    show()
                }, onAnimationEnd = {
                        if (uiState.focused) binding.root.requestFocus()
                    })
            }
        }
    }

    fun setListener(newListener: Listener) {
        listener = newListener
    }

    private fun setupTextArea() {
        binding.root.minLine = MIN_LINES
        binding.root.maxLine = MAX_LINES
        binding.root.setSecondIcon(R.drawable.ic_expand)
        binding.root.editText.imeOptions = EditorInfo.IME_ACTION_DONE
        binding.root.labelText.gone()
    }

    private fun setupListener() {
        binding.root.editText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                listener?.onGainFocus(view)
            } else {
                listener?.onLostFocus(view, binding.root.editText.text?.toString().orEmpty())
            }
        }
        binding.root.icon2.setOnClickListener {
            listener?.onExpandTextArea(binding.root.editText.text?.toString().orEmpty())
        }
    }

    interface Listener {
        fun onGainFocus(view: View)
        fun onLostFocus(view: View, text: String)
        fun onExpandTextArea(text: String)
    }
}
