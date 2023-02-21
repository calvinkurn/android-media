package com.tokopedia.oneclickcheckout.order.view.model

import com.tokopedia.logisticcart.shipping.model.ShopShipment
import com.tokopedia.purchase_platform.common.feature.bometadata.BoMetadata
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnsDataModel

data class OrderShop(
        var shopId: String = "",
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
        var warehouseId: String = "",
        var isFulfillment: Boolean = false,
        var fulfillmentBadgeUrl: String = "",
        var shopShipment: List<ShopShipment> = emptyList(),
        var errors: List<String> = emptyList(),
        var isFreeOngkir: Boolean = false,
        var isFreeOngkirExtra: Boolean = false,
        var freeOngkirImg: String = "",
        var isFreeOngkirPlus: Boolean = false, // flag for plus badge tracker
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
        var addOn: AddOnsDataModel = AddOnsDataModel(),

        // Analytics
        var hasTriggerViewOverweightTicker: Boolean = false,
        var hasTriggerViewErrorOrderLevelTicker: Boolean = false
) {

    val isError: Boolean
        get() = errors.isNotEmpty()

    // Get String Merchant Type For Payment Param
    val merchantType: String
        get() = when (shopTier) {
            0 -> MERCHANT_TYPE_REGULAR_MERCHANT
            1 -> MERCHANT_TYPE_POWER_MERCHANT
            2 -> MERCHANT_TYPE_OFFICIAL_STORE
            3 -> MERCHANT_TYPE_POWER_MERCHANT_PRO
            else -> ""
        }

    fun shouldValidateWeight(): Boolean {
        return maximumWeight > 0 && maximumWeightWording.isNotBlank()
    }

    companion object {
        const val MAXIMUM_WEIGHT_WORDING_REPLACE_KEY = "{{weight}}"
        const val WEIGHT_KG_DIVIDER = 1000.0

        //0 = RM, 1 = PM, 2 = OS, 3 = PMPRO
        private const val MERCHANT_TYPE_REGULAR_MERCHANT = "RM"
        private const val MERCHANT_TYPE_POWER_MERCHANT = "PM"
        private const val MERCHANT_TYPE_OFFICIAL_STORE = "OS"
        private const val MERCHANT_TYPE_POWER_MERCHANT_PRO = "PMPRO"
    }
}
