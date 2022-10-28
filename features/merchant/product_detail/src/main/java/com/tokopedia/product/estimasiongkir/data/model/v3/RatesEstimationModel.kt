package com.tokopedia.product.estimasiongkir.data.model.v3

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RatesEstimationModel(
        @SerializedName("shop")
        @Expose
        val shop: ShopModel = ShopModel(),

        @SerializedName("address")
        @Expose
        val address: AddressModel = AddressModel(),

        @SerializedName("texts")
        @Expose
        val texts: SummaryText = SummaryText(),

        @SerializedName("rates")
        @Expose
        val rates: RatesModel = RatesModel(),

        @SerializedName("tokocabang_from")
        @Expose
        val tokoCabangData: TokoCabangData = TokoCabangData(),

        @SerializedName("free_shipping")
        @Expose
        val freeShipping: FreeShipping = FreeShipping(),

        @SerializedName("is_blackbox")
        @Expose
        val isBlackbox: Boolean = false
){
    data class Data(
            @SerializedName("data")
            @Expose
            val data: RatesEstimationModel? = RatesEstimationModel()
    )

    data class Response (
            @SerializedName("ratesEstimateV3")
            @Expose
            val data: Data? = Data()
    )
}

data class TokoCabangData(
        @SerializedName("icon_url")
        @Expose
        val iconUrl: String = "",

        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("content")
        @Expose
        val content: String = "",
)

data class FreeShipping(
        @SerializedName("flag")
        @Expose
        val flag: Int = 0,

        @SerializedName("shipping_price")
        @Expose
        val shippingPrice: String = "",

        @SerializedName("eta_text")
        @Expose
        val etaText: String = "",

        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("desc")
        @Expose
        val desc: String = "",

        @SerializedName("raw_shipping_rate")
        @Expose
        val rawShippingRate: Double = 0.0,

        @SerializedName("is_quota_empty")
        @Expose
        val isQuotaEmpty: Boolean = false,

        @SerializedName("free_shipping_bottomsheet")
        @Expose
        val freeShippingEtas: List<Eta> = emptyList()
){
        data class Eta(
                @SerializedName("shipping_price")
                @Expose
                val shippingPrice: String = "",

                @SerializedName("eta_text")
                @Expose
                val etaText: String = "",

                @SerializedName("raw_shipping_rate")
                @Expose
                val rawShippingRate: Double = 0.0
        )
}