package com.tokopedia.feedcomponent.view.adapter.viewholder.post.image

import android.os.Build
import android.view.ViewTreeObserver
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXMedia
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
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
        itemView.videoPreviewImage.setOnClickListener {
            listener.onImageClick(element.positionInFeed, pagerPosition, element.redirectLink)
            if (!element.trackingList.isEmpty()) {
                listener.onAffiliateTrackClicked(element.trackingList, true)
            }
        }
        itemView.videoPreviewImage.viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        val viewTreeObserver = itemView.videoPreviewImage.viewTreeObserver
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            viewTreeObserver.removeOnGlobalLayoutListener(this)
                        } else {
                            @Suppress("DEPRECATION")
                            viewTreeObserver.removeGlobalOnLayoutListener(this)
                        }

                        itemView.videoPreviewImage.maxHeight = itemView.videoPreviewImage.width
                        itemView.videoPreviewImage.requestLayout()
                    }
                }
        )
        itemView.videoPreviewImage.loadImage(element.image)
        listener.userImagePostImpression(element.positionInFeed, pagerPosition)
    }

    interface ImagePostListener {
        fun userImagePostImpression(positionInFeed: Int, contentPosition: Int)

        fun userCarouselImpression(
            activityId: String,
            media: FeedXMedia,
            positionInFeed: Int,
            postType: String,
            isFollowed: Boolean,
            shopId: String
        )
        fun userGridPostImpression(
            positionInFeed: Int,
            activityId: String,
            postType: String,
            shopId: String,
        )
        fun userProductImpression(
            positionInFeed: Int,
            activityId: String,
            postType: String,
            shopId: String,
            productList: List<FeedXProduct>)


        fun onImageClick(positionInFeed: Int, contentPosition: Int, redirectLink: String)

        fun onAffiliateTrackClicked(trackList: List<TrackingViewModel>, isClick: Boolean)
    }
}