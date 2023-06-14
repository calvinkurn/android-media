package com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid

import android.media.MediaPlayer
import android.net.Uri
import android.view.ViewTreeObserver
import android.webkit.URLUtil
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.MediaItem
import com.tokopedia.feedcomponent.util.ContentNetworkListener
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.BasePostViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.post.grid.MultimediaGridModel
import com.tokopedia.feedcomponent.view.widget.FeedMultipleImageView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.videoplayer.view.widget.VideoPlayerView

/**
 * @author by yoasfs on 2019-07-01
 */
class MultimediaGridViewHolder(
    private val feedMultipleImageViewListener: FeedMultipleImageView.FeedMultipleImageViewListener,
    private val feedType: String
) : BasePostViewHolder<MultimediaGridModel>() {

    private val feedMultipleImageView: FeedMultipleImageView = itemView.findViewById(R.id.feedMultipleImageView)
    private val layoutVideo: VideoPlayerView = itemView.findViewById(R.id.layout_video)
    private val layoutDummy: FrameLayout = itemView.findViewById(R.id.layout_dummy)
    private val ivImage: AppCompatImageView = itemView.findViewById(R.id.image)
    private val loadingProgress: ProgressBar = itemView.findViewById(R.id.loading_progress)
    private val frameVideo: FrameLayout = itemView.findViewById(R.id.frame_video)

    var isPlaying = false
    var TYPE_VIDEO = "video"
    override var layoutRes: Int = R.layout.item_post_multimedia

    override fun bind(element: MultimediaGridModel) {
        element.mediaItemList.forEach {
            it.positionInFeed = element.positionInFeed
        }
        feedMultipleImageView.viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val viewTreeObserver = feedMultipleImageView.viewTreeObserver
                    viewTreeObserver.removeOnGlobalLayoutListener(this)

                    feedMultipleImageView.layoutParams.height =
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    feedMultipleImageView.requestLayout()
                    ivImage.gone()
                }
            }
        )

        feedMultipleImageView.bind(element.mediaItemList, feedType)
        feedMultipleImageView.setFeedMultipleImageViewListener(feedMultipleImageViewListener)

        if (isSingleItemVideo(element)) {
            layoutDummy.visible()
            val mediaItem = element.mediaItemList[0]
            if (canPlayVideo(mediaItem) && ContentNetworkListener.isWifiEnabled(context) && mediaItem.videos.isNotEmpty()) {
                playVideo(mediaItem.videos[0].url)
            } else {
                stopVideo()
            }
        } else {
            layoutDummy.gone()
        }

        layoutDummy.setOnClickListener {
            if (isSingleItemVideo(element)) {
                val mediaItem = element.mediaItemList.first()
                if (mediaItem.videos.isNotEmpty()) playVideo(mediaItem.videos[0].url)
            }
        }
    }

    private fun isSingleItemVideo(element: MultimediaGridModel): Boolean {
        return element.mediaItemList.size == 1 && element.mediaItemList[0].type == TYPE_VIDEO
    }

    private fun canPlayVideo(element: MediaItem): Boolean {
        return element.isCanPlayVideo
    }

    private fun playVideo(url: String) {
        if (!isPlaying) {
            feedMultipleImageView.performClick()
            loadingProgress.show()
            layoutDummy.gone()
            frameVideo.invisible()

            if (URLUtil.isValidUrl(url)) {
                layoutVideo.setVideoURI(Uri.parse(url))
                layoutVideo.setOnPreparedListener(object : MediaPlayer.OnPreparedListener {
                    override fun onPrepared(mp: MediaPlayer) {
                        mp.isLooping = true
                        mp.setOnInfoListener(object : MediaPlayer.OnInfoListener {
                            override fun onInfo(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
                                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                                    loadingProgress.gone()
                                    frameVideo.visible()
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
    }

    private fun stopVideo() {
        if (isPlaying) {
            layoutVideo.stopPlayback()
            layoutVideo.gone()
            layoutDummy.show()
            isPlaying = false
        }
    }
}
