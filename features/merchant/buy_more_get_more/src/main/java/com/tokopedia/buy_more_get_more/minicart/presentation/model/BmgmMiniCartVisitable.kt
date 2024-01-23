package com.tokopedia.buy_more_get_more.minicart.presentation.model

import com.tokopedia.bmsm_widget.presentation.model.GiftWidgetState
import com.tokopedia.bmsm_widget.presentation.model.ProductGiftUiModel
import com.tokopedia.buy_more_get_more.minicart.presentation.adapter.factory.BmgmMiniCartAdapterFactory
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.purchase_platform.common.feature.bmgm.data.uimodel.BmgmCommonDataModel

/**
 * Created by @ilhamsuaib on 14/08/23.
 */
sealed interface BmgmMiniCartVisitable : BaseMiniCartVisitable {

    data class TierUiModel(
        val tierId: Long = BmgmCommonDataModel.NON_DISCOUNT_TIER_ID,
        val tierMessage: String = "",
        val tierDiscountStr: String = "",
        val priceBeforeBenefit: Double = 0.0,
        val priceAfterBenefit: Double = 0.0,
        val products: List<ProductUiModel> = emptyList(),
        val productsBenefit: List<ProductUiModel> = emptyList(),
        val benefitWording: String = "",
        val benefitCta: String = ""
    ) : BmgmMiniCartVisitable {

        val impressHolder: ImpressHolder = ImpressHolder()

        override fun type(typeFactory: BmgmMiniCartAdapterFactory): Int {
            return typeFactory.type(this)
        }

        override fun getItemId(): String = tierId.toString()

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
        val quantity: Int = 0,
        val minQuantity: Int = 0,
        val maxQuantity: Int = 0,
        val tierId: Long = 0L,
        val ui: Ui = Ui()
    ) : BmgmMiniCartVisitable {

        override fun type(typeFactory: BmgmMiniCartAdapterFactory): Int {
            return typeFactory.type(this)
        }

        override fun getItemId(): String = productId

        data class Ui(
            val topMarginInDp: Int = Int.ZERO, val showDivider: Boolean = false
        )
    }

    object PlaceholderUiModel : BmgmMiniCartVisitable {

        private const val PLACEHOLDER_ID = "mini_cart_placeholder"

        override fun type(typeFactory: BmgmMiniCartAdapterFactory): Int {
            return typeFactory.type(this)
        }

        override fun getItemId(): String = PLACEHOLDER_ID
    }

    data class GwpGiftPlaceholder(val productImage: String) : BmgmMiniCartVisitable {

        companion object {
            private const val ITEM_ID = "gwp_gift_placeholder"
        }

        override fun getItemId(): String = ITEM_ID

        override fun type(typeFactory: BmgmMiniCartAdapterFactory): Int {
            return typeFactory.type(this)
        }
    }

    data class GwpGiftWidgetUiModel(
        val productList: List<ProductGiftUiModel>,
        val benefitWording: String,
        val benefitCta: String,
        val state: GiftWidgetState = GiftWidgetState.LOADING
    ) : BmgmMiniCartVisitable {

        companion object {
            private const val ITEM_ID = "gwp_gift_mini_cart"
        }

        override fun getItemId(): String = ITEM_ID

        override fun type(typeFactory: BmgmMiniCartAdapterFactory): Int {
            return typeFactory.type(this)
        }
    }
}
