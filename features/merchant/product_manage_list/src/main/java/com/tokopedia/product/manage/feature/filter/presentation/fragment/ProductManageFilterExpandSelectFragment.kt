package com.tokopedia.product.manage.feature.filter.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.product.manage.ProductManageInstance
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.common.feature.list.analytics.ProductManageTracking
import com.tokopedia.product.manage.feature.filter.data.mapper.ProductManageFilterMapper
import com.tokopedia.product.manage.feature.filter.di.DaggerProductManageFilterComponent
import com.tokopedia.product.manage.feature.filter.di.ProductManageFilterComponent
import com.tokopedia.product.manage.feature.filter.presentation.adapter.SelectAdapter
import com.tokopedia.product.manage.feature.filter.presentation.adapter.factory.SelectAdapterTypeFactory
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.ChecklistUiModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterUiModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.SelectUiModel
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment.Companion.ACTIVITY_EXPAND_FLAG
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment.Companion.CACHE_MANAGER_KEY
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment.Companion.ETALASE_CACHE_MANAGER_KEY
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment.Companion.SORT_CACHE_MANAGER_KEY
import com.tokopedia.product.manage.feature.filter.presentation.viewmodel.ProductManageFilterExpandSelectViewModel
import com.tokopedia.product.manage.feature.filter.presentation.widget.ChecklistClickListener
import com.tokopedia.product.manage.feature.filter.presentation.widget.SelectClickListener
import kotlinx.android.synthetic.main.fragment_product_manage_filter_select.*
import javax.inject.Inject

class ProductManageFilterExpandSelectFragment :
        Fragment(), SelectClickListener, ChecklistClickListener,
        HasComponent<ProductManageFilterComponent> {

    companion object {
        fun createInstance(flag: String, cacheManagerId: String): ProductManageFilterExpandSelectFragment {
            return ProductManageFilterExpandSelectFragment().apply {
                arguments = Bundle().apply {
                    putString(ACTIVITY_EXPAND_FLAG, flag)
                    putString(CACHE_MANAGER_KEY, cacheManagerId)
                }
            }
        }
    }

    @Inject
    lateinit var productManageFilterExpandSelectViewModel: ProductManageFilterExpandSelectViewModel

    private var adapter: SelectAdapter? = null
    private var flag: String = ""
    private var isChipsShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        var cacheManagerId = ""
        arguments?.let {
            flag = it.getString(ACTIVITY_EXPAND_FLAG) ?: ""
            cacheManagerId = it.getString(CACHE_MANAGER_KEY) ?: ""
        }
        val manager = this.context?.let { SaveInstanceCacheManager(it, savedInstanceState) }
        val cacheManager = if (savedInstanceState == null) this.context?.let { SaveInstanceCacheManager(it, cacheManagerId) } else manager
        val filterUiModel: FilterUiModel? = flag.let { cacheManager?.get(it, FilterUiModel::class.java) }
        filterUiModel?.let {
            isChipsShown = it.isChipsShown
            val dataToDisplay = ProductManageFilterMapper.mapFilterViewModelsToSelectViewModels(filterUiModel)
            productManageFilterExpandSelectViewModel.updateData(dataToDisplay)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_product_manage_filter_select, container, false)
        val adapterTypeFactory = SelectAdapterTypeFactory(this, this)
        adapter = SelectAdapter(adapterTypeFactory)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        filterSelectRecyclerView.adapter = adapter
        filterSelectRecyclerView.layoutManager = LinearLayoutManager(this.context)
        initView()
    }

    override fun onSelectClick(element: SelectUiModel) {
        val cacheManager = context?.let { SaveInstanceCacheManager(it, true) }
        val cacheManagerId = cacheManager?.id
        val needSort = productManageFilterExpandSelectViewModel.updateSelectedItem(element)
        val dataToSave = productManageFilterExpandSelectViewModel.selectData.value?.toMutableList() ?: mutableListOf()
        if(needSort) dataToSave.sortByDescending { it.isSelected }
        if(flag == SORT_CACHE_MANAGER_KEY) {
            cacheManager?.put(SORT_CACHE_MANAGER_KEY, ProductManageFilterMapper.mapSelectViewModelsToFilterViewModel(
                    SORT_CACHE_MANAGER_KEY,
                    dataToSave,
                    isChipsShown
            ))
            this.activity?.setResult(ProductManageFilterFragment.UPDATE_SORT_SUCCESS_RESPONSE, Intent().putExtra(CACHE_MANAGER_KEY, cacheManagerId))
            ProductManageTracking.eventMoreSorting(element.name)
        } else {
            cacheManager?.put(ETALASE_CACHE_MANAGER_KEY, ProductManageFilterMapper.mapSelectViewModelsToFilterViewModel(
                    ETALASE_CACHE_MANAGER_KEY,
                    dataToSave,
                    isChipsShown
            ))
            this.activity?.setResult(ProductManageFilterFragment.UPDATE_ETALASE_SUCCESS_RESPONSE, Intent().putExtra(CACHE_MANAGER_KEY, cacheManagerId))
        }
        this.activity?.finish()
    }

    override fun onChecklistClick(element: ChecklistUiModel) {
        //No Op
    }

    override fun getComponent(): ProductManageFilterComponent? {
        return activity?.run {
            DaggerProductManageFilterComponent
                    .builder()
                    .productManageComponent(ProductManageInstance.getComponent(application))
                    .build()
        }
    }

    override fun onDestroy() {
        removeObservers()
        super.onDestroy()
    }

    private fun initInjector() {
        component?.inject(this)
    }

    private fun initView() {
        configToolbar()
        observeFilterViewModel()
    }

    private fun configToolbar() {
        filterSelectHeader.isShowBackButton = true
        filterSelectHeader.isShowShadow = false
        filterSelectHeader.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        context?.let {
            if(flag == SORT_CACHE_MANAGER_KEY) {
                filterSelectHeader.title = it.resources.getString(R.string.product_manage_filter_sort_select_title)
            } else {
                filterSelectHeader.title = it.resources.getString(R.string.product_manage_filter_etalase_select_title)
            }
        }
    }

    private fun observeFilterViewModel() {
        productManageFilterExpandSelectViewModel.selectData.observe(viewLifecycleOwner, Observer {
            adapter?.updateSelectData(it)
        })
    }

    private fun removeObservers() {
        removeObservers(productManageFilterExpandSelectViewModel.selectData)
    }
}