package com.tokopedia.pdpsimulation.paylater.domain.usecase

import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterApplicationDetail
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterStatusContent
import com.tokopedia.pdpsimulation.paylater.domain.model.UserCreditApplicationStatus
import com.tokopedia.pdpsimulation.paylater.mapper.*
import com.tokopedia.unifycomponents.Label
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class PayLaterApplicationStatusMapperUseCase @Inject constructor() : UseCase<PayLaterAppStatus>() {
    private val PARAM_APP_STATUS_DATA = "param_app_status_data"
    private val APPLICATION_STATE_DATA_FAILURE = "NULL_DATA"

    fun mapLabelDataToApplicationStatus(
            userCreditApplicationStatus: UserCreditApplicationStatus,
            onSuccess: (PayLaterAppStatus) -> Unit,
            onError: (Throwable) -> Unit,
    ) {
        useCaseRequestParams = RequestParams().apply {
            putObject(PARAM_APP_STATUS_DATA, userCreditApplicationStatus)
        }
        execute({
            onSuccess(it)
        }, {
            onError(it)
        }, useCaseRequestParams)
    }

    override suspend fun executeOnBackground(): PayLaterAppStatus {
        val appStatusData = (useCaseRequestParams.getObject(PARAM_APP_STATUS_DATA)
                ?: throw  NullPointerException(APPLICATION_STATE_DATA_FAILURE)) as UserCreditApplicationStatus
        return handleResponse(appStatusData)
    }

    private fun handleResponse(appStatusData: UserCreditApplicationStatus): PayLaterAppStatus {
        var isPayLaterActive = false
        appStatusData.run {
            if (applicationDetailList.isNullOrEmpty()) {
                return StatusFail
            } else {
                applicationDetailList.map {
                    val appStatus = setLabelData(it)
                    it.payLaterStatusContent = computeSubHeaderText(it)
                    if (appStatus is PayLaterStatusActive ||
                            appStatus is PayLaterStatusApproved ||
                            appStatus is PayLaterStatusWaiting)
                        isPayLaterActive = true
                }
            }
        }
        return StatusAppSuccess(appStatusData, isPayLaterActive)
    }

    /**
     * setLabelData -> set string resource id and label type once in application status data
     * @param payLaterApplicationDetail : ApplicationStatus Data
     */
    private fun setLabelData(payLaterApplicationDetail: PayLaterApplicationDetail): PayLaterApplicationStatus {
        val applicationStatusType = PayLaterApplicationStatusMapper.getApplicationStatusType(payLaterApplicationDetail)
        when (applicationStatusType) {
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
                payLaterApplicationDetail.payLaterApplicationStatusLabelType = Label.GENERAL_LIGHT_GREEN
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
            else -> {
                payLaterApplicationDetail.payLaterApplicationStatusLabelStringId = 0
                payLaterApplicationDetail.payLaterApplicationStatusLabelType = 0
            }
        }
        return applicationStatusType
    }

    // pre-compute sub header text according to expiration date logic
    private fun computeSubHeaderText(payLaterApplicationDetail: PayLaterApplicationDetail): PayLaterStatusContent {
        val subHeader: String
        payLaterApplicationDetail.let {
            subHeader = (it.payLaterStatusContent?.verificationContentSubHeader ?: "") +
                    if (isExpirationDateAvailable(it)) "<b>${it.payLaterExpirationDate ?: ""}</b>"
                    else ""
            return PayLaterStatusContent(
                    it.payLaterStatusContent?.verificationContentEmail,
                    subHeader,
                    it.payLaterStatusContent?.verificationContentPhoneNumber,
                    it.payLaterStatusContent?.verificationContentPopUpDetail,
                    it.payLaterStatusContent?.verificationContentInfo
            )
        }
    }

    /*
    *  application expiration date will appear on application status Waiting and Rejected
    * */
    private fun isExpirationDateAvailable(applicationDetail: PayLaterApplicationDetail): Boolean {
        if (applicationDetail.payLaterExpirationDate.isNullOrEmpty()) return false
        val status = PayLaterApplicationStatusMapper.getApplicationStatusType(applicationDetail)
        return status in listOf(PayLaterStatusWaiting, PayLaterStatusRejected)
    }
}


sealed class PayLaterAppStatus
data class StatusAppSuccess(val userCreditApplicationStatus: UserCreditApplicationStatus, val isPayLaterActive: Boolean) : PayLaterAppStatus()
object StatusFail : PayLaterAppStatus()