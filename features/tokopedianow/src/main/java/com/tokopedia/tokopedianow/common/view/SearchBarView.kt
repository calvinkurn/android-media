package com.tokopedia.tokopedianow.common.view

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.tokopedianow.databinding.LayoutSearchBarViewBinding
import com.tokopedia.unifyprinciples.Typography

class SearchBarView (
    context: Context,
    attrs: AttributeSet)
: ConstraintLayout(context, attrs) {

    companion object {
        const val SOFT_INPUT_FLAG = 0
    }

    var onTextSubmitListener: ((String) -> Unit)? = null

    private val layout: ConstraintLayout? by lazy { binding?.layout }
    private val editText: EditText? by lazy { binding?.editText }
    private val backButton: IconUnify? by lazy { binding?.backButton }
    private val clearButton: IconUnify? by lazy { binding?.clearButton }

    private var binding: LayoutSearchBarViewBinding? = null

    private val searchNavigationOnClickListener = OnClickListener { view ->
        when {
            view === backButton -> clickBackButton()
            view === clearButton -> clickClearButton()
        }
    }

    var searchHint: String
        get() = editText?.hint.toString()
        set(value) {
            editText?.hint = value
        }

    init {
        initiateView()
    }

    override fun setBackground(
        background: Drawable?
    ) {
        layout?.background = background
    }

    override fun setBackgroundColor(
        color: Int
    ) {
        layout?.setBackgroundColor(color)
    }

    override fun clearFocus() {
        super.clearFocus()
        hideKeyboard(
            view = this
        )
        editText?.clearFocus()
    }

    private fun initiateView() {
        binding = LayoutSearchBarViewBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )

        binding?.apply {
            configureSearchNavigationLayout(
                binding = this
            )
            setSearchNavigationListener(
                binding = this
            )
            initSearchView(
                binding = this
            )
        }
    }

    private fun configureSearchNavigationLayout(
        binding: LayoutSearchBarViewBinding
    ) {
        configureSearchNavigationEditText(
            binding = binding
        )
    }

    private fun configureSearchNavigationEditText(
        binding: LayoutSearchBarViewBinding
    ) {
        val context = binding.editText.context
        binding.editText.setHintTextColor(
            ContextCompat.getColor(
                context,
                com.tokopedia.unifyprinciples.R.color.Unify_NN950_32
            )
        )
    }

    private fun setSearchNavigationListener(
        binding: LayoutSearchBarViewBinding
    ) {
        binding.apply {
            backButton.setOnClickListener(
                searchNavigationOnClickListener
            )
            clearButton.setOnClickListener(
                searchNavigationOnClickListener
            )
        }
    }

    private fun initSearchView(
        binding: LayoutSearchBarViewBinding
    ) {
        binding.editText.apply {
            showKeyboard(this)

            typeface = Typography.getFontType(
                    context = context,
                    isBold = false,
                    fontVariant = Typography.DISPLAY_2
                )

            afterTextChanged { text ->
                clearButton?.showWithCondition(
                    shouldShow = text.isNotBlank()
                )
            }

            setOnEditorActionListener { _, _, _ ->
                submitText(
                    text = text?.trim().toString()
                )
                hideKeyboard(
                    view = this
                )
                true
            }
        }
    }

    private fun submitText(
        text: String
    ) {
        if (text.isNotBlank()) {
            onTextSubmitListener?.invoke(text)
        }
    }

    private fun showKeyboard(
        view: View
    ) {
        view.requestFocus()

        val imm = view.context?.getSystemService(
            Context.INPUT_METHOD_SERVICE
        ) as? InputMethodManager

        imm?.showSoftInput(
            view,
            SOFT_INPUT_FLAG
        )
    }

    private fun hideKeyboard(
        view: View
    ) {
        val imm = view.context.getSystemService(
            Context.INPUT_METHOD_SERVICE
        ) as? InputMethodManager

        imm?.hideSoftInputFromWindow(
            view.windowToken,
            SOFT_INPUT_FLAG
        )
    }

    private fun clickBackButton() {
        val activity = context as? Activity
        KeyboardHandler.DropKeyboard(
            activity,
            editText
        )
        activity?.onBackPressed()
    }

    private fun clickClearButton() {
        editText?.text?.clear()
        clearButton?.hide()
        hideKeyboard(
            view = this
        )
    }
}
