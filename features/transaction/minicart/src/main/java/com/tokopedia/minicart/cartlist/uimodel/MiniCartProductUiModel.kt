package com.tokopedia.minicart.cartlist.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.minicart.cartlist.adapter.MiniCartListAdapterTypeFactory
import com.tokopedia.minicart.common.data.response.minicartlist.Action
import com.tokopedia.minicart.common.data.response.minicartlist.Action.Companion.ACTION_DELETE
import com.tokopedia.minicart.common.data.response.minicartlist.WholesalePrice

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
        var productOriginalPrice: Double = 0.0,
        var productWholeSalePrice: Double = 0.0,
        var productInitialPriceBeforeDrop: Double = 0.0,
        var productPrice: Double = 0.0,
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
        var placeholderNote: String = "",
        var isProductDisabled: Boolean = false,
        var productCashbackPercentage: Int = 0,
        var bundleId: String = "",
        var bundleGroupId: String = "",
        var bundleName: String = "",
        var bundlePrice: Double = 0.0,
        var bundlePriceFmt: String = "",
        var bundleOriginalPrice: Double = 0.0,
        var bundleOriginalPriceFmt: String = "",
        var bundleMinOrder: Int = 0,
        var bundleMaxOrder: Int = 0,
        var bundleQty: Int = 0,
        var bundleLabelQty: Int = 0,
        var bundleMultiplier: Int = 0,
        var bundleIconUrl: String = "",
        var slashPriceLabel: String = "",
        var isBundlingItem: Boolean = false,
        var showBundlingHeader: Boolean = false,
        var showBottomDivider: Boolean = false,
        var isLastProductItem: Boolean = false,
        var editBundleApplink: String = "",

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

    fun deepCopy(): MiniCartProductUiModel {
        return MiniCartProductUiModel(
                cartId = this.cartId,
                productId = this.productId,
                parentId = this.parentId,
                productImageUrl = this.productImageUrl,
                productName = this.productName,
                productVariantName = this.productVariantName,
                productQtyLeft = this.productQtyLeft,
                productSlashPriceLabel = this.productSlashPriceLabel,
                productOriginalPrice = this.productOriginalPrice,
                productWholeSalePrice = this.productWholeSalePrice,
                productInitialPriceBeforeDrop = this.productInitialPriceBeforeDrop,
                productPrice = this.productPrice,
                productInformation = this.productInformation,
                productNotes = this.productNotes,
                productQty = this.productQty,
                productWeight = this.productWeight,
                productMinOrder = this.productMinOrder,
                productMaxOrder = this.productMaxOrder,
                productActions = this.productActions,
                selectedUnavailableActionId = this.selectedUnavailableActionId,
                selectedUnavailableActionLink = this.selectedUnavailableActionLink,
                wholesalePriceGroup = this.wholesalePriceGroup,
                maxNotesLength = this.maxNotesLength,
                isProductDisabled = this.isProductDisabled,
                productCashbackPercentage = this.productCashbackPercentage,
                campaignId = this.campaignId,
                attribution = this.attribution,
                warehouseId = this.warehouseId,
                categoryId = this.categoryId,
                category = this.category,
                shopId = this.shopId,
                shopName = this.shopName,
                shopType = this.shopType,
                freeShippingType = this.freeShippingType,
                errorType = this.errorType
        )
    }

    fun hasDeleteAction(): Boolean {
            return productActions.find { it.id == ACTION_DELETE } != null
    }

    fun setQuantity(qty: Int) {
            if(isBundlingItem) {
                    bundleQty = qty
                    productQty = bundleMultiplier * qty
            } else {
                    productQty = qty
            }
    }

    fun getQuantity(): Int {
            return if(isBundlingItem) {
                    bundleQty
            } else {
                    productQty
            }
    }

    fun getPrice(): Double {
            return if(isBundlingItem) {
                    bundlePrice
            } else {
                    productPrice
            }
    }

    fun getOriginalPrice(): Double {
            return if(isBundlingItem) {
                    bundleOriginalPrice
            } else {
                    productOriginalPrice
            }
    }

    fun getMaxOrder(): Int {
            return if(isBundlingItem) {
                    bundleMaxOrder
            } else {
                    productMaxOrder
            }
    }

    fun getMinOrder(): Int {
            return if(isBundlingItem) {
                    bundleMinOrder
            } else {
                    productMinOrder
            }
    }
}
