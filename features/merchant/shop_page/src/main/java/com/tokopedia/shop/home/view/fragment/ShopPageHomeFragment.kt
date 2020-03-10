package com.tokopedia.shop.home.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.tokopedia.shop.analytic.ShopPageHomeTracking
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.home.di.component.DaggerShopPageHomeComponent
import com.tokopedia.shop.home.di.module.ShopPageHomeModule
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapter
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeMultipleImageColumnViewHolder
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory
import com.tokopedia.shop.home.view.model.ShopHomeProductViewModel
import com.tokopedia.shop.home.view.model.ShopPageHomeLayoutUiModel
import com.tokopedia.shop.home.view.viewmodel.ShopHomeViewModel
import com.tokopedia.shop.product.view.adapter.scrolllistener.DataEndlessScrollListener
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ShopPageHomeFragment : BaseListFragment<Visitable<*>, ShopHomeAdapterTypeFactory>(), ShopHomeMultipleImageColumnViewHolder.ShopHomeMultipleImageColumnListener {

    companion object {
        const val KEY_SHOP_ID = "SHOP_ID"
        const val KEY_IS_OFFICIAL_STORE = "IS_OFFICIAL_STORE"
        const val KEY_IS_GOLD_MERCHANT = "IS_GOLD_MERCHANT"

        const val SPAN_COUNT = 2

        fun createInstance(shopId: String, isOfficialStore: Boolean, isGoldMerchant: Boolean): Fragment {
            val bundle = Bundle()
            bundle.putString(KEY_SHOP_ID, shopId)
            bundle.putBoolean(KEY_IS_OFFICIAL_STORE, isOfficialStore)
            bundle.putBoolean(KEY_IS_GOLD_MERCHANT, isGoldMerchant)
            return ShopPageHomeFragment().apply {
                arguments = bundle
            }
        }
    }

    @Inject
    lateinit var shopPageHomeTracking: ShopPageHomeTracking
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: ShopHomeViewModel
    private var shopId: String = ""
    private var isOfficialStore: Boolean = false
    private var isGoldMerchant: Boolean = false
    private val shopPageHomeLayoutUiModel: ShopPageHomeLayoutUiModel?
        get() = (viewModel.shopHomeLayoutData.value as? Success)?.data
    private val customDimensionShopPage: CustomDimensionShopPage by lazy {
        CustomDimensionShopPage.create(shopId, isOfficialStore, isGoldMerchant)
    }

    private val shopHomeAdapter by lazy {
        adapter as ShopHomeAdapter
    }

    private val shopHomeAdapterTypeFactory by lazy {
        ShopHomeAdapterTypeFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getIntentData()
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopHomeViewModel::class.java)
        customDimensionShopPage.updateCustomDimensionData(shopId, isOfficialStore, isGoldMerchant)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_page_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getRecyclerView(view)?.apply {
            layoutManager = recyclerViewLayoutManager
        }
        observeLiveData()

    }

    override fun onPause() {
        super.onPause()
        shopPageHomeTracking.sendAllTrackingQueue()
    }

    override fun loadInitialData() {
        showLoading()
        viewModel.getShopPageHomeData(shopId)
    }

    private fun getIntentData() {
        arguments?.let {
            shopId = it.getString(KEY_SHOP_ID, "")
            isOfficialStore = it.getBoolean(KEY_IS_OFFICIAL_STORE, false)
            isGoldMerchant = it.getBoolean(KEY_IS_GOLD_MERCHANT, false)
        }
    }

    private fun observeLiveData() {
        viewModel.shopHomeLayoutData.observe(this, Observer {
            hideLoading()
            when (it) {
                is Success -> {
                    onSuccessGetShopHomeLayoutData(it.data)
                }
            }
        })

        viewModel.productListData.observe(this, Observer {
            hideLoading()
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

    private fun onSuccessGetShopHomeLayoutData(data: ShopPageHomeLayoutUiModel) {
        shopHomeAdapter.hideLoading()
        shopHomeAdapter.setHomeLayoutData(data.listWidget)
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
                loadNextData(page)
            }
        }
    }

    fun loadNextData(page: Int){
        if(shopId.isNotEmpty()) {
            viewModel.getNextProductList(shopId, page)
        }
    }
    override fun loadData(page: Int) {}

    override fun onMultipleImageColumnItemImpression(displayWidgetUiModel: ShopHomeDisplayWidgetUiModel?, displayWidgetItem: ShopHomeDisplayWidgetUiModel.DisplayWidgetItem, parentPosition: Int, adapterPosition: Int) {
        shopPageHomeTracking.impressionDisplayWidget(
                false,
                shopId,
                shopPageHomeLayoutUiModel?.layoutId ?: "",
                displayWidgetUiModel?.name ?: "",
                displayWidgetUiModel?.widgetId ?: "",
                parentPosition + 1,
                displayWidgetUiModel?.header?.ratio ?: "",
                displayWidgetItem.appLink,
                displayWidgetItem.imageUrl,
                adapterPosition + 1,
                customDimensionShopPage
        )
    }

    override fun onMultipleImageColumnItemClicked(displayWidgetUiModel: ShopHomeDisplayWidgetUiModel?, displayWidgetItem: ShopHomeDisplayWidgetUiModel.DisplayWidgetItem) {
        Toast.makeText(context, "Multiple column image clicked", Toast.LENGTH_SHORT).show()
    }
}