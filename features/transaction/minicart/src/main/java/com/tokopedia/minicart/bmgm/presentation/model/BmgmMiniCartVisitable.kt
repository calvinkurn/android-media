package com.tokopedia.minicart.bmgm.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.minicart.bmgm.presentation.adapter.factory.BmgmMiniCartAdapterFactory
import com.tokopedia.purchase_platform.common.feature.bmgm.data.uimodel.BmgmCommonDataModel

/**
 * Created by @ilhamsuaib on 14/08/23.
 */
sealed interface BmgmMiniCartVisitable : Visitable<BmgmMiniCartAdapterFactory> {

    data class TierUiModel(
        val tierId: Long = BmgmCommonDataModel.NON_DISCOUNT_TIER_ID,
        val tierMessage: String = "",
        val tierDiscountStr: String = "",
        val priceBeforeBenefit: Double = 0.0,
        val priceAfterBenefit: Double = 0.0,
        val products: List<ProductUiModel> = emptyList(),
        var shouldShowShiningEffect: Boolean = true
    ) : BmgmMiniCartVisitable {

        override fun type(typeFactory: BmgmMiniCartAdapterFactory): Int {
            return typeFactory.type(this)
        }

        fun isDiscountTier(): Boolean {
            return tierId != BmgmCommonDataModel.NON_DISCOUNT_TIER_ID
        }
    }

    data class ProductUiModel(
        val productId: String = "",
        val warehouseId: String = "",
        val productName: String = "",
        val productImage: String = "",
        val cartId: String = "",
        val finalPrice: Double = 0.0,
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