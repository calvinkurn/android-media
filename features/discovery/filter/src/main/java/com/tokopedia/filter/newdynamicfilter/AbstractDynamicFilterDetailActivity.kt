package com.tokopedia.filter.newdynamicfilter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView

import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.filter.common.data.Option
import com.tokopedia.design.search.EmptySearchResultView
import com.tokopedia.filter.R
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTracking
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTrackingData
import com.tokopedia.filter.newdynamicfilter.view.DynamicFilterDetailView
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration

import java.util.ArrayList

import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

abstract class AbstractDynamicFilterDetailActivity<T : RecyclerView.Adapter<*>> : BaseActivity(), DynamicFilterDetailView {

    protected lateinit var optionList: List<Option>
    protected lateinit var abstractDynamicFilterAdapter: T
    protected var recyclerView: RecyclerView? = null

    private var searchInputContainer: View? = null
    private var searchInputView: EditText? = null
    private var searchResultEmptyView: EmptySearchResultView? = null
    private var searchFilter: OptionSearchFilter? = null
    private var buttonApply: TextView? = null
    private var buttonReset: TextView? = null
    private var buttonClose: View? = null
    private var topBarTitle: TextView? = null
    private var loadingView: View? = null

    private var isSearchable: Boolean = false
    private lateinit var searchHint: String
    private lateinit var pageTitle: String
    private var isAutoTextChange = false
    private var subscription: Subscription? = null
    private var isUsingTracking: Boolean = false
    private var trackingData: FilterTrackingData? = null

