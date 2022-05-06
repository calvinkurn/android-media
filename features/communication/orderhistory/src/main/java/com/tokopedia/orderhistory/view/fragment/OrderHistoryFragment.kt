package com.tokopedia.orderhistory.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.atc_common.AtcFromExternalSource
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.orderhistory.R
import com.tokopedia.orderhistory.analytic.OrderHistoryAnalytic
import com.tokopedia.orderhistory.data.ChatHistoryProductResponse
import com.tokopedia.orderhistory.data.Product
import com.tokopedia.orderhistory.databinding.FragmentOrderhistoryOrderHistoryBinding
import com.tokopedia.orderhistory.di.OrderHistoryComponent
import com.tokopedia.orderhistory.view.adapter.OrderHistoryAdapter
import com.tokopedia.orderhistory.view.adapter.OrderHistoryTypeFactory
import com.tokopedia.orderhistory.view.adapter.OrderHistoryTypeFactoryImpl
import com.tokopedia.orderhistory.view.adapter.viewholder.OrderHistoryViewHolder
import com.tokopedia.orderhistory.view.viewmodel.OrderHistoryViewModel
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR
import com.tokopedia.unifycomponents.Toaster.TYPE_NORMAL
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.TOASTER_RED
import javax.inject.Inject

class OrderHistoryFragment : BaseListFragment<Visitable<*>, OrderHistoryTypeFactory>(),
        OrderHistoryViewHolder.Listener, WishListActionListener, WishlistV2ActionListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var analytic: OrderHistoryAnalytic

    @Inject
    lateinit var session: UserSessionInterface

    private val binding: FragmentOrderhistoryOrderHistoryBinding? by viewBinding()

    var remoteConfig: RemoteConfig? = null

    private val viewModelFragmentProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }
    private val viewModel by lazy {
        viewModelFragmentProvider.get(OrderHistoryViewModel::class.java)
    }
    private lateinit var adapter: OrderHistoryAdapter
    private var shopId: String? = null

    override fun getRecyclerViewResourceId() = R.id.recycler_view

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_orderhistory_order_history, container, false).also {
            initializeArguments()
            initRemoteConfig()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerview()
        setupProductListObserver()
    }

    private fun initRemoteConfig() {
        remoteConfig = FirebaseRemoteConfigImpl(context)
    }

    private fun setupRecyclerview() {
        binding?.recyclerView?.clearItemDecoration()
    }

    private fun initializeArguments() {
        shopId = arguments?.getString(ApplinkConst.OrderHistory.PARAM_SHOP_ID)
    }

    private fun setupProductListObserver() {
        viewModel.product.observe(viewLifecycleOwner, Observer { result ->
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
        val buyParam = getAtcBuyParam(product)
        viewModel.addProductToCart(buyParam, {
            analytic.trackSuccessDoBuy(product, it)
            RouteManager.route(context, ApplinkConst.CART)
        }, { msg ->
            showErrorMessage(msg)
        })
    }

    private fun getAtcBuyParam(product: Product): RequestParams {
        val addToCartRequestParams = AddToCartRequestParams(
                productId = product.productId.toLong(),
                shopId = product.shopId.toInt(),
                quantity = product.minOrder,
                atcFromExternalSource = AtcFromExternalSource.ATC_FROM_TOPCHAT
        )
        return RequestParams.create().apply {
            putObject(
                    AddToCartUseCase.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST,
                    addToCartRequestParams
            )
        }
    }

    private fun showErrorMessage(msg: String) {
        view?.let {
            Toaster.build(it, msg, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
        }
    }

    override fun onClickAddToWishList(product: Product) {
        context?.let { viewModel.addToWishList(product.productId, session.userId, this, this, it) }
    }

    override fun onClickCardProduct(product: Product, position: Int) {
        analytic.eventClickProduct(context, product, position)
        RouteManager.route(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, product.productId)
    }

    override fun trackSeenProduct(product: Product, position: Int) {
        analytic.eventSeenProductAttachment(product, session, position)
    }

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

    override fun onErrorAddWishList(throwable: Throwable, productId: String) {
        view?.let {
            Toaster.build(it, com.tokopedia.network.utils.ErrorHandler.getErrorMessage(context, throwable), Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
        }
    }

    override fun onSuccessAddWishlist(
        result: AddToWishlistV2Response.Data.WishlistAddV2,
        productId: String
    ) {
        var msg = ""
        if (result.message.isEmpty()) {
            if (result.success) getString(com.tokopedia.wishlist_common.R.string.on_success_add_to_wishlist_msg)
            else getString(com.tokopedia.wishlist_common.R.string.on_failed_add_to_wishlist_msg)
        } else {
            msg = result.message
        }

        var typeToaster = TYPE_NORMAL
        if (result.toasterColor == TOASTER_RED || !result.success) typeToaster = TYPE_ERROR

        var ctaText = getString(com.tokopedia.wishlist_common.R.string.cta_success_add_to_wishlist)
        if (result.button.text.isNotEmpty()) ctaText = result.button.text

        view?.let {
            Toaster.build(it, msg, Toaster.LENGTH_SHORT, typeToaster, ctaText) { goToWishList() }.show()
        }
    }

    private fun goToWishList() {
        RouteManager.route(context, ApplinkConst.NEW_WISHLIST)
    }

    override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
        view?.let {
            errorMessage?.let { it1 -> Toaster.build(it, it1, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show() }
        }
    }

    override fun onSuccessAddWishlist(productId: String) {
        view?.let {
            val successMessage = it.context.getString(com.tokopedia.wishlist_common.R.string.on_success_add_to_wishlist_msg)
            val ctaText = it.context.getString(com.tokopedia.wishlist_common.R.string.cta_success_add_to_wishlist)
            Toaster.build(
                    it,
                    successMessage,
                    Toaster.LENGTH_SHORT,
                    Toaster.TYPE_NORMAL,
                    ctaText
            ) { goToWishList() }.show()
        }
    }

    override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {}
    override fun onErrorRemoveWishlist(throwable: Throwable, productId: String) { }
    override fun onSuccessRemoveWishlist(productId: String) {}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_GO_TO_NORMAL_CHECKOUT -> onReturnFromNormalCheckout(resultCode, data)
        }
    }

    override fun onDestroyView() {
        Toaster.onCTAClick = View.OnClickListener { }
        super.onDestroyView()
    }
}