package com.tokopedia.video_widget.carousel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class VideoCarouselVideoItemAdapter(
    diffCallback: DiffUtil.ItemCallback<VideoCarouselDataView.VideoItem> = VideoCarouselDataViewVideoItemDiffUtil()
) : ListAdapter<VideoCarouselDataView.VideoItem, VideoCarouselVideoItemViewHolder>(diffCallback) {
    private var listener: VideoCarouselItemListener? = null
    private var isWifiConnected = false
    private var isSneakPeekEnabled = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoCarouselVideoItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(VideoCarouselVideoItemViewHolder.LAYOUT, parent, false)
        return VideoCarouselVideoItemViewHolder(view).apply {
            view.setOnClickListener {
                val currentPosition = adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    listener?.onVideoCarouselItemClicked(getItem(currentPosition))
                }
            }
        }
    }

    override fun onBindViewHolder(holder: VideoCarouselVideoItemViewHolder, position: Int) {
        val isRenderPlayButton = isWifiConnected || isSneakPeekEnabled
        holder.bind(getItem(position), isRenderPlayButton)
        listener?.onVideoCarouselItemImpressed(getItem(position))
    }

    fun onWifiConnectionChange(isWifiConnected: Boolean) {
        this.isWifiConnected = isWifiConnected
        if(itemCount > 0) {
            notifyItemRangeChanged(0, itemCount)
        }
    }

    fun setSneakPeakLayout(isSneakPeekEnabled: Boolean) {
        this.isSneakPeekEnabled = isSneakPeekEnabled
        if(itemCount > 0) {
            notifyItemRangeChanged(0, itemCount)
        }
    }

    fun setListener(listener: VideoCarouselItemListener?) {
        this.listener = listener
    }
}
