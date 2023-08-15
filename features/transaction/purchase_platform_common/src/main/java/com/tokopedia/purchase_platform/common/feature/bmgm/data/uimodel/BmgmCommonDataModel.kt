package com.tokopedia.purchase_platform.common.feature.bmgm.data.uimodel

import com.tokopedia.utils.currency.CurrencyFormatUtil

/**
 * Created by @ilhamsuaib on 07/08/23.
 */

class BmgmCommonDataModel(
    val offerId: Long = 0L,
    val offerName: String = "",
    val offerMessage: String = "",
    val hasReachMaxDiscount: Boolean = false,
    val totalDiscount: Double = 0.0,
    val priceBeforeBenefit: Double = 0.0,
    val finalPrice: Double = 0.0,
    val showMiniCartFooter: Boolean = false,
    val tiersApplied: List<TierModel> = emptyList()
) {

    companion object {
        const val PARAM_KEY_BMGM_DATA = "bmgm_data"

        const val NON_DISCOUNT_TIER_ID = 0L
    }

    data class TierModel(
        val tierId: Long = NON_DISCOUNT_TIER_ID,
        val tierMessage: String = "",
        val tierDiscountStr: String = "",
        val priceBeforeBenefit: Double = 0.0,
        val priceAfterBenefit: Double = 0.0,
        val products: List<ProductModel> = emptyList()
    ) {

        /**
         * tier_id = 0 means the product is not in discount group
         * */
        fun isNonDiscountProducts(): Boolean {
            return tierId == NON_DISCOUNT_TIER_ID
        }
    }

    data class ProductModel(
        val productId: String = "",
        val warehouseId: String = "",
        val productName: String = "",
        val productImage: String = "",
        val productPrice: Double = 0.0,
        val quantity: Int = 0
    ) {
        fun getProductPriceFmt(): String {
            return CurrencyFormatUtil.convertPriceValueToIdrFormat(productPrice, false)
        }
    }
}
