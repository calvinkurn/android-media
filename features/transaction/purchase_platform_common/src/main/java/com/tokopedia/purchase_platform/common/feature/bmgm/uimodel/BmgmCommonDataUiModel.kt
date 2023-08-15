package com.tokopedia.purchase_platform.common.feature.bmgm.uimodel

/**
 * Created by @ilhamsuaib on 07/08/23.
 */

class BmgmCommonDataUiModel(
    val offerId: Long = 0L,
    val offerName: String = "",
    val offerMessage: String = "",
    val hasReachMaxDiscount: Boolean = false,
    val totalDiscount: Double = 0.0,
    val priceBeforeBenefit: Double = 0.0,
    val finalPrice: Double = 0.0,
    val showMiniCartFooter: Boolean = false,
    val tiersApplied: List<TierUiModel> = emptyList()
) {

    companion object {
        const val PARAM_KEY_BMGM_DATA = "bmgm_data"
    }

    data class TierUiModel(
        val tierId: Long = 0L,
        val tierMessage: String = "",
        val tierDiscountStr: String = "",
        val priceBeforeBenefit: Double = 0.0,
        val priceAfterBenefit: Double = 0.0,
        val products: List<ProductUiModel> = emptyList()
    ) {

        /**
         * tier_id = 0 means the product is not in discount group
         * */
        fun isNonDiscountProducts(): Boolean = tierId == 0L
    }

    data class ProductUiModel(
        val productId: String = "",
        val warehouseId: String = "",
        val productName: String = "",
        val productImage: String = "",
        val productPrice: Double = 0.0,
        val productPriceFmt: String = "",
        val quantity: Int = 0
    )
}