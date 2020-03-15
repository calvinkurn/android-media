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
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.product.manage.ProductManageInstance
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.filter.data.mapper.ProductManageFilterMapper
import com.tokopedia.product.manage.feature.filter.di.DaggerProductManageFilterComponent
import com.tokopedia.product.manage.feature.filter.di.ProductManageFilterComponent
import com.tokopedia.product.manage.feature.filter.di.ProductManageFilterModule
import com.tokopedia.product.manage.feature.filter.presentation.adapter.SelectAdapter
import com.tokopedia.product.manage.feature.filter.presentation.adapter.factory.SelectAdapterTypeFactory
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.ChecklistViewModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterViewModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.SelectViewModel
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment.Companion.CATEGORIES_CACHE_MANAGER_KEY
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment.Companion.OTHER_FILTER_CACHE_MANAGER_KEY
import com.tokopedia.product.manage.feature.filter.presentation.viewmodel.ProductManageFilterExpandChecklistViewModel
import com.tokopedia.product.manage.feature.filter.presentation.widget.ChecklistClickListener
import com.tokopedia.product.manage.feature.filter.presentation.widget.SelectClickListener
import com.tokopedia.product.manage.feature.list.utils.ProductManageTracking
import kotlinx.android.synthetic.main.fragment_product_manage_filter_search.*
import java.util.*
import javax.inject.Inject

