package com.tokopedia.tkpd.home.adapter.viewholder

import android.content.Context
import android.content.Intent
import android.support.annotation.LayoutRes
import android.view.View

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.tkpd.R
import com.tokopedia.tkpd.home.adapter.viewmodel.WishlistTopAdsViewModel
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker
import com.tokopedia.topads.sdk.domain.model.Data
import com.tokopedia.topads.sdk.domain.model.Product
import com.tokopedia.topads.sdk.domain.model.Shop
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener
import com.tokopedia.topads.sdk.widget.TopAdsCarouselView

/**
 * Author errysuprayogi on 03,July,2019
 */
class WishlistTopAdsListViewHolder(itemView: View) : AbstractViewHolder<WishlistTopAdsViewModel>(itemView), TopAdsItemClickListener {

    private var topAdsCarouselView: TopAdsCarouselView
    private val context: Context
    private var keyword: String = ""

    init {
        this.context = itemView.context
        this.topAdsCarouselView = itemView.findViewById(R.id.topads);
    }

    override fun bind(element: WishlistTopAdsViewModel) {
        this.keyword = element.query
        topAdsCarouselView!!.setAdsItemClickListener(this)
        topAdsCarouselView.setAdsItemImpressionListener(object : TopAdsItemImpressionListener() {
            override fun onImpressionProductAdsItem(position: Int, product: Product) {
                TopAdsGtmTracker.eventWishlistProductView(context, product, keyword, position)
            }
        })
        topAdsCarouselView.setData(element.topAdsModel)
    }

    override fun onProductItemClicked(position: Int, product: Product) {
        val intent = getProductIntent(product.id)
        context!!.startActivity(intent)
        TopAdsGtmTracker.eventWishlistProductClick(context, product, keyword, position)
    }

    private fun getProductIntent(productId: String): Intent? {
        return if (context != null) {
            RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId)
        } else {
            null
        }
    }

    override fun onShopItemClicked(position: Int, shop: Shop) {

    }

    override fun onAddFavorite(position: Int, data: Data) {

    }

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.layout_wishlist_topads
    }
}
