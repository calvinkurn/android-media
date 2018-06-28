package com.tokopedia.product.edit.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.adapter.viewholder.SectionVideoRecommendationViewHolder
import com.tokopedia.product.edit.adapter.viewholder.VideoRecommendationFeaturedViewHolder
import com.tokopedia.product.edit.viewmodel.VideoRecommendationViewModel

class ProductAddVideoRecommendationFeaturedAdapter(var videoRecommendationFeatured: List<VideoRecommendationViewModel>,
                                                   var videoFeaturedClickListener : SectionVideoRecommendationViewHolder.VideoFeaturedClickListener,
                                                   var videoIDs: ArrayList<String>) : RecyclerView.Adapter<VideoRecommendationFeaturedViewHolder>(){

    override fun onBindViewHolder(holder: VideoRecommendationFeaturedViewHolder, position: Int) {
        holder.textTitle.text = videoRecommendationFeatured.get(position).snippetTitle
        holder.textChannel.text = videoRecommendationFeatured.get(position).snippetChannel
        ImageHandler.loadImageThumbs(holder.imageThumbnail.context, holder.imageThumbnail, videoRecommendationFeatured.get(position).thumbnailUrl)
        holder.currentVideoRecommendationViewModel = videoRecommendationFeatured.get(position)
    }

    override fun getItemCount(): Int {
        return videoRecommendationFeatured.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VideoRecommendationFeaturedViewHolder {
        val itemLayoutView = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.item_product_add_video_recommendation_featured, parent, false)

        return VideoRecommendationFeaturedViewHolder(itemLayoutView, videoFeaturedClickListener, videoIDs)
    }

    fun replaceData(videoRecommendationFeatured: List<VideoRecommendationViewModel>) {
        this.videoRecommendationFeatured = videoRecommendationFeatured
        notifyDataSetChanged()
    }

}