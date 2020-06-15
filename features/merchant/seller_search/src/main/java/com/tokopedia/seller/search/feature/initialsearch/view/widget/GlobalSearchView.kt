package com.tokopedia.seller.search.feature.initialsearch.view.widget

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.text.style.UnderlineSpan
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.EditorInfo
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.feature.initialsearch.view.fragment.InitialSearchFragment
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_global_search_view.view.*

class GlobalSearchView: BaseCustomView {

    private var searchKeyword = ""

    private var initialSearchListener: GlobalSearchViewListener.InitialSearchListener? = null
    private var suggestionSearchListener: GlobalSearchViewListener.SuggestionSearchListener? = null

    constructor(context: Context): super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.widget_global_search_view, this)
    }

    fun setInitialSearchListener(initialSearchListener: GlobalSearchViewListener.InitialSearchListener) {
        this.initialSearchListener = initialSearchListener
    }

    fun setSuggestionSearchListener(suggestionSearchListener: GlobalSearchViewListener.SuggestionSearchListener) {
        this.suggestionSearchListener = suggestionSearchListener
    }

    private fun initSearchBarView() {
        searchBarView?.apply {
            isClearable = true
            iconListener = {
                if (searchBarPlaceholder.isNotEmpty()) {
                    //TODO refresh
                }
            }
            val query = searchBarTextField.text.trim().toString()

            searchBarTextField.setOnFocusChangeListener { _, hasFocus ->
                if(hasFocus) { }
            }

            searchBarTextField.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {
                    for (span in s.getSpans(0, s.length, UnderlineSpan::class.java)) {
                        s.removeSpan(span)
                    }
                }

                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if(s.length < InitialSearchFragment.MIN_CHARACTER_SEARCH) {
                        //TODO history
                        suggestionSearchListener?.onMinCharState()
                    } else {
                        //TODO suggestion
                        searchKeyword = s.trim().toString()
                        suggestionSearchListener?.onSuggestionSearch(searchKeyword)
                    }
                }
            })

            searchBarTextField.setOnEditorActionListener { _, actionId, _ ->

                if(actionId == EditorInfo.IME_ACTION_SEARCH) {

                    if(query.length < InitialSearchFragment.MIN_CHARACTER_SEARCH) {
                        //TODO min char
                    } else {
                        //TODO Keyword
                    }
                    return@setOnEditorActionListener true
                }

                return@setOnEditorActionListener false
            }
        }
    }

    interface GlobalSearchViewListener {
        interface InitialSearchListener {
            fun onInitialSearchHistorySearch(keyword: String)
            fun onNoHistoryState()
        }
        interface SuggestionSearchListener {
            fun onSuggestionSearch(keyword: String)
            fun onMinCharState()
            fun onNoResultState()
        }
    }
}