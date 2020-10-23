package com.tokopedia.common_tradein.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class TradeInParams(
        @SerializedName("ProductId")
        var productId: Int = 0,
        @SerializedName("ShopId")
        var shopId: Int = 0,
        @SerializedName("CategoryId")
        var categoryId: Int = 0,
        @SerializedName("UserId")
        var userId: Int = 0,
        @SerializedName("DeviceId")
        var deviceId: String? = null,
        @SerializedName("NewPrice")
        var newPrice: Int = 0,
        @SerializedName("IsPreOrder")
        var isPreorder: Boolean = false,
        @SerializedName("IsOnCampaign")
        var isOnCampaign: Boolean = false,
        @SerializedName("TradeInType")
        var tradeInType: Int = 0,
        @SerializedName("ModelId")
        var modelID: Int = 0,
        @SerializedName("WidgetString")
        var widgetString: String? = null,

        var productName: String? = null,
        var usedPrice: Int = 0,
        var remainingPrice: Int = 0,
        var isUseKyc: Int = 0,
        var isEligible: Int = 0,
        var origin: String? = null,
        var productImage: String? = null,
        var weight: Int = 0) : Parcelable {

    fun setPrice(price: Int) {
        newPrice = price
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        val TRADE_IN_PARAMS = TradeInParams::class.java.simpleName
        const val PARAM_NEW_PRICE = "NEW PRICE"
        const val PARAM_DEVICE_ID = "DEVICE ID"
        const val PARAM_PHONE_TYPE = "PHONE TYPE"
        const val PARAM_PHONE_PRICE = "PHONE PRICE"
        const val PARAM_USER_ID = "USER ID"
        const val PARAM_PRODUCT_ID = "PRODUCT ID"
        const val PARAM_NEW_DEVICE_NAME = "NEW DEVICE NAME"
        const val PARAM_USE_KYC = "USE KYC"
        const val PARAM_PERMISSION_GIVEN = "PERMISSION GIVEN"
        const val HANDFONE_ID = 24
        const val HANDFONE = "Handphone"
    }
}