package com.tokopedia.product.manage.feature.filter.presentation.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.product.manage.ProductManageInstance
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.filter.data.mapper.ProductManageFilterMapper
import com.tokopedia.product.manage.feature.filter.data.model.FilterOptionWrapper
import com.tokopedia.product.manage.feature.filter.di.DaggerProductManageFilterComponent
import com.tokopedia.product.manage.feature.filter.di.ProductManageFilterComponent
import com.tokopedia.product.manage.feature.filter.di.ProductManageFilterModule
import com.tokopedia.product.manage.feature.filter.presentation.activity.ProductManageFilterExpandActivity
import com.tokopedia.product.manage.feature.filter.presentation.adapter.FilterAdapter
import com.tokopedia.product.manage.feature.filter.presentation.adapter.factory.FilterAdapterTypeFactory
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterDataViewModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterViewModel
import com.tokopedia.product.manage.feature.filter.presentation.viewmodel.ProductManageFilterViewModel
import com.tokopedia.product.manage.feature.filter.presentation.widget.ChipClickListener
import com.tokopedia.product.manage.feature.filter.presentation.widget.SeeAllListener
import com.tokopedia.product.manage.feature.filter.presentation.widget.ShowChipsListener
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_filter.*
import kotlinx.android.synthetic.main.fragment_product_manage.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ProductManageFilterFragment : BottomSheetUnify(),
        HasComponent<ProductManageFilterComponent>,
        SeeAllListener, ChipClickListener, ShowChipsListener {

    companion object {
        const val ACTIVITY_EXPAND_FLAG = "expand_type"
        const val CACHE_MANAGER_KEY = "cache_id"
        const val SORT_CACHE_MANAGER_KEY = "sort"
        const val ETALASE_CACHE_MANAGER_KEY = "etalase"
        const val CATEGORIES_CACHE_MANAGER_KEY = "categories"
        const val OTHER_FILTER_CACHE_MANAGER_KEY = "filter"
        const val BOTTOMSHEET_TITLE = "Filter"
        const val REST_BUTTON_TEXT = "Reset"
        const val EXPAND_FILTER_REQUEST = 1
        const val UPDATE_SORT_SUCCESS_RESPONSE = 200
        const val UPDATE_ETALASE_SUCCESS_RESPONSE = 300
        const val UPDATE_CATEGORIES_SUCCESS_RESPONSE = 400
        const val UPDATE_OTHER_FILTER_SUCCESS_RESPONSE = 500
        const val ITEM_SORT_INDEX = 0
        const val ITEM_ETALASE_INDEX = 1
        const val ITEM_CATEGORIES_INDEX = 2
        const val ITEM_OTHER_FILTER_INDEX = 3
        const val SELECTED_FILTER = "selected_filters"

        fun createInstance(context: Context, cacheManagerId: String) : ProductManageFilterFragment {
            return ProductManageFilterFragment().apply{
                val view = View.inflate(context, R.layout.fragment_filter,null)
                setChild(view)
                setTitle(BOTTOMSHEET_TITLE)
                arguments = Bundle().apply {
                    putString(CACHE_MANAGER_KEY, cacheManagerId)
                }
            }
        }
    }

    @Inject
    lateinit var productManageFilterViewModel: ProductManageFilterViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

    private var filterAdapter: FilterAdapter? = null
    private var layoutManager: LinearLayoutManager? = null
    private var cacheManagerId: String = ""

    var isResultReady: Boolean = false
    var resultCacheManagerId: String = ""
    var selectedFilterOptions: FilterOptionWrapper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        arguments?.let {
            cacheManagerId = it.getString(CACHE_MANAGER_KEY) ?: ""
        }
        val manager = this.context?.let { SaveInstanceCacheManager(it, savedInstanceState) }
        val savedInstanceManager = if (savedInstanceState == null) this.context?.let { SaveInstanceCacheManager(it, cacheManagerId) } else manager
        //TODO use this cache manager to get filters from product list page
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        productManageFilterViewModel.getData(userSession.shopId)
        showLoading()
        layoutManager = LinearLayoutManager(this.context)
        val adapterTypeFactory = FilterAdapterTypeFactory(this, this, this)
        filterAdapter = FilterAdapter(adapterTypeFactory)
        filter_recycler_view.layoutManager = layoutManager
        filter_recycler_view.adapter = filterAdapter
        observeCombinedResponse()
        observeFilterData()
        initView()
    }

    override fun onDestroy() {
        productManageFilterViewModel.flush()
        super.onDestroy()
    }

    override fun getComponent(): ProductManageFilterComponent? {
        return activity?.run {
            DaggerProductManageFilterComponent
                    .builder()
                    .productManageFilterModule(ProductManageFilterModule())
                    .productManageComponent(ProductManageInstance.getComponent(application))
                    .build()
        }
    }

    override fun onSeeAll(element: FilterViewModel) {
        val intent = Intent(this.activity,ProductManageFilterExpandActivity::class.java)
        val cacheManager = context?.let { SaveInstanceCacheManager(it, true) }
        when(element.title) {
            ProductManageFilterMapper.SORT_HEADER -> {
                cacheManager?.put(SORT_CACHE_MANAGER_KEY, element)
                intent.putExtra(ACTIVITY_EXPAND_FLAG, SORT_CACHE_MANAGER_KEY)
            }
            ProductManageFilterMapper.ETALASE_HEADER -> {
                cacheManager?.put(ETALASE_CACHE_MANAGER_KEY, element)
                intent.putExtra(ACTIVITY_EXPAND_FLAG, ETALASE_CACHE_MANAGER_KEY)
            }
            ProductManageFilterMapper.CATEGORY_HEADER -> {
                cacheManager?.put(CATEGORIES_CACHE_MANAGER_KEY, element)
                intent.putExtra(ACTIVITY_EXPAND_FLAG, CATEGORIES_CACHE_MANAGER_KEY)
            }
            ProductManageFilterMapper.OTHER_FILTER_HEADER -> {
                cacheManager?.put(OTHER_FILTER_CACHE_MANAGER_KEY, element)
                intent.putExtra(ACTIVITY_EXPAND_FLAG, OTHER_FILTER_CACHE_MANAGER_KEY)
            }
        }
        intent.putExtra(CACHE_MANAGER_KEY, cacheManager?.id)
        startActivityForResult(intent, EXPAND_FILTER_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == EXPAND_FILTER_REQUEST) {
            val cacheManager = context?.let { SaveInstanceCacheManager(it, data?.getStringExtra(CACHE_MANAGER_KEY)) }
            when(resultCode) {
                UPDATE_SORT_SUCCESS_RESPONSE -> {
                    val dataToUpdate: FilterViewModel? = cacheManager?.get(SORT_CACHE_MANAGER_KEY, FilterViewModel::class.java)
                    dataToUpdate?.let {
                        productManageFilterViewModel.updateSpecificData(it, ITEM_SORT_INDEX)
                    }
                }
                UPDATE_ETALASE_SUCCESS_RESPONSE -> {
                    val dataToUpdate: FilterViewModel? = cacheManager?.get(ETALASE_CACHE_MANAGER_KEY, FilterViewModel::class.java)
                    dataToUpdate?.let {
                        productManageFilterViewModel.updateSpecificData(it, ITEM_ETALASE_INDEX)
                    }
                }
                UPDATE_CATEGORIES_SUCCESS_RESPONSE -> {
                    val dataToUpdate: FilterViewModel? = cacheManager?.get(CATEGORIES_CACHE_MANAGER_KEY, FilterViewModel::class.java)
                    dataToUpdate?.let {
                        productManageFilterViewModel.updateSpecificData(it, ITEM_CATEGORIES_INDEX)
                    }
                }
                UPDATE_OTHER_FILTER_SUCCESS_RESPONSE -> {
                    val dataToUpdate: FilterViewModel? = cacheManager?.get(OTHER_FILTER_CACHE_MANAGER_KEY, FilterViewModel::class.java)
                    dataToUpdate?.let {
                        productManageFilterViewModel.updateSpecificData(it, ITEM_OTHER_FILTER_INDEX)
                    }
                }
            }
        }
    }

    override fun onChipClicked(data: FilterDataViewModel, canSelectMany: Boolean, title: String) {
        if(canSelectMany) {
            productManageFilterViewModel.updateSelect(data)
        } else {
            productManageFilterViewModel.updateSelect(data, title)
        }
    }

    override fun onShowChips(element: FilterViewModel) {
        productManageFilterViewModel.updateShow(element)
    }

    private fun initInjector() {
        component?.inject(this)
    }

    private fun observeCombinedResponse() {
        productManageFilterViewModel.filterOptionsResponse.observe(this, Observer {
            when(it) {
                is Success -> {
                    val mappedResult = ProductManageFilterMapper.mapCombinedResultToFilterViewModels(it.data)
                    productManageFilterViewModel.updateData(mappedResult)
                    filterAdapter?.updateData(mappedResult)
                    hideLoading()
                }
                is Fail -> {
                    this.dismiss()
                }
            }
        })
    }

    private fun initView() {
        btn_close_bottom_sheet.setOnClickListener {
            isResultReady = true
            productManageFilterViewModel.filterData.value?.let { data ->
                context?.let {
                    val dataToSave = ProductManageFilterMapper.mapFiltersToFilterOptions(data)
                    selectedFilterOptions = dataToSave
                    //TODO leo tanya hendry
//                    val savedInstanceManager = SaveInstanceCacheManager(it, true)
//                    savedInstanceManager.put(
//                            customId = SELECTED_FILTER,
//                            objectToPut = dataToSave,
//                            cacheDuration = TimeUnit.MINUTES.toMillis(5))
//                    resultCacheManagerId = savedInstanceManager.id ?: ""
                }
            }
            this.dismiss()
            super.dismiss()
        }
        initBottomSheetReset()
    }

    private fun checkSelected(filterData: List<FilterViewModel>): Boolean {
        filterData.forEach { filter ->
            filter.data.forEach {
                if(it.select) return true
            }
        }
        return false
    }

    private fun observeFilterData() {
        productManageFilterViewModel.filterData.observe(this, Observer {
            filterAdapter?.updateData(it)
            if(checkSelected(it)) {
                bottomSheetAction.visibility = View.VISIBLE
            } else {
                bottomSheetAction.visibility = View.GONE
            }
        })
    }

    private fun initBottomSheetReset() {
        bottomSheetAction.visibility = View.GONE
        bottomSheetAction.text = REST_BUTTON_TEXT
        bottomSheetAction.setOnClickListener {
            productManageFilterViewModel.clearSelected()
        }
    }

    private fun showLoading() {
        btn_close_bottom_sheet.isEnabled = false
        filter_recycler_view.visibility= View.INVISIBLE
        filter_loader.visibility = View.VISIBLE
        ImageHandler.loadGif(filter_loader_image, R.drawable.ic_loading_indeterminate, R.drawable.ic_loading_indeterminate)
    }

    private fun hideLoading() {
        filter_loader.visibility = View.GONE
        btn_close_bottom_sheet.isEnabled = true
        filter_recycler_view.visibility= View.VISIBLE
        ImageHandler.clearImage(filter_loader_image)
    }
}