package com.tokopedia.cartrevamp.view.uimodel

import com.tokopedia.cartrevamp.data.model.response.shopgroupsimplified.Action
import com.tokopedia.cartrevamp.data.model.response.shopgroupsimplified.ProductInformationWithIcon
import com.tokopedia.cartrevamp.data.model.response.shopgroupsimplified.WholesalePrice
import com.tokopedia.purchase_platform.common.feature.bometadata.BoMetadata

data class CartItemHolderData(
    var cartString: String = "",
    var cartStringOrder: String = "",
    var actionsData: List<Action> = emptyList(),
    var isError: Boolean = false,
    var errorType: String = "",
    var isSelected: Boolean = false,
    var isSingleChild: Boolean = false,
    var selectedUnavailableActionId: String = "",
    var selectedUnavailableActionLink: String = "",
    var shouldValidateWeight: Boolean = false,
    var shouldCheckBoAffordability: Boolean = true,
    var productName: String = "",
    var productImage: String = "",
    var productId: String = "",
    var productInformation: List<String> = emptyList(),
    var productInformationWithIcon: List<ProductInformationWithIcon> = emptyList(),
    var productAlertMessage: String = "",
    var productPrice: Double = 0.0,
    var productOriginalPrice: Double = 0.0,
    var productInitialPriceBeforeDrop: Double = 0.0,
    var productSlashPriceLabel: String = "",
    var productQtyLeft: String = "", // eg : sisa 3
    var variant: String = "",
    var minOrder: Int = 0,
    var maxOrder: Int = 0,
    var isTokoNow: Boolean = false,
    var wholesalePriceData: List<WholesalePrice> = emptyList(),
    var isPreOrder: Boolean = false,
    var isWishlisted: Boolean = false,
    var cartId: String = "",
    var isCod: Boolean = false,
    var productWeight: Int = 0,
    var parentId: String = "",
    var productCashBack: String = "",
    var quantity: Int = 0,
    var notes: String = "",
    var placeholderNote: String = "",
    var maxNotesLength: Int = 0,
    var isBundlingItem: Boolean = false,
    var isMultipleBundleProduct: Boolean = false,
    var bundlingItemPosition: Int = 0,
    var bundleId: String = "",
    var bundleGroupId: String = "",
    var bundleTitle: String = "",
    var bundlePrice: Double = 0.0,
    var bundleSlashPriceLabel: String = "",
    var bundleOriginalPrice: Double = 0.0,
    var bundleQuantity: Int = 0,
    var originalBundleQuantity: Int = 0,
    var editBundleApplink: String = "",
    var bundleIconUrl: String = "",
    var bundleGrayscaleIconUrl: String = "",
    var bundleLabelQuantity: Int = 0,
    var needPrescription: Boolean = false,
    var butuhResepText: String = "",
    var butuhResepIconUrl: String = "",
    var isFinalItem: Boolean = false,
    var isAlreadyShowMinimumQuantityPurchasedError: Boolean = false,
    var isAlreadyShowMaximumQuantityPurchasedError: Boolean = false,

    // OWOC
    var shopHolderData: CartShopHolderData = CartShopHolderData(),
    var isShopShown: Boolean = false,
    var originWarehouseIds: List<Int> = emptyList(),

    // Analytics data
    var category: String = "",
    var categoryId: String = "",
    var trackerAttribution: String = "",
    var trackerListName: String = "",
    var promoCodes: String = "",
    var promoDetails: String = "",
    var isFreeShippingExtra: Boolean = false,
    var isFreeShipping: Boolean = false,
    var freeShippingName: String = "",
    var campaignId: String = "",
    var originalQty: Int = 0,
    var originalNotes: String = "",
    var warehouseId: String = "",
    var isFulfillment: Boolean = false,
    var bundleType: String = "",
    var shopBoMetadata: BoMetadata = BoMetadata(),
    var shopCartShopGroupTickerData: CartShopGroupTickerData = CartShopGroupTickerData(),
    var bundleIds: List<String> = emptyList(),
    var showErrorBottomDivider: Boolean = false,
    var shouldDivideHalfErrorBottomDivider: Boolean = false,

    // Will be set after calculation
    var wholesalePrice: Double = 0.0,
    var wholesalePriceFormatted: String? = null,

    // AddOns Product
    var addOnsProduct: CartAddOnData = CartAddOnData(),

    // BMGM
    var bmGmCartInfoData: CartDetailInfo = CartDetailInfo(),
    var isBmGmProduct: Boolean = false,
    var isShowBmGmDivider: Boolean = false
) {
    companion object {
        const val BUNDLING_ITEM_DEFAULT = 0
        const val BUNDLING_ITEM_HEADER = 1
        const val BUNDLING_ITEM_FOOTER = 2
    }
}
