package com.tokopedia.feedplus.domain.mapper

import com.tokopedia.feedplus.presentation.model.FeedCardImageContentModel
import com.tokopedia.feedplus.presentation.model.FeedCardLivePreviewContentModel
import com.tokopedia.feedplus.presentation.model.FeedCardVideoContentModel
import com.tokopedia.feedplus.presentation.model.FeedFollowRecommendationModel
import com.tokopedia.feedplus.presentation.model.FeedTrackerDataModel
import com.tokopedia.feedplus.presentation.model.type.AuthorType

/**
 * Created By : Muhammad Furqan on 03/05/23
 */
class MapperFeedModelToTrackerDataModel(
    val tabType: String,
    val entryPoint: String,
    val entrySource: FeedEntrySource,
) {

    data class FeedEntrySource(
        val categoryId: String,
        val entryPoint: String,
    )

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
            entryPoint = entryPoint,
            entrySource = entrySource,
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
            entryPoint = entryPoint,
            entrySource = entrySource,
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
            entryPoint = entryPoint,
            entrySource = entrySource,
        )

    fun transformProfileRecommendationToTrackerModel(
        profile: FeedFollowRecommendationModel.Profile
    ) = FeedTrackerDataModel(
        activityId = "",
        authorId = profile.id,
        tabType = tabType,
        typename = "",
        type = "",
        authorType = if (profile.isShop) AuthorType.Shop else AuthorType.User,
        mediaType = "",
        isFollowing = profile.isFollowed,
        contentScore = "",
        hasVoucher = false,
        campaignStatus = "",
        entryPoint = entryPoint,
        entrySource = entrySource,
    )
}