class ProductManageFilterExpandChecklistFragment :
        Fragment(), ChecklistClickListener, SelectClickListener,
        HasComponent<ProductManageFilterComponent> {

    companion object {
        fun createInstance(flag: String, cacheManagerId: String): ProductManageFilterExpandChecklistFragment {
            return ProductManageFilterExpandChecklistFragment().apply {
                arguments = Bundle().apply {
                    putString(ProductManageFilterFragment.ACTIVITY_EXPAND_FLAG, flag)
                    putString(ProductManageFilterFragment.CACHE_MANAGER_KEY, cacheManagerId)
                }
            }
        }
    }

    @Inject
    lateinit var productManageFilterExpandChecklistViewModel: ProductManageFilterExpandChecklistViewModel

    private var adapter: SelectAdapter? = null
    private var flag: String = ""
    private var isChipsShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        var cacheManagerId = ""
        arguments?.let {
            flag = it.getString(ProductManageFilterFragment.ACTIVITY_EXPAND_FLAG) ?: ""
            cacheManagerId = it.getString(ProductManageFilterFragment.CACHE_MANAGER_KEY) ?: ""
        }
        val manager = this.context?.let { SaveInstanceCacheManager(it, savedInstanceState) }
        val cacheManager = if (savedInstanceState == null) this.context?.let { SaveInstanceCacheManager(it, cacheManagerId) } else manager
        val filterViewModel: FilterViewModel? = flag.let { cacheManager?.get(it, FilterViewModel::class.java) }
        filterViewModel?.let {
            isChipsShown = filterViewModel.isChipsShown
            productManageFilterExpandChecklistViewModel.initData(ProductManageFilterMapper.mapFilterViewModelsToChecklistViewModels(it))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_product_manage_filter_search, container, false)
        val adapterTypeFactory = SelectAdapterTypeFactory(this, this)
        adapter = SelectAdapter(adapterTypeFactory)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        filterCheckListRecyclerView.adapter = adapter
        filterCheckListRecyclerView.layoutManager = LinearLayoutManager(this.context)
        observeDataLength()
        observeChecklistData()
        initView()
    }

    override fun onChecklistClick(element: ChecklistViewModel) {
        productManageFilterExpandChecklistViewModel.updateSelectedItem(element)
        if(element.isSelected) {
            ProductManageTracking.eventMoreOthersFilter(element.name, getString(R.string.product_manage_tracking_active))
        } else {
            ProductManageTracking.eventMoreOthersFilter(element.name, getString(R.string.product_manage_tracking_not_active))
        }
    }

    override fun onSelectClick(element: SelectViewModel) {
        //No Op
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

    private fun initInjector() {
        component?.inject(this)
    }

    private fun initView() {
        configToolbar()
        filterCheckListRecyclerView.setOnTouchListener { _, _ ->
            filterSearchBar?.hideKeyboard()
            filterSubmitButton.visibility = View.VISIBLE
            false
        }
        initButtons()
    }

    private fun configToolbar() {
        filterSearchHeader.isShowShadow = false
        filterSearchHeader.isShowBackButton = true
        filterSearchHeader.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        context?.let {
            if(flag == CATEGORIES_CACHE_MANAGER_KEY) {
                filterSearchHeader.title = it.resources.getString(R.string.product_manage_filter_all_categories_title)
                initSearchView()
            } else {
                filterSearchHeader.title = it.resources.getString(R.string.product_manage_filter_other_filters_title)
            }
            filterSearchHeader.actionText = it.resources.getString(R.string.filter_expand_reset)
        }
    }

    private fun initSearchView() {
        filterSearchBar.setDelayTextChanged(250)
        filterSearchBar.setListener(object : SearchInputView.Listener {
            override fun onSearchSubmitted(text: String?) {
                filterSearchBar.hideKeyboard()
            }
            override fun onSearchTextChanged(text: String?) {
                hideError()
                text?.let {
                    if(it.isNotEmpty()) {
                        search(it).let { result ->
                            if(result.isNotEmpty()) {
                                adapter?.updateChecklistData(result)
                            } else {
                                showError()
                            }
                        }
                    } else {
                        productManageFilterExpandChecklistViewModel.checklistData.value?.toList()?.let {data ->
                            adapter?.updateChecklistData(data)
                        }
                    }
                }
            }
        })
        filterSearchBar.visibility = View.VISIBLE
        filterSearchBar.setFocusChangeListener {
            if(filterSubmitButton.visibility == View.VISIBLE) {
                filterSubmitButton.visibility = View.GONE
            } else {
                filterSubmitButton.visibility = View.VISIBLE
            }
        }
    }

    private fun observeDataLength() {
        productManageFilterExpandChecklistViewModel.dataSize.observe(this, Observer {
            if(it > 0) {
                showButtons()
            } else {
                hideButtons()
            }
        })
    }

    private fun observeChecklistData() {
        productManageFilterExpandChecklistViewModel.checklistData.observe(this, Observer {
            adapter?.updateChecklistData(it)
        })
    }

    private fun showButtons() {
        filterSubmitButton.isEnabled = true
        filterSearchHeader.actionTextView?.visibility = View.VISIBLE
    }

    private fun hideButtons() {
        filterSubmitButton.isEnabled = false
        filterSearchHeader.actionTextView?.visibility = View.GONE
    }

    private fun initButtons() {
        filterSubmitButton.setOnClickListener {
            val cacheManager = context?.let { context -> SaveInstanceCacheManager(context, true) }
            val cacheManagerId = cacheManager?.id
            productManageFilterExpandChecklistViewModel.checklistData.value?.sortByDescending { it.isSelected }
            val dataToSave = productManageFilterExpandChecklistViewModel.checklistData.value?.toList() ?: listOf()
            if(flag == CATEGORIES_CACHE_MANAGER_KEY) {
                cacheManager?.put(CATEGORIES_CACHE_MANAGER_KEY,
                        ProductManageFilterMapper.mapChecklistViewModelsToFilterViewModel(
                                CATEGORIES_CACHE_MANAGER_KEY,
                                dataToSave,
                                isChipsShown
                        ))
                this.activity?.setResult(ProductManageFilterFragment.UPDATE_CATEGORIES_SUCCESS_RESPONSE,
                        Intent().putExtra(ProductManageFilterFragment.CACHE_MANAGER_KEY, cacheManagerId))
            } else {
                cacheManager?.put(OTHER_FILTER_CACHE_MANAGER_KEY,
                        ProductManageFilterMapper.mapChecklistViewModelsToFilterViewModel(
                                OTHER_FILTER_CACHE_MANAGER_KEY,
                                dataToSave,
                                isChipsShown
                        ))
                this.activity?.setResult(ProductManageFilterFragment.UPDATE_OTHER_FILTER_SUCCESS_RESPONSE,
                        Intent().putExtra(ProductManageFilterFragment.CACHE_MANAGER_KEY, cacheManagerId))
            }
            this.activity?.finish()
            ProductManageTracking.eventMoreOthersFilterSave()
        }
        filterSearchHeader.actionTextView?.setOnClickListener {
            adapter?.reset()
            productManageFilterExpandChecklistViewModel.clearAllChecklist()
        }
    }

    private fun search(searchQuery: String): List<ChecklistViewModel> {
        val result = productManageFilterExpandChecklistViewModel.checklistData.value?.filter { data ->
            data.name.toLowerCase(Locale.getDefault()) == searchQuery.toLowerCase(Locale.getDefault())
        }
        if(result.isNullOrEmpty()) {
            return emptyList()
        }
        return result
    }

    private fun showError() {
        filterCheckListRecyclerView.visibility = View.GONE
        filterSearchErrorImage.visibility = View.VISIBLE
        filterSearchErrorText.visibility = View.VISIBLE
    }

    private fun hideError() {
        filterCheckListRecyclerView.visibility = View.VISIBLE
        filterSearchErrorImage?.visibility = View.GONE
        filterSearchErrorText.visibility = View.GONE
    }


}