package com.tokopedia.product.edit.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.viewmodel.VideoRecommendationViewModel

class VideoRecommendationFeaturedViewHolder(itemView: View,
                                            var videoFeaturedClickListener : SectionVideoRecommendationViewHolder.VideoFeaturedClickListener) : RecyclerView.ViewHolder(itemView) {

    var imageThumbnail: ImageView = itemView.findViewById(R.id.image_thumbnail)
    var textTitle: TextView = itemView.findViewById(R.id.text_title)
    var textChannel: TextView = itemView.findViewById(R.id.text_channel)
    var imageChosen: ImageView = itemView.findViewById(R.id.image_chosen)

    lateinit var currentVideoRecommendationViewModel : VideoRecommendationViewModel

    init {
        setView()
    }

    fun setView(){
        itemView.setOnClickListener({
            videoFeaturedClickListener.onVideoFeaturedClicked(currentVideoRecommendationViewModel)
        })
    }
}