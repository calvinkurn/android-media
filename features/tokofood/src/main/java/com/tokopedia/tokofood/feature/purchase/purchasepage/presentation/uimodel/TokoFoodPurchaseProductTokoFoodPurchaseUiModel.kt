package com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.detail.common.getCurrencyFormatted
import com.tokopedia.tokofood.common.domain.response.CartListCartGroupCartVariant
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodProductVariant
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateProductVariantParam
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.adapter.TokoFoodPurchaseAdapterTypeFactory

data class TokoFoodPurchaseProductTokoFoodPurchaseUiModel(
        var isAvailable: Boolean = true,
        var id: String = "",
        var name: String = "",
        var imageUrl: String = "",
        var addOns: List<String> = emptyList(),
        var originalPrice: Double = 0.0,
        var originalPriceFmt: String = "",
        var price: Double = 0.0,
        var priceFmt: String = "",
        var discountPercentage: String = "",
        var notes: String = "",
        var quantity: Int = 0,
        var minQuantity: Int = 0,
        var maxQuantity: Int = 0,
        var cartId: String = "",
        var isQuantityChanged: Boolean = false,
        val variantsParam: List<UpdateProductVariantParam> = listOf(),
        val variants: List<CartListCartGroupCartVariant> = listOf()
) : Visitable<TokoFoodPurchaseAdapterTypeFactory>, BaseTokoFoodPurchaseUiModel() {

        fun getBasePrice(): Double {
                return price - getVariantsTotal()
        }

        fun getBasePriceFmt(): String {
                return getBasePrice().getCurrencyFormatted()
        }

        private fun getVariantsTotal(): Double {
                var total = 0.0
                variants.forEach { variant ->
                        variant.options.forEach { option ->
                                if (option.isSelected) {
                                        total += option.price
                                }
                        }
                }
                return total
        }

    override fun type(typeFactory: TokoFoodPurchaseAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
