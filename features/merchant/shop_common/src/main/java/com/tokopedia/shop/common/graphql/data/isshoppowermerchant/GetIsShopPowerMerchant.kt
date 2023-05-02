package com.tokopedia.shop.common.graphql.data.isshoppowermerchant

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.shop.common.data.model.ShopInfoData
import com.tokopedia.shop.common.data.source.cloud.model.FreeOngkir

data class GetIsShopPowerMerchant(
        @SerializedName("data")
        @Expose
        val data: Data = Data(),
        @SerializedName("header")
        @Expose
        val messageError: Header = Header()
) {

    data class Response(
            @SerializedName("goldGetPMOSStatus")
            val result: GetIsShopPowerMerchant = GetIsShopPowerMerchant()
    )

    data class Data(
            @SerializedName("shopID")
            @Expose
            val shopId: String = "",
            @SerializedName("power_merchant")
            @Expose
            val powerMerchant: PowerMerchant = PowerMerchant()
    ) {
        data class PowerMerchant(
                @SerializedName("status")
                @Expose
                val status: String = "",
                @SerializedName("pm_tier")
                @Expose
                val pmTier: Int = 0,
                @SerializedName("auto_extend")
                @Expose
                val autoExtend: AutoExtend = AutoExtend(),
                @SerializedName("expired_time")
                @Expose
                val expiredTime: String = "",
                @SerializedName("shop_popup")
                @Expose
                val shopPopup: Boolean = false
        ){
            data class AutoExtend(
                    @SerializedName("status")
                    @Expose
                    val status: String = "",
                    @SerializedName("tkpd_product_id")
                    @Expose
                    val tkpdProductId: String = ""
            )
        }
    }

    data class Header(
            @SerializedName("process_time")
            @Expose
            val isOfficial: Float = 0f,
            @SerializedName("messages")
            @Expose
            val messages: List<String> = listOf(),
            @SerializedName("reason")
            @Expose
            val reason: String = "",
            @SerializedName("error_code")
            @Expose
            val errorCode: String = ""
    )
}
