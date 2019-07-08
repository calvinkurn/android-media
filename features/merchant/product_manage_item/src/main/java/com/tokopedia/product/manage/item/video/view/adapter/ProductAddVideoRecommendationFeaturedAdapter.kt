package com.tokopedia.product.manage.item.video.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.utils.YoutubeUtil
import com.tokopedia.product.manage.item.video.view.adapter.viewholder.SectionVideoRecommendationViewHolder
import com.tokopedia.product.manage.item.video.view.adapter.viewholder.VideoRecommendationFeaturedViewHolder
import com.tokopedia.product.manage.item.video.view.model.VideoRecommendationViewModel
import kotlinx.android.synthetic.main.item_product_add_video_recommendation_featured.view.*


class ProductAddVideoRecommendationFeaturedAdapter(var videoRecommendationFeatured: ArrayList<VideoRecommendationViewModel?>,
                                                   private var videoFeaturedClickListener : SectionVideoRecommendationViewHolder.VideoFeaturedClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is VideoRecommendationFeaturedViewHolder) {
            holder.itemView.textTitle.text = videoRecommendationFeatured[position]?.snippetTitle
            holder.itemView.textChannel.text = videoRecommendationFeatured[position]?.snippetChannel
            holder.itemView.textDuration.text = YoutubeUtil.convertYoutubeTimeFormattoHHMMSS(videoRecommendationFeatured[position]?.duration)
            ImageHandler.loadImageRounded2(holder.itemView.imageThumbnail.context, holder.itemView.imageThumbnail, videoRecommendationFeatured[position]?.thumbnailUrl)
            if(videoRecommendationFeatured[position]!!.chosen){
                holder.itemView.imageChosen.setImageDrawable(MethodChecker.getDrawable(holder.itemView.getContext(),
                        R.drawable.ic_check_video_featured))
            } else {
                holder.itemView.imageChosen.setImageDrawable(MethodChecker.getDrawable(holder.itemView.getContext(),
                        R.drawable.ic_add_video_featured))
            }
            holder.currentVideoRecommendationViewModel = videoRecommendationFeatured[position]!!
        }
    }

    override fun getItemCount() = videoRecommendationFeatured.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            1 -> {
                val itemLayoutView = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_product_add_video_recommendation_featured, parent, false)
                VideoRecommendationFeaturedViewHolder(itemLayoutView, videoFeaturedClickListener)
            }
            else -> {
                val itemLayoutView = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_shimmering_video_reccomendation, parent, false)
                LoadingViewHolder(itemLayoutView)
            }
        }
    }

    override fun getItemViewType(position: Int) = if (videoRecommendationFeatured[position] != null) 1 else 0

    fun replaceData(videoRecommendationFeatured: ArrayList<VideoRecommendationViewModel?>) {
        this.videoRecommendationFeatured = videoRecommendationFeatured
        notifyDataSetChanged()
    }

    inner class LoadingViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        init {
        }
    }
}