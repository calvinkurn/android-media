package com.tokopedia.product.edit.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.listener.VideoChoosenListener
import com.tokopedia.product.edit.viewmodel.VideoViewModel

class VideoViewHolder(itemView: View,
                      var videoChoosenListener: VideoChoosenListener) : AbstractViewHolder<VideoViewModel>(itemView){

    var imageThumbnail: ImageView = itemView.findViewById(R.id.image_thumbnail)
    var textTitle: TextView = itemView.findViewById(R.id.text_title)
    var textChannel: TextView = itemView.findViewById(R.id.text_channel)
    var imageDelete: ImageView = itemView.findViewById(R.id.image_video_delete)
    var textTagRecommendation: TextView = itemView.findViewById(R.id.text_tag_recommendation)

    init {
        setViews()
    }

    private fun setViews(){
        imageDelete.setOnClickListener({
            videoChoosenListener.onVideoChoosenDeleted(adapterPosition)
        })
    }

    override fun bind(videoViewModel: VideoViewModel) {
        textTitle.text = videoViewModel.snippetTitle
        textChannel.text = videoViewModel.snippetChannel
        ImageHandler.loadImageThumbs(imageThumbnail.context, imageThumbnail, videoViewModel.thumbnailUrl)
        if(videoViewModel.recommendation!!){
            textTagRecommendation.visibility = View.VISIBLE
        } else {
            textTagRecommendation.visibility = View.GONE
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_add_video_choosen
    }
}