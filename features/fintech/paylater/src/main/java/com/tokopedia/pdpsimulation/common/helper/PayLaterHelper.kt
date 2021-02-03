package com.tokopedia.pdpsimulation.common.helper

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterApplicationDetail
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterItemProductData
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterPartnerStepDetails
import com.tokopedia.pdpsimulation.paylater.mapper.PayLaterPartnerTypeMapper
import com.tokopedia.pdpsimulation.paylater.mapper.ProcessingApplicationPartnerType
import com.tokopedia.pdpsimulation.paylater.mapper.RegisterStepsPartnerType
import com.tokopedia.pdpsimulation.paylater.mapper.UsageStepsPartnerType
import com.tokopedia.pdpsimulation.paylater.presentation.detail.bottomsheet.PayLaterActionStepsBottomSheet
import com.tokopedia.pdpsimulation.paylater.presentation.registration.PayLaterVerificationBottomSheet
import java.io.File

object PayLaterHelper {

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
                    populateBundle(bundle, data.partnerApplyDetails, "${context?.getString(R.string.pay_later_how_to_register)} ${data.partnerName}")
                    PayLaterActionStepsBottomSheet.show(bundle, childFragmentManager)
                }
                is UsageStepsPartnerType -> {
                    populateBundle(bundle, data.partnerUsageDetails, "${context?.getString(R.string.pay_later_how_to_use)} ${data.partnerName}")
                    PayLaterActionStepsBottomSheet.show(bundle, childFragmentManager)
                }
                is ProcessingApplicationPartnerType -> {
                    bundle.putParcelable(PayLaterVerificationBottomSheet.APPLICATION_STATUS, partnerApplicationDetail)
                    PayLaterVerificationBottomSheet.show(bundle, childFragmentManager)
                }
            }
        }
    }

    private fun populateBundle(bundle: Bundle, partnerData: PayLaterPartnerStepDetails?, title: String) {
        bundle.putParcelable(PayLaterActionStepsBottomSheet.STEPS_DATA, partnerData)
        bundle.putString(PayLaterActionStepsBottomSheet.ACTION_TITLE, title)
    }

    fun getJson(path: String): String {
        val uri = this.javaClass.classLoader?.getResource(path)
        val file = File(uri?.path)
        return String(file.readBytes())
    }
}