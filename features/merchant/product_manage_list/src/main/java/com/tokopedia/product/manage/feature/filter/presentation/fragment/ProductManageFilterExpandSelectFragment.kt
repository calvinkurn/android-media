package com.tokopedia.product.manage.feature.filter.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
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
import com.tokopedia.product.manage.feature.filter.presentation.adapter.SelectAdapter
import com.tokopedia.product.manage.feature.filter.presentation.adapter.factory.SelectAdapterTypeFactory
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.ChecklistViewModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterViewModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.SelectViewModel
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment.Companion.ACTIVITY_EXPAND_FLAG
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment.Companion.CACHE_MANAGER_KEY
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment.Companion.ETALASE_CACHE_MANAGER_KEY
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment.Companion.SORT_CACHE_MANAGER_KEY
import com.tokopedia.product.manage.feature.filter.presentation.viewmodel.ProductManageFilterExpandSelectViewModel
import com.tokopedia.product.manage.feature.filter.presentation.widget.ChecklistClickListener
import com.tokopedia.product.manage.feature.filter.presentation.widget.SelectClickListener
import com.tokopedia.unifyprinciples.Typography
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ProductManageFilterExpandSelectFragment :
        Fragment(), SelectClickListener, ChecklistClickListener,
        HasComponent<ProductManageFilterComponent> {

    companion object {
        const val SORT_TITLE = "Urutkan"
        const val ETALASE_TITLE = "Etalase"
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

    private var cacheManager: SaveInstanceCacheManager? = null
    private var toolbar: Toolbar? = null
    private var title: Typography? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: SelectAdapter? = null
    private var flag: String = ""
    private var cacheManagerId: String = ""
    private var selectedElement: SelectViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        arguments?.let {
            flag = it.getString(ACTIVITY_EXPAND_FLAG) ?: ""
            cacheManagerId = it.getString(CACHE_MANAGER_KEY) ?: ""
        }
        val manager = this.context?.let { SaveInstanceCacheManager(it, savedInstanceState) }
        cacheManager = if (savedInstanceState == null) this.context?.let { SaveInstanceCacheManager(it, cacheManagerId) } else manager
        val filterViewModel: FilterViewModel? = flag.let { cacheManager?.get(it, FilterViewModel::class.java) }
        filterViewModel?.let {
            val dataToDisplay = ProductManageFilterMapper.mapFilterViewModelsToSelectViewModels(filterViewModel)
            productManageFilterExpandSelectViewModel.updateData(dataToDisplay)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(com.tokopedia.product.manage.R.layout.fragment_product_manage_filter_select, container, false)
        val adapterTypeFactory = SelectAdapterTypeFactory(this, this)
        adapter = SelectAdapter(adapterTypeFactory)
        recyclerView = view.findViewById(com.tokopedia.product.manage.R.id.select_recycler_view)
        toolbar = view.findViewById(com.tokopedia.product.manage.R.id.select_toolbar)
        title = view.findViewById(com.tokopedia.product.manage.R.id.page_title)
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(this.context)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        cacheManager?.onSave(outState)
        flag.let { cacheManager?.put(it, productManageFilterExpandSelectViewModel.selectData.value, TimeUnit.DAYS.toMillis(1)) }
    }

    override fun onSelectClick(element: SelectViewModel) {
        if(selectedElement != null) {
            productManageFilterExpandSelectViewModel.updateSelectedItem(selectedElement, element)
        } else {
            productManageFilterExpandSelectViewModel.updateSelectedItem(element)
        }
        if(flag == SORT_CACHE_MANAGER_KEY) {
            cacheManager?.put(SORT_CACHE_MANAGER_KEY, ProductManageFilterMapper.mapSelectViewModelsToFilterViewModel(
                    SORT_CACHE_MANAGER_KEY,
                    productManageFilterExpandSelectViewModel.selectData.value?.toList() ?: listOf()
            ))
            this.activity?.setResult(ProductManageFilterFragment.UPDATE_SORT_SUCCESS_RESPONSE)
        } else {
            cacheManager?.put(ETALASE_CACHE_MANAGER_KEY, ProductManageFilterMapper.mapSelectViewModelsToFilterViewModel(
                    ETALASE_CACHE_MANAGER_KEY,
                    productManageFilterExpandSelectViewModel.selectData.value?.toList() ?: listOf()
            ))
            this.activity?.setResult(ProductManageFilterFragment.UPDATE_ETALASE_SUCCESS_RESPONSE)
        }
        this.activity?.finish()
    }

    override fun onChecklistClick(element: ChecklistViewModel) {
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
        observeFilterViewModel()
    }

    private fun configToolbar() {
        toolbar?.setNavigationIcon(com.tokopedia.product.manage.R.drawable.product_manage_arrow_back)
        flag.let {
            if(it == SORT_CACHE_MANAGER_KEY) {
                title?.text = SORT_TITLE
            } else {
                title?.text = ETALASE_TITLE
            }
        }
        activity?.let {
            (it as? AppCompatActivity)?.let { appCompatActivity ->
                appCompatActivity.setSupportActionBar(toolbar)
                appCompatActivity.supportActionBar?.setDisplayShowTitleEnabled(false)
                appCompatActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun findSelectedData(selectViewModels: List<SelectViewModel>): SelectViewModel? {
        val selectedData = selectViewModels.filter {
            it.isSelected
        }
        return if(selectedData.isNotEmpty()) {
            selectedData.first()
        } else {
            null
        }
    }

    private fun observeFilterViewModel() {
        productManageFilterExpandSelectViewModel.selectData.observe(this, Observer {
            adapter?.updateSelectData(it)
            selectedElement = findSelectedData(it)
        })
    }
}