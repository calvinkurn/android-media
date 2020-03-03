package com.tokopedia.product.manage.feature.filter.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.product.manage.ProductManageInstance
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
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import java.util.*
import java.util.concurrent.TimeUnit
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

    private var cacheManager: SaveInstanceCacheManager? = null
    private var toolbar: Toolbar? = null
    private var title: Typography? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: SelectAdapter? = null
    private var flag: String = ""
    private var cacheManagerId: String = ""
    private var searchView: SearchInputView? = null
    private var reset: Typography? = null
    private var submitButton: UnifyButton? = null
    private var errorImage: ImageView? = null
    private var errorMessage: Typography? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        arguments?.let {
            flag = it.getString(ProductManageFilterFragment.ACTIVITY_EXPAND_FLAG) ?: ""
            cacheManagerId = it.getString(ProductManageFilterFragment.CACHE_MANAGER_KEY) ?: ""
        }
        val manager = this.context?.let { SaveInstanceCacheManager(it, savedInstanceState) }
        cacheManager = if (savedInstanceState == null) this.context?.let { SaveInstanceCacheManager(it, cacheManagerId) } else manager
        val filterViewModel: FilterViewModel? = flag.let { cacheManager?.get(it, FilterViewModel::class.java) }
        filterViewModel?.let {
            productManageFilterExpandChecklistViewModel.initData(ProductManageFilterMapper.mapFilterViewModelsToChecklistViewModels(it))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(com.tokopedia.product.manage.R.layout.fragment_product_manage_filter_search, container, false)
        val adapterTypeFactory = SelectAdapterTypeFactory(this, this)
        adapter = SelectAdapter(adapterTypeFactory)
        recyclerView = view.findViewById(com.tokopedia.product.manage.R.id.filter_search_recycler_view)
        toolbar = view.findViewById(com.tokopedia.product.manage.R.id.checklist_toolbar)
        title = view.findViewById(com.tokopedia.product.manage.R.id.page_title)
        reset = view.findViewById(com.tokopedia.product.manage.R.id.reset_checklist)
        submitButton = view.findViewById(com.tokopedia.product.manage.R.id.btn_submit)
        searchView = view.findViewById(com.tokopedia.product.manage.R.id.filter_category_search)
        errorImage = view.findViewById(com.tokopedia.product.manage.R.id.filter_search_error_img)
        errorMessage = view.findViewById(com.tokopedia.product.manage.R.id.filter_search_error_text)
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(this.context)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeDataLength()
        observeChecklistData()
        initView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        cacheManager?.onSave(outState)
        flag.let { cacheManager?.put(it, productManageFilterExpandChecklistViewModel.checklistData.value, TimeUnit.DAYS.toMillis(1)) }
    }

    override fun onChecklistClick(element: ChecklistViewModel) {
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
        recyclerView?.setOnTouchListener { _, _ ->
            searchView?.hideKeyboard()
            submitButton?.visibility = View.VISIBLE
            false
        }
        initButtons()
    }

    private fun configToolbar() {
        toolbar?.setNavigationIcon(com.tokopedia.product.manage.R.drawable.product_manage_arrow_back)
        activity?.let {
            (it as? AppCompatActivity)?.let { appCompatActivity ->
                appCompatActivity.setSupportActionBar(toolbar)
                appCompatActivity.supportActionBar?.setDisplayShowTitleEnabled(false)
                appCompatActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun initSearchView() {
        searchView?.setDelayTextChanged(250)
        searchView?.setListener(object : SearchInputView.Listener {
            override fun onSearchSubmitted(text: String?) {
                searchView?.hideKeyboard()
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
        searchView?.visibility = View.VISIBLE
        searchView?.setFocusChangeListener {
            if(submitButton?.visibility == View.VISIBLE) {
                submitButton?.visibility = View.GONE
            } else {
                submitButton?.visibility = View.VISIBLE
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
        submitButton?.isEnabled = true
        reset?.visibility = View.VISIBLE
    }

    private fun hideButtons() {
        submitButton?.isEnabled = false
        reset?.visibility = View.GONE
    }

    private fun initButtons() {
        submitButton?.setOnClickListener {
            if(flag == CATEGORIES_CACHE_MANAGER_KEY) {
                cacheManager?.put(CATEGORIES_CACHE_MANAGER_KEY,
                        ProductManageFilterMapper.mapChecklistViewModelsTpFilterViewModel(
                                CATEGORIES_CACHE_MANAGER_KEY,
                                productManageFilterExpandChecklistViewModel.checklistData.value ?: listOf()
                        ))
                this.activity?.setResult(ProductManageFilterFragment.UPDATE_CATEGORIES_SUCCESS_RESPONSE)
            } else {
                cacheManager?.put(OTHER_FILTER_CACHE_MANAGER_KEY,
                        ProductManageFilterMapper.mapChecklistViewModelsTpFilterViewModel(
                                OTHER_FILTER_CACHE_MANAGER_KEY,
                                productManageFilterExpandChecklistViewModel.checklistData.value ?: listOf()
                        ))
                this.activity?.setResult(ProductManageFilterFragment.UPDATE_OTHER_FILTER_SUCCESS_RESPONSE)
            }
            this.activity?.finish()
        }
        reset?.setOnClickListener {
            adapter?.clearAllChecklists()
            productManageFilterExpandChecklistViewModel.clearAllChecklist()
        }
    }

    private fun initTitle() {
        if(flag == CATEGORIES_CACHE_MANAGER_KEY) {
            title?.text = CATEGORIES_TITLE
            initSearchView()
        } else {
            title?.text = OTHER_FILTER_TITLE
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
        recyclerView?.visibility = View.GONE
        errorImage?.visibility = View.VISIBLE
        errorMessage?.visibility = View.VISIBLE
    }

    private fun hideError() {
        recyclerView?.visibility = View.VISIBLE
        errorImage?.visibility = View.GONE
        errorMessage?.visibility = View.GONE
    }


}