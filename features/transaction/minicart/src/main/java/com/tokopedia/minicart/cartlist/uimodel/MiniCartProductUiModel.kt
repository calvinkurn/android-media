package com.tokopedia.minicart.cartlist.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.minicart.cartlist.adapter.MiniCartListAdapterTypeFactory
import com.tokopedia.minicart.common.data.response.minicartlist.Action

data class MiniCartProductUiModel(
        var cartId: String = "",
        var productId: String = "",
        var productImageUrl: String = "",
        var productName: String = "",
        var productVariantName: String = "",
        var productQtyLeft: String = "",
        var productSlashPriceLabel: String = "",
        var productOriginalPrice: Long = 0L,
        var productWholeSalePrice: Long = 0L,
        var productInitialPriceBeforeDrop: Long = 0L,
        var productPrice: Long = 0L,
        var productInformation: List<String> = emptyList(),
        var productNotes: String = "",
        var productQty: Int = 0,
        var productMinOrder: Int = 0,
        var productMaxOrder: Int = 0,
        var productActions: List<Action> = emptyList(),
        var selectedUnavailableActionId: String = "",
        var selectedUnavailableActionLink: String = "",
        var maxNotesLength: Int = 0,
        var isProductDisabled: Boolean = false
) : Visitable<MiniCartListAdapterTypeFactory> {

    override fun type(typeFactory: MiniCartListAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}