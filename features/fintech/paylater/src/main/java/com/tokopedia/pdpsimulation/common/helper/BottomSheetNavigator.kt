package com.tokopedia.pdpsimulation.paylater.helper

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.tokopedia.pdpsimulation.paylater.presentation.bottomsheet.PayLaterActionStepsBottomSheet
import com.tokopedia.pdpsimulation.paylater.presentation.bottomsheet.PayLaterInstallmentFeeInfo
import com.tokopedia.pdpsimulation.paylater.presentation.bottomsheet.PayLaterTokopediaGopayBottomsheet

class BottomSheetNavigator(val childFragmentManager: FragmentManager) {

    fun <T : Any> showBottomSheet(
        modelClass: Class<T>,
        bundle: Bundle,
        productId: String? = null
    ) {
        when {
            modelClass.isAssignableFrom(PayLaterActionStepsBottomSheet::class.java) -> {
                PayLaterActionStepsBottomSheet.show(
                    bundle,
                    childFragmentManager
                )
            }

            modelClass.isAssignableFrom(PayLaterInstallmentFeeInfo::class.java) ->
                PayLaterInstallmentFeeInfo.show(bundle, childFragmentManager)

            modelClass.isAssignableFrom(PayLaterTokopediaGopayBottomsheet::class.java) -> {
                bundle.putString(productID, productId)
                PayLaterTokopediaGopayBottomsheet.show(
                    bundle,
                    childFragmentManager
                )
            }

        }
    }

    companion object {
        const val productID = "productID"
    }
}