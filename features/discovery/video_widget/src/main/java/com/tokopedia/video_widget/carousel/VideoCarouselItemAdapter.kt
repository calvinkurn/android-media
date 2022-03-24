package com.tokopedia.video_widget.carousel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class VideoCarouselItemAdapter(
    diffCallback: DiffUtil.ItemCallback<VideoCarouselItemModel> = VideoCarouselItemDiffUtil()
) : ListAdapter<VideoCarouselItemModel, VideoCarouselItemViewHolder>(diffCallback) {
    private var listener: VideoCarouselItemListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoCarouselItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(VideoCarouselItemViewHolder.LAYOUT, parent, false)
        return VideoCarouselItemViewHolder(view).apply {
            view.setOnClickListener {
                val currentPosition = adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    listener?.onVideoCarouselItemClicked(currentPosition)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: VideoCarouselItemViewHolder, position: Int) {
        holder.bind(getItem(position))
        listener?.onVideoCarouselItemImpressed(position)
    }

    fun setListener(listener: VideoCarouselItemListener?) {
        this.listener = listener
    }
}