package com.tokopedia.shop.info.data.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductShopPackSpeed(
        @SerializedName("classification")
        @Expose
        val classification: Classification = Classification(),

        @SerializedName("day")
        @Expose
        val day: Int = 0,

        @SerializedName("hour")
        @Expose
        val hour: Int = 0,

        @SerializedName("speedFmt")
        @Expose
        val speedFmt: String = "",

        @SerializedName("totalOrder")
        @Expose
        val totalOrder: Int = 0
){
        data class Response(
                @SerializedName("ProductShopPackSpeedQuery")
                @Expose
                val productShopPackSpeed: ProductShopPackSpeed = ProductShopPackSpeed()
        )

        data class Classification(
                @SerializedName("demand")
                @Expose
                val demand: ClassificationSpeed = ClassificationSpeed(),

                @SerializedName("regular")
                @Expose
                val regular: ClassificationSpeed = ClassificationSpeed()
        )

        data class ClassificationSpeed(
                @SerializedName("day")
                @Expose
                val day: Int = 0,

                @SerializedName("hour")
                @Expose
                val hour: Int = 0,

                @SerializedName("speedFmt")
                @Expose
                val speedFmt: String = "",

                @SerializedName("totalOrder")
                @Expose
                val totalOrder: Int = 0
        )
}