package com.tokopedia.orderhistory.view.fragment

import android.app.Activity
import android.content.Intent
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
import com.tokopedia.purchase_platform.common.constant.ATC_ONLY
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import javax.inject.Inject

class OrderHistoryFragment : BaseListFragment<Visitable<*>, OrderHistoryTypeFactory>(),
        OrderHistoryViewHolder.Listener, WishListActionListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var analytic: OrderHistoryAnalytic
    @Inject
    lateinit var session: UserSessionInterface

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

    override fun getScreenName(): String = name

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
        val atcAndBuyAction = ATC_ONLY
        val needRefresh = true
        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.NORMAL_CHECKOUT).apply {
            putExtra(ApplinkConst.Transaction.EXTRA_SHOP_ID, product.shopId)
            putExtra(ApplinkConst.Transaction.EXTRA_PRODUCT_ID, product.productId)
            putExtra(ApplinkConst.Transaction.EXTRA_QUANTITY, quantity)
            putExtra(ApplinkConst.Transaction.EXTRA_SELECTED_VARIANT_ID, product.productId)
            putExtra(ApplinkConst.Transaction.EXTRA_ACTION, atcAndBuyAction)
            putExtra(ApplinkConst.Transaction.EXTRA_OCS, false)
            putExtra(ApplinkConst.Transaction.EXTRA_NEED_REFRESH, needRefresh)
            putExtra(ApplinkConst.Transaction.EXTRA_REFERENCE, ApplinkConst.TOPCHAT)
            putExtra(ApplinkConst.Transaction.EXTRA_CATEGORY_ID, product.categoryId)
            putExtra(ApplinkConst.Transaction.EXTRA_CATEGORY_NAME, product.categoryId)
            putExtra(ApplinkConst.Transaction.EXTRA_PRODUCT_TITLE, product.name)
            putExtra(ApplinkConst.Transaction.EXTRA_PRODUCT_PRICE, product.priceInt.toFloat())
            putExtra(ApplinkConst.Transaction.EXTRA_CUSTOM_EVENT_ACTION, product.buyEventAction)
            putExtra(ApplinkConst.Transaction.EXTRA_CUSTOM_DIMENSION40, "/chat - buy again")
        }
        startActivityForResult(intent, REQUEST_GO_TO_NORMAL_CHECKOUT)
    }

    override fun onClickAddToWishList(product: Product) {
        viewModel.addToWishList(product.productId, session.userId, this)
    }

    override fun onClickCardProduct(product: Product, position: Int) {
        analytic.eventClickProduct(context, product, position)
        RouteManager.route(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, product.productId)
    }

    override fun trackSeenProduct(product: Product, position: Int) {
        analytic.eventSeenProductAttachment(product, session, position)
    }

    override fun onSuccessAddWishlist(productId: String?) {
        view?.let {
            val successMessage = it.context.getString(R.string.title_orderhistory_success_atc)
            val ctaText = it.context.getString(R.string.cta_orderhistory_success_atw)
            Toaster.make(
                    it,
                    successMessage,
                    Toaster.LENGTH_SHORT,
                    Toaster.TYPE_NORMAL,
                    ctaText,
                    View.OnClickListener { goToWishList() }
            )
        }
    }

    override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
        if (errorMessage == null) return
        view?.let {
            Toaster.make(it, errorMessage, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_GO_TO_NORMAL_CHECKOUT -> onReturnFromNormalCheckout(resultCode, data)
        }
    }

    override fun onSuccessRemoveWishlist(productId: String?) {}
    override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {}

    private fun goToWishList() {
        RouteManager.route(context, ApplinkConst.NEW_WISHLIST)
    }

    private fun onReturnFromNormalCheckout(resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) return
        if (data == null) return
        val message = data.getStringExtra(ApplinkConst.Transaction.RESULT_ATC_SUCCESS_MESSAGE)
                ?: return
        view?.let {
            val ctaText = it.context.getString(R.string.cta_orderhistory_success_atw)
            Toaster.make(
                    it,
                    message,
                    Toaster.LENGTH_SHORT,
                    Toaster.TYPE_NORMAL,
                    ctaText,
                    View.OnClickListener { goToCheckoutPage() }
            )
        }
    }

    private fun goToCheckoutPage() {
        RouteManager.route(context, ApplinkConstInternalMarketplace.CART)
    }

    companion object {
        private const val name = "order_history"
        private const val REQUEST_GO_TO_NORMAL_CHECKOUT = 115
        fun createInstance(extra: Bundle?): OrderHistoryFragment {
            return OrderHistoryFragment().apply {
                arguments = extra
            }
        }
    }
}