package com.tokopedia.oneclickcheckout.order.data.get

import com.google.gson.annotations.SerializedName
import com.tokopedia.purchase_platform.common.constant.LogisticConstant.BO_TYPE_PLUS
import com.tokopedia.purchase_platform.common.constant.LogisticConstant.BO_TYPE_PLUS_DT

class ShipmentInformationResponse(
        @SerializedName("shop_location")
        val shopLocation: String = "",
        @SerializedName("free_shipping")
        val freeShipping: FreeShipping = FreeShipping(),
        @SerializedName("free_shipping_extra")
        val freeShippingExtra: FreeShipping = FreeShipping(),
        @SerializedName("free_shipping_general")
        val freeShippingGeneral: FreeShippingGeneral = FreeShippingGeneral(),
        @SerializedName("preorder")
        val preorder: PreOrder = PreOrder()
)

class FreeShipping(
        @SerializedName("eligible")
        val eligible: Boolean = false,
        @SerializedName("badge_url")
        val badgeUrl: String = ""
)

class FreeShippingGeneral(
        @SerializedName("badge_url")
        val badgeUrl: String = "",
        @SerializedName("bo_type")
        val boType: Int = 0,
        @SerializedName("bo_name")
        val boName: String = "",
) {
    fun isBoTypePlus(): Boolean {
        return boType == BO_TYPE_PLUS || boType == BO_TYPE_PLUS_DT
    }
}

class PreOrder(
        @SerializedName("is_preorder")
        val isPreorder: Boolean = false,
        @SerializedName("duration")
        val duration: String = ""
)