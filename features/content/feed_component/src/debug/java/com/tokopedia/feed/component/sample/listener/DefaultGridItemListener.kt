package com.tokopedia.feed.component.sample.listener

import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostAdapter
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingModel

class DefaultGridItemListener : GridPostAdapter.GridItemListener {
    override fun onGridItemClick(
        positionInFeed: Int,
        activityId: String,
        productId: String,
        redirectLink: String,
        type: String,
        isFollowed: Boolean,
        shopId: String,
        hasVoucher: Boolean,
        feedXProducts: List<FeedXProduct>,
        index: Int
    ) {

    }

    override fun onAffiliateTrackClicked(trackList: List<TrackingModel>, isClick: Boolean) {
    }
}
