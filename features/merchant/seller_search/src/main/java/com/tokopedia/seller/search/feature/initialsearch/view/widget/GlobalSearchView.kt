package com.tokopedia.seller.search.feature.initialsearch.view.widget

import android.content.Context
import android.graphics.Rect
import android.text.Editable
import android.text.TextWatcher
import android.text.style.UnderlineSpan
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.seller.search.R
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_global_search_view.view.*
import java.util.*

class GlobalSearchView : BaseCustomView {

    companion object {
        const val DEBOUNCE_DELAY_MILLIS = 1000L
        const val MIN_CHARACTER_SEARCH = 3
    }

    private var timer = Timer()

    private var mClearingFocus: Boolean = false
    private var searchKeyword = ""
    private var hint: String? = ""

    private var activity: AppCompatActivity? = null

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
        btnBackHome()
        searchBarView.searchBarTextField.postDelayed({
            showKeyboard(searchBarView.searchBarTextField)
            searchBarView?.searchBarTextField?.text?.length?.let { searchBarView.searchBarTextField.setSelection(it) }
        }, 200)
    }

    override fun requestFocus(direction: Int, previouslyFocusedRect: Rect?): Boolean {
        if (mClearingFocus) return false
        return if (!isFocusable) false else searchBarView.searchBarTextField.requestFocus(direction, previouslyFocusedRect)
    }

    fun setSearchViewListener(searchViewListener: GlobalSearchViewListener) {
        this.searchViewListener = searchViewListener
    }


    fun setActivity(activity: AppCompatActivity) {
        this.activity = activity
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
        mClearingFocus = true
        hideKeyboard(this)
        super.clearFocus()
        searchBarView?.searchBarTextField?.clearFocus()
        mClearingFocus = false
    }

    private fun initSearchBarView() {
        searchBarView?.apply {
            isClearable = true
            iconListener = {
                if (searchBarPlaceholder.isNotEmpty()) {
                    searchBarTextField.text.clear()
                    searchKeyword = searchBarTextField.text.trim().toString()

                    onEditTextChangeListener {
                        searchViewListener?.onQueryTextChangeListener(searchKeyword)
                    }
                }
            }

            searchBarTextField.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    showKeyboard(searchBarTextField)
                }
            }

            searchBarTextField.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {
                    val keyword = s.trim().toString()
                    when {
                        keyword.isEmpty() -> {
                            searchKeyword = keyword
                            onEditTextChangeListener {
                                searchViewListener?.onQueryTextChangeListener(searchKeyword)
                            }
                        }
                        keyword.length < MIN_CHARACTER_SEARCH -> {
                            onEditTextChangeListener {
                                searchViewListener?.onMinCharState()
                            }
                        }
                        else -> {
                            searchKeyword = keyword
                            onEditTextChangeListener {
                                searchViewListener?.onQueryTextChangeListener(searchKeyword)
                            }
                        }
                    }

                    for (span in s.getSpans(0, s.length, UnderlineSpan::class.java)) {
                        s.removeSpan(span)
                    }
                }

                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
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

    private fun onEditTextChangeListener(editTextListener: () -> Unit) {
        timer.cancel()
        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                editTextListener.invoke()
            }
        }, DEBOUNCE_DELAY_MILLIS)
    }

    private fun btnBackHome() {
        actionUpBtn?.setOnClickListener {
            KeyboardHandler.DropKeyboard(activity, searchBarView?.searchBarTextField)
            activity?.finish()
        }
    }

    interface GlobalSearchViewListener {
        fun onQueryTextChangeListener(keyword: String)
        fun onMinCharState()
    }
}