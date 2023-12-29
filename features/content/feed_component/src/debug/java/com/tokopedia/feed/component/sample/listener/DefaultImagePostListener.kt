package com.tokopedia.feed.component.sample.listener

import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.ImagePostViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingModel

class DefaultImagePostListener : ImagePostViewHolder.ImagePostListener {
    override fun userImagePostImpression(positionInFeed: Int, contentPosition: Int) {

    }

    override fun userCarouselImpression(feedXCard: FeedXCard, positionInFeed: Int) {

    }

    override fun userGridPostImpression(
        positionInFeed: Int,
        activityId: String,
        postType: String,
        shopId: String,
        hasVoucher: Boolean
    ) {

    }

    override fun userProductImpression(
        positionInFeed: Int,
        activityId: String,
        postType: String,
        shopId: String,
        isFollowed: Boolean,
        productList: List<FeedXProduct>
    ) {

    }

    override fun onImageClick(
        postId: String,
        positionInFeed: Int,
        contentPosition: Int,
        redirectLink: String
    ) {

    }

    override fun onAffiliateTrackClicked(trackList: List<TrackingModel>, isClick: Boolean) {

    }
}
