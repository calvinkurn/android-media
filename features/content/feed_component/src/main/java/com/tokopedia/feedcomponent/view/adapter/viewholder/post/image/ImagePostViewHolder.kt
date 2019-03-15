package com.tokopedia.feedcomponent.view.adapter.viewholder.post.image

import android.os.Build
import android.view.ViewTreeObserver
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.BasePostViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.post.image.ImagePostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingViewModel
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.item_post_image.view.*

/**
 * @author by milhamj on 04/12/18.
 */
class ImagePostViewHolder(private val listener: ImagePostListener) : BasePostViewHolder<ImagePostViewModel>() {

    override var layoutRes = R.layout.item_post_image

    override fun bind(element: ImagePostViewModel) {
        itemView.image.setOnClickListener {
            listener.onImageClick(element.positionInFeed, pagerPosition, element.redirectLink)
            if (!element.trackingList.isEmpty()) {
                listener.onAffilaiteTrackClicked(element.trackingList)
            }
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
        itemView.image.loadImage(element.image)
    }

    interface ImagePostListener {
        fun onImageClick(positionInFeed: Int, contentPosition: Int, redirectLink: String)

        fun onAffilaiteTrackClicked(trackList : MutableList<TrackingViewModel>)
    }
}