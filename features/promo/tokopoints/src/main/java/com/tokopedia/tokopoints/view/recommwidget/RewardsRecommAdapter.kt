package com.tokopedia.tokopoints.view.recommwidget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.tokopointhome.RecommendationWrapper
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil

class RewardsRecommAdapter(val list: ArrayList<RecommendationWrapper>) :
    RecyclerView.Adapter<RewardsRecommAdapter.ProductCardViewHolder>() {

    inner class ProductCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val productView = itemView.findViewById<ProductCardGridView>(R.id.productCardView)

        fun bind(model: RecommendationWrapper) {
            productView.setProductModel(model.recomData)
            val impressItem = model.recomendationItem
            productView.setImageProductViewHintListener(
                model.recomendationItem,
                object : ViewHintListener {
                    override fun onViewHint() {
                        recomViewAnalytics(impressItem, position)
                    }
                }
            )

            productView.setOnClickListener {
                recomClickAnalytics(impressItem, position)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductCardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tp_layout_recomm_item, parent, false)
        return ProductCardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ProductCardViewHolder, position: Int) {
        holder.bind(list[position])
    }

    private fun recomViewAnalytics(impressItem: RecommendationItem, position: Int) {
        AnalyticsTrackerUtil.impressionProductRecomItem(
            impressItem.productId.toString(),
            impressItem.recommendationType,
            position,
            "none / other",
            impressItem.categoryBreadcrumbs,
            impressItem.name,
            "none / other",
            impressItem.price,
            impressItem.isTopAds
        )
    }

    private fun recomClickAnalytics(impressItem: RecommendationItem, position: Int) {
        AnalyticsTrackerUtil.clickProductRecomItem(
            impressItem.productId.toString(),
            impressItem.recommendationType,
            position,
            "none / other",
            impressItem.categoryBreadcrumbs,
            impressItem.name,
            "none / other",
            impressItem.price,
            impressItem.isTopAds
        )
    }
}
