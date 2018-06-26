package com.tokopedia.product.edit.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.viewmodel.VideoViewModel

class VideoViewHolder(itemView: View) : AbstractViewHolder<VideoViewModel>(itemView) {

    private lateinit var imageAddVideoUrl: ImageView
    private lateinit var textAddVideoUrlTitle: TextView
    private lateinit var textAddVideoUrlDescription: TextView
    private lateinit var imageAddVideoUrlRemove: ImageView

    init {
        findViews(itemView)
    }

    private fun findViews(view: View) {
        imageAddVideoUrl = view.findViewById(R.id.image_add_video_url)
        textAddVideoUrlTitle = view.findViewById(R.id.text_add_video_url_title)
        textAddVideoUrlDescription = view.findViewById(R.id.text_add_video_url_description)
        imageAddVideoUrlRemove = view.findViewById(R.id.image_add_video_url_remove)
    }

    override fun bind(videoViewModel: VideoViewModel) {
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_add_video_choosen
    }
}