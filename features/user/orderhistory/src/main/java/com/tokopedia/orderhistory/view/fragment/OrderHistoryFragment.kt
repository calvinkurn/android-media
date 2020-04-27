package com.tokopedia.orderhistory.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.orderhistory.R
import com.tokopedia.orderhistory.analytic.OrderHistoryAnalytic
import com.tokopedia.orderhistory.data.ChatHistoryProductResponse
import com.tokopedia.orderhistory.data.Product
import com.tokopedia.orderhistory.di.OrderHistoryComponent
import com.tokopedia.orderhistory.view.adapter.OrderHistoryAdapter
import com.tokopedia.orderhistory.view.adapter.OrderHistoryTypeFactory
import com.tokopedia.orderhistory.view.adapter.OrderHistoryTypeFactoryImpl
import com.tokopedia.orderhistory.view.adapter.viewholder.OrderHistoryViewHolder
import com.tokopedia.orderhistory.view.viewmodel.OrderHistoryViewModel
import com.tokopedia.purchase_platform.common.constant.ATC_AND_BUY
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class OrderHistoryFragment : BaseListFragment<Visitable<*>, OrderHistoryTypeFactory>(),
        OrderHistoryViewHolder.Listener {

    private val screenName = "order_history"

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var analytic: OrderHistoryAnalytic

    private var recycler: VerticalRecyclerView? = null
    private val viewModelFragmentProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelFragmentProvider.get(OrderHistoryViewModel::class.java) }
    private lateinit var adapter: OrderHistoryAdapter
    private var shopId: String? = null

    override fun getRecyclerViewResourceId() = R.id.recycler_view

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_orderhistory_order_history, container, false).also {
            bindView(it)
            setupRecyclerview()
            initializeArguments()
            setupProductListObserver()
        }
    }

    private fun bindView(view: View?) {
        recycler = view?.findViewById(recyclerViewResourceId)
    }

    private fun setupRecyclerview() {
        recycler?.clearItemDecoration()
    }

    private fun initializeArguments() {
        shopId = arguments?.getString(ApplinkConst.OrderHistory.PARAM_SHOP_ID)
    }

    private fun setupProductListObserver() {
        viewModel.product.observe(this, Observer { result ->
            when (result) {
                is Success -> onSuccessGetProductDate(result.data)
                is Fail -> showGetListError(result.throwable)
            }
        })
    }

    private fun onSuccessGetProductDate(data: ChatHistoryProductResponse) {
        renderList(data.products, data.hasNext)
    }

    override fun getScreenName(): String = screenName

    override fun initInjector() {
        getComponent(OrderHistoryComponent::class.java).inject(this)
    }

    override fun getAdapterTypeFactory(): OrderHistoryTypeFactory {
        return OrderHistoryTypeFactoryImpl(this)
    }

    override fun onItemClicked(t: Visitable<*>?) {}

    override fun loadData(page: Int) {
        viewModel.loadProductHistory(shopId)
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, OrderHistoryTypeFactory> {
        return OrderHistoryAdapter(adapterTypeFactory).also { adapter = it }
    }

    override fun onClickBuyAgain(product: Product) {
        val quantity = product.minOrder
        val atcAndBuyAction = ATC_AND_BUY
        val needRefresh = true
//        val shopName = view?.getShopName()
        val intent =  RouteManager.getIntent(context, ApplinkConstInternalMarketplace.NORMAL_CHECKOUT).apply {
            putExtra(ApplinkConst.Transaction.EXTRA_SHOP_ID, product.shopId)
            putExtra(ApplinkConst.Transaction.EXTRA_PRODUCT_ID, product.productId)
            putExtra(ApplinkConst.Transaction.EXTRA_QUANTITY, quantity)
            putExtra(ApplinkConst.Transaction.EXTRA_SELECTED_VARIANT_ID, product.productId)
            putExtra(ApplinkConst.Transaction.EXTRA_ACTION, atcAndBuyAction)
            putExtra(ApplinkConst.Transaction.EXTRA_OCS, false)
            putExtra(ApplinkConst.Transaction.EXTRA_NEED_REFRESH, needRefresh)
            putExtra(ApplinkConst.Transaction.EXTRA_REFERENCE, ApplinkConst.ORDER_HISTORY)
            putExtra(ApplinkConst.Transaction.EXTRA_CATEGORY_ID, product.categoryId)
            putExtra(ApplinkConst.Transaction.EXTRA_CATEGORY_NAME, product.categoryId)
            putExtra(ApplinkConst.Transaction.EXTRA_PRODUCT_TITLE, product.name)
            putExtra(ApplinkConst.Transaction.EXTRA_PRODUCT_PRICE, product.priceInt.toFloat())
//            putExtra(ApplinkConst.Transaction.EXTRA_CUSTOM_EVENT_LABEL, product.getAtcEventLabel())
//            putExtra(ApplinkConst.Transaction.EXTRA_CUSTOM_EVENT_ACTION, product.getBuyEventAction())
        }
        activity?.startActivity(intent)
    }

    companion object {
        fun createInstance(extra: Bundle?): OrderHistoryFragment {
            return OrderHistoryFragment().apply {
                arguments = extra
            }
        }
    }
}