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
                    bundle.putParcelable(PayLaterActionStepsBottomSheet.STEPS_DATA, data.partnerApplyDetails)
                    bundle.putString(PayLaterActionStepsBottomSheet.ACTION_TITLE, "${context?.getString(R.string.pay_later_how_to_register)} ${data.partnerName}")
                    PayLaterActionStepsBottomSheet.show(bundle, childFragmentManager)

                }
                is UsageStepsPartnerType -> {
                    bundle.putParcelable(PayLaterActionStepsBottomSheet.STEPS_DATA, data.partnerUsageDetails)
                    bundle.putString(PayLaterActionStepsBottomSheet.ACTION_TITLE, "${context?.getString(R.string.pay_later_how_to_use)} ${data.partnerName}")
                    PayLaterActionStepsBottomSheet.show(bundle, childFragmentManager)
                }
                is ProcessingApplicationPartnerType -> {
                    bundle.putParcelable(PayLaterVerificationBottomSheet.APPLICATION_STATUS, partnerApplicationDetail)
                    PayLaterVerificationBottomSheet.show(bundle, childFragmentManager)
                }
            }
        }
    }

    fun getJson(path : String) : String {
        val uri = this.javaClass.classLoader?.getResource(path)
        val file = File(uri?.path)
        return String(file.readBytes())
    }
}