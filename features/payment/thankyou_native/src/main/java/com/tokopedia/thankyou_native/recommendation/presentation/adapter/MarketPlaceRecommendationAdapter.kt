package com.tokopedia.thankyou_native.recommendation.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.productcard.v2.BlankSpaceConfig
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.thankyou_native.recommendation.presentation.adapter.listener.MarketPlaceRecommendationViewListener
import com.tokopedia.thankyou_native.recommendation.model.MarketPlaceRecommendationModel
import com.tokopedia.thankyou_native.recommendation.presentation.adapter.viewholder.MarketPlaceRecommendationViewHolder

class MarketPlaceRecommendationAdapter(val marketPlaceRecommendationModelList: List<MarketPlaceRecommendationModel>,
                                       private val blankSpaceConfig: BlankSpaceConfig,
                                       val listener: MarketPlaceRecommendationViewListener?) :
        ListAdapter<ProductCardModel, MarketPlaceRecommendationViewHolder>(ProductModelDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketPlaceRecommendationViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context)
                .inflate(MarketPlaceRecommendationViewHolder.LAYOUT_ID, parent, false)
        return MarketPlaceRecommendationViewHolder(view)
    }

    override fun getItemCount(): Int {
        return marketPlaceRecommendationModelList.count()
    }

    override fun onBindViewHolder(holderMarketPlace: MarketPlaceRecommendationViewHolder, position: Int) {
        holderMarketPlace.bind(marketPlaceRecommendationModelList[position], blankSpaceConfig, listener)
    }

    override fun onViewRecycled(holderMarketPlace: MarketPlaceRecommendationViewHolder) {
        super.onViewRecycled(holderMarketPlace)
        holderMarketPlace.clearImage()
    }

    override fun onViewAttachedToWindow(holderMarketPlace: MarketPlaceRecommendationViewHolder) {
        super.onViewAttachedToWindow(holderMarketPlace)
        holderMarketPlace.getThankYouRecommendationModel()?.let {
            if (!it.isSeenOnceByUser) {
                listener?.onRecommendationItemDisplayed(it.recommendationItem,
                        holderMarketPlace.adapterPosition + 1)
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