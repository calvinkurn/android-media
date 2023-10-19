package com.tokopedia.shop.info.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetEpharmacyShopInfo(
    @SerializedName("data")
    @Expose
    val data: Data = Data()
)

data class Data(
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
            val sipaNumber: String = "",
            @SerializedName("epharmacy_working_hours")
            @Expose
            val epharmacyWorkingHours: List<EpharmacyWorkingHour> = listOf()
        )

        data class EpharmacyWorkingHour(
            @SerializedName("days")
            @Expose
            val days: List<String> = listOf(),
            @SerializedName("opening_hours")
            @Expose
            val openingHours: OpeningHours = OpeningHours()
        )

        data class OpeningHours(
            @SerializedName("open_time")
            @Expose
            val openTime: String = "",
            @SerializedName("close_time")
            @Expose
            val closeTime: String = ""
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
