package com.tokopedia.product.manage.feature.filter.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.product.manage.ProductManageInstance
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.filter.data.mapper.ProductManageFilterMapper
import com.tokopedia.product.manage.feature.filter.di.DaggerProductManageFilterComponent
import com.tokopedia.product.manage.feature.filter.di.ProductManageFilterComponent
import com.tokopedia.product.manage.feature.filter.presentation.adapter.SelectAdapter
import com.tokopedia.product.manage.feature.filter.presentation.adapter.factory.SelectAdapterTypeFactory
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.ChecklistUiModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterUiModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.SelectUiModel
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment.Companion.CATEGORIES_CACHE_MANAGER_KEY
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment.Companion.OTHER_FILTER_CACHE_MANAGER_KEY
import com.tokopedia.product.manage.feature.filter.presentation.util.textwatcher.SearchListener
import com.tokopedia.product.manage.feature.filter.presentation.util.textwatcher.SearchTextWatcher
import com.tokopedia.product.manage.feature.filter.presentation.viewmodel.ProductManageFilterExpandChecklistViewModel
import com.tokopedia.product.manage.feature.filter.presentation.widget.ChecklistClickListener
import com.tokopedia.product.manage.feature.filter.presentation.widget.SelectClickListener
import com.tokopedia.product.manage.common.feature.list.analytics.ProductManageTracking
import com.tokopedia.product.manage.databinding.FragmentProductManageFilterSearchBinding
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class ProductManageFilterExpandChecklistFragment :
        Fragment(), ChecklistClickListener, SelectClickListener,
        SearchListener, HasComponent<ProductManageFilterComponent> {

    companion object {
        private const val TOGGLE_ACTIVE = "active"
        private const val TOGGLE_NOT_ACTIVE = "not active"
        private const val DELAY_TEXT_CHANGE = 250L

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

    private var binding by autoClearedNullable<FragmentProductManageFilterSearchBinding>()

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
        val filterUiModel: FilterUiModel? = flag.let { cacheManager?.get(it, FilterUiModel::class.java) }
        filterUiModel?.let {
            isChipsShown = filterUiModel.isChipsShown
            productManageFilterExpandChecklistViewModel.initData(ProductManageFilterMapper.mapFilterViewModelsToChecklistViewModels(it))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProductManageFilterSearchBinding.inflate(inflater, container, false)
        val adapterTypeFactory = SelectAdapterTypeFactory(this, this)
        adapter = SelectAdapter(adapterTypeFactory)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.filterCheckListRecyclerView?.adapter = adapter
        binding?.filterCheckListRecyclerView?.layoutManager = LinearLayoutManager(this.context)
        observeDataLength()
        observeChecklistData()
        initView()
    }

    override fun onChecklistClick(element: ChecklistUiModel) {
        productManageFilterExpandChecklistViewModel.updateSelectedItem(element)
        if(element.isSelected) {
            ProductManageTracking.eventMoreOthersFilter(element.name, TOGGLE_ACTIVE)
        } else {
            ProductManageTracking.eventMoreOthersFilter(element.name, TOGGLE_NOT_ACTIVE)
        }
    }

    override fun onSelectClick(element: SelectUiModel) {
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

    override fun onSearchTextChanged(text: String) {
        processSearch(text)
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
        binding?.filterCheckListRecyclerView?.setOnTouchListener { _, _ ->
            binding?.filterSearchBar?.clearFocus()
            false
        }
        initButtons()
    }

    private fun configToolbar() {
        binding?.filterSearchHeader?.run {
            isShowShadow = false
            isShowBackButton = true
            setNavigationOnClickListener {
                this@ProductManageFilterExpandChecklistFragment.activity?.onBackPressed()
            }
            context?.let {
                if(flag == CATEGORIES_CACHE_MANAGER_KEY) {
                    title = it.resources.getString(R.string.product_manage_filter_all_categories_title)
                    initSearchView()
                } else {
                    title = it.resources.getString(R.string.product_manage_filter_other_filters_title)
                }
                actionText = it.resources.getString(R.string.filter_expand_reset)
            }
        }
    }

    private fun initSearchView() {
        binding?.filterSearchBar?.run {
            clearFocus()
            showIcon = false
            context?.let {
                searchBarPlaceholder = it.resources.getString(R.string.filter_search_bar)
            }
            val searchTextWatcher =
                SearchTextWatcher(
                    searchBarTextField,
                    DELAY_TEXT_CHANGE,
                    this@ProductManageFilterExpandChecklistFragment
                )
            searchBarTextField.addTextChangedListener(searchTextWatcher)
            searchBarTextField.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    processSearch(searchBarTextField.text.toString())
                    clearFocus()
                    return@setOnEditorActionListener true
                }
                false
            }
            visibility = View.VISIBLE
        }
    }

    private fun observeDataLength() {
        productManageFilterExpandChecklistViewModel.dataSize.observe(viewLifecycleOwner, Observer {
            if(it > 0) {
                showButtons()
            } else {
                hideButtons()
            }
        })
    }

    private fun observeChecklistData() {
        productManageFilterExpandChecklistViewModel.checklistData.observe(viewLifecycleOwner, Observer {
            adapter?.updateChecklistData(it)
        })
    }

    private fun showButtons() {
        binding?.filterSubmitButton?.isEnabled = true
        binding?.filterSearchHeader?.actionTextView?.visibility = View.VISIBLE
    }

    private fun hideButtons() {
        binding?.filterSubmitButton?.isEnabled = false
        binding?.filterSearchHeader?.actionTextView?.visibility = View.GONE
    }

    private fun initButtons() {
        binding?.filterSubmitButton?.setOnClickListener {
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
        binding?.filterSearchHeader?.actionTextView?.setOnClickListener {
            adapter?.reset()
            productManageFilterExpandChecklistViewModel.clearAllChecklist()
        }
    }

    private fun search(searchQuery: String): List<ChecklistUiModel> {
        val result = productManageFilterExpandChecklistViewModel.checklistData.value?.filter { data ->
            val words = data.name.split(" ")
            checkWords(words, searchQuery) || data.name.startsWith(searchQuery.trimEnd(), true)
        }
        if(result.isNullOrEmpty()) {
            return emptyList()
        }
        return result
    }

    private fun checkWords(words: List<String>, query: String): Boolean {
        words.forEach {
            if(it.startsWith(query, true)) {
                return true
            }
        }
        return false
    }

    private fun showError() {
        binding?.filterCheckListRecyclerView?.visibility = View.GONE
        binding?.filterSearchErrorImage?.visibility = View.VISIBLE
        binding?.filterSearchErrorText?.visibility = View.VISIBLE
    }

    private fun hideError() {
        binding?.filterCheckListRecyclerView?.visibility = View.VISIBLE
        binding?.filterSearchErrorImage?.visibility = View.GONE
        binding?.filterSearchErrorText?.visibility = View.GONE
    }

    private fun processSearch(searchText: String) {
        if(searchText.isNotEmpty()) {
            val result = search(searchText)
            if(result.isNotEmpty()) {
                hideError()
                adapter?.updateChecklistData(result)
            } else {
                showError()
            }
        } else {
            productManageFilterExpandChecklistViewModel.checklistData.value?.toList()?.let {data ->
                adapter?.updateChecklistData(data)
            }
            hideError()
        }
    }

    private fun removeObservers() {
        removeObservers(productManageFilterExpandChecklistViewModel.checklistData)
        removeObservers(productManageFilterExpandChecklistViewModel.dataSize)
    }

}