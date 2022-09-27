package com.tokopedia.pdpsimulation.activateCheckout.helper

import android.os.Bundle
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.InstallmentBottomSheetDetail
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.PaylaterGetOptimizedModel
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.TenureDetail
import com.tokopedia.pdpsimulation.activateCheckout.presentation.bottomsheet.SelectGateWayBottomSheet
import com.tokopedia.pdpsimulation.common.analytics.OccBottomSheetImpression
import com.tokopedia.pdpsimulation.paylater.presentation.bottomsheet.PayLaterInstallmentFeeInfo

object OccBundleHelper {

    fun setBundleForBottomSheetPartner(
        bundelData: BundleData
    ): Bundle {

        val bundle = Bundle().apply {
            putParcelable(
                SelectGateWayBottomSheet.GATEWAY_LIST,
                bundelData.listOfGateway
            )
            putString(
                SelectGateWayBottomSheet.SELECTED_GATEWAY,
                bundelData.gateWayId
            )
            putString(
                SelectGateWayBottomSheet.CURRENT_VARINT,
                bundelData.variantName
            )
            putString(
                SelectGateWayBottomSheet.CURRENT_PRODUCT_ID,
                bundelData.productId
            )
            putString(
                SelectGateWayBottomSheet.CURRENT_SELECTED_TENURE,
                bundelData.tenureSelected
            )
            putInt(
                SelectGateWayBottomSheet.CURRENT_QUANTITY,
                bundelData.quantity
            )
            if (bundelData.listofTenureDetail.size > bundelData.selectedPosition) {
                putString(
                    SelectGateWayBottomSheet.SELECTED_EMI,
                    bundelData.listofTenureDetail[bundelData.selectedPosition].monthly_installment.orEmpty()
                )
            } else {
                putString(
                    SelectGateWayBottomSheet.SELECTED_EMI,
                    ""
                )
            }
        }
        return bundle
    }


    fun setBundleForInstalmentBottomSheet(installmentBottomSheetDetail: InstallmentBottomSheetDetail): Bundle {
        val bundle = Bundle().apply {
            installmentBottomSheetDetail.gatwayToChipMap[installmentBottomSheetDetail.gatewayIdSelected]?.let { checkoutData ->
                val eventImpression = OccBottomSheetImpression().apply {
                    productPrice = installmentBottomSheetDetail.selectedProductPrice
                    userStatus = checkoutData.userState ?: ""
                    tenureOption =
                        checkoutData.tenureDetail[installmentBottomSheetDetail.selectedTenure].tenure
                    payLaterPartnerName = checkoutData.gateway_name ?: ""
                }
                putParcelable(PayLaterInstallmentFeeInfo.IMPRESSION_DETAIL, eventImpression)
            }
            putParcelable(
                PayLaterInstallmentFeeInfo.INSTALLMENT_DETAIL,
                installmentBottomSheetDetail.installmentDetail
            )
        }
        return bundle


    }

}

data class BundleData(
    val listofTenureDetail: List<TenureDetail>,
    val selectedPosition: Int,
    val listOfGateway: PaylaterGetOptimizedModel,
    val gateWayId: String,
    val variantName: String,
    val productId: String,
    val tenureSelected: String,
    val quantity: Int
)
