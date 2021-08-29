package com.tokopedia.exploreCategory.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.TextFieldUnify2

class AffiliateLinkTextField(context: Context, attrs: AttributeSet) : TextFieldUnify2(context,attrs) {

    private var relatedView : View? = null

    init {
        labelText.hide()
        textInputLayout.isHelperTextEnabled = false
        editText.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus)
                editingState(true)
        }
        editText.setOnClickListener {
            editingState(true)
        }
    }

    fun setRelatedView(relatedView : View){
        relatedView.setOnClickListener {
            editingState(false)
        }
        this.relatedView = relatedView
    }

    private fun showKeyboard(view: View) {
        view.requestFocus()
        val imm = view.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.showSoftInput(view, 0)
    }

    private fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun editingState(state : Boolean){
        if(state){
            relatedView?.show()
            showKeyboard(editText)
        }else {
            relatedView?.hide()
            hideKeyboard(editText)
        }
    }

    override fun dispatchKeyEventPreIme(event: KeyEvent?): Boolean {
        if (event?.keyCode == KeyEvent.KEYCODE_BACK) {
            editingState(false)
            return true
        }
        return super.dispatchKeyEventPreIme(event)
    }
}