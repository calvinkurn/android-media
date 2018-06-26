package com.tokopedia.product.edit.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.adapter.viewholder.VideoRecommendationFeaturedViewHolder
import com.tokopedia.product.edit.model.VideoRecommendationData

class ProductAddVideoRecommendationFeaturedAdapter(var videoFeaturedList: List<VideoRecommendationData>) : RecyclerView.Adapter<VideoRecommendationFeaturedViewHolder>(){


    override fun onBindViewHolder(holder: VideoRecommendationFeaturedViewHolder?, position: Int) {
//        holder.mainContent
    }

    override fun getItemCount(): Int {
        return videoFeaturedList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VideoRecommendationFeaturedViewHolder {
        val itemLayoutView = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.item_product_add_video_recommendation_featured, parent, false)

        return VideoRecommendationFeaturedViewHolder(itemLayoutView)
    }

    fun replaceData(videoFeaturedList: List<VideoRecommendationData>) {
        this.videoFeaturedList = videoFeaturedList
        notifyDataSetChanged()
    }

}