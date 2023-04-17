package com.tokopedia.feed.component.sample.listener

import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.FollowCta
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.post.TrackingPostModel
import com.tokopedia.feedcomponent.view.viewmodel.track.TrackingModel

class DefaultDynamicPostListener : DynamicPostViewHolder.DynamicPostListener {

    override fun onAvatarClick(
        positionInFeed: Int,
        redirectUrl: String,
        activityId: String,
        activityName: String,
        followCta: FollowCta,
        type: String,
        isFollowed: Boolean,
        shopId: String,
        mediaType: String,
        isCaption: Boolean
    ) {
        
    }

    override fun onHeaderActionClick(
        positionInFeed: Int,
        id: String,
        type: String,
        isFollow: Boolean,
        postType: String,
        isVideo: Boolean,
        isBottomSheetMenuOnFeed: Boolean,
        isFollowedFromFollowRestrictionBottomSheet: Boolean
    ) {
    }

    override fun onMenuClick(
        positionInFeed: Int,
        postId: String,
        reportable: Boolean,
        deletable: Boolean,
        editable: Boolean,
        isFollowed: Boolean,
        authorId: String,
        authorType: String,
        postType: String,
        mediaType: String,
        caption: String,
        playChannelId: String
    ) {
        
    }

    override fun onCaptionClick(positionInFeed: Int, redirectUrl: String) {
        
    }

    override fun onLikeClick(
        positionInFeed: Int,
        id: Long,
        isLiked: Boolean,
        postType: String,
        isFollowed: Boolean,
        type: Boolean,
        shopId: String,
        mediaType: String?,
        playChannelId: String,
        authorType: String
    ) {
        
    }

    override fun onCommentClick(
        positionInFeed: Int,
        id: String,
        authorType: String,
        type: String,
        isFollowed: Boolean,
        mediaType: String,
        shopId: String,
        playChannelId: String,
        isClickIcon: Boolean
    ) {
        
    }

    override fun onStatsClick(
        title: String,
        activityId: String,
        productIds: List<String>,
        likeCount: Int,
        commentCount: Int
    ) {
        
    }

    override fun onShareClick(
        positionInFeed: Int,
        id: String,
        title: String,
        description: String,
        url: String,
        imageUrl: String,
        postTypeASGC: Boolean,
        type: String,
        isFollowed: Boolean,
        shopId: String,
        mediaType: String,
        isTopads: Boolean,
        playChannelId: String,
        weblink: String
    ) {
        
    }

    override fun onFooterActionClick(positionInFeed: Int, redirectUrl: String) {
        
    }

    override fun onPostTagItemClick(
        positionInFeed: Int,
        redirectUrl: String,
        postTagItem: PostTagItem,
        itemPosition: Int
    ) {
        
    }

    override fun onFullScreenCLick(
        feedXCard: FeedXCard,
        positionInFeed: Int,
        redirectUrl: String,
        currentTime: Long,
        shouldTrack: Boolean,
        isFullScreenButton: Boolean
    ) {
        
    }

    override fun addVODView(
        feedXCard: FeedXCard,
        playChannelId: String,
        rowNumber: Int,
        time: Long,
        hitTrackerApi: Boolean
    ) {
        
    }

    override fun sendWatchVODTracker(
        feedXCard: FeedXCard,
        playChannelId: String,
        rowNumber: Int,
        time: Long
    ) {
        
    }

    override fun onPostTagBubbleClick(
        positionInFeed: Int,
        redirectUrl: String,
        postTagItem: FeedXProduct,
        adClickUrl: String
    ) {
        
    }

    override fun onAffiliateTrackClicked(trackList: List<TrackingModel>, isClick: Boolean) {
        
    }

    override fun onPostTagItemBuyClicked(
        positionInFeed: Int,
        postTagItem: PostTagItem,
        authorType: String
    ) {
        
    }

    override fun onHashtagClicked(hashtagText: String, trackingPostModel: TrackingPostModel) {
        
    }

    override fun onReadMoreClicked(trackingPostModel: TrackingPostModel) {
        
    }

    override fun onReadMoreClicked(card: FeedXCard, positionInFeed: Int) {
        
    }

    override fun onImageClicked(card: FeedXCard) {
        
    }

    override fun onTagClicked(
        card: FeedXCard,
        products: List<FeedXProduct>,
        listener: DynamicPostViewHolder.DynamicPostListener,
        mediaType: String,
        positionInFeed: Int
    ) {
        
    }

    override fun onIngatkanSayaBtnImpressed(card: FeedXCard, positionInFeed: Int) {
        
    }

    override fun onIngatkanSayaBtnClicked(card: FeedXCard, positionInFeed: Int) {
        
    }

    override fun changeUpcomingWidgetToOngoing(card: FeedXCard, positionInFeed: Int) {
        
    }

    override fun removeOngoingCampaignSaleWidget(card: FeedXCard, positionInFeed: Int) {
        
    }

    override fun muteUnmuteVideo(
        card: FeedXCard,
        mute: Boolean,
        positionInFeed: Int,
        mediaType: String
    ) {
        
    }

    override fun onImpressionTracking(feedXCard: FeedXCard, positionInFeed: Int) {
        
    }

    override fun onHashtagClickedFeed(hashtagText: String, feedXCard: FeedXCard) {
        
    }

    override fun onFollowClickAds(positionInFeed: Int, shopId: String, adId: String) {
        
    }

    override fun onClickSekSekarang(
        postId: String,
        shopId: String,
        type: String,
        isFollowed: Boolean,
        hasVoucher: Boolean,
        positionInFeed: Int,
        feedXCard: FeedXCard
    ) {
        
    }

}
