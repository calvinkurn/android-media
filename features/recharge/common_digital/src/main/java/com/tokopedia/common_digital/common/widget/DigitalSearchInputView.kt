package com.tokopedia.common_digital.common.widget

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.tokopedia.common_digital.R
import com.tokopedia.design.text.SearchInputView
import org.jetbrains.annotations.NotNull

class DigitalSearchInputView @JvmOverloads constructor(@NotNull context: Context,
                                                       attrs: AttributeSet? = null,
                                                       defStyleAttr: Int = 0)
    : SearchInputView(context, attrs, defStyleAttr) {

    private lateinit var listener: DigitalSearchInputListener

    override fun getLayout(): Int {
        return R.layout.widget_digital_search_input_view
    }

    override fun getSearchImageViewResourceId(): Int {
        return R.id.image_view_search
    }

    override fun getSearchTextViewResourceId(): Int {
        return R.id.edit_text_search
    }

    override fun getCloseImageButtonResourceId(): Int {
        return R.id.image_button_close
    }

    fun setDigitalSearchInputListener(listener: DigitalSearchInputListener) {
        this.listener = listener
    }

    override fun getOnEditorActionListener(): TextView.OnEditorActionListener {
        return if (::listener.isInitialized) {
            object : TextView.OnEditorActionListener {
                override fun onEditorAction(textView: TextView, actionId: Int, event: KeyEvent?): Boolean {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        hideKeyboard()
                        listener.onSearchSubmitted(textView.text.toString())
                        return true
                    } else if (actionId == EditorInfo.IME_ACTION_DONE) {
                        hideKeyboard()
                        listener.onSearchDone(textView.text.toString())
                        return true
                    }
                    return false
                }
            }
        } else super.getOnEditorActionListener()
    }

    interface DigitalSearchInputListener {
        fun onSearchSubmitted(text: String?)
        fun onSearchDone(text: String)
    }

}