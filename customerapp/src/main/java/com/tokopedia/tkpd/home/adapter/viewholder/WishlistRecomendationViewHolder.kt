package com.tokopedia.tkpd.home.adapter.viewholder

import android.content.Context
import android.support.annotation.LayoutRes
import android.view.View

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.navigation.analytics.InboxGtmTracker
import com.tokopedia.recommendation_widget_common.presentation.RecommendationCardView
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.tkpd.R
import com.tokopedia.tkpd.home.adapter.viewmodel.WishlistRecomendationViewModel
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker
import com.tokopedia.topads.sdk.domain.model.Category
import com.tokopedia.topads.sdk.domain.model.Product

/**
 * Author errysuprayogi on 03,July,2019
 */
class WishlistRecomendationViewHolder(itemView: View) : AbstractViewHolder<WishlistRecomendationViewModel>(itemView), RecommendationCardView.TrackingListener {

    private val recommendationCardView: RecommendationCardView? = itemView.findViewById(com.tokopedia.navigation.R.id.productCardView)
    private val context: Context? = itemView.context

    override fun bind(element: WishlistRecomendationViewModel) {
        recommendationCardView?.setRecommendationModel(element.recommendationItem, this)
        val item = element.recommendationItem
        if (item.isTopAds) {
            recommendationCardView?.setRecommendationModel(item, this)
        }
    }

    override fun onImpressionTopAds(item: RecommendationItem) {
        val product = Product()
        product.id = item.productId.toString()
        product.name = item.name
        product.priceFormat = item.price
        product.category = Category(item.departmentId)
        TopAdsGtmTracker.getInstance().addInboxProductViewImpressions(product, adapterPosition, item.recommendationType)
    }

    override fun onImpressionOrganic(item: RecommendationItem) {
        InboxGtmTracker.getInstance().addInboxProductViewImpressions(item, adapterPosition)
    }

    override fun onClickTopAds(item: RecommendationItem) {
        val product = Product()
        product.id = item.productId.toString()
        product.name = item.name
        product.priceFormat = item.price
        product.category = Category(item.departmentId)
        context?.run {
            TopAdsGtmTracker.getInstance().eventInboxProductClick(context, product, adapterPosition, item.recommendationType)
        }
    }

    override fun onClickOrganic(item: RecommendationItem) {
        context?.run {
            InboxGtmTracker.getInstance().eventInboxProductClick(context, item, adapterPosition)
        }
    }

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.wishlist_item_recomnedation
    }
}
