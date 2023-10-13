package com.tokopedia.shop.info.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetEpharmacyShopInfoResponse(
    @SerializedName("data")
    @Expose
    val data: Data = Data()
) {

    data class Data(
        @SerializedName("getEpharmacyShopInfo")
        @Expose
        val getEpharmacyShopInfo: GetEpharmacyShopInfo = GetEpharmacyShopInfo()
    )

    data class GetEpharmacyShopInfo(
        @SerializedName("header")
        @Expose
        val header: Header = Header(),

        @SerializedName("data")
        @Expose
        val data: DetailData = DetailData()
    ) {

        data class Header(
            @SerializedName("process_time")
            @Expose
            val processTime: Float = 0.0f,

            @SerializedName("error_code")
            @Expose
            val errorCode: Int = 0,

            @SerializedName("error_message")
            @Expose
            val errorMessage: List<String> = emptyList()
        )

        data class DetailData(
            @SerializedName("sia_number")
            @Expose
            val siaNumber: String = "",

            @SerializedName("sipa_number")
            @Expose
            val sipaNumber: String = "",

            @SerializedName("apj")
            @Expose
            val apj: String = "",

            @SerializedName("epharmacy_working_hours_fmt")
            @Expose
            val epharmacyWorkingHoursFmt: List<String> = emptyList()
        )
    }
}
