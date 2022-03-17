package com.tokopedia.video_widget.carousel

import androidx.recyclerview.widget.DiffUtil

class VideoCarouselItemDiffUtil : DiffUtil.ItemCallback<VideoCarouselItemModel>() {
    override fun areItemsTheSame(
        oldItem: VideoCarouselItemModel,
        newItem: VideoCarouselItemModel
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: VideoCarouselItemModel,
        newItem: VideoCarouselItemModel
    ): Boolean {
        return oldItem == newItem
    }
}