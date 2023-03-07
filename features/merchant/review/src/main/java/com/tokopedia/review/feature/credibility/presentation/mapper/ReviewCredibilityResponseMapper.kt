package com.tokopedia.review.feature.credibility.presentation.mapper

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.review.R
import com.tokopedia.review.feature.credibility.data.ReviewerCredibilityLabel
import com.tokopedia.review.feature.credibility.data.ReviewerCredibilityStat
import com.tokopedia.review.feature.credibility.data.ReviewerCredibilityStatsWrapper
import com.tokopedia.review.feature.credibility.presentation.uimodel.ReviewCredibilityAchievementBoxUiModel
import com.tokopedia.review.feature.credibility.presentation.uimodel.ReviewCredibilityFooterUiModel
import com.tokopedia.review.feature.credibility.presentation.uimodel.ReviewCredibilityHeaderUiModel
import com.tokopedia.review.feature.credibility.presentation.uimodel.ReviewCredibilityStatisticBoxUiModel
import com.tokopedia.reviewcommon.uimodel.StringRes
import com.tokopedia.webview.KEY_ALLOW_OVERRIDE


object ReviewCredibilityResponseMapper {

    private const val PREFIX_HTTP = "http"

    private fun toReviewCredibilityAchievementUiModel(
        label: ReviewerCredibilityLabel
    ): List<ReviewCredibilityAchievementBoxUiModel.ReviewCredibilityAchievementUiModel> {
        return label.achievements?.mapNotNull {
            if (it.image.isNullOrBlank() || it.name.isNullOrBlank() || it.color.isNullOrBlank() || it.mementoLink.isNullOrBlank()) {
                null
            } else {
                ReviewCredibilityAchievementBoxUiModel.ReviewCredibilityAchievementUiModel(
                    avatar = it.image,
                    name = it.name,
                    color = it.color,
                    mementoLink = it.mementoLink
                )
            }
        }.orEmpty()
    }

    private fun toReviewCredibilityAchievementCtaUiModel(
        label: ReviewerCredibilityLabel
    ): ReviewCredibilityAchievementBoxUiModel.Button {
        return ReviewCredibilityAchievementBoxUiModel.Button(
            text = label.totalAchievementFmt.orEmpty(),
            appLink = label.achievementListLink.orEmpty()
        )
    }

    private fun toReviewCredibilityStatisticUiModel(
        stats: List<ReviewerCredibilityStat>
    ): List<ReviewCredibilityStatisticBoxUiModel.ReviewCredibilityStatisticUiModel> {
        return stats.map {
            ReviewCredibilityStatisticBoxUiModel.ReviewCredibilityStatisticUiModel(
                icon = it.imageURL, title = it.title, count = it.countFmt
            )
        }
    }

    private fun toReviewCredibilityFooterButtonUiModel(
        label: ReviewerCredibilityLabel
    ): ReviewCredibilityFooterUiModel.Button {
        return ReviewCredibilityFooterUiModel.Button(
            text = label.ctaText, appLink = label.applink
        )
    }

    private fun mapProfileButtonUrl(buttonProfileLink: String?): String {
        return buttonProfileLink?.let {
            if (it.startsWith(PREFIX_HTTP)) {
                "${ApplinkConst.WEBVIEW}?${KEY_ALLOW_OVERRIDE}=false&url=$it"
            } else {
                it
            }
        }.orEmpty()
    }

    fun toReviewCredibilityHeaderUiModel(
        response: ReviewerCredibilityStatsWrapper,
        reviewerUserID: String,
        viewerUserId: String,
        productID: String,
        source: String
    ): ReviewCredibilityHeaderUiModel {
        return ReviewCredibilityHeaderUiModel(
            reviewerProfilePicture = response.userProfile?.profilePicture.orEmpty(),
            reviewerName = response.userProfile?.firstName.orEmpty(),
            reviewerJoinDate = response.userProfile?.joinDate.orEmpty(),
            reviewerProfileButtonText = response.userProfile?.buttonProfileText.orEmpty(),
            reviewerProfileButtonUrl = mapProfileButtonUrl(response.userProfile?.buttonProfileLink),
            trackingData = mapCredibilityHeaderTrackingData(reviewerUserID, viewerUserId, productID, source)
        )
    }

    private fun mapCredibilityHeaderTrackingData(
        reviewerUserID: String,
        viewerUserId: String,
        productID: String,
        source: String
    ): ReviewCredibilityHeaderUiModel.TrackingData {
        return ReviewCredibilityHeaderUiModel.TrackingData(
            reviewerUserId = reviewerUserID,
            viewerUserId = viewerUserId,
            productId = productID,
            pageSource = source
        )
    }

    fun toReviewCredibilityAchievementBoxUiModel(
        response: ReviewerCredibilityStatsWrapper
    ): ReviewCredibilityAchievementBoxUiModel {
        return ReviewCredibilityAchievementBoxUiModel(
            title = response.label.name,
            label = response.label.subLabel,
            achievements = toReviewCredibilityAchievementUiModel(
                response.label
            ),
            cta = toReviewCredibilityAchievementCtaUiModel(
                response.label
            )
        )
    }

    fun toReviewCredibilityStatisticBoxUiModel(
        response: ReviewerCredibilityStatsWrapper
    ): ReviewCredibilityStatisticBoxUiModel {
        return ReviewCredibilityStatisticBoxUiModel(
            title = response.label.subtitle,
            statistics = toReviewCredibilityStatisticUiModel(
                response.stats
            )
        )
    }

    fun toReviewCredibilityFooterUiModel(
        response: ReviewerCredibilityStatsWrapper
    ): ReviewCredibilityFooterUiModel {
        return ReviewCredibilityFooterUiModel(
            description = composeFooterDescription(response),
            button = toReviewCredibilityFooterButtonUiModel(
                response.label
            )
        )
    }

    private fun composeFooterDescription(response: ReviewerCredibilityStatsWrapper): StringRes {
        return StringRes(
            R.string.review_credibility_footer_format,
            listOf(response.label.footer, response.label.infoText)
        )
    }
}
