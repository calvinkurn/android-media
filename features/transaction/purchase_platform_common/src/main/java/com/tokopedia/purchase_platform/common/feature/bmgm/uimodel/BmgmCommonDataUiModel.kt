package com.tokopedia.purchase_platform.common.feature.bmgm.uimodel

import com.tokopedia.purchase_platform.common.feature.bmgm.adapter.BmgmMiniCartAdapterFactory

/**
 * Created by @ilhamsuaib on 07/08/23.
 */

class BmgmCommonDataUiModel(
    val offerId: Long = 0L,
    val offerName: String = "",
    val offerMessage: String = "",
    val offerDiscount: Double = 0.0,
    val hasReachMaxDiscount: Boolean = false,
    val products: List<BmgmMiniCartVisitable> = emptyList()
) {

    data class BundledProductUiModel(
        val tierId: Long = 0L,
        val tierDiscountStr: String = "",
        val priceBeforeBenefit: Double = 0.0,
        val priceAfterBenefit: Double = 0.0,
        val products: List<SingleProductUiModel> = emptyList()
    ) : BmgmMiniCartVisitable {

        override fun type(typeFactory: BmgmMiniCartAdapterFactory): Int {
            return typeFactory.type(this)
        }
    }

    data class SingleProductUiModel(
        val productId: String = "",
        val warehouseId: String = "",
        val productName: String = "",
        val productImage: String = "",
        val productPrice: Double = 0.0,
        val productPriceFmt: String = "",
        val quantity: Int = 0
    ) : BmgmMiniCartVisitable {

        override fun type(typeFactory: BmgmMiniCartAdapterFactory): Int {
            return typeFactory.type(this)
        }
    }
}