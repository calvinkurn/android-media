package com.tokopedia.minicart.cartlist.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.minicart.cartlist.adapter.MiniCartListAdapterTypeFactory
import com.tokopedia.minicart.common.data.response.minicartlist.Action
import com.tokopedia.minicart.common.data.response.minicartlist.WholesalePrice

// TODO: BUNDLING NOW MINI CART
data class MiniCartProductUiModel(
        var cartId: String = "",
        var productId: String = "",
        var parentId: String = "",
        var cartString: String = "",
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
        var productWeight: Int = 0,
        var productMinOrder: Int = 0,
        var productMaxOrder: Int = 0,
        var productActions: List<Action> = emptyList(),
        var selectedUnavailableActionId: String = "",
        var selectedUnavailableActionLink: String = "",
        var wholesalePriceGroup: List<WholesalePrice> = emptyList(),
        var maxNotesLength: Int = 0,
        var isProductDisabled: Boolean = false,
        var productCashbackPercentage: Int = 0,

        // Fields below are for analytics purpose only
        var campaignId: String = "",
        var attribution: String = "",
        var warehouseId: String = "",
        var categoryId: String = "",
        var category: String = "",
        var shopId: String = "",
        var shopName: String = "",
        var shopType: String = "",
        var freeShippingType: String = "",
        var errorType: String = ""
) : Visitable<MiniCartListAdapterTypeFactory> {

    override fun type(typeFactory: MiniCartListAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}