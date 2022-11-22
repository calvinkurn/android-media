package com.tokopedia.feedcomponent.view.adapter.viewholder.post.image

import android.view.ViewTreeObserver
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXMedia
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.BasePostViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.post.image.ImagePostModel
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingModel
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.Product
import kotlinx.android.synthetic.main.item_fc_post_image.view.*

/**
 * @author by milhamj on 04/12/18.
 */
class ImagePostViewHolder(private val listener: ImagePostListener) : BasePostViewHolder<ImagePostModel>() {

    override var layoutRes = R.layout.item_fc_post_image

    override fun bind(element: ImagePostModel) {
        itemView.image.setOnClickListener {
            listener.onImageClick(element.positionInFeed, pagerPosition, element.redirectLink)
            if (element.trackingList.isNotEmpty()) {
                listener.onAffiliateTrackClicked(element.trackingList, true)
            }
        }
        itemView.image.viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val viewTreeObserver = itemView.image.viewTreeObserver
                    viewTreeObserver.removeOnGlobalLayoutListener(this)

                    itemView.image.maxHeight = itemView.image.width
                    itemView.image.requestLayout()
                }
            }
        )
        itemView.image.loadImage(element.image)
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
                shopId: String,
                postPosition: Int,
                cpmData: CpmData,
                products: List<Product>
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
            isFollowed: Boolean = false,
            productList: List<FeedXProduct>)


        fun onImageClick(positionInFeed: Int, contentPosition: Int, redirectLink: String)

        fun onAffiliateTrackClicked(trackList: List<TrackingModel>, isClick: Boolean)
    }
}
