package com.tokopedia.product.edit.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.adapter.viewholder.VideoRecommendationViewHolder

class ProductAddVideoRecommendationFeaturedAdapter(var videoFeaturedList: List<String>) : RecyclerView.Adapter<VideoRecommendationViewHolder>(){


    override fun onBindViewHolder(holder: VideoRecommendationViewHolder?, position: Int) {
//        holder.mainContent
    }

    override fun getItemCount(): Int {
        return videoFeaturedList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VideoRecommendationViewHolder {
        val itemLayoutView = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.item_product_add_video_recommendation_featured, parent, false)

        return VideoRecommendationViewHolder(itemLayoutView)
    }

}