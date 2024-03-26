package com.tokopedia.feedplus.browse.presentation.view

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import com.tokopedia.feedplus.databinding.ViewFeedSearchHeaderLayoutBinding
import com.tokopedia.header.HeaderUnify
import com.tokopedia.unifycomponents.SearchBarUnify
import com.tokopedia.feedplus.R as feedplusR

class FeedSearchHeaderView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    val header: HeaderUnify
        get() = binding.headerUnify

    private val searchBar: SearchBarUnify

    private var searchAction: (keyword: String) -> Unit = {}

    private val binding = ViewFeedSearchHeaderLayoutBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    init {
        val styledAttribute = getContext().obtainStyledAttributes(attrs, feedplusR.styleable.FeedSearchHeaderView)
        val isClearable = styledAttribute.getBoolean(feedplusR.styleable.FeedSearchHeaderView_isClearable, false)
        val isShowShadow = styledAttribute.getBoolean(feedplusR.styleable.FeedSearchHeaderView_isShowShadow, true)
        styledAttribute.recycle()

        binding.headerUnify.let { headerUnify ->
            headerUnify.isShowShadow = isShowShadow

            SearchBarUnify(context).also { searchBar ->
                this.searchBar = searchBar
                headerUnify.customView(searchBar)

                searchBar.isClearable = isClearable
                searchBar.showIcon = isClearable

                searchBar.searchBarTextField.setOnEditorActionListener { textView, keyIndex, _ ->
                    if (keyIndex == EditorInfo.IME_ACTION_SEARCH) {
                        searchAction(textView.text.toString())
                    }
                    true
                }
            }
        }
    }

    /**
     * True -> Listener bind success
     */
    fun onBackClicked(onClick: () -> Unit = {}): Boolean {
        binding.headerUnify.setNavigationOnClickListener {
            onClick()
        }
        return true
    }

    fun setSearchPlaceholder(placeholder: String) {
        searchBar.searchBarPlaceholder = placeholder
    }

    fun setSearchbarFocusListener(listener: OnFocusChangeListener) {
        searchBar.searchBarTextField.onFocusChangeListener = listener
    }

    fun setSearchbarText(text: String) {
        searchBar.searchBarTextField.setText(text)
    }

    fun setSearchDoneListener(listener: (keyword: String) -> Unit) {
        searchAction = listener
    }

    fun setSearchFocus() {
        searchBar.searchBarTextField.requestFocus()
        showSoftKeyboard()
    }

    private fun showSoftKeyboard() {
        Handler().postDelayed({
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
