package com.tokopedia.product.manage.feature.list.data.model


import com.google.gson.annotations.SerializedName

data class ShopWarehouseResponse(
    @SerializedName("keroWarehouseShop")
    val keroWarehouseShop: KeroWarehouseShop
) {
    data class KeroWarehouseShop(
        @SerializedName("config")
        val config: String? = null,
        @SerializedName("data")
        val `data`: Data? = null,
        @SerializedName("server_process_time")
        val serverProcessTime: String? = null,
        @SerializedName("status")
        val status: String? = null
    ) {
        data class Data(
            @SerializedName("fulfillment")
            val fulfillment: List<Fulfillment>?
        ) {
            data class Fulfillment(
                @SerializedName("partner_id")
                val partnerId: Int?,
                @SerializedName("partner_name")
                val partnerName: String?,
                @SerializedName("status")
                val status: Int?
            )
        }
    }
}
