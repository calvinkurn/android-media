package com.tokopedia.sellerorder.detail.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SomDynamicPriceResponse(
        @Expose
        @SerializedName("get_som_dynamic_price")
        val getSomDynamicPrice: GetSomDynamicPrice = GetSomDynamicPrice()
) {
    data class GetSomDynamicPrice(
            @Expose
            @SerializedName("payment_data")
            val paymentData: PaymentData = PaymentData(),
            @Expose
            @SerializedName("payment_method")
            val paymentMethod: List<PaymentMethod> = listOf(),
            @Expose
            @SerializedName("pricing_data")
            val pricingData: List<PricingData> = listOf()
    ) {
        data class PaymentMethod(
                @Expose
                @SerializedName("label")
                val label: String = "",
                @Expose
                @SerializedName("value")
                val value: String = ""
        )

        data class PaymentData(
                @Expose
                @SerializedName("label")
                val label: String = "",
                @Expose
                @SerializedName("text_color")
                val textColor: String = "",
                @Expose
                @SerializedName("value")
                val value: String = ""
        )

        data class PricingData(
                @Expose
                @SerializedName("label")
                val label: String = "",
                @Expose
                @SerializedName("text_color")
                val textColor: String = "",
                @Expose
                @SerializedName("value")
                val value: String = ""
        )
    }
}