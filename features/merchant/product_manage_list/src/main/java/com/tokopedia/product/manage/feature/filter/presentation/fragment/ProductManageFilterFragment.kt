package com.tokopedia.product.manage.feature.filter.presentation.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.product.manage.ProductManageInstance
import com.tokopedia.product.manage.feature.filter.data.mapper.ProductManageFilterMapper
import com.tokopedia.product.manage.feature.filter.di.DaggerProductManageFilterComponent
import com.tokopedia.product.manage.feature.filter.di.ProductManageFilterComponent
import com.tokopedia.product.manage.feature.filter.di.ProductManageFilterModule
import com.tokopedia.product.manage.feature.filter.presentation.activity.ProductManageFilterExpandActivity
import com.tokopedia.product.manage.feature.filter.presentation.adapter.FilterAdapter
import com.tokopedia.product.manage.feature.filter.presentation.adapter.factory.FilterAdapterTypeFactory
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterDataViewModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterViewModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.SelectViewModel
import com.tokopedia.product.manage.feature.filter.presentation.viewmodel.ProductManageFilterViewModel
import com.tokopedia.product.manage.feature.filter.presentation.widget.ChipClickListener
import com.tokopedia.product.manage.feature.filter.presentation.widget.SeeAllListener
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_filter.*
import javax.inject.Inject

class ProductManageFilterFragment : BottomSheetUnify(),
        HasComponent<ProductManageFilterComponent>,
        SeeAllListener, ChipClickListener{

    companion object {
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
        const val FIRST_INDEX = 0

        fun createInstance(context: Context) : ProductManageFilterFragment {
            return ProductManageFilterFragment().apply{
                val view = View.inflate(context, com.tokopedia.product.manage.R.layout.fragment_filter,null)
                setChild(view)
                setTitle(BOTTOMSHEET_TITLE)
                clearClose(true)
            }
        }
    }

    @Inject
    lateinit var productManageFilterViewModel: ProductManageFilterViewModel

    @Inject
    lateinit var userSession: UserSessionInterface

    private var filterAdapter: FilterAdapter? = null
    private var layoutManager: LinearLayoutManager? = null
    private var recyclerView: RecyclerView? = null
    private var savedInstanceManager: SaveInstanceCacheManager? = null
    private var sortData: FilterViewModel? = null
    private var etalaseData: FilterViewModel? = null
    private var categoriesData: FilterViewModel? = null
    private var otherFilterData: FilterViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        savedInstanceManager = this.context?.let { SaveInstanceCacheManager(it, true) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        savedInstanceManager?.onSave(outState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        layoutManager = LinearLayoutManager(this.context)
        recyclerView = view.findViewById(com.tokopedia.product.manage.R.id.filter_recycler_view)
        val adapterTypeFactory = FilterAdapterTypeFactory(this, this)
        filterAdapter = FilterAdapter(adapterTypeFactory)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.adapter = filterAdapter
        productManageFilterViewModel.getData(userSession.shopId)
        observeCombinedResponse()
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
        when(element.title) {
            ProductManageFilterMapper.SORT_HEADER -> {
                savedInstanceManager?.put(SORT_CACHE_MANAGER_KEY, element)
                intent.putExtra(ACTIVITY_EXPAND_FLAG, SORT_CACHE_MANAGER_KEY)
            }
            ProductManageFilterMapper.ETALASE_HEADER -> {
                savedInstanceManager?.put(ETALASE_CACHE_MANAGER_KEY, element)
                intent.putExtra(ACTIVITY_EXPAND_FLAG, ETALASE_CACHE_MANAGER_KEY)
            }
            ProductManageFilterMapper.CATEGORY_HEADER -> {
                savedInstanceManager?.put(CATEGORIES_CACHE_MANAGER_KEY, element)
                intent.putExtra(ACTIVITY_EXPAND_FLAG, CATEGORIES_CACHE_MANAGER_KEY)
            }
            ProductManageFilterMapper.OTHER_FILTER_HEADER -> {
                savedInstanceManager?.put(OTHER_FILTER_CACHE_MANAGER_KEY, element)
                intent.putExtra(ACTIVITY_EXPAND_FLAG, OTHER_FILTER_CACHE_MANAGER_KEY)
            }
        }
        intent.putExtra(CACHE_MANAGER_KEY, savedInstanceManager?.id)
        startActivityForResult(intent, EXPAND_FILTER_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == EXPAND_FILTER_REQUEST) {
            when(resultCode) {
                UPDATE_SORT_SUCCESS_RESPONSE -> {
                    val dataToUpdate: FilterViewModel? = savedInstanceManager?.get(SORT_CACHE_MANAGER_KEY, FilterViewModel::class.java)
                    sortData = dataToUpdate
                    filterAdapter?.updateSpecificData(dataToUpdate, ITEM_SORT_INDEX)
                }
                UPDATE_ETALASE_SUCCESS_RESPONSE -> {
                    val dataToUpdate: FilterViewModel? = savedInstanceManager?.get(ETALASE_CACHE_MANAGER_KEY, FilterViewModel::class.java)
                    etalaseData = dataToUpdate
                    filterAdapter?.updateSpecificData(dataToUpdate, ITEM_ETALASE_INDEX)
                }
                UPDATE_CATEGORIES_SUCCESS_RESPONSE -> {
                    val dataToUpdate: FilterViewModel? = savedInstanceManager?.get(CATEGORIES_CACHE_MANAGER_KEY, FilterViewModel::class.java)
                    categoriesData = dataToUpdate
                    filterAdapter?.updateSpecificData(dataToUpdate, ITEM_CATEGORIES_INDEX)
                }
                UPDATE_OTHER_FILTER_SUCCESS_RESPONSE -> {
                    val dataToUpdate: FilterViewModel? = savedInstanceManager?.get(OTHER_FILTER_CACHE_MANAGER_KEY, FilterViewModel::class.java)
                    otherFilterData = dataToUpdate
                    filterAdapter?.updateSpecificData(dataToUpdate, ITEM_OTHER_FILTER_INDEX)
                }
            }
        }
    }

    override fun onChipClicked() {

    }

    private fun initInjector() {
        component?.inject(this)
    }

    private fun observeCombinedResponse() {
        productManageFilterViewModel.combinedResponse.observe(this, Observer {
            when(it) {
                is Success -> {
                    val mappedResult = ProductManageFilterMapper.mapCombinedResultToFilterViewModels(it.data)
                    filterAdapter?.updateData(mappedResult)
                    sortData = mappedResult[ITEM_SORT_INDEX]
                    etalaseData = mappedResult[ITEM_ETALASE_INDEX]
                    categoriesData = mappedResult[ITEM_CATEGORIES_INDEX]
                    otherFilterData = mappedResult[ITEM_OTHER_FILTER_INDEX]
                }
                is Fail -> {
                    this.dismiss()
                }
            }
        })
    }

    private fun initView() {
        btn_close_bottom_sheet.setOnClickListener {
            this.dismiss()
        }
    }
}