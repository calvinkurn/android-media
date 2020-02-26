package com.tokopedia.product.manage.feature.filter.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.product.manage.ProductManageInstance
import com.tokopedia.product.manage.feature.filter.data.mapper.ProductManageFilterMapper
import com.tokopedia.product.manage.feature.filter.di.DaggerProductManageFilterComponent
import com.tokopedia.product.manage.feature.filter.di.ProductManageFilterComponent
import com.tokopedia.product.manage.feature.filter.di.ProductManageFilterModule
import com.tokopedia.product.manage.feature.filter.presentation.ProductManageFilterExpandActivity
import com.tokopedia.product.manage.feature.filter.presentation.adapter.FilterAdapter
import com.tokopedia.product.manage.feature.filter.presentation.adapter.FilterAdapterTypeFactory
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterViewModel
import com.tokopedia.product.manage.feature.filter.presentation.viewmodel.ProductManageFilterViewModel
import com.tokopedia.product.manage.feature.filter.presentation.widget.SeeAllListener
import com.tokopedia.product.manage.oldlist.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ProductManageFilterFragment : BottomSheetDialogFragment(),
        HasComponent<ProductManageFilterComponent>,
        SeeAllListener {

    companion object {
        const val ACTIVITY_EXPAND_FLAG = "expand_type"
        const val CACHE_MANAGER_KEY = "cache_id"
        const val SORT_CACHE_MANAGER_KEY = "sort"
        const val ETALASE_CACHE_MANAGER_KEY = "etalase"
        const val CATEGORIES_CACHE_MANAGER_KEY = "categories"
        const val OTHER_FILTER_CACHE_MANAGER_KEY = "filter"
        const val BOTTOMSHEET_TITLE = "Filter"

        fun createInstance() : ProductManageFilterFragment {
            return ProductManageFilterFragment()
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        savedInstanceManager = this.context?.let { SaveInstanceCacheManager(it, true) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        savedInstanceManager?.onSave(outState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_filter, container, false)
        layoutManager = LinearLayoutManager(this.context)
        recyclerView = view.findViewById(R.id.filter_recycler_view)
        val adapterTypeFactory = FilterAdapterTypeFactory(this)
        filterAdapter = FilterAdapter(adapterTypeFactory)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.adapter = filterAdapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        productManageFilterViewModel.getData(userSession.shopId)
        observeCombinedResponse()
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
        this.activity?.startActivity(intent)

    }

    private fun initInjector() {
        component?.inject(this)
    }

    private fun observeCombinedResponse() {
        productManageFilterViewModel.combinedResponse.observe(this, Observer {
            when(it) {
                is Success -> {
                    filterAdapter?.updateData(ProductManageFilterMapper.mapCombinedResultToFilterViewModels(it.data))
                }
                is Fail -> {
                    showErrorMessage()
                }
            }
        })
    }

    private fun showErrorMessage() {

    }
}