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
import com.tokopedia.topads.sdk.utils.ImpresionTask

/**
 * Author errysuprayogi on 13,March,2019
 */
class RecomendationViewHolder(itemView: View, private val listener: InboxAdapterListener) : AbstractViewHolder<Recomendation>(itemView), RecommendationCardView.TrackingListener {
    override fun onImpression(item: RecommendationItem) {
        if(item.isTopAds){
            ImpresionTask().execute(item.trackerImageUrl)
            val product = Product()
            product.id = item.productId.toString()
            product.name = item.name
            product.priceFormat = item.price
            product.category = Category(item.departmentId)
            TopAdsGtmTracker.getInstance().addInboxProductViewImpressions(product, adapterPosition)
        } else{
            InboxGtmTracker.getInstance().addInboxProductViewImpressions(item, adapterPosition)
        }
    }

    override fun onClick(item: RecommendationItem) {
        if (item.isTopAds) {
            ImpresionTask().execute(item.clickUrl)
            val product = Product()
            product.id = item.productId.toString()
            product.name = item.name
            product.priceFormat = item.price
            product.category = Category(item.departmentId)
            TopAdsGtmTracker.getInstance().eventInboxProductClick(context, product, adapterPosition)
        } else {
            InboxGtmTracker.getInstance().eventInboxProductClick(context, item, adapterPosition)
        }
    }

    private val recommendationCardView: RecommendationCardView
    private val context: Context

    init {
        this.context = itemView.context
        recommendationCardView = itemView.findViewById(R.id.productCardView)
    }

    override fun bind(element: Recomendation) {
        recommendationCardView.setRecommendationModel(element.recommendationItem, this)
        val item = element.recommendationItem
        if (item.isTopAds) {
            recommendationCardView.setRecommendationModel(item, this)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_recomendation
    }
}
