package com.tokopedia.video_widget.carousel

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.video_widget.R
import com.tokopedia.video_widget.databinding.VideoCarouselItemBinding

class VideoCarouselItemViewHolder(
    view: View
) : RecyclerView.ViewHolder(view){
    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.video_carousel_item
    }

    private var binding: VideoCarouselItemBinding? by viewBinding()

    fun bind(videoItem: VideoCarouselItemModel, isWifiConnected: Boolean) {
        val binding = binding ?: return
        binding.root.setVideoCarouselItemModel(videoItem)
        renderPlayButtonVisibility(binding.root, isWifiConnected)
    }

    private fun renderPlayButtonVisibility(
        view: VideoCarouselCardView,
        isWifiConnected: Boolean
    ) {
        if(isWifiConnected) {
            view.hidePlayButton()
        } else {
            view.showPlayButton()
        }
    }

    fun recycle() {
        binding?.root?.recycle()
    }
}