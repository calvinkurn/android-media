package com.tokopedia.tokopedianow.common.view

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.discovery.common.model.SearchParameter
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

    private val searchParameter: SearchParameter by lazy { SearchParameter() }
    private val layout: ConstraintLayout? by lazy { binding?.layout }
    private val editText: EditText? by lazy { binding?.editText }
    private val backButton: IconUnify? by lazy { binding?.backButton }
    private val clearButton: IconUnify? by lazy { binding?.clearButton }

    private var binding: LayoutSearchBarViewBinding? = null
    private var listener: OnTextListener? = null

    private val searchNavigationOnClickListener = OnClickListener { view ->
        when {
            view === backButton -> {
                val activity = context as? Activity
                KeyboardHandler.DropKeyboard(activity, editText)
                activity?.onBackPressed()
            }
            view === clearButton -> {
                editText?.text?.clear()
                clearButton?.hide()
                hideKeyboard(this)
            }
        }
    }

    init {
        initiateView()
    }

    override fun setBackground(background: Drawable?) {
        layout?.background = background
    }

    override fun setBackgroundColor(color: Int) {
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
        binding = LayoutSearchBarViewBinding.inflate(LayoutInflater.from(context), this, true)

        binding?.apply {
            configureSearchNavigationLayout(this)
            setSearchNavigationListener(this)
            initSearchView(this)
        }
    }

    private fun configureSearchNavigationLayout(binding: LayoutSearchBarViewBinding) {
        configureSearchNavigationEditText(binding)
    }

    private fun configureSearchNavigationEditText(binding: LayoutSearchBarViewBinding) {
        val context = binding.editText.context
        binding.editText.setHintTextColor(
            ContextCompat.getColor(
                context,
                com.tokopedia.unifyprinciples.R.color.Unify_N700_32
            )
        )
    }

    private fun setSearchNavigationListener(binding: LayoutSearchBarViewBinding) {
        binding.apply {
            backButton.setOnClickListener(searchNavigationOnClickListener)
            clearButton.setOnClickListener(searchNavigationOnClickListener)
        }
    }

    private fun initSearchView(binding: LayoutSearchBarViewBinding) {
        binding.editText.apply {
            showKeyboard(this)

            typeface = Typography
                .getFontType(
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
                    text = text.toString()
                )
                hideKeyboard(
                    view = this
                )
                true
            }
        }
    }

    private fun submitText(text: CharSequence) {
        searchParameter.setSearchQuery(text.toString().trim())
        listener?.onTextSubmit(searchParameter)
    }

    private fun showKeyboard(view: View) {
        view.requestFocus()
        val imm = view.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, SOFT_INPUT_FLAG)
    }

    private fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, SOFT_INPUT_FLAG)
    }

    fun setQueryTextListener(onQueryListener: OnTextListener) {
        listener = onQueryListener
    }

    fun setHint(hint: String) {
        editText?.hint = hint
    }

    fun setText(text: String) {
        editText?.setText(text)
    }

    interface OnTextListener {
        fun onTextSubmit(searchParameter: SearchParameter): Boolean
    }
}