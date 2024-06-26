package com.tokopedia.feedcomponent.view.adapter.viewholder.post.video

import android.media.MediaPlayer
import android.net.Uri
import android.view.View
import android.view.ViewTreeObserver
import android.webkit.URLUtil
import android.widget.FrameLayout
import android.widget.ImageView
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.feedcomponent.util.ContentNetworkListener
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.BasePostViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.post.video.VideoModel
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.videoplayer.view.widget.VideoPlayerView
import com.tokopedia.design.R as designR

/**
 * @author by yfsx on 20/03/19.
 */
class VideoViewHolder(private val listener: VideoViewListener) :
    BasePostViewHolder<VideoModel>() {

    override var layoutRes = R.layout.item_post_video
    var isPlaying = false

    companion object {
        const val STRING_DEFAULT_TRANSCODING = "customerTrans"
    }

    override fun bind(element: VideoModel) {
        val ivImage: ImageView = itemView.findViewById(R.id.image)
        val icPlay: ImageUnify = itemView.findViewById(R.id.ic_play)
        val frameVideo: FrameLayout = itemView.findViewById(R.id.frame_video)
        val layoutVideo: VideoPlayerView = itemView.findViewById(R.id.layout_video)

        if (!element.url.contains(STRING_DEFAULT_TRANSCODING)) {
            ivImage.setImageResource(designR.drawable.ic_loading_image)
            ivImage.setOnClickListener {
                if (element.url.isNotBlank()) {
                    listener.onVideoPlayerClicked(
                        element.positionInFeed,
                        pagerPosition,
                        element.postId,
                        element.redirectLink,
                        "",
                        "",
                        true,
                        0L
                    )
                }
            }
        } else {
            icPlay.gone()
        }
        ivImage.viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val viewTreeObserver = ivImage.viewTreeObserver
                    viewTreeObserver.removeOnGlobalLayoutListener(this)

                    ivImage.maxHeight = ivImage.width
                    ivImage.requestLayout()
                }
            }
        )
        ivImage.loadImage(element.thumbnail)
        if (canPlayVideo(element)) {
            playVideo(frameVideo, layoutVideo, icPlay, ivImage, element.url)
        } else {
            stopVideo(layoutVideo)
        }
    }

    private fun canPlayVideo(element: VideoModel): Boolean {
        return element.canPlayVideo && ContentNetworkListener.isWifiEnabled(itemView.context)
    }

    private fun playVideo(
        frameVideo: FrameLayout,
        layoutVideo: VideoPlayerView,
        icPlay: ImageUnify,
        ivImage: ImageView,
        url: String) {
        if (!isPlaying) {
            frameVideo.invisible()
            if (URLUtil.isValidUrl(url))
                layoutVideo.setVideoURI(Uri.parse(url))
            layoutVideo.setOnPreparedListener(object : MediaPlayer.OnPreparedListener {
                override fun onPrepared(mp: MediaPlayer) {
                    mp.isLooping = true
                    icPlay.gone()
                    ivImage.gone()
                    mp.setOnInfoListener(object : MediaPlayer.OnInfoListener {
                        override fun onInfo(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
                            if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                                frameVideo.visibility = View.VISIBLE
                                return true
                            }
                            return false
                        }
                    })
                }
            })
            layoutVideo.start()
            isPlaying = true
        }
    }

    private fun stopVideo(layoutVideo: VideoPlayerView) {
        if (isPlaying) {
            layoutVideo.stopPlayback()
            layoutVideo.gone()
            isPlaying = false
        }
    }

    interface VideoViewListener {

        fun onVideoPlayerClicked(
            positionInFeed: Int,
            contentPosition: Int,
            postId: String,
            redirectUrl: String,
            authorId: String,
            authorType: String,
            isFollowed: Boolean,
            startTime: Long
        )

        fun onVideoStopTrack(feedXCard: FeedXCard, duration: Long)

        fun onAffiliateTrackClicked(trackList: List<TrackingModel>, isClick: Boolean)
    }
}
