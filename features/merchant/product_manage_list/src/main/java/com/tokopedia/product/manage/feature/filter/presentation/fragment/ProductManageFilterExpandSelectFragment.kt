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
import com.tokopedia.product.manage.feature.filter.data.mapper.ProductManageFilterMapper
import com.tokopedia.product.manage.feature.filter.presentation.adapter.SelectAdapter
import com.tokopedia.product.manage.feature.filter.presentation.adapter.factory.SelectAdapterTypeFactory
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.ChecklistViewModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterViewModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.SelectViewModel
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment.Companion.ACTIVITY_EXPAND_FLAG
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment.Companion.CACHE_MANAGER_KEY
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment.Companion.ETALASE_CACHE_MANAGER_KEY
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment.Companion.SORT_CACHE_MANAGER_KEY
import com.tokopedia.product.manage.feature.filter.presentation.widget.ChecklistClickListener
import com.tokopedia.product.manage.feature.filter.presentation.widget.SelectClickListener
import com.tokopedia.unifyprinciples.Typography
import java.util.concurrent.TimeUnit

class ProductManageFilterExpandSelectFragment :
        Fragment(), SelectClickListener, ChecklistClickListener {

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

    private var cacheManager: SaveInstanceCacheManager? = null
    private var toolbar: Toolbar? = null
    private var title: Typography? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: SelectAdapter? = null
    private var flag: String = ""
    private var filterViewModel: FilterViewModel? = null
    private var cacheManagerId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            flag = it.getString(ACTIVITY_EXPAND_FLAG) ?: ""
            cacheManagerId = it.getString(CACHE_MANAGER_KEY) ?: ""
        }
        cacheManager = this.context?.let { SaveInstanceCacheManager(it, savedInstanceState) }
        val manager = if (savedInstanceState == null) this.context?.let { SaveInstanceCacheManager(it, cacheManagerId) } else cacheManager
        filterViewModel = flag.let { manager?.get(it, FilterViewModel::class.java) }
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
        filterViewModel?.let {
            initView(it)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        cacheManager?.onSave(outState)
        flag.let { cacheManager?.put(it, filterViewModel, TimeUnit.DAYS.toMillis(1)) }
    }

    override fun onSelectClick(element: SelectViewModel) {
        if(flag == SORT_CACHE_MANAGER_KEY) {
            cacheManager?.put(SORT_CACHE_MANAGER_KEY, element)
        } else {
            cacheManager?.put(ETALASE_CACHE_MANAGER_KEY, element)
        }
        this.activity?.finish()
    }

    override fun onChecklistClick(element: ChecklistViewModel) {
        //No Op
    }

    private fun initView(filterViewModel: FilterViewModel) {
        configToolbar()
        adapter?.updateSelectData(ProductManageFilterMapper.mapFilterViewModelsToSelectViewModels(filterViewModel))
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
}