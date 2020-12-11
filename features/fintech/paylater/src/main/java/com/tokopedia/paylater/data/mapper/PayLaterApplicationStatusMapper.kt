package com.tokopedia.paylater.data.mapper

import com.tokopedia.paylater.domain.model.PayLaterApplicationDetail

const val STATUS_APPROVED = "Approved"
const val STATUS_WAITING = "Waiting"
const val STATUS_REJECTED = "Rejected"
const val STATUS_ACTIVE = "Active"
const val STATUS_SUSPENDED = "Suspended"
const val STATUS_EXPIRED = "Expired"
const val STATUS_FAILED = "Failed"
const val STATUS_CANCELLED = "Cancelled"


sealed class PayLaterApplicationStatus(val tag: String)
object PayLaterStatusApproved: PayLaterApplicationStatus(STATUS_APPROVED)
object PayLaterStatusWaiting: PayLaterApplicationStatus(STATUS_WAITING)
object PayLaterStatusRejected: PayLaterApplicationStatus(STATUS_REJECTED)
object PayLaterStatusActive: PayLaterApplicationStatus(STATUS_ACTIVE)
object PayLaterStatusSuspended: PayLaterApplicationStatus(STATUS_SUSPENDED)
object PayLaterStatusExpired: PayLaterApplicationStatus(STATUS_EXPIRED)
object PayLaterStatusFailed: PayLaterApplicationStatus(STATUS_FAILED)
object PayLaterStatusCancelled: PayLaterApplicationStatus(STATUS_CANCELLED)
object PayLaterApplicationStatusMapper {

    fun getApplicationStatusType(payLaterApplicationStatus: PayLaterApplicationDetail): PayLaterApplicationStatus {
        return when (payLaterApplicationStatus.payLaterApplicationStatus) {
            STATUS_APPROVED -> PayLaterStatusApproved
            STATUS_WAITING -> PayLaterStatusWaiting
            STATUS_REJECTED -> PayLaterStatusRejected
            STATUS_ACTIVE -> PayLaterStatusActive
            STATUS_SUSPENDED -> PayLaterStatusSuspended
            STATUS_EXPIRED -> PayLaterStatusSuspended
            STATUS_FAILED -> PayLaterStatusExpired
            else -> PayLaterStatusCancelled
        }
    }
}