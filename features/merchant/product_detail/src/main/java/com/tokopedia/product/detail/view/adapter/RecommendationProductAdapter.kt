package com.tokopedia.product.detail.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.util.ProductDetailTracking
import com.tokopedia.recommendation_widget_common.presentation.RecommendationCardView
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.trackingoptimizer.TrackingQueue

class RecommendationProductAdapter(private var product: RecommendationWidget,
                                   private val userActiveListener: UserActiveListener,
                                   private var pageName: String,
                                   private var trackingQueue: TrackingQueue) : RecyclerView.Adapter<RecommendationProductAdapter.RecommendationProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationProductViewHolder {
        return RecommendationProductViewHolder(parent.inflateLayout(R.layout.item_product_recommendation))
    }

    override fun getItemCount(): Int {
        return product.recommendationItemList.size
    }

    override fun onBindViewHolder(holder: RecommendationProductViewHolder, position: Int) {
        holder.bind(product.recommendationItemList[position])
    }

    inner class RecommendationProductViewHolder(itemView: View) : RecommendationCardView.TrackingListener, RecyclerView.ViewHolder(itemView) {
        private val productDetailTracking = ProductDetailTracking()
        private val recommendationCardView: RecommendationCardView? = itemView.findViewById(R.id.productCardView)

        fun bind(product: RecommendationItem) {
            recommendationCardView?.setRecommendationModel(product, this)
            recommendationCardView?.setWishlistButtonVisible(false)
        }

        override fun onImpressionTopAds(item: RecommendationItem) {
            productDetailTracking.eventRecommendationImpression(adapterPosition, item, userActiveListener.isUserSessionActive, pageName, product.title, trackingQueue)
        }

        override fun onImpressionOrganic(item: RecommendationItem) {
            productDetailTracking.eventRecommendationImpression(adapterPosition, item, userActiveListener.isUserSessionActive, pageName, product.title, trackingQueue)
        }

        override fun onClickTopAds(item: RecommendationItem) {
            productDetailTracking.eventRecommendationClick(item, adapterPosition, userActiveListener.isUserSessionActive,pageName,product.title)
        }

        override fun onClickOrganic(item: RecommendationItem) {
            productDetailTracking.eventRecommendationClick(item, adapterPosition, userActiveListener.isUserSessionActive,pageName,product.title)
        }

    }

    interface UserActiveListener{
        val isUserSessionActive: Boolean
    }
}