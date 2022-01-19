package com.tokopedia.pdpsimulation.common.helper

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.tokopedia.pdpsimulation.common.listener.PdpSimulationCallback
import com.tokopedia.pdpsimulation.paylater.presentation.detail.bottomsheet.PayLaterActionStepsBottomSheet
import com.tokopedia.pdpsimulation.paylater.presentation.detail.bottomsheet.PayLaterAdditionalFeeInfo
import com.tokopedia.pdpsimulation.paylater.presentation.detail.bottomsheet.PayLaterFaqBottomSheet
import com.tokopedia.pdpsimulation.paylater.presentation.detail.bottomsheet.PayLaterTokopediaGopayBottomsheet


class BottomSheetNavigator(val childFragmentManager: FragmentManager) {

    fun <T : Any> showBottomSheet(
        modelClass: Class<T>,
        bundle: Bundle,
        pdpSimulationCallback: PdpSimulationCallback,
        productId: String
    ) {
        when {


            modelClass.isAssignableFrom(PayLaterActionStepsBottomSheet::class.java) -> {
                PayLaterActionStepsBottomSheet.show(
                    bundle,
                    pdpSimulationCallback,
                    childFragmentManager
                )
            }

            modelClass.isAssignableFrom(PayLaterFaqBottomSheet::class.java) ->
                PayLaterFaqBottomSheet.show(bundle, pdpSimulationCallback, childFragmentManager)

            modelClass.isAssignableFrom(PayLaterAdditionalFeeInfo::class.java) ->
                PayLaterAdditionalFeeInfo.show(pdpSimulationCallback, childFragmentManager)

            modelClass.isAssignableFrom(PayLaterTokopediaGopayBottomsheet::class.java) -> {
                bundle.putString(productID, productId)
                PayLaterTokopediaGopayBottomsheet.show(
                    bundle,
                    pdpSimulationCallback,
                    childFragmentManager
                )
            }
        }
    }

    companion object {
        const val productID = "productID"
    }
}