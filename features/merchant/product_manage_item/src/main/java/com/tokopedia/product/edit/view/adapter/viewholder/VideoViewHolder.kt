package com.tokopedia.product.edit.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.util.YoutubeUtil
import com.tokopedia.product.edit.view.listener.VideoChosenListener
import com.tokopedia.product.edit.view.viewmodel.VideoViewModel
import kotlinx.android.synthetic.main.item_product_add_video_choosen.view.*

class VideoViewHolder(itemView: View,
                      var videoChoosenListener: VideoChosenListener) : AbstractViewHolder<VideoViewModel>(itemView){

    init {
        setViews()
    }

    private fun setViews(){
        itemView.llDelete.setOnClickListener({
            videoChoosenListener.onVideoChosenDeleteClicked(adapterPosition)
        })
    }

    override fun bind(videoViewModel: VideoViewModel) {
        itemView.textTitle.text = videoViewModel.snippetTitle
        itemView.textChannel.text = videoViewModel.snippetChannel
        itemView.textDuration.text = YoutubeUtil.convertYoutubeTimeFormattoHHMMSS(videoViewModel.duration)
        ImageHandler.loadImageRounded2(itemView.imageThumbnail.context, itemView.imageThumbnail, videoViewModel.thumbnailUrl)

        itemView.setOnClickListener({
            videoChoosenListener.onVideoChosenClicked(adapterPosition)
        })
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_add_video_choosen
    }
}