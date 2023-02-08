package com.tokopedia.cart.view.uimodel

import com.tokopedia.cart.data.model.response.shopgroupsimplified.ShopTypeInfo
import com.tokopedia.logisticcart.shipping.model.ShopShipment
import com.tokopedia.purchase_platform.common.feature.bometadata.BoMetadata

data class CartShopHolderData(
    var cartString: String = "",
    var shopId: String = "",
    var shopName: String = "",
    var shopTypeInfo: ShopTypeInfo = ShopTypeInfo(),
    var isFulfillment: Boolean = false,
    var fulfillmentName: String = "",
    var fulfillmentBadgeUrl: String = "",
    var estimatedTimeArrival: String = "",
    var productUiModelList: MutableList<CartItemHolderData> = ArrayList(),
    var isShowPin: Boolean = false,
    var pinCoachmarkMessage: String = "",
    var isTokoNow: Boolean = false,
    var preOrderInfo: String = "",
    var incidentInfo: String = "",
    var isFreeShippingExtra: Boolean = false,
    var freeShippingBadgeUrl: String = "",
    var isFreeShippingPlus: Boolean = false, // flag for plus badge tracker
    var hasSeenFreeShippingBadge: Boolean = false, // flag for tracker
    var maximumWeightWording: String = "",
    var maximumShippingWeight: Double = 0.0,
    var totalWeight: Double = 0.0,
    var isAllSelected: Boolean = false,
    var isPartialSelected: Boolean = false,
    var isCollapsible: Boolean = false,
    var isCollapsed: Boolean = false,
    var clickedCollapsedProductIndex: Int = -1,
    var isNeedToRefreshWeight: Boolean = false,
    var isError: Boolean = false,
    var promoCodes: List<String> = emptyList(),
    var shopShipments: List<ShopShipment> = emptyList(),
    var districtId: String = "",
    var postalCode: String = "",
    var latitude: String = "",
    var longitude: String = "",
    var boMetadata: BoMetadata = BoMetadata(),
    var cartShopGroupTicker: CartShopGroupTickerData = CartShopGroupTickerData(),
    var addOnText: String = "",
    var addOnImgUrl: String = "",
    var addOnId: String = "",
    var warehouseId: Long = 0,
    var isPo: Boolean = false,
    var poDuration: String = "",
    var boCode: String = "",
    var coachmarkPlus: CartShopCoachmarkPlusData = CartShopCoachmarkPlusData(),
) {
    val shouldValidateWeight: Boolean
        get() = maximumShippingWeight > 0.0 && maximumWeightWording.isNotEmpty()

    val isOverweight: Boolean
        get() = shouldValidateWeight && totalWeight > maximumShippingWeight

    val hasSelectedProduct: Boolean
        get() = isAllSelected || isPartialSelected

    fun deepCopy(): CartShopHolderData {
        return CartShopHolderData(
            cartString = this.cartString,
            shopId = this.shopId,
            shopName = this.shopName,
            shopTypeInfo = this.shopTypeInfo,
            isFulfillment = this.isFulfillment,
            fulfillmentName = this.fulfillmentName,
            fulfillmentBadgeUrl = this.fulfillmentBadgeUrl,
            estimatedTimeArrival = this.estimatedTimeArrival,
            productUiModelList = this.productUiModelList.toMutableList(),
            isShowPin = this.isShowPin,
            pinCoachmarkMessage = this.pinCoachmarkMessage,
            isTokoNow = this.isTokoNow,
            preOrderInfo = this.preOrderInfo,
            incidentInfo = this.incidentInfo,
            isFreeShippingExtra = this.isFreeShippingExtra,
            freeShippingBadgeUrl = this.freeShippingBadgeUrl,
            maximumWeightWording = this.maximumWeightWording,
            maximumShippingWeight = this.maximumShippingWeight,
            totalWeight = this.totalWeight,
            isAllSelected = this.isAllSelected,
            isPartialSelected = this.isPartialSelected,
            isCollapsible = this.isCollapsible,
            isCollapsed = this.isCollapsed,
            clickedCollapsedProductIndex = this.clickedCollapsedProductIndex,
            isNeedToRefreshWeight = this.isNeedToRefreshWeight,
            isError = this.isError,
            promoCodes = this.promoCodes.toMutableList(),
            shopShipments = this.shopShipments.toMutableList(),
            districtId = this.districtId,
            postalCode = this.postalCode,
            latitude = this.latitude,
            longitude = this.longitude,
            boMetadata = this.boMetadata,
            cartShopGroupTicker = this.cartShopGroupTicker,
            addOnText = this.addOnText,
            addOnImgUrl = this.addOnImgUrl,
            addOnId = this.addOnId,
            warehouseId = this.warehouseId,
            isPo = this.isPo,
            poDuration = this.poDuration,
            boCode = this.boCode
        )
    }

    companion object {
        const val MAXIMUM_WEIGHT_WORDING_REPLACE_KEY = "{{weight}}"
    }
}

data class CartShopGroupTickerData(
    var enableBoAffordability: Boolean = false,
    var enableCartAggregator: Boolean = false,
    var enableBundleCrossSell: Boolean = false,
    var state: CartShopGroupTickerState = CartShopGroupTickerState.FIRST_LOAD,
    var tickerText: String = "",
    var errorText: String = "",
    var leftIcon: String = "",
    var leftIconDark: String = "",
    var rightIcon: String = "",
    var rightIconDark: String = "",
    var applink: String = "",
    var action: String = "",
    var cartBundlingBottomSheetData: CartBundlingBottomSheetData = CartBundlingBottomSheetData(),

    // list of cartIds for tracker
    var cartIds: String = "",
    var hasSeenTicker: Boolean = false
) {

    companion object {
        const val ACTION_REDIRECT_PAGE = "redirect_page"
        const val ACTION_OPEN_BOTTOM_SHEET_BUNDLING = "open_bottomsheet_bundling"
    }
}

class CartShopCoachmarkPlusData(
    val isShown: Boolean = false,
    val title: String = "",
    val content: String = "",
)

enum class CartShopGroupTickerState {
    FIRST_LOAD, LOADING, FAILED, SUCCESS_AFFORD, SUCCESS_NOT_AFFORD, EMPTY
}
