package com.tokopedia.product.edit.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.viewmodel.VideoViewModel

class VideoViewHolder(itemView: View) : AbstractViewHolder<VideoViewModel>(itemView) {

    var imageThumbnail: ImageView = itemView.findViewById(R.id.image_thumbnail)
    var textTitle: TextView = itemView.findViewById(R.id.text_title)
    var textChannel: TextView = itemView.findViewById(R.id.text_channel)
    var imageDelete: ImageView = itemView.findViewById(R.id.image_video_delete)

    init {
        setViews()
    }

    fun setViews(){
        imageDelete.setOnClickListener(View.OnClickListener {

        })
    }

    override fun bind(videoViewModel: VideoViewModel) {
        textTitle.text = videoViewModel.snippetTitle
        textChannel.text = videoViewModel.snippetDescription
        ImageHandler.loadImageThumbs(imageThumbnail.context, imageThumbnail, videoViewModel.thumbnailUrl)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_add_video_choosen
    }
}