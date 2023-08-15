package com.tokopedia.minicart.bmgm.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.minicart.bmgm.presentation.adapter.factory.BmgmMiniCartAdapterFactory

/**
 * Created by @ilhamsuaib on 14/08/23.
 */
sealed interface BmgmMiniCartVisitable : Visitable<BmgmMiniCartAdapterFactory> {

    data class TierUiModel(
        val tierId: Long = NON_DISCOUNT_TIER_ID,
        val tierMessage: String = "",
        val tierDiscountStr: String = "",
        val priceBeforeBenefit: Double = 0.0,
        val priceAfterBenefit: Double = 0.0,
        val products: List<ProductUiModel> = emptyList()
    ) : BmgmMiniCartVisitable {

        companion object {
            const val NON_DISCOUNT_TIER_ID = 0L
        }

        override fun type(typeFactory: BmgmMiniCartAdapterFactory): Int {
            return typeFactory.type(this)
        }
    }

    data class ProductUiModel(
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

    object PlaceholderUiModel : BmgmMiniCartVisitable {
        override fun type(typeFactory: BmgmMiniCartAdapterFactory): Int {
            return typeFactory.type(this)
        }
    }
}