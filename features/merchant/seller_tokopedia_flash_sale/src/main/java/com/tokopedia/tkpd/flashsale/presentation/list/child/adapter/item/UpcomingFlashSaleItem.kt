package com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.item

import com.tokopedia.campaign.components.adapter.DelegateAdapterItem
import java.util.Date

data class UpcomingFlashSaleItem(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val remainingQuota: Int,
    val maxProductSubmission: Int,
    val formattedStartDate: String,
    val formattedEndDate: String,
    val endDate : Date,
    val status: Status,
    val quotaUsagePercentage: Int,
    val distanceDaysToReviewStartDate: Int,
    val reviewStartDate: Date
) : DelegateAdapterItem {
    override fun id() = id

    enum class Status {
        REGISTRATION_OPEN,
        REGISTRATION_CLOSED
    }
}
