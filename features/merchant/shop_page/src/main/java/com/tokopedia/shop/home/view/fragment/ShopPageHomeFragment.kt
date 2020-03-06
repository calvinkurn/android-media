package com.tokopedia.shop.home.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.shop.R
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.home.di.component.DaggerShopPageHomeComponent
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory
import com.tokopedia.shop.home.view.viewmodel.ShopHomeViewModel
import com.tokopedia.shop.home.di.module.ShopPageHomeModule
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapter
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductViewModel
import com.tokopedia.shop.product.view.adapter.scrolllistener.DataEndlessScrollListener
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ShopPageHomeFragment : BaseListFragment<Visitable<*>, ShopHomeAdapterTypeFactory>() {

    companion object {
        const val KEY_SHOP_ID = "SHOP_ID"
        const val SPAN_COUNT = 2

        fun createInstance(shopId: String): Fragment {
            val bundle = Bundle()
            bundle.putString(KEY_SHOP_ID, shopId)
            return ShopPageHomeFragment().apply {
                arguments = bundle
            }
        }
    }

    var shopId: String = ""

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: ShopHomeViewModel

    private val shopHomeAdapter by lazy {
        adapter as ShopHomeAdapter
    }

    private val shopHomeAdapterTypeFactory by lazy {
        ShopHomeAdapterTypeFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getIntentData()
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopHomeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_page_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getRecyclerView(view)?.apply {
            layoutManager = recyclerViewLayoutManager
        }
        viewModel.getShopPageHomeData(shopId)
        observeLiveData()
    }

    private fun getIntentData() {
        arguments?.let {
            shopId = it.getString(KEY_SHOP_ID, "")
        }
    }

    private fun observeLiveData() {
        viewModel.shopHomeLayoutData.observe(this, Observer {
            when (it) {
                is Success -> {
                    onSuccessGetShopHomeLayoutData(it.data)
                }
            }
        })

        viewModel.productListData.observe(this, Observer {
            when (it) {
                is Success -> {
                    onSuccessGetProductListData(it.data.first, it.data.second)
                }
            }
        })
    }

    private fun onSuccessGetProductListData(hasNextPage: Boolean, productList: List<ShopHomeProductViewModel>) {
        shopHomeAdapter.hideLoading()
        if(shopHomeAdapter.endlessDataSize == 0) {
            shopHomeAdapter.setEtalaseTitleData()
        }
        shopHomeAdapter.setProductListData(productList)
    }

    private fun onSuccessGetShopHomeLayoutData(data: List<BaseShopHomeWidgetUiModel>) {
        shopHomeAdapter.setHomeLayoutData(data)
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, ShopHomeAdapterTypeFactory> {
        return ShopHomeAdapter(shopHomeAdapterTypeFactory)
    }

    override fun getAdapterTypeFactory() = shopHomeAdapterTypeFactory

    private val gridLayoutManager: StaggeredGridLayoutManager by lazy {
        StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)
    }

    override fun getRecyclerViewLayoutManager(): RecyclerView.LayoutManager {
        return gridLayoutManager
    }

    override fun onItemClicked(t: Visitable<*>?) {
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerShopPageHomeComponent
                .builder()
                .shopPageHomeModule(ShopPageHomeModule())
                .shopComponent(getComponent(ShopComponent::class.java))
                .build()
                .inject(this)
    }

    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : DataEndlessScrollListener(recyclerViewLayoutManager, shopHomeAdapter) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                shopHomeAdapter.showLoading()
                loadData(page)
            }
        }
    }

    override fun callInitialLoadAutomatically() = false

    override fun loadData(page: Int) {
        if(shopId.isNotEmpty()) {
            viewModel.getNextProductList(shopId, page)
        }
    }
}