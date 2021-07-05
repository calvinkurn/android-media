package com.tokopedia.product.manage.feature.filter.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.product.manage.ProductManageInstance
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.common.util.ProductManageListErrorHandler
import com.tokopedia.product.manage.feature.filter.data.mapper.ProductManageFilterMapper
import com.tokopedia.product.manage.feature.filter.data.mapper.ProductManageFilterMapper.Companion.CATEGORY_HEADER
import com.tokopedia.product.manage.feature.filter.data.mapper.ProductManageFilterMapper.Companion.ETALASE_HEADER
import com.tokopedia.product.manage.feature.filter.data.mapper.ProductManageFilterMapper.Companion.OTHER_FILTER_HEADER
import com.tokopedia.product.manage.feature.filter.data.mapper.ProductManageFilterMapper.Companion.SORT_HEADER
import com.tokopedia.product.manage.feature.filter.data.model.FilterOptionWrapper
import com.tokopedia.product.manage.feature.filter.di.DaggerProductManageFilterComponent
import com.tokopedia.product.manage.feature.filter.di.ProductManageFilterComponent
import com.tokopedia.product.manage.feature.filter.presentation.activity.ProductManageFilterExpandActivity
import com.tokopedia.product.manage.feature.filter.presentation.adapter.FilterAdapter
import com.tokopedia.product.manage.feature.filter.presentation.adapter.factory.FilterAdapterTypeFactory
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterDataUiModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterUiModel
import com.tokopedia.product.manage.feature.filter.presentation.viewmodel.ProductManageFilterViewModel
import com.tokopedia.product.manage.feature.filter.presentation.widget.ChipsAdapter
import com.tokopedia.product.manage.feature.filter.presentation.widget.SeeAllListener
import com.tokopedia.product.manage.feature.filter.presentation.widget.ShowChipsListener
import com.tokopedia.product.manage.common.feature.list.analytics.ProductManageTracking
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_product_manage_filter_new.*
import javax.inject.Inject

