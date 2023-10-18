package com.tokopedia.feed.component.sample.listener

import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.video.VideoViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingModel

class DefaultVideoViewListener : VideoViewHolder.VideoViewListener {
    override fun onVideoPlayerClicked(
        positionInFeed: Int,
        contentPosition: Int,
        postId: String,
        redirectUrl: String,
        authorId: String,
        authorType: String,
        isFollowed: Boolean,
        startTime: Long
    ) {
        
    }

    override fun onVideoStopTrack(feedXCard: FeedXCard, duration: Long) {
        
    }

    override fun onAffiliateTrackClicked(trackList: List<TrackingModel>, isClick: Boolean) {
        
    }
}
