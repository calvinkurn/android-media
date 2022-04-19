package com.tokopedia.seller.search.feature.initialsearch.view.widget

import android.content.Context
import android.graphics.Rect
import android.text.Editable
import android.text.TextWatcher
import android.text.style.UnderlineSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.seller.search.databinding.WidgetGlobalSearchViewBinding
import com.tokopedia.unifycomponents.BaseCustomView

class GlobalSearchView : BaseCustomView {

    private var mClearingFocus: Boolean = false
    private var searchKeyword = ""

    private var activity: AppCompatActivity? = null

    private var searchViewListener: GlobalSearchViewListener? = null
    private var searchTextBoxListener: SearchTextBoxListener? = null

    var binding: WidgetGlobalSearchViewBinding? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        binding = WidgetGlobalSearchViewBinding.inflate(LayoutInflater.from(context))
        addView(binding?.root)
        initSearchBarView()
        editTextViewRequestFocus()
        btnBackHome()
    }

    override fun requestFocus(direction: Int, previouslyFocusedRect: Rect?): Boolean {
        if (mClearingFocus) return false
        return if (!isFocusable) {
            false
        } else {
            binding?.searchBarView?.searchBarTextField?.requestFocus(
                direction,
                previouslyFocusedRect
            ) ?: false
        }
    }

    fun setSearchViewListener(searchViewListener: GlobalSearchViewListener) {
        this.searchViewListener = searchViewListener
    }

    fun setSearchTextBoxListener(searchTextBoxListener: SearchTextBoxListener) {
        this.searchTextBoxListener = searchTextBoxListener
    }

    fun setKeyword(keyword: String) {
        searchKeyword = keyword
        binding?.searchBarView?.searchBarTextField?.setText(searchKeyword)
    }

    fun setKeywordSearchBar(keyword: String) {
        searchKeyword = keyword
        binding?.run {
            searchBarView.searchBarTextField.setText(searchKeyword)
            searchBarView.searchBarTextField.postDelayed({
                searchBarView.searchBarTextField.text?.length?.let {
                    searchBarView.searchBarTextField.setSelection(
                        it
                    )
                }
            }, 200)
        }
        searchViewListener?.onQueryTextChangeListener(searchKeyword)
    }

    fun setPlaceholder(placeholder: String) {
        binding?.searchBarView?.searchBarPlaceholder = placeholder
    }

    fun setActivity(activity: AppCompatActivity) {
        this.activity = activity
    }

    private fun showKeyboard(view: View) {
        view.requestFocus()
        val imm =
            view.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.showSoftInput(view, 0)
    }

    private fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun clearFocus() {
        mClearingFocus = true
        hideKeyboard(this)
        super.clearFocus()
        binding?.searchBarView?.searchBarTextField?.clearFocus()
        mClearingFocus = false
    }

    private fun editTextViewRequestFocus() {
        binding?.run {
            searchBarView.searchBarTextField.setText(searchKeyword)
            searchViewListener?.onQueryTextChangeListener(searchKeyword)
            searchBarView.searchBarTextField.postDelayed({
                showKeyboard(searchBarView.searchBarTextField)
                searchBarView.searchBarTextField.text?.length?.let {
                    searchBarView.searchBarTextField.setSelection(
                        it
                    )
                }
            }, 200)
        }
    }

    private fun initSearchBarView() {
        binding?.run {
            searchBarView.run {
                searchBarTextField.imeOptions = EditorInfo.IME_ACTION_SEARCH

                isClearable = true
                clearListener = {
                    searchTextBoxListener?.onClearTextBoxListener()
                }
                iconListener = {
                    if (searchBarPlaceholder.isNotEmpty()) {
                        searchBarTextField.text.clear()
                        searchKeyword = searchBarTextField.text.trim().toString()
                        searchViewListener?.onQueryTextChangeListener(searchKeyword)
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

                    override fun beforeTextChanged(
                        s: CharSequence,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        val keyword = s.trim().toString()

                        searchKeyword = keyword

                        searchViewListener?.onQueryTextChangeListener(searchKeyword)
                    }
                })


                searchBarTextField.setOnEditorActionListener { _, actionId, event ->
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        searchViewListener?.onQueryTextChangeListener(
                            searchBarTextField.text.trim().toString()
                        )
                        hideKeyboard(searchBarView.searchBarTextField)
                        return@setOnEditorActionListener true
                    }

                    return@setOnEditorActionListener false
                }
            }
        }
    }

    private fun btnBackHome() {
        binding?.run {
            actionUpBtn.setOnClickListener {
                searchViewListener?.onBackButtonSearchBar(
                    searchBarView.searchBarTextField.text?.trim()?.toString().orEmpty()
                )
                KeyboardHandler.DropKeyboard(activity, searchBarView.searchBarTextField)
                activity?.finish()
            }
        }
    }

    interface GlobalSearchViewListener {
        fun onQueryTextChangeListener(keyword: String)
        fun onBackButtonSearchBar(keyword: String)
    }

    interface SearchTextBoxListener {
        fun onClearTextBoxListener()
    }
}