class ProductManageFilterFragment(private var onFinishedListener: OnFinishedListener? = null,
                                  private var filterOptionWrapper: FilterOptionWrapper? = null) : BottomSheetUnify(),
        HasComponent<ProductManageFilterComponent>,
        SeeAllListener, ChipsAdapter.ChipClickListener, ShowChipsListener {

    companion object {
        const val CACHE_MANAGER_ID = "cache_manager_id"

        const val KEY_FILTER_OPTION_WRAPPER = "filter_option_wrapper"
        const val KEY_NEED_TO_POSTPONE_ACTIVITY_RESULT = "postpone_activity_result"
        const val KEY_SELECTED_FILTERS = "selected_filters"

        const val ACTIVITY_EXPAND_FLAG = "expand_type"
        const val CACHE_MANAGER_KEY = "cache_id"
        const val SORT_CACHE_MANAGER_KEY = "sort"
        const val ETALASE_CACHE_MANAGER_KEY = "etalase"
        const val CATEGORIES_CACHE_MANAGER_KEY = "categories"
        const val OTHER_FILTER_CACHE_MANAGER_KEY = "filter"
        const val BOTTOMSHEET_TITLE = "Filter"
        const val EXPAND_FILTER_REQUEST = 1
        const val UPDATE_SORT_SUCCESS_RESPONSE = 200
        const val UPDATE_ETALASE_SUCCESS_RESPONSE = 300
        const val UPDATE_CATEGORIES_SUCCESS_RESPONSE = 400
        const val UPDATE_OTHER_FILTER_SUCCESS_RESPONSE = 500
        const val ITEM_SORT_INDEX = 0
        const val ITEM_ETALASE_INDEX = 1
        const val ITEM_CATEGORIES_INDEX = 2
        const val ITEM_OTHER_FILTER_INDEX = 3

        fun createInstance(filterOptionWrapper: FilterOptionWrapper?, onFinishedListener: OnFinishedListener) : ProductManageFilterFragment {
            return ProductManageFilterFragment(onFinishedListener, filterOptionWrapper)
        }
    }

    @Inject
    lateinit var productManageFilterViewModel: ProductManageFilterViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

    private var filterAdapter: FilterAdapter? = null
    private var layoutManager: LinearLayoutManager? = null
    private var needToPostponeActivityResult: Boolean = false
    private var postponedActivityResult: Runnable? = null
    private var cacheManager: SaveInstanceCacheManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            val cacheManagerId = it.getString(CACHE_MANAGER_ID).orEmpty()
            cacheManager = context?.let { SaveInstanceCacheManager(it, cacheManagerId) }
            filterOptionWrapper = cacheManager?.get(KEY_FILTER_OPTION_WRAPPER, FilterOptionWrapper::class.java, null)
            needToPostponeActivityResult = cacheManager?.get(KEY_NEED_TO_POSTPONE_ACTIVITY_RESULT, Boolean::class.java, false) ?: false
        }
        val view = View.inflate(context, R.layout.fragment_product_manage_filter_new,null)
        setChild(view)
        setTitle(BOTTOMSHEET_TITLE)
        initInjector()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeCombinedResponse()
        observeFilterData()
        initView()
        productManageFilterViewModel.getData(userSession.shopId)
        showLoading()
    }

    override fun onDestroy() {
        productManageFilterViewModel.flush()
        super.onDestroy()
    }

    override fun getComponent(): ProductManageFilterComponent? {
        return activity?.run {
            DaggerProductManageFilterComponent
                    .builder()
                    .productManageComponent(ProductManageInstance.getComponent(application))
                    .build()
        }
    }

    override fun onSeeAll(element: FilterUiModel) {
        val intent = Intent(this.activity,ProductManageFilterExpandActivity::class.java)
        val cacheManager = context?.let { SaveInstanceCacheManager(it, true) }
        when(element.title) {
            SORT_HEADER -> {
                cacheManager?.put(SORT_CACHE_MANAGER_KEY, element)
                intent.putExtra(ACTIVITY_EXPAND_FLAG, SORT_CACHE_MANAGER_KEY)
                ProductManageTracking.eventSortingFilter()
            }
            ETALASE_HEADER -> {
                cacheManager?.put(ETALASE_CACHE_MANAGER_KEY, element)
                intent.putExtra(ACTIVITY_EXPAND_FLAG, ETALASE_CACHE_MANAGER_KEY)
            }
            CATEGORY_HEADER -> {
                cacheManager?.put(CATEGORIES_CACHE_MANAGER_KEY, element)
                intent.putExtra(ACTIVITY_EXPAND_FLAG, CATEGORIES_CACHE_MANAGER_KEY)
                ProductManageTracking.eventCategoryFilter()
            }
            OTHER_FILTER_HEADER -> {
                cacheManager?.put(OTHER_FILTER_CACHE_MANAGER_KEY, element)
                intent.putExtra(ACTIVITY_EXPAND_FLAG, OTHER_FILTER_CACHE_MANAGER_KEY)
                ProductManageTracking.eventOthersFilter()
            }
        }
        intent.putExtra(CACHE_MANAGER_KEY, cacheManager?.id)
        startActivityForResult(intent, EXPAND_FILTER_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == EXPAND_FILTER_REQUEST) {
            if (!needToPostponeActivityResult) {
                processActivityResult(resultCode, data)
            } else {
                postponedActivityResult = Runnable {
                    processActivityResult(resultCode, data)
                }
            }
        }
    }

    override fun onChipClicked(element: FilterDataUiModel, canSelectMany: Boolean, title: String) {
        if(canSelectMany) {
            productManageFilterViewModel.updateSelect(element)
            if(title == OTHER_FILTER_HEADER) {
                ProductManageTracking.eventOthersFilterName(element.name)
            }
        } else {
            productManageFilterViewModel.updateSelect(element, title)
            if(title == ETALASE_HEADER) {
                ProductManageTracking.eventEtalaseFilter(element.name)
            } else {
                ProductManageTracking.eventSortingFilterName(element.name)
            }
        }
    }

    override fun onShowChips(element: FilterUiModel) {
        productManageFilterViewModel.updateShow(element)
        when(element.title) {
            SORT_HEADER -> filterRecyclerView.scrollToPosition(ITEM_SORT_INDEX)
            ETALASE_HEADER -> filterRecyclerView.scrollToPosition(ITEM_ETALASE_INDEX)
            CATEGORY_HEADER -> filterRecyclerView.scrollToPosition(ITEM_CATEGORIES_INDEX)
            else -> filterRecyclerView.scrollToPosition(ITEM_OTHER_FILTER_INDEX)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val selectedFilters = productManageFilterViewModel.getSelectedFilters()
        val cacheManager = context?.let { SaveInstanceCacheManager(it, true) }
        cacheManager?.put(KEY_FILTER_OPTION_WRAPPER, filterOptionWrapper)
        cacheManager?.put(KEY_NEED_TO_POSTPONE_ACTIVITY_RESULT, true)
        cacheManager?.put(KEY_SELECTED_FILTERS, selectedFilters)
        outState.putString(CACHE_MANAGER_ID, cacheManager?.id.orEmpty())
    }

    private fun processActivityResult(resultCode: Int, data: Intent?) {
        val cacheManager = context?.let { SaveInstanceCacheManager(it, data?.getStringExtra(CACHE_MANAGER_KEY)) }
        when(resultCode) {
            UPDATE_SORT_SUCCESS_RESPONSE -> {
                val dataToUpdate: FilterUiModel? = cacheManager?.get(SORT_CACHE_MANAGER_KEY, FilterUiModel::class.java)
                dataToUpdate?.let {
                    productManageFilterViewModel.updateSpecificData(it, ITEM_SORT_INDEX)
                }
            }
            UPDATE_ETALASE_SUCCESS_RESPONSE -> {
                val dataToUpdate: FilterUiModel? = cacheManager?.get(ETALASE_CACHE_MANAGER_KEY, FilterUiModel::class.java)
                dataToUpdate?.let {
                    productManageFilterViewModel.updateSpecificData(it, ITEM_ETALASE_INDEX)
                }
            }
            UPDATE_CATEGORIES_SUCCESS_RESPONSE -> {
                val dataToUpdate: FilterUiModel? = cacheManager?.get(CATEGORIES_CACHE_MANAGER_KEY, FilterUiModel::class.java)
                dataToUpdate?.let {
                    productManageFilterViewModel.updateSpecificData(it, ITEM_CATEGORIES_INDEX)
                }
            }
            UPDATE_OTHER_FILTER_SUCCESS_RESPONSE -> {
                val dataToUpdate: FilterUiModel? = cacheManager?.get(OTHER_FILTER_CACHE_MANAGER_KEY, FilterUiModel::class.java)
                dataToUpdate?.let {
                    productManageFilterViewModel.updateSpecificData(it, ITEM_OTHER_FILTER_INDEX)
                }
            }
        }
    }

    private fun initInjector() {
        component?.inject(this)
    }

    private fun observeCombinedResponse() {
        productManageFilterViewModel.filterOptionsResponse.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Success -> {
                    val mappedResult = ProductManageFilterMapper.mapCombinedResultToFilterViewModels(it.data)
                    productManageFilterViewModel.updateData(mappedResult)
                    getSelectedFilters()
                }
                is Fail -> {
                    this.dismiss()
                    ProductManageListErrorHandler.logExceptionToCrashlytics(it.throwable)
                }
            }
        })
    }

    private fun initView() {
        layoutManager = LinearLayoutManager(this.context)
        val adapterTypeFactory = FilterAdapterTypeFactory(this, this, this)
        filterAdapter = FilterAdapter(adapterTypeFactory)
        filterRecyclerView.layoutManager = layoutManager
        filterRecyclerView.adapter = filterAdapter
        buttonCloseBottomSheet.setOnClickListener {
            productManageFilterViewModel.filterData.value?.let { data ->
                val dataToSave = ProductManageFilterMapper.mapFiltersToFilterOptions(data)
                onFinishedListener?.onFinish(dataToSave)
                super.dismiss()
                ProductManageTracking.eventFilter()
            }
        }
        this.setCloseClickListener {
            productManageFilterViewModel.filterData.value?.let { data ->
                val dataToSave = ProductManageFilterMapper.mapFiltersToFilterOptions(data)
                onFinishedListener?.onFinish(dataToSave)
                super.dismiss()
            }
        }
        this.isFullpage = true
        adjustBottomSheetPadding()
        initBottomSheetReset()
    }

    private fun checkSelected(filterData: List<FilterUiModel>): Boolean {
        filterData.forEach { filter ->
            filter.data.forEach {
                if(it.select) return true
            }
        }
        return false
    }

    private fun observeFilterData() {
        productManageFilterViewModel.filterData.observe(viewLifecycleOwner, Observer {
            if (needToPostponeActivityResult) {
                postponedActivityResult?.run()
            }
            selectPreviouslySelectedFilters(it)
            hideLoading()
            filterAdapter?.updateData(it)
            if(checkSelected(it)) {
                bottomSheetAction.visibility = View.VISIBLE
            } else {
                bottomSheetAction.visibility = View.GONE
            }
        })
    }

    private fun selectPreviouslySelectedFilters(filterList: MutableList<FilterUiModel>) {
        val selectedFilters: List<FilterDataUiModel> = cacheManager?.get(KEY_SELECTED_FILTERS, object : TypeToken<List<FilterDataUiModel>>() {}.type, emptyList())
                ?: emptyList()
        selectedFilters.onEach { selectedFilter ->
            filterList.forEach loop@{ filterUiModel ->
                filterUiModel.data.forEach { filterDataUiModel ->
                    if (filterDataUiModel.id == selectedFilter.id && filterDataUiModel.name == selectedFilter.name &&
                            filterDataUiModel.value == selectedFilter.value) {
                        filterDataUiModel.select = filterDataUiModel.id == selectedFilter.id
                        return@loop
                    }
                }
            }
        }
    }

    private fun initBottomSheetReset() {
        bottomSheetAction.visibility = View.GONE
        context?.let {
            bottomSheetAction.text = it.resources.getString(R.string.filter_expand_reset)
        }
        bottomSheetAction.setOnClickListener {
            productManageFilterViewModel.clearSelected()
        }
    }

    private fun showLoading() {
        buttonCloseBottomSheet.isEnabled = false
        filterRecyclerView.visibility= View.INVISIBLE
        filterLoadingSpinner.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        filterLoadingSpinner.visibility = View.GONE
        buttonCloseBottomSheet.isEnabled = true
        filterRecyclerView.visibility= View.VISIBLE
    }

    private fun adjustBottomSheetPadding() {
        bottomSheetWrapper.setPadding(0,0,0,0)
        (bottomSheetHeader.layoutParams as LinearLayout.LayoutParams).setMargins(16.toPx(),16.toPx(),16.toPx(),16.toPx())
    }

    private fun getSelectedFilters() {
        filterOptionWrapper?.let {
            ProductManageFilterMapper.mapFilterOptionWrapperToSelectedSort(it)?.let { selectedSort ->
                productManageFilterViewModel.updateSelect(selectedSort,
                        SORT_HEADER)
            }
            ProductManageFilterMapper.mapFilterOptionWrapperToSelectedEtalase(it)?.let { selectedEtalase ->
                productManageFilterViewModel.updateSelect(selectedEtalase,
                        ETALASE_HEADER)
            }
            ProductManageFilterMapper.mapFilterOptionWrapperToSelectedCategories(it).forEach { data ->
                productManageFilterViewModel.updateSelect(data)
            }
            ProductManageFilterMapper.mapFilterOptionWrapperToSelectedOtherFilters(it).forEach { data ->
                productManageFilterViewModel.updateSelect(data)
            }
            productManageFilterViewModel.updateShow(it.filterShownState)
        }
    }

    fun setOnFinishedListener(onFinishedListener: OnFinishedListener) {
        this.onFinishedListener = onFinishedListener
    }

    interface OnFinishedListener {
        fun onFinish(selectedData: FilterOptionWrapper)
    }
}