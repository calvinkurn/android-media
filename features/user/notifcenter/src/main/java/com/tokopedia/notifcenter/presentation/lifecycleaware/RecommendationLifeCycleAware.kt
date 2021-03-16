package com.tokopedia.notifcenter.presentation.lifecycleaware

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.discovery.common.manager.ProductCardOptionsWishlistCallback
import com.tokopedia.discovery.common.manager.handleProductCardOptionsActivityResult
import com.tokopedia.discovery.common.manager.showProductCardOptions
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.analytics.NotificationTopAdsAnalytic
import com.tokopedia.notifcenter.presentation.adapter.NotificationAdapter
import com.tokopedia.notifcenter.presentation.viewmodel.INotificationViewModel
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster

class RecommendationLifeCycleAware constructor(
        private val topAdsAnalytic: NotificationTopAdsAnalytic,
        private var trackingQueue: TrackingQueue?,
        private val rvAdapter: NotificationAdapter?,
        private var viewModel: INotificationViewModel?,
        private var fragment: Fragment?,
        private var context: Context?
) : LifecycleObserver, RecommendationListener {

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        TopAdsGtmTracker.getInstance().eventInboxProductView(trackingQueue)
        topAdsAnalytic.eventInboxTopAdsProductView(trackingQueue)
        trackingQueue?.sendAll()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun cleanUpReference() {
        fragment = null
        context = null
        trackingQueue = null
        viewModel = null
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        handleProductCardOptionsActivityResult(
                requestCode, resultCode, data,
                object : ProductCardOptionsWishlistCallback {
                    override fun onReceiveWishlistResult(
                            productCardOptionsModel: ProductCardOptionsModel
                    ) {
                        handleWishListAction(productCardOptionsModel)
                    }
                }
        )
    }

    private fun handleWishListAction(productCardOptionsModel: ProductCardOptionsModel) {
        handleWishListActionForLoggedInUser(productCardOptionsModel)
    }

    private fun handleWishListActionForLoggedInUser(
            productCardOptionsModel: ProductCardOptionsModel
    ) {
        if (productCardOptionsModel.wishlistResult.isSuccess) {
            handleWishListActionSuccess(productCardOptionsModel)
        } else {
            handleWishlistActionFailed()
        }
    }

    private fun handleWishListActionSuccess(productCardOptionsModel: ProductCardOptionsModel) {
        val isAddWishlist = productCardOptionsModel.wishlistResult.isAddWishlist
        topAdsAnalytic.eventClickRecommendationWishlist(isAddWishlist)
        rvAdapter?.notifyItemChanged(productCardOptionsModel.productPosition, isAddWishlist)
        if (isAddWishlist) {
            showSuccessAddWishlist()
        } else {
            showSuccessRemoveWishlist()
        }
    }

    private fun showSuccessAddWishlist() {
        val view: View = fragment?.activity?.findViewById(android.R.id.content) ?: return
        val message = getString(com.tokopedia.wishlist.common.R.string.msg_success_add_wishlist)
        Toaster.build(view, message, Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL,
                getString(R.string.recom_go_to_wishlist),
                View.OnClickListener { v: View? ->
                    RouteManager.route(context, ApplinkConst.WISHLIST)
                }
        ).show()
    }

    private fun getString(@StringRes stringRes: Int): String {
        return context?.getString(stringRes) ?: ""
    }

    private fun showSuccessRemoveWishlist() {
        val view: View? = fragment?.activity?.findViewById(android.R.id.content)
        val message = getString(com.tokopedia.wishlist.common.R.string.msg_success_remove_wishlist)
        if (view == null) return
        Toaster.build(view, message, Toaster.LENGTH_LONG).show()
    }

    private fun handleWishlistActionFailed() {
        val rootView = fragment?.view?.rootView
        rootView?.let {
            Toaster.build(it, ErrorHandler.getErrorMessage(rootView.context, null),
                    Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
        }
    }

    override fun onProductClick(
            item: RecommendationItem, layoutType: String?,
            vararg position: Int
    ) {
        if (item.isTopAds) {
            onClickTopAds(item)
        } else {
            onClickOrganic(item)
        }
        val intent = RouteManager.getIntent(context,
                ApplinkConstInternalMarketplace.PRODUCT_DETAIL, item.productId.toString()
        )
        if (position.isNotEmpty()) {
            intent.putExtra(PDP_EXTRA_UPDATED_POSITION, position[0])
        }
        fragment?.startActivityForResult(intent, REQUEST_FROM_PDP)
    }

    override fun onProductImpression(item: RecommendationItem) {
        if (item.isTopAds) {
            onImpressionTopAds(item)
        } else {
            onImpressionOrganic(item)
        }
    }

    private fun onImpressionTopAds(item: RecommendationItem) {
        TopAdsUrlHitter(context).hitImpressionUrl(
                fragment?.activity?.javaClass?.name, item.trackerImageUrl,
                item.productId.toString(), item.name, item.imageUrl, COMPONENT_NAME_TOP_ADS
        )
        topAdsAnalytic.addInboxTopAdsProductViewImpressions(item, item.position, item.isTopAds)
    }

    private fun onImpressionOrganic(item: RecommendationItem) {
        topAdsAnalytic.addInboxTopAdsProductViewImpressions(item, item.position, item.isTopAds)
    }

    override fun onWishlistClick(
            item: RecommendationItem, isAddWishlist: Boolean,
            callback: (Boolean, Throwable?) -> Unit
    ) {
        if (isAddWishlist) {
            viewModel?.addWishlist(item, callback)
        } else {
            viewModel?.removeWishList(item, callback)
        }
    }

    private fun onClickTopAds(item: RecommendationItem) {
        TopAdsUrlHitter(context).hitClickUrl(
                fragment?.activity?.javaClass?.name, item.clickUrl, item.productId.toString(),
                item.name, item.imageUrl, COMPONENT_NAME_TOP_ADS
        )
        topAdsAnalytic.eventInboxTopAdsProductClick(
                item, item.position, item.isTopAds
        )
    }

    private fun onClickOrganic(item: RecommendationItem) {
        topAdsAnalytic.eventInboxTopAdsProductClick(
                item, item.position, item.isTopAds
        )
    }

    override fun onThreeDotsClick(item: RecommendationItem, vararg position: Int) {
        if (position.isEmpty()) return
        fragment?.let {
            showProductCardOptions(it, createProductCardOptionsModel(item, position[0]))
        }
    }

    private fun createProductCardOptionsModel(
            recommendationItem: RecommendationItem,
            productPosition: Int
    ): ProductCardOptionsModel {
        val productCardOptionsModel = ProductCardOptionsModel()
        productCardOptionsModel.hasWishlist = true
        productCardOptionsModel.isWishlisted = recommendationItem.isWishlist
        productCardOptionsModel.productId = recommendationItem.productId.toString()
        productCardOptionsModel.isTopAds = recommendationItem.isTopAds
        productCardOptionsModel.topAdsWishlistUrl = recommendationItem.wishlistUrl
        productCardOptionsModel.productPosition = productPosition
        return productCardOptionsModel
    }

    companion object {

        private const val REQUEST_FROM_PDP = 138
        private const val PDP_EXTRA_UPDATED_POSITION = "wishlistUpdatedPosition"
        private const val COMPONENT_NAME_TOP_ADS = "Inbox Recommendation Top Ads"
    }
}