package com.tokopedia.feedcomponent.view.adapter.viewholder.post.video

import android.net.Uri
import android.os.Build
import android.view.View
import android.view.ViewTreeObserver
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.BasePostViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.post.video.VideoViewModel
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingViewModel
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.item_post_video.view.*

/**
 * @author by yfsx on 20/03/19.
 */
class VideoViewHolder(private val listener: VideoViewListener) : BasePostViewHolder<VideoViewModel>() {


    override var layoutRes = R.layout.item_post_video
    var isPlaying = false


    companion object {
        const val STRING_DEFAULT_TRANSCODING = "customerTrans"
        const val TAG = "TAG_VIDEO_VIEW_HOLDER"
    }

    override fun bind(element: VideoViewModel) {
        if (!element.url.contains(STRING_DEFAULT_TRANSCODING)) {
            itemView.image.setOnClickListener {
                if (!element.url.isBlank()) {
                    listener.onVideoPlayerClicked(
                            element.positionInFeed,
                            pagerPosition,
                            element.postId.toString())
                }
            }
        } else {
            itemView.ic_play.visibility = View.GONE
        }
        itemView.image.viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        val viewTreeObserver = itemView.image.viewTreeObserver
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            viewTreeObserver.removeOnGlobalLayoutListener(this)
                        } else {
                            @Suppress("DEPRECATION")
                            viewTreeObserver.removeGlobalOnLayoutListener(this)
                        }

                        itemView.image.maxHeight = itemView.image.width
                        itemView.image.requestLayout()
                    }
                }
        )
        itemView.image.loadImage(element.thumbnail)
        if (element.canPlayVideo) {
            playVideo(element.url)
        } else {
            stopVideo()
        }
    }

    fun playVideo(url: String) {
        if (!isPlaying) {
            itemView.layout_video.visibility = View.VISIBLE
            itemView.layout_video.setVideoURI(Uri.parse(url))
            itemView.layout_video.setOnPreparedListener {
                it.isLooping = true
            }
            itemView.layout_video.start()
            isPlaying = true
        }
    }

    fun stopVideo() {
        if (isPlaying) {
            itemView.layout_video.stopPlayback()
            itemView.layout_video.visibility = View.GONE
            isPlaying = false
        }
    }

    interface VideoViewListener {

        fun onVideoPlayerClicked(positionInFeed: Int,
                                 contentPosition: Int,
                                 postId: String)

        fun onAffiliateTrackClicked(trackList: MutableList<TrackingViewModel>, isClick: Boolean)
    }
}