package com.tokopedia.tokofood.feature.merchant.presentation.bottomsheet

import android.os.Bundle
import com.tokopedia.tokofood.common.domain.response.CartTokoFoodBottomSheet
import com.tokopedia.unifycomponents.BottomSheetUnify

class PhoneNumberVerificationBottomSheet: BottomSheetUnify() {

    companion object {
        private const val BUNDLE_KEY_BOTTOM_SHEET_DATA = "PRODUCT_POSITION"
        @JvmStatic
        fun createInstance(bottomSheetData: CartTokoFoodBottomSheet): PhoneNumberVerificationBottomSheet {
            return PhoneNumberVerificationBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelable(BUNDLE_KEY_BOTTOM_SHEET_DATA, bottomSheetData)
                }
            }
        }
    }


}