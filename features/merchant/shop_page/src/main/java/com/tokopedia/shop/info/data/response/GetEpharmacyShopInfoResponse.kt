package com.tokopedia.shop.info.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetEpharmacyShopInfoResponse(
    @SerializedName("getEpharmacyShopInfo")
    @Expose
    val getEpharmacyShopInfo: GetEpharmacyShopInfoData = GetEpharmacyShopInfoData()
) {

    data class GetEpharmacyShopInfoData(
        @SerializedName("header")
        @Expose
        val header: Header = Header(),
        @SerializedName("data")
        @Expose
        val dataEpharm: DataEpharm = DataEpharm()
    ) {

        data class DataEpharm(
            @SerializedName("apj")
            @Expose
            val apj: String = "",
            @SerializedName("epharmacy_working_hours_fmt")
            @Expose
            val epharmacyWorkingHoursFmt: List<String> = listOf(),
            @SerializedName("sia_number")
            @Expose
            val siaNumber: String = "",
            @SerializedName("sipa_number")
            @Expose
            val sipaNumber: String = ""
        )

        data class Header(
            @SerializedName("error_code")
            @Expose
            val errorCode: Int = 0,
            @SerializedName("error_message")
            @Expose
            val errorMessage: List<String> = listOf(),
            @SerializedName("process_time")
            @Expose
            val processTime: Double = 0.0
        )
    }
}
