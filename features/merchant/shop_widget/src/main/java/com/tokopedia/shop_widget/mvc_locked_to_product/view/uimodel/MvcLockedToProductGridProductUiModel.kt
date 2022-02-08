package com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter.MvcLockedToProductTypeFactory

class MvcLockedToProductGridProductUiModel(
    val productID: String = "",
    val childIDs: List<String> = listOf(),
    val city: String = "",
    val minimumOrder: Int = 0,
    val stock: Int = 0,
    val productInCart: ProductInCart = ProductInCart(),
    val isVariant: Boolean = false,
    var productCardModel: ProductCardModel = ProductCardModel()
) : Visitable<MvcLockedToProductTypeFactory>, ImpressHolder() {

    data class ProductInCart(
        var productId: String = "",
        var qty: Int = 0
    )

    override fun type(typeFactory: MvcLockedToProductTypeFactory): Int {
        return typeFactory.type(this)
    }
}