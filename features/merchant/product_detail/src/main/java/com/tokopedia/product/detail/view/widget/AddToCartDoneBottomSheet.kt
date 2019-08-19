package com.tokopedia.product.detail.view.widget

import android.app.Dialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.discovery.common.constants.SearchConstant.Wishlist.WIHSLIST_STATUS_IS_WISHLIST
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneAddedProductViewModel
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneRecommendationViewModel
import com.tokopedia.product.detail.data.util.ProductDetailTracking
import com.tokopedia.product.detail.di.DaggerProductDetailComponent
import com.tokopedia.product.detail.di.ProductDetailComponent
import com.tokopedia.product.detail.view.adapter.AddToCartDoneAdapter
import com.tokopedia.product.detail.view.adapter.AddToCartDoneTypeFactory
import com.tokopedia.product.detail.view.adapter.RecommendationProductAdapter
import com.tokopedia.product.detail.view.viewholder.AddToCartDoneAddedProductViewHolder
import com.tokopedia.product.detail.view.viewholder.AddToCartDoneRecommendationViewHolder
import com.tokopedia.product.detail.view.viewmodel.AddToCartDoneViewModel
import com.tokopedia.product.detail.view.viewmodel.Loaded
import com.tokopedia.product.detail.view.viewmodel.Loading
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class AddToCartDoneBottomSheet : BottomSheets(), AddToCartDoneAddedProductViewHolder.AddToCartDoneAddedProductListener, HasComponent<ProductDetailComponent>,
        RecommendationListener {

    companion object {
        private const val PDP_EXTRA_UPDATED_POSITION = "wishlistUpdatedPosition"
        private const val REQUEST_FROM_PDP = 394
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var productDetailTracking: ProductDetailTracking
    private lateinit var trackingQueue: TrackingQueue
    lateinit var addToCartDoneViewModel: AddToCartDoneViewModel
    lateinit var atcDoneAdapter: AddToCartDoneAdapter
    var lastAdapterPosition = -1

    lateinit var rv: RecyclerView
    var model: AddToCartDoneAddedProductViewModel? = null
    override fun getLayoutResourceId(): Int {
        return R.layout.add_to_cart_done_bottomsheet
    }

    override fun initView(view: View?) {
        view?.let {
            rv = it.findViewById(R.id.recycler_view_add_to_cart_top_ads)
        }
    }

    override fun setupDialog(dialog: Dialog?, style: Int) {
        super.setupDialog(dialog, style)
        initInjector()
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        addToCartDoneViewModel = viewModelProvider.get(AddToCartDoneViewModel::class.java)
        addToCartDoneViewModel.loadTopAdsProduct.observe(this, Observer {
            when (it) {
                is Loading -> {
                    atcDoneAdapter.addElement(LoadingMoreModel())
                    atcDoneAdapter.notifyDataSetChanged()
                }
                is Loaded -> {
                    (it.data as? Success)?.data?.let { result ->
                        atcDoneAdapter.clearAllElements()
                        renderRecommendationData(result)
                        atcDoneAdapter.addElement(0, model)
                        atcDoneAdapter.notifyDataSetChanged()
                    }
                }
            }
        })
        arguments?.let {
            model = it.getParcelable("model")
        }
        context?.let {
            trackingQueue = TrackingQueue(it)
        }
        val factory = AddToCartDoneTypeFactory(this, this)
        atcDoneAdapter = AddToCartDoneAdapter(factory)
        val linearLayoutManager = LinearLayoutManager(context)
        rv.layoutManager = linearLayoutManager
        rv.adapter = atcDoneAdapter
        atcDoneAdapter.addElement(model)
        model?.productId?.let { addToCartDoneViewModel.getRecommendationProduct(it) }
    }

    private fun renderRecommendationData(result: List<RecommendationWidget>) {
        for (res in result) {
            atcDoneAdapter.addElement(AddToCartDoneRecommendationViewModel(res))
        }
    }

    private fun initInjector() {
        component.inject(this)
    }

    private fun goToCart() {
        activity?.let {
            startActivity(RouteManager.getIntent(it, ApplinkConst.CART))
        }
    }

    private fun goToPDP(item: RecommendationItem, position: Int) {
        RouteManager.getIntent(
                activity,
                ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                item.productId.toString()
        ).run {
            putExtra(PDP_EXTRA_UPDATED_POSITION, position)
            startActivityForResult(this, REQUEST_FROM_PDP)
        }
    }

    override fun state(): BottomSheetsState {
        return BottomSheetsState.FLEXIBLE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_FROM_PDP) {
            data?.let {
                val wishlistStatusFromPdp = data.getBooleanExtra(WIHSLIST_STATUS_IS_WISHLIST,
                        false)
                val position = data.getIntExtra(PDP_EXTRA_UPDATED_POSITION, -1)
                updateWishlist(wishlistStatusFromPdp, position)
            }
            lastAdapterPosition = -1
        }
    }

    private fun updateWishlist(isWishlist: Boolean, position: Int) {
        if (rv.findViewHolderForAdapterPosition(lastAdapterPosition) is AddToCartDoneRecommendationViewHolder) {
            (rv.findViewHolderForAdapterPosition(lastAdapterPosition) as AddToCartDoneRecommendationViewHolder).updateWishlist(
                    position,
                    isWishlist
            )
        }
    }


    override fun onProductClick(item: RecommendationItem, layoutType: String?, vararg position: Int) {
        productDetailTracking.eventAddToCartRecommendationClick(
                item,
                item.position,
                addToCartDoneViewModel.isUserSessionActive(),
                item.header
        )
        if (position.size > 1) {
            lastAdapterPosition = position[0]
            goToPDP(item, position[1])
        } else {
            goToPDP(item, position[0])
        }
    }

    override fun onProductImpression(item: RecommendationItem) {
        productDetailTracking.eventAddToCartRecommendationImpression(
                item.position,
                item,
                addToCartDoneViewModel.isUserSessionActive(),
                item.pageName,
                item.header,
                trackingQueue
        )
    }

    override fun onWishlistClick(item: RecommendationItem, isAddWishlist: Boolean, callback: (Boolean, Throwable?) -> Unit) {
//        showSuccessRemoveWishlist(
//
//        )
        if (isAddWishlist) {
            addToCartDoneViewModel.addWishList(item.productId.toString(), callback)
        } else {
            addToCartDoneViewModel.removeWishList(item.productId.toString(), callback)
        }
        productDetailTracking.eventAddToCartRecommendationWishlist(item, addToCartDoneViewModel.isUserSessionActive(), isAddWishlist)
    }

    private fun showSuccessRemoveWishlist(view: View, message: String) {
        val snackBar = Snackbar.make(
                view,
                message,
                Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        val padding = view.resources.getDimensionPixelSize(R.dimen.dp_16)
        snackBarView.setPadding(padding, 0, padding, 0)
        snackBarView.setBackgroundColor(Color.TRANSPARENT)
        val rootSnackBarView = snackBarView as FrameLayout
        rootSnackBarView.getChildAt(0).setBackgroundResource(R.drawable.bg_toaster_normal)
        snackBar.show()
    }

    override fun onButtonGoToCartClicked() {
        goToCart()
    }

    override fun getComponent(): ProductDetailComponent = DaggerProductDetailComponent.builder()
            .baseAppComponent(
                    (activity?.applicationContext as BaseMainApplication).baseAppComponent
            ).build()

}