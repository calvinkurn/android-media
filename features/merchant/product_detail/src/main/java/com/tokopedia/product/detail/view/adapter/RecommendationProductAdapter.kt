package com.tokopedia.product.detail.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.util.ProductDetailTracking
import com.tokopedia.recommendation_widget_common.presentation.RecommendationCardView
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationModel
import com.tokopedia.topads.sdk.domain.model.Category
import com.tokopedia.topads.sdk.domain.model.Product

class RecommendationProductAdapter(private var product: RecommendationModel) : RecyclerView.Adapter<RecommendationProductAdapter.RecommendationProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationProductAdapter.RecommendationProductViewHolder {
        return RecommendationProductViewHolder(parent.inflateLayout(R.layout.item_product_recommendation))
    }

    override fun getItemCount(): Int {
        return product.recommendationItemList.size
    }

    override fun onBindViewHolder(holder: RecommendationProductAdapter.RecommendationProductViewHolder, position: Int) {
        holder.bind(product.recommendationItemList[position])
    }

    class RecommendationProductViewHolder(itemView: View) : RecommendationCardView.TrackingListener, RecyclerView.ViewHolder(itemView) {
        private val productDetailTracking = ProductDetailTracking()
        private val recommendationCardView: RecommendationCardView? = itemView.findViewById(R.id.productCardView)

        fun bind(product: RecommendationItem) {
            recommendationCardView?.setRecommendationModel(product, this)
            recommendationCardView?.setWishlistButtonVisible(false)
        }

        override fun onImpressionTopAds(item: RecommendationItem) {
            val product = Product()
            product.id = item.productId.toString()
            product.name = item.name
            product.priceFormat = item.price
            product.category = Category(item.departmentId)
            productDetailTracking.eventRecommendationImpression(adapterPosition, product, item.recommendationType, item.isTopAds)
        }

        override fun onImpressionOrganic(item: RecommendationItem) {
            val product = Product()
            product.id = item.productId.toString()
            product.name = item.name
            product.priceFormat = item.price
            product.category = Category(item.departmentId)
            productDetailTracking.eventRecommendationImpression(adapterPosition, product, item.recommendationType, item.isTopAds)
        }

        override fun onClickTopAds(item: RecommendationItem) {
            val product = Product()
            product.id = item.productId.toString()
            product.name = item.name
            product.priceFormat = item.price
            product.category = Category(item.departmentId)
            productDetailTracking.eventRecommendationClick(product, adapterPosition, item.recommendationType, item.isTopAds)
        }

        override fun onClickOrganic(item: RecommendationItem) {
            val product = Product()
            product.id = item.productId.toString()
            product.name = item.name
            product.priceFormat = item.price
            product.category = Category(item.departmentId)
            productDetailTracking.eventRecommendationClick(product, adapterPosition, item.recommendationType, item.isTopAds)
        }


    }
}