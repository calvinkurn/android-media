package com.tokopedia.tradein.model


import com.google.gson.annotations.SerializedName

data class ShopInfoDataModel(
        @SerializedName("shopInfoByID")
        val shopInfoByID: ShopInfoByID
) {
    data class ShopInfoByID(
            @SerializedName("error")
            val error: Error,
            @SerializedName("result")
            val result: List<Result>
    ) {
        data class Error(
                @SerializedName("message")
                val message: String
        )

        data class Result(
                @SerializedName("shipmentInfo")
                val shipmentInfo: List<ShipmentInfo>
        ) {
            data class ShipmentInfo(
                    @SerializedName("awbStatus")
                    val awbStatus: Int,
                    @SerializedName("code")
                    val code: String,
                    @SerializedName("image")
                    val image: String,
                    @SerializedName("isAvailable")
                    val isAvailable: Int,
                    @SerializedName("isPickup")
                    val isPickup: Int,
                    @SerializedName("maxAddFee")
                    val maxAddFee: Int,
                    @SerializedName("name")
                    val name: String,
                    @SerializedName("shipmentID")
                    val shipmentID: String
            )
        }
    }
}
