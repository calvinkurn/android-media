package com.tokopedia.video_widget.carousel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class VideoCarouselAdapter : RecyclerView.Adapter<VideoCarouselViewHolder>() {
    private val videoCarouselItem: MutableList<VideoCarouselItemModel> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoCarouselViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(VideoCarouselViewHolder.LAYOUT, parent, false)
        return VideoCarouselViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoCarouselViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return videoCarouselItem.size
    }
}