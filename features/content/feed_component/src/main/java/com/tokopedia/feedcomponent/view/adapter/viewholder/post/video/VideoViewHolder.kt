package com.tokopedia.feedcomponent.view.adapter.viewholder.post.video

import android.os.Build
import android.view.ViewTreeObserver
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.BasePostViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.post.video.VideoViewModel
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingViewModel
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.item_post_image.view.*

/**
 * @author by yfsx on 20/03/19.
 */
class VideoViewHolder(private val listener: VideoViewListener): BasePostViewHolder<VideoViewModel>() {

    override var layoutRes = R.layout.item_post_video

    override fun bind(element: VideoViewModel) {
        itemView.image.setOnClickListener {
           listener.onVideoPlayerClicked(
                   element.positionInFeed,
                   pagerPosition,
                   element.postId.toString())
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
    }

    interface VideoViewListener {

        fun onVideoPlayerClicked(positionInFeed: Int,
                                 contentPosition: Int,
                                 postId: String)

        fun onAffiliateTrackClicked(trackList : MutableList<TrackingViewModel>)
    }
}