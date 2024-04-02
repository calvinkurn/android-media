package com.tokopedia.feedplus.browse.presentation.view

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import com.tokopedia.header.HeaderUnify
import com.tokopedia.unifycomponents.SearchBarUnify
import com.tokopedia.feedplus.R as feedplusR

class FeedHeaderView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private var searchbar: SearchBarUnify? = null
    var header: HeaderUnify? = null

    private var searchAction: (keyword: String) -> Unit = {}
    private var handler: Handler? = null

    init {
        val styledAttribute = getContext().obtainStyledAttributes(attrs, feedplusR.styleable.FeedHeaderView)
        val isClearable = styledAttribute.getBoolean(feedplusR.styleable.FeedHeaderView_isCleanable, false)
        val isShowShadow = styledAttribute.getBoolean(feedplusR.styleable.FeedHeaderView_isShowShadow, true)
        val withSearchbar = styledAttribute.getBoolean(feedplusR.styleable.FeedHeaderView_withSearchbar, false)
        styledAttribute.recycle()

        LayoutInflater.from(context).inflate(feedplusR.layout.view_feed_header_layout, this, true)
        header = findViewById<HeaderUnify>(feedplusR.id.header_unify).also {
            it.isShowShadow = isShowShadow
        }
        if (withSearchbar) initSearchBar(isClearable)
    }

    /**
     * True -> Listener bind success
     */
    fun onBackClicked(onClick: () -> Unit = {}): Boolean {
        return header?.let {
            it.setNavigationOnClickListener {
                onClick()
            }
            true
        } ?: false
    }

    fun setSearchPlaceholder(placeholder: String) {
        searchbar?.searchBarPlaceholder = placeholder
    }

    fun setSearchbarFocusListener(listener: OnFocusChangeListener) {
        searchbar?.searchBarTextField?.onFocusChangeListener = listener
    }

    fun setSearchbarText(text: String) {
        searchbar?.searchBarTextField?.setText(text)
    }

    fun setSearchDoneListener(listener: (keyword: String) -> Unit) {
        searchAction = listener
    }

    fun setSearchFocus() {
        searchbar?.searchBarTextField?.requestFocus()
        showSoftKeyboard()
    }

    fun initSearchBar(isClearable: Boolean, searchPlaceholder: String = "") {
        SearchBarUnify(context).also { searchbar ->
            searchbar.isClearable = isClearable
            searchbar.showIcon = isClearable
            searchbar.searchBarPlaceholder = searchPlaceholder

            this.searchbar = searchbar
            header?.customView(searchbar)

            searchbar.searchBarTextField.setOnEditorActionListener { textView, keyIndex, _ ->
                if (keyIndex == EditorInfo.IME_ACTION_SEARCH) {
                    searchAction(textView.text.toString())
                }
                true
            }
        }
    }

    private fun showSoftKeyboard() {
        handler = Handler()
        handler?.postDelayed({
            context?.getSystemService(Context.INPUT_METHOD_SERVICE)?.let {
                (it as InputMethodManager).toggleSoftInput(
                    InputMethodManager.SHOW_IMPLICIT,
                    InputMethodManager.HIDE_IMPLICIT_ONLY
                )
            }
        }, KEYBOARD_SHOW_DELAY)
    }

    companion object {
        // need delay for activity render before show keyboard
        private const val KEYBOARD_SHOW_DELAY = 500L
    }
}
