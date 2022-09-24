package com.tokopedia.review.feature.credibility.presentation.mapper

import com.tokopedia.review.R
import com.tokopedia.review.feature.credibility.data.ReviewerCredibilityLabel
import com.tokopedia.review.feature.credibility.data.ReviewerCredibilityStat
import com.tokopedia.review.feature.credibility.data.ReviewerCredibilityStatsWrapper
import com.tokopedia.review.feature.credibility.presentation.uimodel.ReviewCredibilityAchievementBoxUiModel
import com.tokopedia.review.feature.credibility.presentation.uimodel.ReviewCredibilityFooterUiModel
import com.tokopedia.review.feature.credibility.presentation.uimodel.ReviewCredibilityHeaderUiModel
import com.tokopedia.review.feature.credibility.presentation.uimodel.ReviewCredibilityStatisticBoxUiModel
import com.tokopedia.reviewcommon.uimodel.StringRes

object ReviewCredibilityResponseMapper {

    private fun toReviewCredibilityAchievementUiModel(
        label: ReviewerCredibilityLabel
    ): List<ReviewCredibilityAchievementBoxUiModel.ReviewCredibilityAchievementUiModel> {
        return label.achievements?.mapNotNull {
            if (it.image.isNullOrBlank() || it.name.isNullOrBlank() || it.total.isNullOrBlank() || it.color.isNullOrBlank() || it.mementoLink.isNullOrBlank()) {
                null
            } else {
                ReviewCredibilityAchievementBoxUiModel.ReviewCredibilityAchievementUiModel(
                    avatar = it.image,
                    name = it.name,
                    counter = it.total,
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

    fun toReviewCredibilityHeaderUiModel(
        response: ReviewerCredibilityStatsWrapper
    ): ReviewCredibilityHeaderUiModel {
        return ReviewCredibilityHeaderUiModel(
            reviewerName = response.label.userName,
            reviewerJoinDate = response.label.joinDate,
            showReviewerJoinDate = response.label.achievements.isNullOrEmpty()
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