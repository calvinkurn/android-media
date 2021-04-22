package com.tokopedia.product.estimasiongkir.data.model.v3

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RangePrice(
        @SerializedName("min_price")
        @Expose
        val minPrice: Int = 0,

        @SerializedName("max_price")
        @Expose
        val maxPrice: Int = 0
)

data class ETD(
        @SerializedName("min_etd")
        @Expose
        val minEtd: Int = 0,

        @SerializedName("max_etd")
        @Expose
        val maxEtd: Int = 0
)

data class ETA(
        @SerializedName("text_eta")
        @Expose
        val textEta: String = ""
)

data class SummaryText(
        @SerializedName("text_min_price")
        @Expose
        val minPrice: String = "",

        @SerializedName("text_destination")
        @Expose
        val destination: String = ""
){
        @Transient var shopCity: String = ""
}

data class Texts(
        @SerializedName("text_range_price")
        @Expose
        val rangePrice: String = "",

        @SerializedName("text_etd")
        @Expose
        val etd: String = "",

        @SerializedName("text_notes")
        @Expose
        val notes: String = "",

        @SerializedName("text_service_notes")
        @Expose
        val serviceNotes: String = "",

        @SerializedName("text_price")
        @Expose
        val price: String = "",

        @SerializedName("text_service_desc")
        @Expose
        val serviceDesc: String = ""
)

data class Price(
        @SerializedName("price")
        @Expose
        val price: Int = 0,

        @SerializedName("formatted_price")
        @Expose
        val priceFmt: String = ""
)

data class Error(
        @SerializedName("error_id")
        @Expose
        val id: String = "",

        @SerializedName("error_message")
        @Expose
        val message: String = ""
)

data class COD(
        @SerializedName("is_cod_available")
        @Expose
        val isCodAvailable: Int = 0,

        @SerializedName("cod_text")
        @Expose
        val text: String = "",

        @SerializedName("cod_price")
        @Expose
        val price: Int = 0,

        @SerializedName("formatted_price")
        @Expose
        val priceFmt: String = ""
)

data class Info(
        @SerializedName("cod_info")
        @Expose
        val codInfo: CodInfo = CodInfo(),

        @SerializedName("blackbox_info")
        @Expose
        val blackboxInfo: BlackboxInfo = BlackboxInfo()
){

    data class CodInfo(
            @SerializedName("failed_message")
            @Expose
            val failedMessage: List<String> = listOf()
    )

    data class BlackboxInfo(
            @SerializedName("text_info")
            @Expose
            val textInfo: String = ""
    )
}