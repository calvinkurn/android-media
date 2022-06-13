package com.tokopedia.filter.newdynamicfilter

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.TextView
import android.widget.Toast

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.design.keyboard.KeyboardHelper
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.R
import com.tokopedia.resources.common.R as RCommon
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.data.Option.Companion.KEY_CATEGORY
import com.tokopedia.filter.newdynamicfilter.adapter.DynamicFilterAdapter
import com.tokopedia.filter.newdynamicfilter.adapter.typefactory.DynamicFilterTypeFactoryImpl
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.filter.newdynamicfilter.helper.DynamicFilterDbManager
import com.tokopedia.filter.newdynamicfilter.helper.FilterDbHelper
import com.tokopedia.filter.newdynamicfilter.helper.FilterDetailActivityRouter
import com.tokopedia.filter.newdynamicfilter.helper.FilterHelper
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.filter.newdynamicfilter.view.DynamicFilterView

import java.util.HashMap

import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import kotlin.collections.ArrayList

class RevampedDynamicFilterActivity : BaseActivity(), DynamicFilterView {

    private var recyclerView: RecyclerView? = null
    private var adapter: DynamicFilterAdapter? = null
    private var buttonApply: TextView? = null
    private var buttonReset: TextView? = null
    private var buttonClose: View? = null
    private var mainLayout: View? = null
    private var loadingView: View? = null

    private lateinit var filterController: FilterController

    private var selectedExpandableItemPosition: Int = 0

    private val compositeSubscription = CompositeSubscription()

    private val subsriberGetFilterListFromDb: Subscriber<List<Filter>>
        get() = object : Subscriber<List<Filter>>() {
            override fun onCompleted() {}

            override fun onError(throwable: Throwable?) {
                throwable?.printStackTrace()
                Toast.makeText(this@RevampedDynamicFilterActivity, getString(R.string.error_get_local_dynamic_filter), Toast.LENGTH_LONG).show()
                finish()
            }

            override fun onNext(filterList: List<Filter>?) {
                if(filterList == null) return
                val initializedFilterList = FilterHelper.initializeFilterList(filterList)
                filterController.initFilterController(searchParameterFromIntent, initializedFilterList)
                adapter?.filterList = initializedFilterList
            }
        }

