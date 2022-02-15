package com.tokopedia.oneclickcheckout.order.view.model

import com.tokopedia.logisticcart.shipping.model.AddOnsDataModel
import com.tokopedia.logisticcart.shipping.model.ShopShipment
import com.tokopedia.purchase_platform.common.feature.bometadata.BoMetadata

data class OrderShop(
        var shopId: Long = 0,
        var shopName: String = "",
        var shopBadge: String = "",
        var shopTier: Int = 0,
        var shopTypeName: String = "",
        var shopType: String = "",
        var isGold: Int = 0,
        var isOfficial: Int = 0,
        var postalCode: String = "",
        var latitude: String = "",
        var longitude: String = "",
        var districtId: String = "",
        var cityName: String = "",
        var warehouseId: Long = 0,
        var isFulfillment: Boolean = false,
        var fulfillmentBadgeUrl: String = "",
        var shopShipment: List<ShopShipment> = emptyList(),
        var errors: List<String> = emptyList(),
        var isFreeOngkir: Boolean = false,
        var isFreeOngkirExtra: Boolean = false,
        var freeOngkirImg: String = "",
        var preOrderLabel: String = "",
        var shopAlertMessage: String = "",
        var unblockingErrorMessage: String = "",
        var firstProductErrorIndex: Int = -1,
        var shopTicker: String = "",
        var isTokoNow: Boolean = false,
        var maximumWeight: Long = 0,
        var maximumWeightWording: String = "",
        var overweight: Double = 0.0,
        var boMetadata: BoMetadata = BoMetadata(),
        var addOns: AddOnsDataModel = AddOnsDataModel(),

        // Analytics
        var hasTriggerViewOverweightTicker: Boolean = false,
        var hasTriggerViewErrorOrderLevelTicker: Boolean = false
) {

    val isError: Boolean
        get() = errors.isNotEmpty()

    fun shouldValidateWeight(): Boolean {
        return maximumWeight > 0 && maximumWeightWording.isNotBlank()
    }

    companion object {
        const val MAXIMUM_WEIGHT_WORDING_REPLACE_KEY = "{{weight}}"
        const val WEIGHT_KG_DIVIDER = 1000.0
    }
}