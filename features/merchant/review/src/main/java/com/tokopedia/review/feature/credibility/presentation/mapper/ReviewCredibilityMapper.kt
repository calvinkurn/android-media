package com.tokopedia.review.feature.credibility.presentation.mapper

import com.tokopedia.review.feature.credibility.data.ReviewerCredibilityLabel
import com.tokopedia.review.feature.credibility.data.ReviewerCredibilityStat
import com.tokopedia.review.feature.credibility.data.ReviewerCredibilityStatsWrapper
import com.tokopedia.review.feature.credibility.presentation.uimodel.ReviewCredibilityAchievementBoxUiModel
import com.tokopedia.review.feature.credibility.presentation.uimodel.ReviewCredibilityFooterUiModel
import com.tokopedia.review.feature.credibility.presentation.uimodel.ReviewCredibilityHeaderUiModel
import com.tokopedia.review.feature.credibility.presentation.uimodel.ReviewCredibilityStatisticBoxUiModel

object ReviewCredibilityMapper {

    private fun mapReviewCredibilityResponseToReviewCredibilityAchievementUiModel(
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

    private fun mapReviewCredibilityResponseToReviewCredibilityAchievementCtaUiModel(
        label: ReviewerCredibilityLabel
    ): ReviewCredibilityAchievementBoxUiModel.Button {
        return ReviewCredibilityAchievementBoxUiModel.Button(
            text = label.totalAchievementFmt.orEmpty(),
            appLink = label.achievementListLink.orEmpty()
        )
    }

    private fun mapReviewCredibilityResponseToReviewCredibilityStatisticUiModel(
        stats: List<ReviewerCredibilityStat>
    ): List<ReviewCredibilityStatisticBoxUiModel.ReviewCredibilityStatisticUiModel> {
        return stats.map {
            ReviewCredibilityStatisticBoxUiModel.ReviewCredibilityStatisticUiModel(
                icon = it.imageURL, title = it.title, count = it.countFmt
            )
        }
    }

    private fun mapReviewCredibilityResponseToReviewCredibilityFooterButtonUiModel(
        label: ReviewerCredibilityLabel
    ): ReviewCredibilityFooterUiModel.Button {
        return ReviewCredibilityFooterUiModel.Button(
            text = label.ctaText, appLink = label.applink
        )
    }

    fun mapReviewCredibilityResponseToReviewCredibilityHeaderUiModel(
        response: ReviewerCredibilityStatsWrapper
    ): ReviewCredibilityHeaderUiModel {
        return ReviewCredibilityHeaderUiModel(
            reviewerName = response.label.userName,
            reviewerJoinDate = response.label.joinDate,
            showReviewerJoinDate = response.label.achievements.isNullOrEmpty()
        )
    }

    fun mapReviewCredibilityResponseToReviewCredibilityAchievementBoxUiModel(
        response: ReviewerCredibilityStatsWrapper
    ): ReviewCredibilityAchievementBoxUiModel {
        return ReviewCredibilityAchievementBoxUiModel(
            title = response.label.name,
            label = response.label.subLabel,
            achievements = mapReviewCredibilityResponseToReviewCredibilityAchievementUiModel(
                response.label
            ),
            cta = mapReviewCredibilityResponseToReviewCredibilityAchievementCtaUiModel(
                response.label
            )
        )
    }

    fun mapReviewCredibilityResponseToReviewCredibilityStatisticBoxUiModel(
        response: ReviewerCredibilityStatsWrapper
    ): ReviewCredibilityStatisticBoxUiModel {
        return ReviewCredibilityStatisticBoxUiModel(
            title = response.label.subtitle,
            statistics = mapReviewCredibilityResponseToReviewCredibilityStatisticUiModel(
                response.stats
            )
        )
    }

    fun mapReviewCredibilityResponseToReviewCredibilityFooterUiModel(
        response: ReviewerCredibilityStatsWrapper
    ): ReviewCredibilityFooterUiModel {
        return ReviewCredibilityFooterUiModel(
            description = response.label.footer,
            button = mapReviewCredibilityResponseToReviewCredibilityFooterButtonUiModel(
                response.label
            )
        )
    }
}