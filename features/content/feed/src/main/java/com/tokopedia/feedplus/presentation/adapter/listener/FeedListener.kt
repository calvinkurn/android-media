package com.tokopedia.feedplus.presentation.adapter.listener

import com.tokopedia.content.common.report_content.model.FeedMenuItem
import com.tokopedia.feedcomponent.view.widget.FeedExoPlayer
import com.tokopedia.feedplus.presentation.model.FeedAuthorModel
import com.tokopedia.feedplus.presentation.model.FeedCardCampaignModel
import com.tokopedia.feedplus.presentation.model.FeedCardProductModel
import com.tokopedia.feedplus.presentation.model.FeedCardVideoContentModel
import com.tokopedia.feedplus.presentation.model.FeedShareModel
import com.tokopedia.feedplus.presentation.model.FeedTopAdsTrackerDataModel
import com.tokopedia.feedplus.presentation.model.FeedTrackerDataModel
import com.tokopedia.feedplus.presentation.model.type.AuthorType
import com.tokopedia.feedplus.presentation.uiview.FeedCampaignRibbonType

interface FeedListener {
    fun onMenuClicked(
        id: String,
        menuItems: List<FeedMenuItem>,
        trackerModel: FeedTrackerDataModel
    )

    fun onProfileClicked(
        appLink: String,
        type: AuthorType,
    )

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
        trackerModel: FeedTrackerDataModel?,
        type: FeedCampaignRibbonType
    )

    fun onTopAdsImpression(topadsTrackerData: FeedTopAdsTrackerDataModel)

    fun onTopAdsClick(topadsTrackerData: FeedTopAdsTrackerDataModel)

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

    fun onASGCGeneralClicked(
        postId: String,
        author: FeedAuthorModel,
        postType: String,
        isFollowing: Boolean,
        campaign: FeedCardCampaignModel,
        hasVoucher: Boolean,
        products: List<FeedCardProductModel>,
        trackerModel: FeedTrackerDataModel?
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
        campaignName: String,
        positionInFeed: Int
    )

    fun onSharePostClicked(data: FeedShareModel, trackerModel: FeedTrackerDataModel)

    fun onLikePostCLicked(
        id: String,
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
    fun onWatchPostVideo(model: FeedCardVideoContentModel, trackerModel: FeedTrackerDataModel)

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

    fun onCommentClick(trackerModel: FeedTrackerDataModel?, contentId: String, isPlayContent: Boolean, rowNumber: Int)
}
