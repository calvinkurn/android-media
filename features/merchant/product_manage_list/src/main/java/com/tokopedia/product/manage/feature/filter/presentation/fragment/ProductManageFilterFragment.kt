package com.tokopedia.product.manage.feature.filter.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.product.manage.ProductManageInstance
import com.tokopedia.product.manage.feature.filter.data.mapper.ProductManageFilterMapper
import com.tokopedia.product.manage.feature.filter.di.DaggerProductManageFilterComponent
import com.tokopedia.product.manage.feature.filter.di.ProductManageFilterComponent
import com.tokopedia.product.manage.feature.filter.di.ProductManageFilterModule
import com.tokopedia.product.manage.feature.filter.presentation.adapter.FilterAdapter
import com.tokopedia.product.manage.feature.filter.presentation.adapter.FilterAdapterTypeFactory
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterViewModel
import com.tokopedia.product.manage.feature.filter.presentation.viewmodel.ProductManageFilterViewModel
import com.tokopedia.product.manage.feature.filter.presentation.widget.SeeAllListener
import com.tokopedia.product.manage.oldlist.R
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ProductManageFilterFragment : BaseDaggerFragment(),
        HasComponent<ProductManageFilterComponent>,
        SeeAllListener {

    companion object {
        fun createInstance() {
            ProductManageFilterFragment()
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
        savedInstanceManager = this.context?.let { SaveInstanceCacheManager(it, savedInstanceState) }
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

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component?.inject(this)
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

    override fun onSeeAll() {
        //TODO
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