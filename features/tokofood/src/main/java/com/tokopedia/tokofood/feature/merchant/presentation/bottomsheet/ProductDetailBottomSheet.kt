package com.tokopedia.tokofood.feature.merchant.presentation.bottomsheet

import android.os.Bundle
import com.tokopedia.tokofood.feature.merchant.presentation.model.ProductUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify

class ProductDetailBottomSheet : BottomSheetUnify() {

    companion object {

        private const val PRODUCT_UI_MODEL = "PRODUCT_UI_MODEL"

        @JvmStatic
        fun createInstance(productUiModel: ProductUiModel): MerchantInfoBottomSheet {
            return MerchantInfoBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelable(PRODUCT_UI_MODEL, productUiModel)
                }
            }
        }
    }

    private var binding: ProductDetailBottomSheet? = null

//    private val productUiModel: ProductUiModel by lazy {
//        arguments?.getParcelable(PRODUCT_UI_MODEL)
//    }
}