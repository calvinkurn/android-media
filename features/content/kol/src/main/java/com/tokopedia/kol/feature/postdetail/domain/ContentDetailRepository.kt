package com.tokopedia.kol.feature.postdetail.domain

import com.tokopedia.feedcomponent.data.feedrevamp.FeedASGCUpcomingReminderStatus
import com.tokopedia.feedcomponent.people.model.MutationUiModel
import com.tokopedia.kol.feature.postdetail.view.datamodel.*
import com.tokopedia.kol.feature.postdetail.view.datamodel.type.ContentLikeAction
import com.tokopedia.kol.feature.postdetail.view.datamodel.type.ShopFollowAction
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response

/**
 * Created by meyta.taliti on 02/08/22.
 */
interface ContentDetailRepository {

    suspend fun getContentDetail(
        contentId: String
    ): ContentDetailUiModel

    suspend fun getContentRecommendation(
        activityId: String,
        cursor: String,
    ): ContentDetailUiModel

    suspend fun getFeedPosts(
        userID: String,
        cursor: String,
        limit: Int,
    ): ContentDetailUiModel

    suspend fun likeContent(
        contentId: String,
        action: ContentLikeAction,
        rowNumber: Int,
    ): LikeContentModel

    suspend fun followShop(
        shopId: String,
        action: ShopFollowAction,
        rowNumber: Int,
        isFollowedFromRSRestrictionBottomSheet: Boolean = false
    ): ShopFollowModel

    suspend fun followUnfollowUser(
        isFollow: Boolean,
        encryptedUserId: String,
    ): MutationUiModel

    suspend fun addToCart(
        productId: String,
        productName: String,
        price: String,
        shopId: String,
    ): Boolean

    suspend fun addToWishlist(
        rowNumber: Int,
        productId: String
    ): WishlistContentModel

    suspend fun deleteContent(
        contentId: String,
        rowNumber: Int,
    ): DeleteContentModel

    suspend fun reportContent(
        contentId: String,
        reasonType: String,
        reasonMessage: String,
        rowNumber: Int,
    ): ReportContentModel

    suspend fun trackVisitChannel(
        channelId: String,
        rowNumber: Int,
    ): VisitContentModel

    suspend fun trackViewer(
        contentId: String,
        rowNumber: Int,
    ): VisitContentModel

    suspend fun checkUpcomingCampaign(
        campaignId: Long
    ): Boolean

    suspend fun subscribeUpcomingCampaign(
        campaignId: Long,
        reminderType: FeedASGCUpcomingReminderStatus
    ): Pair<Boolean, String>
}
