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

        private const val UPCOMING_STATUS = "upcoming"
        private const val PRE_STATUS = "pre"
    }

    fun transformVideoContentToTrackerModel(
        model: FeedCardVideoContentModel
    ) =
        FeedTrackerDataModel(
            activityId = model.id,
            authorId = if (model.author.type.isShop) {
                model.products.firstOrNull()?.shopId.orEmpty()
            } else {
                model.author.id
            },
            tabType = tabType,
            typename = model.typename,
            type = model.type,
            authorType = model.author.type,
            mediaType = model.media.firstOrNull()?.type ?: "",
            isFollowing = model.followers.isFollowed,
            contentScore = model.contentScore,
            hasVoucher = model.hasVoucher,
            campaignStatus = model.campaign.status
                .let {
                    when (it.lowercase()) {
                        UPCOMING_STATUS -> PRE_STATUS
                        "" -> DEFAULT_CAMPAIGN_NO_STATUS
                        else -> it
                    }
                },
            entryPoint = entryPoint
        )

    fun transformImageContentToTrackerModel(
        model: FeedCardImageContentModel
    ) =
        FeedTrackerDataModel(
            activityId = model.id,
            authorId = if (model.author.type.isShop) {
                model.products.firstOrNull()?.shopId.orEmpty()
            } else {
                model.author.id
            },
            tabType = tabType,
            typename = model.typename,
            type = model.type,
            authorType = model.author.type,
            mediaType = model.media.firstOrNull()?.type ?: "",
            isFollowing = model.followers.isFollowed,
            contentScore = model.contentScore,
            hasVoucher = model.hasVoucher,
            campaignStatus = model.campaign.status
                .let {
                    when (it.lowercase()) {
                        UPCOMING_STATUS -> PRE_STATUS
                        "" -> DEFAULT_CAMPAIGN_NO_STATUS
                        else -> it
                    }
                },
            entryPoint = entryPoint
        )

    fun transformLiveContentToTrackerModel(
        model: FeedCardLivePreviewContentModel
    ) =
        FeedTrackerDataModel(
            activityId = model.id,
            authorId = if (model.author.type.isShop) {
                model.products.firstOrNull()?.shopId.orEmpty()
            } else {
                model.author.id
            },
            tabType = tabType,
            typename = model.typename,
            type = model.type,
            authorType = model.author.type,
            mediaType = model.media.firstOrNull()?.type ?: "",
            isFollowing = model.followers.isFollowed,
            contentScore = model.contentScore,
            hasVoucher = model.hasVoucher,
            campaignStatus = model.campaign.status
                .let {
                    when (it.lowercase()) {
                        UPCOMING_STATUS -> PRE_STATUS
                        "" -> DEFAULT_CAMPAIGN_NO_STATUS
                        else -> it
                    }
                },
            entryPoint = entryPoint
        )
}
