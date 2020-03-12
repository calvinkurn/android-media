package com.tokopedia.product.manage.feature.filter.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
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
        const val CATEGORIES_TITLE = "Semua Kategori"
        const val OTHER_FILTER_TITLE = "Filter Lainnya"
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
        filter_search_recycler_view.adapter = adapter
        filter_search_recycler_view.layoutManager = LinearLayoutManager(this.context)
        observeDataLength()
        observeChecklistData()
        initView()
    }

    override fun onChecklistClick(element: ChecklistViewModel) {
        if(flag == OTHER_FILTER_CACHE_MANAGER_KEY) {
            if(!element.isSelected) ProductManageTracking.eventMoreOthersFilter(element.name, getString(R.string.product_manage_stock_reminder_active))
            else ProductManageTracking.eventMoreOthersFilter(element.name, getString(R.string.product_manage_stock_reminder_not_active))
        }
        productManageFilterExpandChecklistViewModel.updateSelectedItem(element)
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
        initTitle()
        configToolbar()
        filter_search_recycler_view.setOnTouchListener { _, _ ->
            filter_category_search?.hideKeyboard()
            btn_submit.visibility = View.VISIBLE
            false
        }
        initButtons()
    }

    private fun configToolbar() {
        checklist_toolbar?.setNavigationIcon(R.drawable.product_manage_arrow_back)
        activity?.let {
            (it as? AppCompatActivity)?.let { appCompatActivity ->
                appCompatActivity.setSupportActionBar(checklist_toolbar)
                appCompatActivity.supportActionBar?.setDisplayShowTitleEnabled(false)
                appCompatActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun initSearchView() {
        filter_category_search.setDelayTextChanged(250)
        filter_category_search.setListener(object : SearchInputView.Listener {
            override fun onSearchSubmitted(text: String?) {
                filter_category_search.hideKeyboard()
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
        filter_category_search.visibility = View.VISIBLE
        filter_category_search.setFocusChangeListener {
            if(btn_submit.visibility == View.VISIBLE) {
                btn_submit.visibility = View.GONE
            } else {
                btn_submit.visibility = View.VISIBLE
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
        btn_submit.isEnabled = true
        reset_checklist.visibility = View.VISIBLE
    }

    private fun hideButtons() {
        btn_submit.isEnabled = false
        reset_checklist.visibility = View.GONE
    }

    private fun initButtons() {
        btn_submit.setOnClickListener {
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
        reset_checklist.setOnClickListener {
            productManageFilterExpandChecklistViewModel.clearAllChecklist()
        }
    }

    private fun initTitle() {
        if(flag == CATEGORIES_CACHE_MANAGER_KEY) {
            page_title.text = CATEGORIES_TITLE
            initSearchView()
        } else {
            page_title.text = OTHER_FILTER_TITLE
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
        filter_search_recycler_view.visibility = View.GONE
        filter_search_error_img.visibility = View.VISIBLE
        filter_search_error_text.visibility = View.VISIBLE
    }

    private fun hideError() {
        filter_search_recycler_view.visibility = View.VISIBLE
        filter_search_error_img?.visibility = View.GONE
        filter_search_error_text.visibility = View.GONE
    }


}