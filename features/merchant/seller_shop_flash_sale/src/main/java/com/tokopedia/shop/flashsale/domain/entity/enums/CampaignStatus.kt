package com.tokopedia.shop.flashsale.domain.entity.enums

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

const val CAMPAIGN_STATUS_ID_DRAFT = 2
const val CAMPAIGN_STATUS_ID_IN_SUBMISSION = 4
const val CAMPAIGN_STATUS_ID_IN_REVIEW = 5
const val CAMPAIGN_STATUS_ID_READY = 6
const val CAMPAIGN_STATUS_ID_ONGOING = 7
const val CAMPAIGN_STATUS_ID_READY_LOCKED = 14


const val CAMPAIGN_STATUS_ID_FINISHED = 8
const val CAMPAIGN_STATUS_PUBLISH_CANCELLED = 9
const val CAMPAIGN_STATUS_SUBMISSION_CANCELLED = 10
const val CAMPAIGN_STATUS_REVIEW_CANCELLED = 11
const val CAMPAIGN_STATUS_READY_CANCELLED = 12
const val CAMPAIGN_STATUS_ID_ONGOING_CANCELLATION = 13
const val CAMPAIGN_STATUS_ID_CANCELLED = 15

@Parcelize
enum class CampaignStatus(val id: Int) : Parcelable {
    DRAFT(CAMPAIGN_STATUS_ID_DRAFT),

    IN_SUBMISSION(CAMPAIGN_STATUS_ID_IN_SUBMISSION),
    IN_REVIEW(CAMPAIGN_STATUS_ID_IN_REVIEW),
    READY(CAMPAIGN_STATUS_ID_READY),
    READY_LOCKED(CAMPAIGN_STATUS_ID_READY_LOCKED),
    ONGOING(CAMPAIGN_STATUS_ID_ONGOING),

    FINISHED(CAMPAIGN_STATUS_ID_FINISHED),
    PUBLISHED_CANCELLED(CAMPAIGN_STATUS_PUBLISH_CANCELLED),
    SUBMISSION_CANCELLED(CAMPAIGN_STATUS_SUBMISSION_CANCELLED),
    REVIEW_CANCELLED(CAMPAIGN_STATUS_REVIEW_CANCELLED),
    READY_CANCELLED(CAMPAIGN_STATUS_READY_CANCELLED),
    ONGOING_CANCELLATION(CAMPAIGN_STATUS_ID_ONGOING_CANCELLATION),
    CANCELLED(CAMPAIGN_STATUS_ID_CANCELLED)
}

fun CampaignStatus.isDraft() : Boolean {
    return this == CampaignStatus.DRAFT
}

fun CampaignStatus.isUpcoming(): Boolean {
    return this == CampaignStatus.READY || this == CampaignStatus.READY_LOCKED
}

fun CampaignStatus.isOngoing(): Boolean {
    return this == CampaignStatus.ONGOING
}

fun CampaignStatus.isActive(): Boolean {
    return this == CampaignStatus.IN_SUBMISSION || this == CampaignStatus.IN_REVIEW || this == CampaignStatus.READY || this == CampaignStatus.READY_LOCKED || this == CampaignStatus.ONGOING
}

fun CampaignStatus.isAvailable(): Boolean {
    return this == CampaignStatus.IN_SUBMISSION || this == CampaignStatus.IN_REVIEW
}

fun CampaignStatus.isFinished(): Boolean {
    return this == CampaignStatus.FINISHED
}

fun CampaignStatus.isCancelled(): Boolean {
    return this == CampaignStatus.PUBLISHED_CANCELLED || this == CampaignStatus.SUBMISSION_CANCELLED || this == CampaignStatus.REVIEW_CANCELLED || this == CampaignStatus.READY_CANCELLED || this == CampaignStatus.ONGOING_CANCELLATION || this == CampaignStatus.CANCELLED
}

val activeCampaignStatusIds = listOf(
    CampaignStatus.IN_SUBMISSION.id,
    CampaignStatus.IN_REVIEW.id,
    CampaignStatus.READY.id,
    CampaignStatus.ONGOING.id,
    CampaignStatus.READY_LOCKED.id
)
