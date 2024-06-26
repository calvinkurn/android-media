package com.tokopedia.sellerorder.detail.data.model

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SomDynamicPriceResponse(
    @SuppressLint("Invalid Data Type")
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
        val pricingData: List<PricingData> = listOf(),
        @Expose
        @SerializedName("promo_shipping")
        val promoShipping: PromoShipping? = null,
        @SerializedName("pof_data")
        val pofData: PofData? = null,
        @SerializedName("note")
        val note: String? = "",
        @SerializedName("income_detail_label")
        val incomeDetailLabel: String? = ""
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
            @SerializedName("value")
            val value: String = ""
        )

        data class PricingData(
            @Expose
            @SerializedName("label")
            val label: String = "",
            @Expose
            @SerializedName("value")
            val value: String = ""
        )

        data class PromoShipping(
            @Expose
            @SerializedName("label")
            val label: String? = "",
            @Expose
            @SerializedName("value")
            val value: String? = "",
            @Expose
            @SerializedName("value_detail")
            val valueDetail: String? = ""
        )

        data class PofData(
            @SerializedName("label")
            val label: String = "",
            @SerializedName("value")
            val value: String = "",
            @SerializedName("header")
            val header: String = "",
            @SerializedName("footer")
            val footer: String = ""
        )
    }
}
