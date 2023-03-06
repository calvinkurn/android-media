package com.tokopedia.product.estimasiongkir.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ScheduledDeliveryRatesModel(
    @SerializedName("delivery_services")
    @Expose
    val deliveryServices: List<DeliveryService> = emptyList()
) {
    data class Response(
        @SerializedName("ongkirGetScheduledDeliveryRates")
        @Expose
        val data: Data = Data()
    )

    data class Data(
        @SerializedName("data")
        @Expose
        val data: ScheduledDeliveryRatesModel = ScheduledDeliveryRatesModel()
    )
}

data class DeliveryService(
    @SerializedName("title")
    @Expose
    val title: String = "",

    @SerializedName("title_label")
    @Expose
    val titleLabel: String = "",

    @SerializedName("available")
    @Expose
    val isAvailable: Boolean = false,

    @SerializedName("hidden")
    @Expose
    val isHidden: Boolean = false,

    @SerializedName("delivery_products")
    @Expose
    val deliveryProducts: List<DeliveryProduct> = emptyList()
)

data class DeliveryProduct(
    @SerializedName("title")
    @Expose
    val title: String = "",

    @SerializedName("available")
    @Expose
    val isAvailable: Boolean = false,

    @SerializedName("hidden")
    @Expose
    val isHidden: Boolean = false,

    @SerializedName("recommend")
    @Expose
    val isRecommend: Boolean = false,

    @SerializedName("text")
    @Expose
    val text: String = "",

    @SerializedName("text_real_price")
    @Expose
    val textRealPrice: String = "",

    @SerializedName("text_final_price")
    @Expose
    val textFinalPrice: String = ""
)