    private val searchParameterFromIntent: Map<String, String>
        get() {
            val searchParameterMapIntent = intent.getSerializableExtra(EXTRA_QUERY_PARAMETERS) as Map<*, *>

            val searchParameter = HashMap<String, String>(searchParameterMapIntent.size)

            for (entry in searchParameterMapIntent.entries) {
                searchParameter.put(entry.key.toString(), entry.value.toString())
            }

            return searchParameter
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_revamped_dynamic_filter)
        bindView()
        initKeyboardVisibilityListener()
        initRecyclerView()
        loadFilterItems()
    }

    private fun bindView() {
        recyclerView = findViewById(R.id.dynamic_filter_recycler_view)
        buttonClose = findViewById(R.id.top_bar_close_button)
        buttonClose?.setOnClickListener { onBackPressed() }
        buttonReset = findViewById(R.id.top_bar_button_reset)
        buttonReset?.visibility = View.VISIBLE
        buttonReset?.setOnClickListener { resetAllFilter() }
        buttonApply = findViewById(R.id.button_finish)
        buttonApply?.setOnClickListener { applyFilter() }
        mainLayout = findViewById(R.id.main_layout)
        loadingView = findViewById(R.id.loading_view)
    }

    private fun initKeyboardVisibilityListener() {
        KeyboardHelper.setKeyboardVisibilityChangedListener(mainLayout, object : KeyboardHelper.OnKeyboardVisibilityChangedListener {
            override fun onKeyboardShown() {
                buttonApply?.visibility = View.GONE
            }

            override fun onKeyboardHide() {
                buttonApply?.visibility = View.VISIBLE
                mainLayout?.requestFocus()
            }
        })
    }

    private fun initRecyclerView() {
        filterController = FilterController()
        val dynamicFilterTypeFactory = DynamicFilterTypeFactoryImpl(this)
        adapter = DynamicFilterAdapter(dynamicFilterTypeFactory)
        recyclerView?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val dividerItemDecoration = DividerItemDecoration(recyclerView?.context, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(resources.getDrawable(RCommon.drawable.bg_line_separator))
        recyclerView?.addItemDecoration(dividerItemDecoration)
        recyclerView?.adapter = adapter
        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    KeyboardHandler.hideSoftKeyboard(this@RevampedDynamicFilterActivity)
                }
            }
        })
    }

    @SuppressWarnings("unchecked")
    private fun loadFilterItems() {
        compositeSubscription.add(
                Observable.just(DynamicFilterDbManager())
                        .map(this::getFilterListFromDbManager)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subsriberGetFilterListFromDb)
        )
    }

    @Throws(RuntimeException::class)
    private fun getFilterListFromDbManager(manager: DynamicFilterDbManager): List<Filter> {
        val data = DynamicFilterDbManager.getFilterData(this, intent.getStringExtra(EXTRA_CALLER_SCREEN_NAME) ?: "")
        val listType = object : TypeToken<List<Filter>>() {}.type
        val gson = Gson()
        return gson.fromJson(data, listType)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.fade_in, R.anim.push_down)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                AbstractDynamicFilterDetailActivity.REQUEST_CODE -> data?.let { handleResultFromDetailPage(it) }
                DynamicFilterLocationActivity.REQUEST_CODE -> handleResultFromLocationPage()
                DynamicFilterCategoryActivity.REQUEST_CODE -> data?.let { handleResultFromCategoryPage(it) }
            }

            adapter?.notifyItemChanged(selectedExpandableItemPosition)
        }

        hideLoading()
    }

    private fun handleResultFromDetailPage(data: Intent) {
        val optionList: List<Option>? = data.getParcelableArrayListExtra<Option>(AbstractDynamicFilterDetailActivity.EXTRA_RESULT)?.toList()
        filterController.setFilter(optionList)
    }

    private fun handleResultFromLocationPage() {
       Observable.create(Observable.OnSubscribe<List<Option>> { subscriber ->
           subscriber?.onNext(FilterDbHelper.loadLocationFilterOptions(this@RevampedDynamicFilterActivity)) })
           .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Subscriber<List<Option>>() {
                override fun onCompleted() {}

                override fun onError(e: Throwable?) {}

                override fun onNext(optionList: List<Option>?) {
                    if(optionList == null) return
                    filterController.setFilter(optionList)
                    adapter?.notifyItemChanged(selectedExpandableItemPosition)
                    hideLoading()
                }
            })
    }

    private fun handleResultFromCategoryPage(data: Intent) {
        val selectedCategoryId = data.getStringExtra(DynamicFilterCategoryActivity.EXTRA_SELECTED_CATEGORY_ID) ?: ""

        val category = adapter?.filterList?.let { FilterHelper.getSelectedCategoryDetailsFromFilterList(it, selectedCategoryId) }

        val selectedCategoryNameFromList = category?.categoryName ?: ""
        val categoryOption = OptionHelper.generateOptionFromCategory(selectedCategoryId, selectedCategoryNameFromList)

        filterController.setFilter(categoryOption, true, true)
    }

    override fun onPriceSliderRelease(minValue: Int, maxValue: Int) {

    }

    override fun onPriceSliderPressed(minValue: Int, maxValue: Int) {

    }

    override fun onMinPriceEditedFromTextInput(minValue: Int) {

    }

    override fun onMaxPriceEditedFromTextInput(maxValue: Int) {

    }

    private fun applyFilter() {
        renderFilterResult()
        finish()
    }

    private fun renderFilterResult() {
        val intent = Intent()
        val filterParameterHashMap = HashMap(filterController.getParameter())
        val activeFilterParameterHashMap = HashMap(filterController.getActiveFilterMap())

        intent.putExtra(EXTRA_QUERY_PARAMETERS, filterParameterHashMap)
        intent.putExtra(EXTRA_SELECTED_FILTERS, activeFilterParameterHashMap)
        intent.putParcelableArrayListExtra(EXTRA_SELECTED_OPTIONS, ArrayList(filterController.getActiveFilterOptionList()))
        setResult(RESULT_OK, intent)
    }

    private fun resetAllFilter() {
        filterController.resetAllFilters()
        adapter?.notifyDataSetChanged()
    }

    override fun onExpandableItemClicked(filter: Filter) {
        showLoading()
        selectedExpandableItemPosition = adapter?.getItemPosition(filter) ?: 0
        if (filter.isCategoryFilter) {
            launchFilterCategoryPage(filter)
        } else {
            enrichWithInputState(filter)
            FilterDetailActivityRouter.launchDetailActivity(this, filter)
        }
    }

    private fun launchFilterCategoryPage(filter: Filter) {
        val categoryId = filterController.getFilterValue(SearchApiConst.SC)
        val selectedCategory = FilterHelper.getSelectedCategoryDetails(filter, categoryId)
        val selectedCategoryRootId = selectedCategory?.categoryRootId ?: ""

        FilterDetailActivityRouter
                .launchCategoryActivity(this, filter, selectedCategoryRootId, categoryId)
    }

    private fun showLoading() {
        loadingView?.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loadingView?.visibility = View.GONE
    }

    private fun enrichWithInputState(filter: Filter) {
        for (option in filter.options) {
            option.inputState = filterController.getFilterViewState(option.uniqueId).toString()
        }
    }

    override fun loadLastCheckedState(option: Option): Boolean {
        return filterController.getFilterViewState(option)
    }

    override fun saveCheckedState(option: Option, isChecked: Boolean) {
        filterController.setFilter(option, isChecked)
    }

    override fun removeSavedTextInput(uniqueId: String) {
        filterController.setFilter(OptionHelper.generateOptionFromUniqueId(uniqueId), false, true)

        val optionKey = OptionHelper.parseKeyFromUniqueId(uniqueId)
    }

    override fun saveTextInput(uniqueId: String, textInput: String) {
        val textInputOption = OptionHelper.generateOptionFromUniqueId(uniqueId)
        textInputOption.value = textInput
        filterController.setFilter(textInputOption, true, true)

        val key = textInputOption.key
    }

    override fun getSelectedOptions(filter: Filter): List<Option> =
            filterController.getSelectedOptions(filter)

    override fun removeSelectedOption(option: Option) = if (KEY_CATEGORY.equals(option.key)) {
        filterController.setFilter(option, false, true)
    } else {
        saveCheckedState(option, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeSubscription.unsubscribe()
    }

    override fun getFilterValue(key: String): String {
        return filterController.getFilterValue(key)
    }

    override fun getFilterViewState(uniqueId: String): Boolean {
        return filterController.getFilterViewState(uniqueId)
    }

    override fun onPriceRangeClicked() {

    }

    companion object {

        const val EXTRA_SELECTED_FILTERS = "EXTRA_SELECTED_FILTERS"
        const val EXTRA_SELECTED_OPTIONS = "EXTRA_SELECTED_OPTIONS"
        const val EXTRA_CALLER_SCREEN_NAME = "EXTRA_CALLER_SCREEN_NAME"
        const val EXTRA_QUERY_PARAMETERS = "EXTRA_QUERY_PARAMETERS"
    }
}