    private val activityContext: Context
        get() = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dynamic_filter_detail)
        bindView()
        showLoading()
        subscription = retrieveOptionListData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Boolean>() {
                    override fun onCompleted() {}

                    override fun onError(e: Throwable) {}

                    override fun onNext(aBoolean: Boolean) {
                        fetchDataFromIntent()
                        initTopBar()
                        initRecyclerView()
                        loadFilterItems(optionList)
                        initSearchView()
                        initListeners()
                        hideLoading()
                    }
                })
    }

    protected open fun retrieveOptionListData(): Observable<Boolean> {
        return Observable.create { subscriber ->
            optionList = intent.getParcelableArrayListExtra(EXTRA_OPTION_LIST)
            subscriber.onNext(true)
        }
    }

    private fun fetchDataFromIntent() {
        isSearchable = intent.getBooleanExtra(EXTRA_IS_SEARCHABLE, false)
        searchHint = intent.getStringExtra(EXTRA_SEARCH_HINT)
        pageTitle = intent.getStringExtra(EXTRA_PAGE_TITLE)
        isUsingTracking = intent.getBooleanExtra(EXTRA_IS_USING_TRACKING, false)
        trackingData = intent.getParcelableExtra(EXTRA_TRACKING_DATA)
    }

    protected open fun bindView() {
        recyclerView = findViewById(R.id.filter_detail_recycler_view)
        searchInputView = findViewById(R.id.filter_detail_search)
        searchInputContainer = findViewById(R.id.filter_detail_search_container)
        searchResultEmptyView = findViewById(R.id.filter_detail_empty_search_result_view)
        topBarTitle = findViewById(R.id.top_bar_title)
        buttonClose = findViewById(R.id.top_bar_close_button)
        buttonClose?.setBackgroundResource(R.drawable.ic_filter_detail_back)
        buttonReset = findViewById(R.id.top_bar_button_reset)
        buttonApply = findViewById(R.id.button_apply)
        loadingView = findViewById(R.id.loading_view)
    }

    private fun initListeners() {
        buttonClose?.setOnClickListener { onBackPressed() }
        buttonReset?.visibility = View.VISIBLE
        buttonReset?.setOnClickListener { resetFilter() }
        buttonApply?.setOnClickListener {
            if (isUsingTracking) {
                trackingData?.let { FilterTracking.eventApplyFilterDetail(it, pageTitle) }
            }
            applyFilter()
        }
    }

    override fun onBackPressed() {
        if (isUsingTracking) {
            trackingData?.let { FilterTracking.eventBackFromFilterDetail(it, pageTitle) }
        }
        super.onBackPressed()
    }

    protected fun showLoading() {
        loadingView?.visibility = View.VISIBLE
    }

    protected fun hideLoading() {
        loadingView?.visibility = View.GONE
    }

    private fun initTopBar() {
        topBarTitle?.text = pageTitle
    }

    private fun initSearchView() {
        if (!isSearchable) {
            searchInputContainer?.visibility = View.GONE
            return
        }

        searchInputContainer?.visibility = View.VISIBLE
        searchInputView?.hint = searchHint
        searchInputView?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (!isAutoTextChange) {
                    getSearchFilter().filter(charSequence)
                }
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })
    }

    private fun getSearchFilter(): OptionSearchFilter {
        if (searchFilter == null) {
            searchFilter = OptionSearchFilter(optionList)
        }
        return searchFilter as OptionSearchFilter
    }

    protected open fun initRecyclerView() {
        abstractDynamicFilterAdapter = getAdapter()
        recyclerView?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView?.addItemDecoration(DividerItemDecoration(this))
        recyclerView?.adapter = abstractDynamicFilterAdapter
        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    hideKeyboard()
                }
            }
        })
    }

    protected abstract fun getAdapter(): T

    override fun onItemCheckedChanged(option: Option, isChecked: Boolean) {
        option.inputState = isChecked.toString()
        hideKeyboard()
        if (isUsingTracking) {
            trackingData?.let {
                FilterTracking.eventFilterJourney(it, pageTitle, option.name,
                        true, isChecked, option.isAnnotation)
            }
        }
    }

    private fun hideKeyboard() {
        KeyboardHandler.hideSoftKeyboard(this)
    }

    protected abstract fun loadFilterItems(options: List<Option>)

    protected open fun applyFilter() {
        val intent = Intent()
        intent.putParcelableArrayListExtra(EXTRA_RESULT, ArrayList(optionList))
        setResult(RESULT_OK, intent)
        finish()
    }

    protected open fun resetFilter() {
        loadFilterItems(optionList)
        clearSearchInput()
        KeyboardHandler.hideSoftKeyboard(this)
        searchResultEmptyView?.visibility = View.GONE
        buttonApply?.visibility = View.VISIBLE
    }

    private fun clearSearchInput() {
        isAutoTextChange = true
        searchInputView?.setText("")
        isAutoTextChange = false
    }

    inner class OptionSearchFilter(optionList: List<Option>) : android.widget.Filter() {
        private val sourceData: ArrayList<Option> = ArrayList()

        init {
            synchronized(this) {
                sourceData.addAll(optionList)
            }
        }

        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filterSeq = constraint.toString().toLowerCase()
            val result = FilterResults()
            if (!TextUtils.isEmpty(filterSeq)) {
                val filter = arrayListOf<Option>()
                for (option in sourceData) {
                    if (option.name.toLowerCase().contains(filterSeq)) {
                        filter.add(option)
                    }
                }
                result.values = filter
                result.count = filter.size
            } else {
                synchronized(this) {
                    result.values = sourceData
                    result.count = sourceData.size
                }
            }
            return result
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            val resultList = results.values as List<Option>

            if (resultList.isEmpty()) {
                searchResultEmptyView?.setSearchCategory(pageTitle)
                searchResultEmptyView?.setSearchQuery(constraint.toString())
                searchResultEmptyView?.visibility = View.VISIBLE
                buttonApply?.visibility = View.GONE
            } else {
                searchResultEmptyView?.visibility = View.GONE
                buttonApply?.visibility = View.VISIBLE
            }

            loadFilterItems(resultList)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        subscription?.unsubscribe()
    }

    companion object {

        const val REQUEST_CODE = 220
        const val EXTRA_RESULT = "EXTRA_RESULT"
        const val EXTRA_OPTION_LIST = "EXTRA_OPTION_LIST"
        const val EXTRA_SEARCH_HINT = "EXTRA_SEARCH_HINT"
        const val EXTRA_IS_SEARCHABLE = "EXTRA_IS_SEARCHABLE"
        const val EXTRA_PAGE_TITLE = "EXTRA_PAGE_TITLE"
        const val EXTRA_IS_USING_TRACKING = "EXTRA_IS_USING_TRACKING"
        const val EXTRA_TRACKING_DATA = "EXTRA_TRACKING_DATA"
    }
}
