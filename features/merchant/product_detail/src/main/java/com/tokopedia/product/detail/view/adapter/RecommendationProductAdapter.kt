package com.tokopedia.product.detail.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.product.detail.R
import com.tokopedia.recommendation_widget_common.presentation.RecommendationCardView
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationModel

class RecommendationProductAdapter(private var product:RecommendationModel): RecyclerView.Adapter<RecommendationProductAdapter.RecommendationProductViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationProductAdapter.RecommendationProductViewHolder {
        return RecommendationProductViewHolder(parent.inflateLayout(R.layout.item_product_recommendation))
    }

    override fun getItemCount(): Int {
        return product.recommendationItemList.size
    }

    override fun onBindViewHolder(holder: RecommendationProductAdapter.RecommendationProductViewHolder, position: Int) {
        holder.bind(product.recommendationItemList[position])
    }

    class RecommendationProductViewHolder(itemView: View) :  RecommendationCardView.TrackingListener,RecyclerView.ViewHolder(itemView){

        private val recommendationCardView: RecommendationCardView? = itemView.findViewById(R.id.productCardView)
        private val context: Context? = itemView.context

        fun bind(product: RecommendationItem){
            recommendationCardView?.setRecommendationModel(product,this)
        }

        override fun onImpressionTopAds(item: RecommendationItem) {

        }

        override fun onImpressionOrganic(item: RecommendationItem) {

        }

        override fun onClickTopAds(item: RecommendationItem) {

        }

        override fun onClickOrganic(item: RecommendationItem) {

        }


    }
}