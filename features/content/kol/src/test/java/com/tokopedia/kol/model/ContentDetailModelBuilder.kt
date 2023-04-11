package com.tokopedia.kol.model

import com.tokopedia.feedcomponent.data.feedrevamp.FeedASGCUpcomingReminderStatus
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.feedcomponent.people.model.MutationUiModel
import com.tokopedia.feedcomponent.view.viewmodel.responsemodel.FeedAsgcCampaignResponseModel
import com.tokopedia.kol.feature.postdetail.view.datamodel.*
import com.tokopedia.kol.feature.postdetail.view.datamodel.type.ContentLikeAction
import com.tokopedia.kol.feature.postdetail.view.datamodel.type.ShopFollowAction

/**
 * Created by meyta.taliti on 07/08/22.
 */
class ContentDetailModelBuilder {

    fun getContentDetail(
        contents: List<FeedXCard> = emptyList(),
        cursor: String = ""
    ) = ContentDetailUiModel(
        postList = contents,
        cursor = cursor,
    )

    fun getLikeContentModel(
        likeAction: ContentLikeAction,
        rowNumber: Int = 0,
    ) = LikeContentModel(
        rowNumber = rowNumber,
        action = likeAction
    )

    fun getShopFollowModel(
        followAction: ShopFollowAction,
        rowNumber: Int = 0,
    ) = ShopFollowModel(
        rowNumber = rowNumber,
        action = followAction,
    )

    fun getUserFollowUnfollowModel(
        isFollow: Boolean,
        currentPosition: Int,
    ) = UGCFollowUnfollowModel(
        isFollow = isFollow,
        currentPosition = currentPosition
    )

    fun buildMutationSuccess(
        message: String = "Yeay",
    ) = MutationUiModel.Success(message)

    fun buildMutationError(
        message: String = "Terjadi kesalahan",
    ) = MutationUiModel.Error(message)

    fun getVisitContentModel(rowNumber: Int = 0) = VisitContentModel(rowNumber)

    fun getDeleteContentModel(rowNumber: Int = 0) = DeleteContentModel(rowNumber)

    fun getWishlistModel(
        rowNumber: Int = 0,
        productId: String = ""
    ) = WishlistContentModel(
        rowNumber = rowNumber,
        productId = productId
    )

    fun getReportContentModel(rowNumber: Int = 0) = ReportContentModel(rowNumber)

    fun getCheckCampaignResponse(campaignId: Long, rowNumber: Int, isAvailable: Boolean) =
        FeedAsgcCampaignResponseModel(
            campaignId = campaignId,
            rowNumber = rowNumber,
            reminderStatus = if (isAvailable) FeedASGCUpcomingReminderStatus.On(campaignId) else FeedASGCUpcomingReminderStatus.Off(
                campaignId
            )
        )

    fun getSetUnsetCampaignResponse(campaignId: Long, rowNumber: Int, reminderStatus: FeedASGCUpcomingReminderStatus) =
        FeedAsgcCampaignResponseModel(
            campaignId = campaignId,
            rowNumber = rowNumber,
            reminderStatus = reminderStatus
            )
}
