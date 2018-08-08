package com.tokopedia.product.manage.item.view.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.product.manage.item.view.viewmodel.VideoRecommendationViewModel
import kotlinx.android.synthetic.main.item_product_add_video_recommendation_featured.view.*

class VideoRecommendationFeaturedViewHolder(itemView: View,
                                            private var videoFeaturedClickListener : SectionVideoRecommendationViewHolder.VideoFeaturedClickListener) : RecyclerView.ViewHolder(itemView) {

    lateinit var currentVideoRecommendationViewModel : VideoRecommendationViewModel

    init {
        setView()
    }

    fun setView(){
        itemView.setOnClickListener({
            videoFeaturedClickListener.onVideoFeaturedClicked(currentVideoRecommendationViewModel)
        })
        itemView.imageChosen.setOnClickListener({
            videoFeaturedClickListener.onVideoPlusClicked(currentVideoRecommendationViewModel)
        })
    }
}