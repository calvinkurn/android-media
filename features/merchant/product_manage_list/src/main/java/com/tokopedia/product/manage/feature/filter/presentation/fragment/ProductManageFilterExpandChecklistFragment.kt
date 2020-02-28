package com.tokopedia.product.manage.feature.filter.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.product.manage.feature.filter.data.mapper.ProductManageFilterMapper
import com.tokopedia.product.manage.feature.filter.presentation.adapter.SelectAdapter
import com.tokopedia.product.manage.feature.filter.presentation.adapter.factory.SelectAdapterTypeFactory
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.ChecklistViewModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterViewModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.SelectViewModel
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment.Companion.CATEGORIES_CACHE_MANAGER_KEY
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment.Companion.OTHER_FILTER_CACHE_MANAGER_KEY
import com.tokopedia.product.manage.feature.filter.presentation.widget.ChecklistClickListener
import com.tokopedia.product.manage.feature.filter.presentation.widget.SelectClickListener
import com.tokopedia.product.manage.oldlist.R
import com.tokopedia.unifyprinciples.Typography
import java.util.concurrent.TimeUnit

class ProductManageFilterExpandChecklistFragment :
        Fragment(), ChecklistClickListener, SelectClickListener {

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

    private var cacheManager: SaveInstanceCacheManager? = null
    private var toolbar: Toolbar? = null
    private var title: Typography? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: SelectAdapter? = null
    private var flag: String = ""
    private var filterViewModel: FilterViewModel? = null
    private var cacheManagerId: String = ""
    private var searchView: SearchInputView? = null
    private var reset: Typography? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            flag = it.getString(ProductManageFilterFragment.ACTIVITY_EXPAND_FLAG) ?: ""
            cacheManagerId = it.getString(ProductManageFilterFragment.CACHE_MANAGER_KEY) ?: ""
        }
        cacheManager = this.context?.let { SaveInstanceCacheManager(it, savedInstanceState) }
        val manager = if (savedInstanceState == null) this.context?.let { SaveInstanceCacheManager(it, cacheManagerId) } else cacheManager
        filterViewModel = flag.let { manager?.get(it, FilterViewModel::class.java) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_product_manage_filter_search, container, false)
        val adapterTypeFactory = SelectAdapterTypeFactory(this, this)
        adapter = SelectAdapter(adapterTypeFactory)
        recyclerView = view.findViewById(R.id.filter_search_recycler_view)
        toolbar = view.findViewById(R.id.checklist_toolbar)
        title = view.findViewById(R.id.page_title)
        reset = view.findViewById(R.id.reset_checklist)
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(this.context)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        filterViewModel?.let {
            initView(it)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        cacheManager?.onSave(outState)
        flag.let { cacheManager?.put(it, filterViewModel, TimeUnit.DAYS.toMillis(1)) }
    }

    override fun onChecklistClick(element: ChecklistViewModel) {
        if(flag == CATEGORIES_CACHE_MANAGER_KEY) {
            cacheManager?.put(CATEGORIES_CACHE_MANAGER_KEY, element)
        } else {
            cacheManager?.put(OTHER_FILTER_CACHE_MANAGER_KEY, element)
        }
    }

    override fun onSelectClick(element: SelectViewModel) {
        //No Op
    }

    private fun initView(filterViewModel: FilterViewModel) {
        if(flag == CATEGORIES_CACHE_MANAGER_KEY) {
            title?.text = CATEGORIES_TITLE
            initSearchView()
        } else {
            title?.text = OTHER_FILTER_TITLE
        }
        configToolbar()
        adapter?.updateChecklistData(ProductManageFilterMapper.mapFilterViewModelsToChecklistViewModels(filterViewModel))
    }

    private fun configToolbar() {
        toolbar?.setNavigationIcon(R.drawable.product_manage_arrow_back)
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
                text?.let {
                    if (it.isNotEmpty()) {

                    } else {

                    }
                }
            }

        })
    }



}