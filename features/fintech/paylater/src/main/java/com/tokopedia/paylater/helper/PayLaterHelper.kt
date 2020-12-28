package com.tokopedia.paylater.helper

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.tokopedia.paylater.R
import com.tokopedia.paylater.data.mapper.*
import com.tokopedia.paylater.domain.model.PayLaterApplicationDetail
import com.tokopedia.paylater.domain.model.PayLaterItemProductData
import com.tokopedia.paylater.presentation.widget.bottomsheet.PayLaterActionStepsBottomSheet
import com.tokopedia.paylater.presentation.widget.bottomsheet.PayLaterVerificationBottomSheet
import com.tokopedia.unifycomponents.Label

object PayLaterHelper {

    /**
     * setLabelData -> set string resource id and label type once in application status data
     * @param payLaterApplicationDetail : ApplicationStatus Data
     */
    fun setLabelData(payLaterApplicationDetail: PayLaterApplicationDetail) {
        when (PayLaterApplicationStatusMapper.getApplicationStatusType(payLaterApplicationDetail)) {
            is PayLaterStatusWaiting -> {
                payLaterApplicationDetail.payLaterApplicationStatusLabelStringId = R.string.payLater_status_waiting
                payLaterApplicationDetail.payLaterApplicationStatusLabelType = Label.GENERAL_LIGHT_ORANGE
            }
            is PayLaterStatusActive -> {
                payLaterApplicationDetail.payLaterApplicationStatusLabelStringId = R.string.payLater_status_active
                payLaterApplicationDetail.payLaterApplicationStatusLabelType = Label.GENERAL_LIGHT_BLUE
            }
            is PayLaterStatusCancelled -> {
                payLaterApplicationDetail.payLaterApplicationStatusLabelStringId = R.string.payLater_status_cancelled
                payLaterApplicationDetail.payLaterApplicationStatusLabelType = Label.GENERAL_LIGHT_RED
            }
            is PayLaterStatusRejected -> {
                payLaterApplicationDetail.payLaterApplicationStatusLabelStringId = R.string.payLater_status_rejected
                payLaterApplicationDetail.payLaterApplicationStatusLabelType = Label.GENERAL_LIGHT_RED
            }
            is PayLaterStatusApproved -> {
                payLaterApplicationDetail.payLaterApplicationStatusLabelStringId = R.string.payLater_status_approved
                payLaterApplicationDetail.payLaterApplicationStatusLabelType = Label.GENERAL_LIGHT_BLUE
            }
            is PayLaterStatusSuspended -> {
                payLaterApplicationDetail.payLaterApplicationStatusLabelStringId = R.string.payLater_status_suspended
                payLaterApplicationDetail.payLaterApplicationStatusLabelType = Label.GENERAL_LIGHT_RED
            }
            is PayLaterStatusFailed -> {
                payLaterApplicationDetail.payLaterApplicationStatusLabelStringId = R.string.payLater_status_failed
                payLaterApplicationDetail.payLaterApplicationStatusLabelType = Label.GENERAL_LIGHT_RED
            }
            is PayLaterStatusExpired -> {
                payLaterApplicationDetail.payLaterApplicationStatusLabelStringId = R.string.payLater_status_expired
                payLaterApplicationDetail.payLaterApplicationStatusLabelType = Label.GENERAL_LIGHT_RED
            }
        }
    }

    /**
     * desc: open required bottom sheet from the signup bottom sheet based on
     * @see PayLaterPartnerTypeMapper
     * @param context : for extracting string resources
     * @param childFragmentManager: for opening new bottom sheet
     * @param productItemData : payLater product data
     * @param partnerApplicationDetail : payLater application status data
     */
    fun openBottomSheet(context: Context?, childFragmentManager: FragmentManager, productItemData: PayLaterItemProductData, partnerApplicationDetail: PayLaterApplicationDetail?) {
        val bundle = Bundle()
        productItemData.let { data ->
            bundle.putString(PayLaterActionStepsBottomSheet.ACTION_URL, data.actionWebUrl)
            when (PayLaterPartnerTypeMapper.getPayLaterPartnerType(data, partnerApplicationDetail)) {
                is RegisterStepsPartnerType -> {
                    bundle.putParcelable(PayLaterActionStepsBottomSheet.STEPS_DATA, data.partnerApplyDetails)
                    bundle.putString(PayLaterActionStepsBottomSheet.ACTION_TITLE, "${context?.getString(R.string.payLater_how_to_register)} ${data.partnerName}")
                    PayLaterActionStepsBottomSheet.show(bundle, childFragmentManager)

                }
                is UsageStepsPartnerType -> {
                    bundle.putParcelable(PayLaterActionStepsBottomSheet.STEPS_DATA, data.partnerUsageDetails)
                    bundle.putString(PayLaterActionStepsBottomSheet.ACTION_TITLE, "${context?.getString(R.string.payLater_how_to_use)} ${data.partnerName}")
                    PayLaterActionStepsBottomSheet.show(bundle, childFragmentManager)
                }
                is ProcessingApplicationPartnerType -> {
                    bundle.putParcelable(PayLaterVerificationBottomSheet.APPLICATION_STATUS, partnerApplicationDetail)
                    PayLaterVerificationBottomSheet.show(bundle, childFragmentManager)
                }
            }
        }
    }

    fun isKredivoApplicationStatusEmpty(payLaterApplicationStatus: ArrayList<PayLaterApplicationDetail>): Boolean {
        for (payLaterItem in payLaterApplicationStatus) {
            if (payLaterItem.payLaterGatewayCode == "KREDIVO" && payLaterItem.payLaterApplicationStatus.isEmpty()) return true
        }
        return false
    }
}