package com.tokopedia.filter.widget

import android.content.Context
import android.content.Intent
import androidx.annotation.AttrRes
import com.google.android.material.bottomsheet.BottomSheetBehavior
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.TextView

import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.design.keyboard.KeyboardHelper
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.R

import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.AbstractDynamicFilterDetailActivity
import com.tokopedia.filter.newdynamicfilter.DynamicFilterCategoryActivity
import com.tokopedia.filter.newdynamicfilter.DynamicFilterLocationActivity
import com.tokopedia.filter.newdynamicfilter.adapter.DynamicFilterAdapter
import com.tokopedia.filter.newdynamicfilter.adapter.typefactory.BottomSheetDynamicFilterTypeFactoryImpl
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTracking
import com.tokopedia.filter.newdynamicfilter.analytics.FilterTrackingData
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.filter.newdynamicfilter.helper.FilterDbHelper
import com.tokopedia.filter.newdynamicfilter.helper.FilterDetailActivityRouter
import com.tokopedia.filter.newdynamicfilter.helper.FilterHelper
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.newdynamicfilter.view.BottomSheetDynamicFilterView

import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

import android.app.Activity.RESULT_OK

class BottomSheetFilterView : BaseCustomView, BottomSheetDynamicFilterView {
    private var filterMainRecyclerView: RecyclerView? = null
    private var filterMainAdapter: DynamicFilterAdapter? = null
    private var buttonReset: TextView? = null
    private var buttonClose: View? = null
    private var buttonFinish: TextView? = null
    private var loadingView: View? = null
    private var bottomSheetLayout: View? = null
    private var bottomSheetBehavior: UserLockBottomSheetBehavior<*>? = null
    private lateinit var bottomSheetBehaviourRootView: View

    private lateinit var callback: Callback

    private var selectedExpandableItemPosition: Int = 0
    private val filterController = FilterController()

    private var trackingData: FilterTrackingData? = null

