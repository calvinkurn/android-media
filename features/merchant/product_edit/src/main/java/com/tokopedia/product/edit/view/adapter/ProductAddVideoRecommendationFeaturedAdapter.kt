package com.tokopedia.product.edit.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.view.adapter.viewholder.SectionVideoRecommendationViewHolder
import com.tokopedia.product.edit.view.adapter.viewholder.VideoRecommendationFeaturedViewHolder
import com.tokopedia.product.edit.view.viewmodel.VideoRecommendationViewModel



class ProductAddVideoRecommendationFeaturedAdapter(var videoRecommendationFeatured: ArrayList<VideoRecommendationViewModel?>,
                                                   var videoFeaturedClickListener : SectionVideoRecommendationViewHolder.VideoFeaturedClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is VideoRecommendationFeaturedViewHolder) {
            holder.textTitle.text = videoRecommendationFeatured[position]?.snippetTitle
            holder.textChannel.text = videoRecommendationFeatured[position]?.snippetChannel
            ImageHandler.loadImageThumbs(holder.imageThumbnail.context, holder.imageThumbnail, videoRecommendationFeatured[position]?.thumbnailUrl)
            if(videoRecommendationFeatured[position]!!.chosen){
                holder.imageChosen.setImageResource(R.drawable.ic_check_video_featured)
            } else {
                holder.imageChosen.setImageResource(R.drawable.ic_add_video_featured)
            }
            holder.currentVideoRecommendationViewModel = videoRecommendationFeatured[position]!!
        }
    }

    override fun getItemCount(): Int {
        return videoRecommendationFeatured.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        lateinit var vh: RecyclerView.ViewHolder

        if(viewType == 1){
            val itemLayoutView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_product_add_video_recommendation_featured, parent, false)
            vh = VideoRecommendationFeaturedViewHolder(itemLayoutView, videoFeaturedClickListener)
        } else {
            val itemLayoutView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_shimmering_video_reccomendation, parent, false)
            vh = LoadingViewHolder(itemLayoutView)
        }

        return vh
    }

    override fun getItemViewType(position: Int): Int {
        return if (videoRecommendationFeatured[position] != null) 1 else 0
    }
    fun replaceData(videoRecommendationFeatured: ArrayList<VideoRecommendationViewModel?>) {
        this.videoRecommendationFeatured = videoRecommendationFeatured
        notifyDataSetChanged()
    }

    inner class LoadingViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        init {
        }
    }
}