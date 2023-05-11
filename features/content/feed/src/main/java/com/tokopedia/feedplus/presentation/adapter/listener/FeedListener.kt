package com.tokopedia.feedplus.presentation.adapter.listener

import com.tokopedia.content.common.report_content.model.FeedContentData
import com.tokopedia.feedcomponent.view.widget.FeedExoPlayer
import com.tokopedia.feedplus.presentation.model.FeedAuthorModel
import com.tokopedia.feedplus.presentation.model.FeedCardCampaignModel
import com.tokopedia.feedplus.presentation.model.FeedCardProductModel
import com.tokopedia.feedplus.presentation.model.FeedTrackerDataModel

interface FeedListener {
    fun onMenuClicked(id: String,
                      editable: Boolean,
                      deletable: Boolean,
                      reportable: Boolean,
                      contentData: FeedContentData,
                      trackerModel: FeedTrackerDataModel)
    fun onFollowClicked(
        id: String,
        encryptedId: String,
        isShop: Boolean,
        trackerData: FeedTrackerDataModel?
    )

    fun changeTab()
    fun reload()
    fun onReminderClicked(
        campaignId: Long,
        setReminder: Boolean,
        trackerModel: FeedTrackerDataModel?
    )

    fun onTimerFinishUpcoming()
    fun onTimerFinishOnGoing()
    fun onTopAdsImpression(
        adViewUrl: String,
        id: String,
        shopId: String,
        uri: String,
        fullEcs: String?,
        position: Int
    )

    fun onProductTagButtonClicked(
        postId: String,
        author: FeedAuthorModel,
        postType: String,
        isFollowing: Boolean,
        campaign: FeedCardCampaignModel,
        hasVoucher: Boolean,
        products: List<FeedCardProductModel>,
        trackerModel: FeedTrackerDataModel?,
        positionInFeed: Int
    )

    fun onProductTagViewClicked(
        postId: String,
        author: FeedAuthorModel,
        postType: String,
        isFollowing: Boolean,
        campaign: FeedCardCampaignModel,
        hasVoucher: Boolean,
        products: List<FeedCardProductModel>,
        totalProducts: Int,
        trackerModel: FeedTrackerDataModel?,
        positionInFeed: Int
    )

    fun onOngoingCampaignClicked(
        postId: String,
        author: FeedAuthorModel,
        postType: String,
        isFollowing: Boolean,
        campaign: FeedCardCampaignModel,
        hasVoucher: Boolean,
        products: List<FeedCardProductModel>,
        trackerModel: FeedTrackerDataModel?,
        positionInFeed: Int
    )

    fun onSharePostClicked(
        id: String,
        authorName: String,
        applink: String,
        weblink: String,
        imageUrl: String
    )

    fun onLikePostCLicked(
        id: String,
        isLiked: Boolean,
        rowNumber: Int,
        trackerModel: FeedTrackerDataModel,
        isDoubleClick: Boolean
    )

    /**
     * Video
     */
    fun getVideoPlayer(id: String): FeedExoPlayer

    fun detachPlayer(player: FeedExoPlayer)
    fun onPauseVideoPost(trackerModel: FeedTrackerDataModel)
    fun onTapHoldSeekbarVideoPost(trackerModel: FeedTrackerDataModel)
    fun onWatchPostVideo(trackerModel: FeedTrackerDataModel)

    fun onSwipeMultiplePost(trackerModel: FeedTrackerDataModel)

    fun onAuthorNameClicked(trackerModel: FeedTrackerDataModel?)
    fun onAuthorProfilePictureClicked(trackerModel: FeedTrackerDataModel?)
    fun onCaptionClicked(trackerModel: FeedTrackerDataModel?)

    fun onLivePreviewClicked(
        trackerModel: FeedTrackerDataModel?,
        positionInFeed: Int,
        productId: String,
        authorName: String
    )

    fun onPostImpression(
        trackerModel: FeedTrackerDataModel?,
        activityId: String,
        positionInFeed: Int
    )

}
