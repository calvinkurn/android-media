package com.tokopedia.product.edit.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.viewmodel.VideoRecommendationViewModel

class VideoRecommendationFeaturedViewHolder(itemView: View,
                                            var videoFeaturedClickListener : SectionVideoRecommendationViewHolder.VideoFeaturedClickListener,
                                            var videoIDs: ArrayList<String>) : RecyclerView.ViewHolder(itemView) {

    var imageThumbnail: ImageView = itemView.findViewById(R.id.image_thumbnail)
    var textTitle: TextView = itemView.findViewById(R.id.text_title)
    var textChannel: TextView = itemView.findViewById(R.id.text_channel)
    var imageChoosen: ImageView = itemView.findViewById(R.id.image_choosen)

    lateinit var currentVideoRecommendationViewModel : VideoRecommendationViewModel

    init {
        setView()
    }

    fun setView(){
        itemView.setOnClickListener({
            if(!videoIDs.contains(currentVideoRecommendationViewModel.videoID)){
                if(currentVideoRecommendationViewModel.choosen){
                    imageChoosen.setImageResource(R.drawable.ic_add_video_featured)
                    currentVideoRecommendationViewModel.choosen = false
                } else {
                    imageChoosen.setImageResource(R.drawable.ic_check_video_featured)
                    currentVideoRecommendationViewModel.choosen = true
                }
            }
            videoFeaturedClickListener.onVideoFeaturedClicked(currentVideoRecommendationViewModel)
        })
    }
}