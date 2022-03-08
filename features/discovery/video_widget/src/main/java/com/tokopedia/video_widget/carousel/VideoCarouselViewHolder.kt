package com.tokopedia.video_widget.carousel

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.video_widget.R
import com.tokopedia.video_widget.VideoPlayer
import com.tokopedia.video_widget.VideoPlayerController
import com.tokopedia.video_widget.VideoPlayerProvider
import com.tokopedia.video_widget.databinding.VideoCarouselItemBinding

class VideoCarouselViewHolder(
    view: View
) : RecyclerView.ViewHolder(view), VideoPlayerProvider {
    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.video_carousel_item
    }

    private var binding: VideoCarouselItemBinding? by viewBinding()
    private val video: VideoPlayerController? by lazy {
        binding?.let {
            VideoPlayerController(it.root, R.id.video_view, R.id.video_thumbnail_imageview)
        }
    }

    fun bind(videoItem: VideoCarouselItemModel) {
        val binding = binding ?: return
        video?.setVideoURL(videoItem.videoURL)
        binding.videoThumbnailImageview.loadImage(videoItem.imageURL)
        binding.titleTextview.text = videoItem.title
        binding.subtitleTextview.apply {
            if (videoItem.subTitle.isEmpty()) {
                gone()
            } else {
                text = videoItem.subTitle
                show()
            }
        }
    }

    override val videoPlayer: VideoPlayer?
        get() = video
}