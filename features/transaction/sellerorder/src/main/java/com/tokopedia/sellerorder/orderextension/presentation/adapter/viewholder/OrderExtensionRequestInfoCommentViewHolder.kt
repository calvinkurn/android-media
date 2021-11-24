package com.tokopedia.sellerorder.orderextension.presentation.adapter.viewholder

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.Utils.hideKeyboard
import com.tokopedia.sellerorder.databinding.ItemOrderExtensionRequestInfoCommentBinding
import com.tokopedia.sellerorder.orderextension.presentation.model.OrderExtensionRequestInfoUiModel
import com.tokopedia.unifycomponents.TextAreaUnify2
import com.tokopedia.utils.view.binding.viewBinding

class OrderExtensionRequestInfoCommentViewHolder(
    itemView: View?,
    private val listener: OrderExtensionRequestInfoCommentListener
) : BaseOrderExtensionRequestInfoViewHolder<OrderExtensionRequestInfoUiModel.CommentUiModel>(
    itemView
) {

    companion object {
        val LAYOUT = R.layout.item_order_extension_request_info_comment

        private const val DEFAULT_MIN_LINE = 4
        private const val DEFAULT_MAX_LINE = 4
        private const val DELAY_FOCUS_TEXT_AREA = 500L
    }

    private val binding by viewBinding<ItemOrderExtensionRequestInfoCommentBinding>()
    private val textWatcher by lazy { createCommentTextWatcher() }
    private val focusChangeListener by lazy { createFocusChangeListener() }

    init {
        setupDefaultState()
    }

    override fun bind(element: OrderExtensionRequestInfoUiModel.CommentUiModel?) {
        super.bind(element)
        binding?.root?.run {
            element?.let {
                setupMessage(it.showedMessage, it.error)
                setupComment()
                onFocusChange(it.hasFocus)
                scheduleRequestFocus()
            }
        }
    }

    override fun bind(
        element: OrderExtensionRequestInfoUiModel.CommentUiModel?,
        payloads: MutableList<Any>
    ) {
        super.bind(element, payloads)
        payloads.firstOrNull()?.let {
            if (it is Pair<*, *>) {
                val (oldItem, newItem) = it
                if (oldItem is OrderExtensionRequestInfoUiModel.CommentUiModel && newItem is OrderExtensionRequestInfoUiModel.CommentUiModel) {
                    if (oldItem.showedMessage != newItem.showedMessage || oldItem.error != newItem.error) {
                        binding?.root?.setupMessage(newItem.showedMessage, newItem.error)
                    }
                    if (oldItem.optionCode != newItem.optionCode) {
                        binding?.root?.setupComment()
                        onFocusChange(newItem.hasFocus)
                        scheduleRequestFocus()
                    }
                    return
                }
            }
        }
    }

    override fun onViewAttachedFromWindow() {
        super.onViewDetachedFromWindow()
        setupListeners()
    }

    override fun onViewDetachedFromWindow() {
        super.onViewDetachedFromWindow()
        binding?.root?.hideKeyboard()
        removeListeners()
    }

    private fun createCommentTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                handleCommentTextChange(s?.toString().orEmpty())
            }

            override fun afterTextChanged(s: Editable?) {}
        }
    }

    private fun createFocusChangeListener(): View.OnFocusChangeListener {
        return View.OnFocusChangeListener { _, hasFocus ->
            onFocusChange(hasFocus)
        }
    }

    private fun setupDefaultState() {
        binding?.root?.run {
            setupHeight(DEFAULT_MIN_LINE, DEFAULT_MAX_LINE)
        }
    }

    private fun setupListeners() {
        setupCommentChangeListener()
        setupFocusChangeListener()
    }

    private fun removeListeners() {
        removeCommentChangeListener()
        removeFocusChangeListener()
    }

    private fun setupCommentChangeListener() {
        binding?.root?.editText?.addTextChangedListener(textWatcher)
    }

    private fun removeCommentChangeListener() {
        binding?.root?.editText?.removeTextChangedListener(textWatcher)
    }

    private fun setupFocusChangeListener() {
        binding?.root?.editText?.onFocusChangeListener = focusChangeListener
    }

    private fun removeFocusChangeListener() {
        binding?.root?.editText?.onFocusChangeListener = null
    }

    private fun TextAreaUnify2.setupComment() {
        editText.setText(element?.value.orEmpty())
    }

    private fun TextAreaUnify2.setupMessage(showedMessage: String, error: Boolean) {
        setMessage(showedMessage)
        isInputError = error
    }

    private fun TextAreaUnify2.setupHeight(min: Int, max: Int) {
        minLine = min
        maxLine = max
    }

    private fun scheduleRequestFocus() {
        binding?.root?.postDelayed({
            requestFocus()
        }, DELAY_FOCUS_TEXT_AREA)
    }

    private fun requestFocus() {
        binding?.root?.run {
            if (element?.hasFocus == true && editText.requestFocus()) {
                editText.setSelection(editText.text.length)
                showKeyboard(editText)
            }
        }
    }

    private fun showKeyboard(editText: EditText) {
        val imm = editText.context?.getSystemService(Context.INPUT_METHOD_SERVICE)
        (imm as? InputMethodManager)?.showSoftInput(editText, 0)
    }

    private fun onFocusChange(hasFocus: Boolean) {
        if (adapterPosition != RecyclerView.NO_POSITION) {
            element?.run {
                this.hasFocus = hasFocus
                listener.onCommentChange(this)
            }
        }
    }

    private fun handleCommentTextChange(text: String) {
        if (adapterPosition != RecyclerView.NO_POSITION) {
            element?.run {
                if (value != text) {
                    value = text
                    listener.onCommentChange(this)
                }
            }
        }
    }

    interface OrderExtensionRequestInfoCommentListener {
        fun onCommentChange(element: OrderExtensionRequestInfoUiModel.CommentUiModel)
    }
}
