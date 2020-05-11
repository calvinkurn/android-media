package com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid

import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.MediaItem
import com.tokopedia.feedcomponent.util.ContentNetworkListener
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.BasePostViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.post.grid.MultimediaGridViewModel
import com.tokopedia.feedcomponent.view.widget.FeedMultipleImageView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.item_post_multimedia.view.*

/**
 * @author by yoasfs on 2019-07-01
 */
class MultimediaGridViewHolder(private val feedMultipleImageViewListener: FeedMultipleImageView.FeedMultipleImageViewListener,
                               private val feedType: String)
    : BasePostViewHolder<MultimediaGridViewModel>() {

    var isPlaying = false
    var TYPE_VIDEO = "video"
    override var layoutRes: Int = R.layout.item_post_multimedia

    override fun bind(element: MultimediaGridViewModel) {
        element.mediaItemList.forEach{
            it.positionInFeed = element.positionInFeed
        }
        itemView.feedMultipleImageView.viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        val viewTreeObserver = itemView.feedMultipleImageView.viewTreeObserver
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            viewTreeObserver.removeOnGlobalLayoutListener(this)
                        } else {
                            @Suppress("DEPRECATION")
                            viewTreeObserver.removeGlobalOnLayoutListener(this)
                        }

                        itemView.feedMultipleImageView.layoutParams.height = FrameLayout.LayoutParams.WRAP_CONTENT
                        itemView.feedMultipleImageView.requestLayout()
                        itemView.image.visibility = View.GONE
                    }
                }
        )

        itemView.feedMultipleImageView.bind(element.mediaItemList, feedType)
        itemView.feedMultipleImageView.setFeedMultipleImageViewListener(feedMultipleImageViewListener)

        if (isSingleItemVideo(element)) {
            itemView.layout_dummy.visibility = View.VISIBLE
            val mediaItem = element.mediaItemList[0]
            if (canPlayVideo(mediaItem) && ContentNetworkListener.isWifiEnabled(itemView.context) && mediaItem.videos.isNotEmpty()) {
                playVideo(mediaItem.videos[0].url)
            } else {
                stopVideo()
            }
        } else {
            itemView.layout_dummy.visibility = View.GONE
        }

        itemView.layout_dummy.setOnClickListener{
            if (isSingleItemVideo(element)) {
                val mediaItem = element.mediaItemList.first()
                if (mediaItem.videos.isNotEmpty()) playVideo(mediaItem.videos[0].url)
            }
        }
    }

    private fun isSingleItemVideo(element: MultimediaGridViewModel): Boolean {
        return element.mediaItemList.size == 1 && element.mediaItemList[0].type == TYPE_VIDEO
    }

    private fun canPlayVideo(element: MediaItem): Boolean {
        return element.isCanPlayVideo
    }

    private fun playVideo(url: String) {
        if (!isPlaying) {
            itemView.feedMultipleImageView.performClick()
            itemView.loading_progress.show()
            itemView.layout_dummy.gone()
            itemView.frame_video.invisible()
            itemView.layout_video.setVideoURI(Uri.parse(url))
            itemView.layout_video.setOnPreparedListener(object: MediaPlayer.OnPreparedListener{
                override fun onPrepared(mp: MediaPlayer) {
                    mp.isLooping = true
                    mp.setOnInfoListener(object: MediaPlayer.OnInfoListener {
                        override fun onInfo(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
                            if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                                itemView.loading_progress.gone()
                                itemView.frame_video.visibility = View.VISIBLE
                                return true
                            }
                            return false
                        }
                    })
                }
            })
            itemView.layout_video.start()
            isPlaying = true
        }
    }

    private fun stopVideo() {
        if (isPlaying) {
            itemView.layout_video.stopPlayback()
            itemView.layout_video.gone()
            itemView.layout_dummy.show()
            isPlaying = false
        }
    }
}