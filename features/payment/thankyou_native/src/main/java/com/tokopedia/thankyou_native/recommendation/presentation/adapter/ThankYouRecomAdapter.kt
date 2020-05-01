package com.tokopedia.thankyou_native.recommendation.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.productcard.v2.BlankSpaceConfig
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.thankyou_native.recommendation.presentation.adapter.model.ThankYouRecommendationModel
import com.tokopedia.thankyou_native.recommendation.presentation.adapter.viewholder.RecommendationViewHolder

class ThankYouRecomAdapter(val thankYouRecommendationModelList: List<ThankYouRecommendationModel>,
                           private val blankSpaceConfig: BlankSpaceConfig,
                           val listener: ThankYouRecomViewListener?) :
        ListAdapter<ProductCardModel, RecommendationViewHolder>(ProductModelDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context)
                .inflate(RecommendationViewHolder.LAYOUT_ID, parent, false)
        return RecommendationViewHolder(view)
    }

    override fun getItemCount(): Int {
        return thankYouRecommendationModelList.count()
    }

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        holder.bind(thankYouRecommendationModelList[position], blankSpaceConfig, listener)
    }

    override fun onViewRecycled(holder: RecommendationViewHolder) {
        super.onViewRecycled(holder)
        holder.clearImage()
    }

    override fun onViewAttachedToWindow(holder: RecommendationViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.getThankYouRecommendationModel()?.let {
            if (!it.isSeenOnceByUser) {
                listener?.onRecommendationItemDisplayed(it.recommendationItem,
                        holder.adapterPosition + 1)
                it.isSeenOnceByUser = true
            }
        }
    }

    class ProductModelDiffUtil : DiffUtil.ItemCallback<ProductCardModel>() {
        override fun areItemsTheSame(oldItem: ProductCardModel, newItem: ProductCardModel): Boolean {
            return oldItem.productName == newItem.productName
        }

        override fun areContentsTheSame(oldItem: ProductCardModel, newItem: ProductCardModel): Boolean {
            return oldItem == newItem
        }
    }

}