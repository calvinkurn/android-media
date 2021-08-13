package com.tokopedia.pdpsimulation.paylater.mapper

import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterApplicationDetail

const val STATUS_APPROVED = "Approved"
const val STATUS_WAITING = "Waiting"
const val STATUS_REJECTED = "Rejected"
const val STATUS_ACTIVE = "Active"
const val STATUS_SUSPENDED = "Suspended"
const val STATUS_EXPIRED = "Expired"
const val STATUS_FAILED = "Failed"
const val STATUS_CANCELLED = "Cancelled"
const val STATUS_EMPTY = "Empty"


sealed class PayLaterApplicationStatus(val status: String)
object PayLaterStatusApproved : PayLaterApplicationStatus(STATUS_APPROVED)
object PayLaterStatusWaiting : PayLaterApplicationStatus(STATUS_WAITING)
object PayLaterStatusRejected : PayLaterApplicationStatus(STATUS_REJECTED)
object PayLaterStatusActive : PayLaterApplicationStatus(STATUS_ACTIVE)
object PayLaterStatusSuspended : PayLaterApplicationStatus(STATUS_SUSPENDED)
object PayLaterStatusExpired : PayLaterApplicationStatus(STATUS_EXPIRED)
object PayLaterStatusFailed : PayLaterApplicationStatus(STATUS_FAILED)
object PayLaterStatusCancelled : PayLaterApplicationStatus(STATUS_CANCELLED)
object PayLaterStatusEmpty : PayLaterApplicationStatus(STATUS_EMPTY)
object PayLaterApplicationStatusMapper {

    fun getApplicationStatusType(payLaterApplicationStatus: PayLaterApplicationDetail): PayLaterApplicationStatus {
        return when (payLaterApplicationStatus.payLaterApplicationStatus) {
            STATUS_APPROVED -> PayLaterStatusApproved
            STATUS_WAITING -> PayLaterStatusWaiting
            STATUS_REJECTED -> PayLaterStatusRejected
            STATUS_ACTIVE -> PayLaterStatusActive
            STATUS_SUSPENDED -> PayLaterStatusSuspended
            STATUS_EXPIRED -> PayLaterStatusExpired
            STATUS_FAILED -> PayLaterStatusFailed
            STATUS_CANCELLED -> PayLaterStatusCancelled
            else -> PayLaterStatusEmpty
        }
    }
}