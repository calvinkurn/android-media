package com.tokopedia.feedplus.domain.mapper

import com.tokopedia.feedplus.presentation.model.FeedCardImageContentModel
import com.tokopedia.feedplus.presentation.model.FeedCardLivePreviewContentModel
import com.tokopedia.feedplus.presentation.model.FeedCardVideoContentModel
import com.tokopedia.feedplus.presentation.model.FeedTrackerDataModel

/**
 * Created By : Muhammad Furqan on 03/05/23
 */
class MapperFeedModelToTrackerDataModel(
    val tabType: String,
    val entryPoint: String
) {

    companion object {
        private const val DEFAULT_CAMPAIGN_NO_STATUS = "no"
    }

    fun transformVideoContentToTrackerModel(
        model: FeedCardVideoContentModel
    ) =
        FeedTrackerDataModel(
            activityId = model.id,
            authorId = if (model.author.isShop) model.products.firstOrNull()?.shopId
                ?: "" else model.author.id,
            tabType = tabType,
            typename = model.typename,
            type = model.type,
            authorType = model.author.type,
            mediaType = model.media.firstOrNull()?.type ?: "",
            isFollowing = model.followers.isFollowed,
            contentScore = model.contentScore,
            hasVoucher = model.hasVoucher,
            campaignStatus = model.campaign.status.ifEmpty { DEFAULT_CAMPAIGN_NO_STATUS },
            entryPoint = entryPoint
        )

    fun transformImageContentToTrackerModel(
        model: FeedCardImageContentModel
    ) =
        FeedTrackerDataModel(
            activityId = model.id,
            authorId = if (model.author.isShop) model.products.firstOrNull()?.shopId
                ?: "" else model.author.id,
            tabType = tabType,
            typename = model.typename,
            type = model.type,
            authorType = model.author.type,
            mediaType = model.media.firstOrNull()?.type ?: "",
            isFollowing = model.followers.isFollowed,
            contentScore = model.contentScore,
            hasVoucher = model.hasVoucher,
            campaignStatus = model.campaign.status.ifEmpty { DEFAULT_CAMPAIGN_NO_STATUS },
            entryPoint = entryPoint
        )


    fun transformLiveContentToTrackerModel(
        model: FeedCardLivePreviewContentModel
    ) =
        FeedTrackerDataModel(
            activityId = model.id,
            authorId = if (model.author.isShop) model.products.firstOrNull()?.shopId
                ?: "" else model.author.id,
            tabType = tabType,
            typename = model.typename,
            type = model.type,
            authorType = model.author.type,
            mediaType = model.media.firstOrNull()?.type ?: "",
            isFollowing = model.followers.isFollowed,
            contentScore = model.contentScore,
            hasVoucher = model.hasVoucher,
            campaignStatus = model.campaign.status.ifEmpty { DEFAULT_CAMPAIGN_NO_STATUS },
            entryPoint = entryPoint
        )

}
