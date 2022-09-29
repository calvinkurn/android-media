package com.tokopedia.tokofood.feature.search.container.presentation.widget

import android.content.Context
import android.graphics.Rect
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.tokofood.databinding.WidgetGlobalSearchTokofoodBinding

class GlobalSearchBarWidget: ConstraintLayout {

    private var mClearingFocus: Boolean = false
    private var searchKeyword = ""

    private var binding: WidgetGlobalSearchTokofoodBinding? = null

    private var globalSearchBarWidgetListener: GlobalSearchBarWidgetListener? = null
    private var searchTextBoxListener: SearchTextBoxListener? = null

    constructor(context: Context) : super(context) {
        initWidget()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initWidget()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initWidget()
    }

    override fun requestFocus(direction: Int, previouslyFocusedRect: Rect?): Boolean {
        if (mClearingFocus) return false
        return if (isFocusable) {
            binding?.searchBarView?.searchBarTextField?.requestFocus(
                direction,
                previouslyFocusedRect
            ).orFalse()
        } else false
    }

    override fun clearFocus() {
        mClearingFocus = true
        hideKeyboard(this)
        super.clearFocus()
        binding?.searchBarView?.searchBarTextField?.clearFocus()
        mClearingFocus = false
    }

    fun setGlobalSearchBarWidgetListener(globalSearchBarWidgetListener: GlobalSearchBarWidgetListener,
                                         searchTextBoxListener: SearchTextBoxListener) {
        this.globalSearchBarWidgetListener = globalSearchBarWidgetListener
        this.searchTextBoxListener = searchTextBoxListener
    }

    fun setKeywordSearchBar(keyword: String) {
        this.searchKeyword = keyword
        searchBarTextFieldRequestFocus()
    }

    private fun initWidget() {
        binding = WidgetGlobalSearchTokofoodBinding.inflate(LayoutInflater.from(context), this, true)
        initSearchBarView()
        setupIconButtons()
    }

    private fun initSearchBarView() {
        binding?.run {
            searchBarView.run {
                searchBarTextField.imeOptions = EditorInfo.IME_ACTION_PREVIOUS

                isClearable = true
                searchBarTextField.setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        showKeyboard(searchBarTextField)
                    }
                }

                searchBarTextField.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable) {}

                    override fun beforeTextChanged(
                        s: CharSequence,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {}

                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        searchBarIcon.isVisible = s.toString().isNotEmpty()
                        searchKeyword = s.toString().trim()
                        searchTextBoxListener?.onQueryTextChangeListener(searchKeyword)
                    }
                })


                searchBarTextField.setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_PREVIOUS) {
                        hideKeyboard(searchBarTextField)
                        return@setOnEditorActionListener true
                    }
                    return@setOnEditorActionListener false
                }
            }
        }
    }

    private fun searchBarTextFieldRequestFocus() {
        binding?.run {
            searchBarView.searchBarTextField.setText(searchKeyword)
            searchTextBoxListener?.onQueryTextChangeListener(searchKeyword)
            showKeyboard(searchBarView.searchBarTextField)
            searchBarView.searchBarTextField.postDelayed({
                searchBarView.searchBarTextField.text?.length?.let {
                    searchBarView.searchBarTextField.setSelection(
                        it
                    )
                }
            }, SELECTION_TEXT_POST_DELAY)
        }
    }

    private fun showKeyboard(view: View) {
        view.requestFocus()
        val imm =
            view.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.showSoftInput(view, Int.ZERO)
    }

    private fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, Int.ZERO)
    }

    private fun setupIconButtons() {
        binding?.run {
            icBackBtn.setOnClickListener {
                globalSearchBarWidgetListener?.onBackButtonSearchBarClicked(
                    searchBarView.searchBarTextField.text?.toString()?.trim().orEmpty()
                )
                hideKeyboard(searchBarView.searchBarTextField)
            }
            icTransactionList.setOnClickListener {
                globalSearchBarWidgetListener?.onTransactionListClicked()
                hideKeyboard(searchBarView.searchBarTextField)
            }
            icHamburgerMenu.setOnClickListener {
                globalSearchBarWidgetListener?.onGlobalNavClicked()
                hideKeyboard(searchBarView.searchBarTextField)
            }
        }
    }

    override fun onDetachedFromWindow() {
        binding = null
        super.onDetachedFromWindow()
    }

    interface GlobalSearchBarWidgetListener {
        fun onBackButtonSearchBarClicked(keyword: String)
        fun onTransactionListClicked()
        fun onGlobalNavClicked()
    }

    interface SearchTextBoxListener {
        fun onQueryTextChangeListener(keyword: String)
    }

    companion object {
        const val SELECTION_TEXT_POST_DELAY = 200L
    }
}