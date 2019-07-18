package com.tokopedia.navigation.presentation.adapter.viewholder

import android.content.Context
import android.support.annotation.LayoutRes
import android.view.View

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.navigation.R
import com.tokopedia.navigation.analytics.InboxGtmTracker
import com.tokopedia.navigation.domain.model.Recomendation
import com.tokopedia.navigation.presentation.view.InboxAdapterListener
import com.tokopedia.recommendation_widget_common.presentation.RecommendationCardView
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker
import com.tokopedia.topads.sdk.domain.model.Category
import com.tokopedia.topads.sdk.domain.model.Product

/**
 * Author errysuprayogi on 13,March,2019
 */
class RecomendationViewHolder(itemView: View, private val listener: InboxAdapterListener) : AbstractViewHolder<Recomendation>(itemView), RecommendationCardView.TrackingListener {
    private val recommendationCardView: RecommendationCardView? = itemView.findViewById(R.id.productCardView)
    private val context: Context? = itemView.context

    override fun onImpressionTopAds(item: RecommendationItem) {
        val product = Product()
        val pos = (adapterPosition - listener.getStartProductPosition())
        product.id = item.productId.toString()
        product.name = item.name
        product.priceFormat = item.price
        product.category = Category(item.departmentId)
        TopAdsGtmTracker.getInstance().addInboxProductViewImpressions(product, pos, item.recommendationType)
    }

    override fun onImpressionOrganic(item: RecommendationItem) {
        val pos = (adapterPosition - listener.getStartProductPosition())
        InboxGtmTracker.getInstance().addInboxProductViewImpressions(item, pos)
    }

    override fun onClickTopAds(item: RecommendationItem) {
        val product = Product()
        val pos = (adapterPosition - listener.getStartProductPosition())
        product.id = item.productId.toString()
        product.name = item.name
        product.priceFormat = item.price
        product.category = Category(item.departmentId)
        context?.run {
            TopAdsGtmTracker.getInstance().eventInboxProductClick(context, product, pos, item.recommendationType)
        }
    }

    override fun onClickOrganic(item: RecommendationItem) {
        val pos = (adapterPosition - listener.getStartProductPosition())
        context?.run {
            InboxGtmTracker.getInstance().eventInboxProductClick(context, item, pos)
        }
    }

    override fun onWishlistClick(item: RecommendationItem, isAddWishlist: Boolean, callback: (Boolean, Throwable?) -> Unit) {

    }

    override fun bind(element: Recomendation) {
        recommendationCardView?.setRecommendationModel(element.recommendationItem, this)
        val item = element.recommendationItem
        if (item.isTopAds) {
            recommendationCardView?.setRecommendationModel(item, this)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_recomendation
    }
}
