package com.tokopedia.video_widget.carousel

import androidx.recyclerview.widget.DiffUtil

class VideoCarouselDataViewVideoItemDiffUtil : DiffUtil.ItemCallback<VideoCarouselDataView.VideoItem>() {
    override fun areItemsTheSame(
        oldItem: VideoCarouselDataView.VideoItem,
        newItem: VideoCarouselDataView.VideoItem
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: VideoCarouselDataView.VideoItem,
        newItem: VideoCarouselDataView.VideoItem
    ): Boolean {
        return oldItem == newItem
    }
}