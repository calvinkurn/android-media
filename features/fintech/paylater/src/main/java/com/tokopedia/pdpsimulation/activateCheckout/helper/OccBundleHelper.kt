package com.tokopedia.pdpsimulation.activateCheckout.helper

import android.os.Bundle
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.InstallmentBottomSheetDetail
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.PaylaterGetOptimizedModel
import com.tokopedia.pdpsimulation.activateCheckout.presentation.bottomsheet.SelectGateWayBottomSheet
import com.tokopedia.pdpsimulation.common.analytics.OccBottomSheetImpression
import com.tokopedia.pdpsimulation.paylater.presentation.bottomsheet.PayLaterInstallmentFeeInfo

object OccBundleHelper {

    fun setBundleForBottomSheetPartner(
        listOfGateway: PaylaterGetOptimizedModel,
        gateWayId: String,
        variantName: String,
        productId: String,
        tenureSelected: String,
        quantity: Int,
        emiAmount: String
    ): Bundle {

        val bundle = Bundle().apply {
            putParcelable(
                SelectGateWayBottomSheet.GATEWAY_LIST,
                listOfGateway
            )
            putString(
                SelectGateWayBottomSheet.SELECTED_GATEWAY,
                gateWayId
            )
            putString(
                SelectGateWayBottomSheet.CURRENT_VARINT,
                variantName
            )
            putString(
                SelectGateWayBottomSheet.CURRENT_PRODUCT_ID,
                productId
            )
            putString(
                SelectGateWayBottomSheet.CURRENT_SELECTED_TENURE,
                tenureSelected
            )
            putInt(
                SelectGateWayBottomSheet.CURRENT_QUANTITY,
                quantity
            )
            putString(
                SelectGateWayBottomSheet.SELECTED_EMI,
                emiAmount
            )

        }


        return bundle
    }


    fun setBundleForInstalmentBottomSheet(installmentBottomSheetDetail: InstallmentBottomSheetDetail): Bundle
    {
        val bundle = Bundle().apply {
            installmentBottomSheetDetail.gatwayToChipMap[installmentBottomSheetDetail.gatewayIdSelected]?.let { checkoutData ->
                val eventImpression = OccBottomSheetImpression().apply{
                    productPrice = installmentBottomSheetDetail.selectedProductPrice
                    userStatus =checkoutData.userState ?: ""
                    tenureOption = checkoutData.tenureDetail[installmentBottomSheetDetail.selectedTenure].tenure
                }
                putParcelable(PayLaterInstallmentFeeInfo.IMPRESSION_DETAIL, eventImpression)
            }
            putParcelable(
                PayLaterInstallmentFeeInfo.INSTALLMENT_DETAIL,installmentBottomSheetDetail.installmentDetail)
        }
        return bundle


    }

}
