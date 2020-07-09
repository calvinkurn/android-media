package com.tokopedia.seller.search.feature.initialsearch.view.widget

import android.content.Context
import android.graphics.Rect
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.text.style.UnderlineSpan
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.seller.search.R
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_global_search_view.view.*
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.concurrent.TimeUnit

class GlobalSearchView : BaseCustomView {

    companion object {
        const val DEBOUNCE_DELAY_MILLIS = 300L
    }

    private var mClearingFocus: Boolean = false
    private var searchKeyword = ""

    private var activity: AppCompatActivity? = null

    var compositeSubscription: CompositeSubscription? = null

    var mHandler: Handler? = null

    private var searchViewListener: GlobalSearchViewListener? = null
    private var searchTextBoxListener: SearchTextBoxListener? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    init {
        mHandler = Handler()
    }

    private fun init() {
        View.inflate(context, R.layout.widget_global_search_view, this)
        initSearchBarView()
        editTextViewRequestFocus()
        btnBackHome()
        initCompositeSubscriber()
    }

    override fun requestFocus(direction: Int, previouslyFocusedRect: Rect?): Boolean {
        if (mClearingFocus) return false
        return if (!isFocusable) false else searchBarView.searchBarTextField.requestFocus(direction, previouslyFocusedRect)
    }

    fun setSearchViewListener(searchViewListener: GlobalSearchViewListener) {
        this.searchViewListener = searchViewListener
    }

    fun setSearchTextBoxListener(searchTextBoxListener: SearchTextBoxListener) {
        this.searchTextBoxListener = searchTextBoxListener
    }

    fun setKeywordSearchBar(keyword: String) {
        searchKeyword = keyword
        searchBarView.searchBarTextField.setText(searchKeyword)
        searchBarView.searchBarTextField.postDelayed({
            searchBarView.searchBarTextField.text?.length?.let { searchBarView.searchBarTextField.setSelection(it) }
        }, 200)
        searchViewListener?.onQueryTextChangeListener(searchKeyword)
    }

    private fun initCompositeSubscriber() {
        compositeSubscription = getNewCompositeSubIfUnsubscribed(compositeSubscription)
        compositeSubscription?.add(Observable.unsafeCreate(Observable.OnSubscribe<String> { subscriber ->
            searchViewListener = object : GlobalSearchViewListener {
                override fun onQueryTextChangeListener(keyword: String) {
                    subscriber.onNext(keyword)
                }

                override fun onBackButtonSearchBar() {}
            }
        })
                .debounce(DEBOUNCE_DELAY_MILLIS, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<String>() {
                    override fun onCompleted() {}

                    override fun onError(e: Throwable) {}

                    override fun onNext(s: String?) {
                        if (s != null) {
                            searchKeyword = s.trim()
                            searchViewListener?.onQueryTextChangeListener(searchKeyword)
                        }
                    }
                }))
    }

    private fun getNewCompositeSubIfUnsubscribed(subscription: CompositeSubscription?): CompositeSubscription {
        return if (subscription == null || subscription.isUnsubscribed) {
            CompositeSubscription()
        } else subscription
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

    override fun clearFocus() {
        mClearingFocus = true
        hideKeyboard(this)
        super.clearFocus()
        searchBarView?.searchBarTextField?.clearFocus()
        mClearingFocus = false
    }

    private fun editTextViewRequestFocus() {
        searchBarView.searchBarTextField.setText(searchKeyword)
        searchViewListener?.onQueryTextChangeListener(searchKeyword)
        searchBarView.searchBarTextField.postDelayed({
            showKeyboard(searchBarView.searchBarTextField)
            searchBarView.searchBarTextField.text?.length?.let { searchBarView.searchBarTextField.setSelection(it) }
        }, 200)
    }

    private fun initSearchBarView() {
        searchBarView?.apply {
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
                    val keyword = s.trim().toString()

                    searchKeyword = keyword

                    onEditTextChangeListener {
                        searchViewListener?.onQueryTextChangeListener(searchKeyword)
                    }

                    for (span in s.getSpans(0, s.length, UnderlineSpan::class.java)) {
                        s.removeSpan(span)
                    }
                }

                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                }
            })


            searchBarTextField.setOnEditorActionListener { _, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchViewListener?.onQueryTextChangeListener(searchBarTextField.text.trim().toString())
                    hideKeyboard(searchBarView.searchBarTextField)
                    return@setOnEditorActionListener true
                }

                return@setOnEditorActionListener false
            }
        }
    }

    private fun onEditTextChangeListener(listener: () -> Unit) {
        mHandler?.postDelayed({ listener() }, DEBOUNCE_DELAY_MILLIS)
    }

    private fun btnBackHome() {
        actionUpBtn?.setOnClickListener {
            searchViewListener?.onBackButtonSearchBar()
            KeyboardHandler.DropKeyboard(activity, searchBarView?.searchBarTextField)
            activity?.finish()
        }
    }

    interface GlobalSearchViewListener {
        fun onQueryTextChangeListener(keyword: String)
        fun onBackButtonSearchBar()
    }

    interface SearchTextBoxListener {
        fun onClearTextBoxListener()
    }
}