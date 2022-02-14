package com.tokopedia.affiliate.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate_toko.R
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AffiliateLinkTextField(context: Context, attrs: AttributeSet) : TextFieldUnify2(context,attrs) {

    private var relatedView : View? = null
    private var editState = false
    private var affiliateLinkTextFieldInterface : AffiliateLinkTextFieldInterface? = null

    init {
        labelText.hide()
        textInputLayout.isHelperTextEnabled = false
        editText.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus)
                editingState(true)
        }
        editText.setOnClickListener {
            editingState(true)
        }
        editText.setOnLongClickListener {
            editingState(true)
            false
        }
        editText.imeOptions = EditorInfo.IME_ACTION_SEARCH
    }

    fun setRelatedView(relatedView : View){
        relatedView.setOnClickListener {
            editingState(false)
        }
        this.relatedView = relatedView
    }

    fun setEventListener(listener : AffiliateLinkTextFieldInterface){
        affiliateLinkTextFieldInterface = listener
    }

    fun setDoneAction(action : () -> Unit){
        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                editingState(false)
                action.invoke()
                true
            }else
                false
        }
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
        this.editState = state
        if(state){
            affiliateLinkTextFieldInterface?.onEditState(true)
            relatedView?.show()
            showKeyboard(editText)
        }else {
            relatedView?.hide()
            hideKeyboard(editText)
        }
    }

    override fun dispatchKeyEventPreIme(event: KeyEvent?): Boolean {
        return if (editState && event?.keyCode == KeyEvent.KEYCODE_BACK) {
            editingState(false)
            true
        }else {
            super.dispatchKeyEventPreIme(event)
        }
    }
}

interface AffiliateLinkTextFieldInterface{
    fun onEditState(state : Boolean)
}