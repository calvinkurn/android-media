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
        var boAffordability: CartShopBoAffordabilityData = CartShopBoAffordabilityData()
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
            boAffordability = this.boAffordability
        )
    }

    companion object {
        const val MAXIMUM_WEIGHT_WORDING_REPLACE_KEY = "{{weight}}"
    }
}

class CartShopBoAffordabilityData(
        var enable: Boolean = true,
        var state: CartShopBoAffordabilityState = CartShopBoAffordabilityState.LOADING,
        var tickerText: String = "",
        var errorText: String = "",

        // list of cartIds for tracker
        var cartIds: String = "",
        var hasSeenTicker: Boolean = false,
)

enum class CartShopBoAffordabilityState {
    LOADING, FAILED, SUCCESS_AFFORD, SUCCESS_NOT_AFFORD, EMPTY
}