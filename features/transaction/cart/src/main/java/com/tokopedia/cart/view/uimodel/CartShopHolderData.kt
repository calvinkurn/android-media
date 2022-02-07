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
    LOADING, FAILED, SUCCESS_AFFORD, SUCCESS_NOT_AFFORD
}