    private val isBottomSheetShown: Boolean
        get() = bottomSheetBehavior != null && bottomSheetBehavior?.state != BottomSheetBehavior.STATE_HIDDEN

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        init()
    }

    private fun init() {
        bottomSheetBehaviourRootView = inflate(context, R.layout.filter_bottom_sheet, this)
        filterMainRecyclerView = bottomSheetBehaviourRootView.findViewById(R.id.dynamic_filter_recycler_view)
        buttonClose = bottomSheetBehaviourRootView.findViewById(R.id.top_bar_close_button)
        buttonReset = bottomSheetBehaviourRootView.findViewById(R.id.top_bar_button_reset)
        bottomSheetLayout = this
        buttonFinish = bottomSheetBehaviourRootView.findViewById(R.id.button_finish)
        loadingView = bottomSheetBehaviourRootView.findViewById(R.id.filterProgressBar)
        initKeyboardVisibilityListener()
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    fun setFilterResultCount(formattedResultCount: String?) {
        buttonFinish?.text = String.format(context.getString(R.string.bottom_sheet_filter_finish_button_template_text), formattedResultCount)
        loadingView?.visibility = View.GONE
    }

    private fun closeView() {
        if (bottomSheetBehavior?.state != BottomSheetBehavior.STATE_HIDDEN && buttonFinish?.visibility == View.VISIBLE) {
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    override fun onExpandableItemClicked(filter: Filter) {
        selectedExpandableItemPosition = filterMainAdapter?.getItemPosition(filter)?: 0
        if (filter.isCategoryFilter) {
            launchFilterCategoryPage(filter)
        } else {
            enrichWithInputState(filter)


            trackingData?.let { FilterTracking.eventNavigateToFilterDetail(it, filter.title) }
            trackingData?.let { FilterDetailActivityRouter.launchDetailActivity(callback.getActivity(), filter, true, it) }
        }
    }

    private fun launchFilterCategoryPage(filter: Filter) {
        val categoryId = filterController.getFilterValue(SearchApiConst.SC)
        val selectedCategory = FilterHelper.getSelectedCategoryDetails(filter, categoryId)
        val selectedCategoryRootId = selectedCategory?.categoryRootId ?: ""

        trackingData?.let { FilterTracking.eventNavigateToFilterDetail(it, resources.getString(R.string.title_category)) }
        trackingData?.let {
            FilterDetailActivityRouter.launchCategoryActivity(callback.getActivity(),
                filter, selectedCategoryRootId, categoryId, true, it)
        }
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
        saveCheckedState(option, isChecked, "")
    }

    override fun saveCheckedState(option: Option, isChecked: Boolean, filterTitle: String) {
        trackingData?.let {
            FilterTracking.eventFilterJourney(it, filterTitle, option.name,
                false, isChecked, option.isAnnotation)
        }
        filterController.setFilter(option, isChecked, option.isTypeRadio)
        applyFilter()
    }

    private fun updateResetButtonVisibility() {
        if (buttonReset != null) {
            buttonReset?.visibility = if (filterController.isFilterActive()) View.VISIBLE else View.GONE
        }
    }

    override fun removeSavedTextInput(uniqueId: String) {
        val option = OptionHelper.generateOptionFromUniqueId(uniqueId)
        filterController.setFilter(option, false, true)
        updateResetButtonVisibility()
    }

    override fun saveTextInput(uniqueId: String, textInput: String) {
        val textInputOption = OptionHelper.generateOptionFromUniqueId(uniqueId)
        textInputOption.value = textInput
        filterController.setFilter(textInputOption, true, true)
        updateResetButtonVisibility()
    }

    override fun getSelectedOptions(filter: Filter): List<Option> {
        return filterController.getSelectedAndPopularOptions(filter)
    }

    override fun removeSelectedOption(option: Option) {
        removeSelectedOption(option, "")
    }

    override fun removeSelectedOption(option: Option, filterTitle: String) {
        if (Option.KEY_CATEGORY.equals(option.key)) {
            trackingData?.let {
                FilterTracking.eventFilterJourney(it, filterTitle, option.name,
                        false, false, option.isAnnotation)
            }
            filterController.setFilter(option, false, true)
            applyFilter()
        } else {
            saveCheckedState(option, false, filterTitle)
        }
    }

    private fun resetAllFilter() {
        filterController.resetAllFilters()
        filterMainAdapter?.notifyDataSetChanged()

        applyFilter()
    }

    fun initFilterBottomSheet(trackingData: FilterTrackingData) {
        this.trackingData = trackingData
        initBottomSheetListener()
        initFilterMainRecyclerView()
    }

    private fun initFilterMainRecyclerView() {
        val dynamicFilterTypeFactory = BottomSheetDynamicFilterTypeFactoryImpl(this)
        filterMainAdapter = DynamicFilterAdapter(dynamicFilterTypeFactory)
        filterMainRecyclerView?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val dividerItemDecoration = DividerItemDecoration(filterMainRecyclerView?.context, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(resources.getDrawable(R.drawable.bg_line_separator))
        filterMainRecyclerView?.addItemDecoration(dividerItemDecoration)
        filterMainRecyclerView?.adapter = filterMainAdapter
        filterMainRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    KeyboardHandler.hideSoftKeyboard(callback.getActivity())
                }
            }
        })
    }

    fun launchFilterBottomSheet() {
        if (bottomSheetBehavior != null) {
            bottomSheetBehavior?.state  = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    fun loadFilterItems(filterList: List<Filter>?, searchParameter: Map<String, String>?) {
        val initializedFilterList = FilterHelper.initializeFilterList(filterList)

        filterController.initFilterController(searchParameter, initializedFilterList)
        updateResetButtonVisibility()
        filterMainAdapter?.filterList = initializedFilterList
    }

    private fun initBottomSheetListener() {
        bottomSheetLayout?.let {
            bottomSheetBehavior = BottomSheetBehavior.from(it) as UserLockBottomSheetBehavior<*>
        }
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehavior?.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    callback.onHide()
                } else {
                    callback.onShow()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })
        buttonClose?.setOnClickListener { closeView() }
        buttonFinish?.setOnClickListener { closeView() }
        buttonReset?.setOnClickListener { resetAllFilter() }
    }

    override fun onPriceSliderRelease(minValue: Int, maxValue: Int) {
        if (filterController.isSliderMinValueHasChanged(minValue)) {
            sendTrackingPriceMin(minValue)
            applyFilter()
        }
        if (filterController.isSliderMaxValueHasChanged(maxValue)) {
            sendTrackingPriceMax(maxValue)
            applyFilter()
        }
    }

    override fun onPriceSliderPressed(minValue: Int, maxValue: Int) {
        filterController.saveSliderValueStates(minValue, maxValue)
    }

    override fun onMinPriceEditedFromTextInput(minValue: Int) {
        sendTrackingPriceMin(minValue)
        applyFilter()
    }

    override fun onMaxPriceEditedFromTextInput(maxValue: Int) {
        sendTrackingPriceMax(maxValue)
        applyFilter()
    }

    private fun sendTrackingPriceMin(value: Int) {
        trackingData?.let {
            FilterTracking.eventFilterJourney(it, Option.KEY_PRICE_MIN, value.toString(),
                    false, true, false)
        }
    }

    private fun sendTrackingPriceMax(value: Int) {
        trackingData?.let {
            FilterTracking.eventFilterJourney(it, Option.KEY_PRICE_MAX, value.toString(),
                    false, true, false)
        }
    }

    override fun isSelectedCategory(option: Option): Boolean {
        return filterController.getFilterViewState(option)
    }

    override fun selectCategory(option: Option, filterTitle: String) {
        trackingData?.let {
            FilterTracking.eventFilterJourney(it, filterTitle, option.name,
                false, true, option.isAnnotation)
        }
        filterController.setFilter(option, true, true)
        applyFilter()
    }

    fun onBackPressed(): Boolean {
        return if (isBottomSheetShown) {
            closeView()
            true
        } else {
            false
        }
    }

    private fun applyFilter() {
        updateResetButtonVisibility()
        loadingView?.visibility = View.VISIBLE
        buttonFinish?.text = ""
        callback.onApplyFilter(filterController.getParameter())
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                AbstractDynamicFilterDetailActivity.REQUEST_CODE -> data?.let { handleResultFromDetailPage(it) }
                DynamicFilterLocationActivity.REQUEST_CODE -> handleResultFromLocationPage()
                DynamicFilterCategoryActivity.REQUEST_CODE -> data?.let { handleResultFromCategoryPage(it) }
            }
            updateResetButtonVisibility()
        }
    }

    private fun handleResultFromDetailPage(data: Intent) {
        val optionList: List<Option> = data.getParcelableArrayListExtra(AbstractDynamicFilterDetailActivity.EXTRA_RESULT)

        filterController.setFilter(optionList)
        applyFilterFromDetailPage()
    }

    private fun handleResultFromLocationPage() {
        Observable.create<List<Option>> {subscriber ->
            subscriber.onNext(FilterDbHelper.loadLocationFilterOptions(context))
        }.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: Subscriber<List<Option>>() {
                override fun onNext(optionList: List<Option>?) {
                    filterController.setFilter(optionList)
                    applyFilterFromDetailPage()
                }
                override fun onCompleted() {}
                override fun onError(e: Throwable?) {}
            })
    }

    private fun handleResultFromCategoryPage(data: Intent) {
        val selectedCategoryId = data.getStringExtra(DynamicFilterCategoryActivity.EXTRA_SELECTED_CATEGORY_ID)

        val category = filterMainAdapter?.filterList?.let { FilterHelper.getSelectedCategoryDetailsFromFilterList(it, selectedCategoryId) }

        val selectedCategoryName = category?.categoryName ?: ""
        val categoryOption = OptionHelper.generateOptionFromCategory(selectedCategoryId, selectedCategoryName)

        filterController.setFilter(categoryOption, true, true)
        applyFilterFromDetailPage()
    }

    private fun applyFilterFromDetailPage() {
        filterMainAdapter?.notifyItemChanged(selectedExpandableItemPosition)
        applyFilter()
    }

    private fun initKeyboardVisibilityListener() {
        KeyboardHelper.setKeyboardVisibilityChangedListener(bottomSheetLayout, object : KeyboardHelper.OnKeyboardVisibilityChangedListener {
            override fun onKeyboardShown() {
                if (bottomSheetBehavior != null && bottomSheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED) {
                    buttonFinish?.visibility = View.GONE
                }
            }

            override fun onKeyboardHide() {
                if (bottomSheetBehavior != null && bottomSheetBehavior?.state == BottomSheetBehavior.STATE_EXPANDED) {
                    buttonFinish?.visibility = View.VISIBLE
                }
            }
        })
    }

    override fun getFilterValue(key: String): String {
        return filterController.getFilterValue(key)
    }

    override fun getFilterViewState(uniqueId: String): Boolean {
        return filterController.getFilterViewState(uniqueId)
    }

    override fun onPriceRangeClicked() {
        applyFilter()
    }

    interface Callback {
        fun getActivity(): AppCompatActivity
        fun onApplyFilter(filterParameter: Map<String, String>?)
        fun onShow()
        fun onHide()
    }
}
