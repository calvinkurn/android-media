package com.tokopedia.paylater.data.mapper

import com.tokopedia.paylater.R
import com.tokopedia.paylater.domain.model.PayLaterApplicationDetail
import com.tokopedia.paylater.domain.model.UserCreditApplicationStatus
import com.tokopedia.unifycomponents.Label

const val STATUS_APPROVED = "Approved"
const val STATUS_WAITING = "Waiting"
const val STATUS_REJECTED = "Rejected"
const val STATUS_ACTIVE = "Active"
const val STATUS_SUSPENDED = "Suspended"
const val STATUS_EXPIRED = "Expired"
const val STATUS_FAILED = "Failed"
const val STATUS_CANCELLED = "Cancelled"
const val STATUS_EMPTY = "Empty"


sealed class PayLaterApplicationStatus(val tag: String)
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

    fun handleApplicationStateResponse(userCreditApplicationStatus: UserCreditApplicationStatus): Boolean {
        userCreditApplicationStatus.run {
            if (!applicationDetailList.isNullOrEmpty()) {
                for (applicationDetail in applicationDetailList) {
                    setLabelData(applicationDetail)
                }
            } else return false
        }
        return true
    }

    /**
     * setLabelData -> set string resource id and label type once in application status data
     * @param payLaterApplicationDetail : ApplicationStatus Data
     */
    private fun setLabelData(payLaterApplicationDetail: PayLaterApplicationDetail) {
        when (getApplicationStatusType(payLaterApplicationDetail)) {
            is PayLaterStatusWaiting -> {
                payLaterApplicationDetail.payLaterApplicationStatusLabelStringId = R.string.pay_later_status_waiting
                payLaterApplicationDetail.payLaterApplicationStatusLabelType = Label.GENERAL_LIGHT_ORANGE
            }
            is PayLaterStatusActive -> {
                payLaterApplicationDetail.payLaterApplicationStatusLabelStringId = R.string.pay_later_status_active
                payLaterApplicationDetail.payLaterApplicationStatusLabelType = Label.GENERAL_LIGHT_BLUE
            }
            is PayLaterStatusCancelled -> {
                payLaterApplicationDetail.payLaterApplicationStatusLabelStringId = R.string.pay_later_status_cancelled
                payLaterApplicationDetail.payLaterApplicationStatusLabelType = Label.GENERAL_LIGHT_RED
            }
            is PayLaterStatusRejected -> {
                payLaterApplicationDetail.payLaterApplicationStatusLabelStringId = R.string.pay_later_status_rejected
                payLaterApplicationDetail.payLaterApplicationStatusLabelType = Label.GENERAL_LIGHT_RED
            }
            is PayLaterStatusApproved -> {
                payLaterApplicationDetail.payLaterApplicationStatusLabelStringId = R.string.pay_later_status_approved
                payLaterApplicationDetail.payLaterApplicationStatusLabelType = Label.GENERAL_LIGHT_BLUE
            }
            is PayLaterStatusSuspended -> {
                payLaterApplicationDetail.payLaterApplicationStatusLabelStringId = R.string.pay_later_status_suspended
                payLaterApplicationDetail.payLaterApplicationStatusLabelType = Label.GENERAL_LIGHT_RED
            }
            is PayLaterStatusFailed -> {
                payLaterApplicationDetail.payLaterApplicationStatusLabelStringId = R.string.pay_later_status_failed
                payLaterApplicationDetail.payLaterApplicationStatusLabelType = Label.GENERAL_LIGHT_RED
            }
            is PayLaterStatusExpired -> {
                payLaterApplicationDetail.payLaterApplicationStatusLabelStringId = R.string.pay_later_status_expired
                payLaterApplicationDetail.payLaterApplicationStatusLabelType = Label.GENERAL_LIGHT_RED
            }
        }
    }
}