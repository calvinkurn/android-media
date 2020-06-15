package com.tokopedia.seller.search.feature.initialsearch.view.widget

import android.content.Context
import android.graphics.Rect
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.text.style.UnderlineSpan
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.tokopedia.seller.search.R
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_global_search_view.view.*

class GlobalSearchView : BaseCustomView {

    companion object {
        const val DEBOUNCE_DELAY_MILLIS = 200L
        const val MIN_CHARACTER_SEARCH = 3
    }

    private var mClearingFocus: Boolean = false
    private var searchKeyword = ""
    private var hint: String? = ""

    private var searchViewListener: GlobalSearchViewListener? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.widget_global_search_view, this)
        initSearchBarView()
    }

    override fun requestFocus(direction: Int, previouslyFocusedRect: Rect?): Boolean {
        if (mClearingFocus) return false
        return if (!isFocusable) false else searchBarView.requestFocus(direction, previouslyFocusedRect)
    }

    fun setSearchViewListener(searchViewListener: GlobalSearchViewListener) {
        this.searchViewListener = searchViewListener
    }

    private fun showKeyboard(view: View) {
        view.requestFocus()
        val imm = view.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, 0)
    }

    private fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun setTextViewHint(hint: CharSequence?) {
        searchBarView?.searchBarTextField?.hint = hint
    }

    override fun clearFocus() {
        super.clearFocus()
        hideKeyboard(this)
        searchBarView?.searchBarTextField?.clearFocus()
    }

    private fun initSearchBarView() {
        searchBarView?.apply {
            isClearable = true
            iconListener = {
                if (searchBarPlaceholder.isNotEmpty()) {
                    //TODO refresh
                    searchBarTextField.text.clear()
                    searchKeyword = searchBarTextField.text.toString()
                    Handler().postDelayed({
                        searchViewListener?.onQueryTextChangeListener(searchKeyword)
                    }, DEBOUNCE_DELAY_MILLIS)
                }
            }

            searchBarTextField.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    showKeyboard(searchBarTextField)
                }
            }

            searchBarTextField.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {
                    for (span in s.getSpans(0, s.length, UnderlineSpan::class.java)) {
                        s.removeSpan(span)
                    }
                }

                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    val keyword = s.trim().toString()
                    when {
                        keyword.isEmpty() -> {
                            searchKeyword = keyword
                            Handler().postDelayed({
                                searchViewListener?.onQueryTextChangeListener(searchKeyword)
                            }, DEBOUNCE_DELAY_MILLIS)
                        }
                        keyword.length < MIN_CHARACTER_SEARCH -> {
                            Handler().postDelayed({
                                searchViewListener?.onMinCharState()
                            }, DEBOUNCE_DELAY_MILLIS)
                        }
                        else -> {
                            searchKeyword = s.trim().toString()
                            Handler().postDelayed({
                                searchViewListener?.onQueryTextChangeListener(searchKeyword)}, DEBOUNCE_DELAY_MILLIS)
                        }
                    }
                }
            })

            searchBarTextField.setOnEditorActionListener { _, actionId, event ->
                if ((actionId == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) {
                    hideKeyboard(searchBarView.searchBarTextField)
                    return@setOnEditorActionListener true
                }

                return@setOnEditorActionListener false
            }
        }
    }

    interface GlobalSearchViewListener {
        fun onQueryTextChangeListener(keyword: String)
        fun onMinCharState()
    }
}