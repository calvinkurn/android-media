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
import com.tokopedia.product.manage.ProductManageInstance
import com.tokopedia.product.manage.feature.filter.data.mapper.ProductManageFilterMapper
import com.tokopedia.product.manage.feature.filter.di.DaggerProductManageFilterComponent
import com.tokopedia.product.manage.feature.filter.di.ProductManageFilterComponent
import com.tokopedia.product.manage.feature.filter.di.ProductManageFilterModule
import com.tokopedia.product.manage.feature.filter.presentation.adapter.FilterAdapter
import com.tokopedia.product.manage.feature.filter.presentation.adapter.FilterAdapterTypeFactory
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewholder.ItemClickListener
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewholder.OnExpandListener
import com.tokopedia.product.manage.feature.filter.presentation.viewmodel.ProductManageFilterViewModel
import com.tokopedia.product.manage.oldlist.R
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ProductManageFilterFragment : BaseDaggerFragment(),
        HasComponent<ProductManageFilterComponent>,
        ItemClickListener, OnExpandListener {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_filter, container, false)
        layoutManager = LinearLayoutManager(this.context)
        recyclerView = view.findViewById(R.id.filter_recycler_view)
        val adapterTypeFactory = FilterAdapterTypeFactory(this, this)
        filterAdapter = FilterAdapter(adapterTypeFactory)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.adapter = filterAdapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        productManageFilterViewModel.getProductListData(userSession.shopId)
        observeProductListMeta()
        observeShopEtalase()
        observeCategories()
    }

    override fun onDestroy() {
        productManageFilterViewModel.productListMetaData.removeObservers(this)
        productManageFilterViewModel.categories.removeObservers(this)
        productManageFilterViewModel.shopEtalase.removeObservers(this)
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

    override fun onItemClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onExpand() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun observeProductListMeta() {
        productManageFilterViewModel.productListMetaData.observe(this, Observer {
            when(it) {
                is Success -> {
                    productManageFilterViewModel.getShopEtalase(userSession.shopId)
                    filterAdapter?.updateSortData(ProductManageFilterMapper.mapMetaResponseToSortOptions(it.data))
                }
                is Fail -> {
                    showErrorMessage()
                }
            }
        })
    }

    private fun observeShopEtalase() {
        productManageFilterViewModel.shopEtalase.observe(this, Observer {
            when(it) {
                is Success -> {
                    productManageFilterViewModel.getCategories()
                    filterAdapter?.updateEtalaseData(ProductManageFilterMapper.mapEtalaseResponseToEtalaseOptions(it.data))
                }
                is Fail -> {
                    showErrorMessage()
                }
            }
        })
    }

    private fun observeCategories() {
        productManageFilterViewModel.categories.observe(this, Observer {
            when(it) {
                is Success -> {
                    filterAdapter?.updateCategoryData(ProductManageFilterMapper.mapCategoryResponseToCategoryOptions(it.data))

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