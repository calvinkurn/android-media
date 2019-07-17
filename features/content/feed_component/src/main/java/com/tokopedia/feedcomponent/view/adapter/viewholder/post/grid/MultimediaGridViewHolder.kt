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
import kotlinx.android.synthetic.main.item_post_multimedia.view.*

/**
 * @author by yoasfs on 2019-07-01
 */
class MultimediaGridViewHolder(private val feedType: String)
    : BasePostViewHolder<MultimediaGridViewModel>() {

    var isPlaying = false
    var TYPE_VIDEO = "video"
    override var layoutRes: Int = R.layout.item_post_multimedia

    override fun bind(element: MultimediaGridViewModel) {
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
        if (isSingleItemVideo(element)) {
            val mediaItem = element.mediaItemList.get(0)
            if (canPlayVideo(mediaItem)) {
                playVideo(mediaItem.videos.get(0).url)
            } else {
                stopVideo()
            }
        }
    }

    private fun isSingleItemVideo(element: MultimediaGridViewModel): Boolean {
        return element.mediaItemList.size == 1 && element.mediaItemList.get(0).type.equals(TYPE_VIDEO)
    }

    private fun canPlayVideo(element: MediaItem): Boolean {
        return element.isCanPlayVideo && ContentNetworkListener.getInstance(itemView.context).isWifiEnabled()
    }

    private fun playVideo(url: String) {
        if (!isPlaying) {
            itemView.frame_video.visibility = View.INVISIBLE
            itemView.layout_video.setVideoURI(Uri.parse(url))
            itemView.layout_video.setOnPreparedListener(object: MediaPlayer.OnPreparedListener{
                override fun onPrepared(mp: MediaPlayer) {
                    mp.isLooping = true
                    mp.setOnInfoListener(object: MediaPlayer.OnInfoListener{
                        override fun onInfo(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
                            if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
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
            itemView.layout_video.visibility = View.GONE
            isPlaying = false
        }
    }
}