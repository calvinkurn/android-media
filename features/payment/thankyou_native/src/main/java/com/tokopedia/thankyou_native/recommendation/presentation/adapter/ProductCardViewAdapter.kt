package com.tokopedia.thankyou_native.recommendation.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.thankyou_native.recommendation.presentation.adapter.listener.ProductCardViewListener
import com.tokopedia.thankyou_native.recommendation.data.ThankYouProductCardModel
import com.tokopedia.thankyou_native.recommendation.presentation.adapter.viewholder.ProductCardViewHolder

class ProductCardViewAdapter(val productCardList: List<ThankYouProductCardModel>,
                             val listener: ProductCardViewListener?) :
        RecyclerView.Adapter<ProductCardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductCardViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context)
                .inflate(ProductCardViewHolder.LAYOUT_ID, parent, false)
        return ProductCardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productCardList.count()
    }

    override fun onBindViewHolder(holder: ProductCardViewHolder, position: Int) {
        holder.bind(productCardList[position], listener)
    }


    override fun onViewAttachedToWindow(holderMarketPlace: ProductCardViewHolder) {
        super.onViewAttachedToWindow(holderMarketPlace)
        holderMarketPlace.getThankYouRecommendationModel()?.let {
            if (!it.isSeenOnceByUser) {
                listener?.onRecommendationItemDisplayed(it.recommendationItem,
                        holderMarketPlace.adapterPosition + 1)
                it.isSeenOnceByUser = true
            }
        }
    }


}