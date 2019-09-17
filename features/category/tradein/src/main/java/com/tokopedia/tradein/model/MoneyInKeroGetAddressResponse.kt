package com.tokopedia.tradein.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tokopedia.logisticaddaddress.domain.model.Token
import kotlinx.android.parcel.Parcelize

data class MoneyInKeroGetAddressResponse(
        @SerializedName("data")
        val data: ResponseData
) {
    data class ResponseData(
            @SerializedName("keroAddressCorner")
            val keroGetAddress: KeroGetAddress
    ) {
        data class KeroGetAddress(
                @SerializedName("data")
                val data: List<Data>,
                @SerializedName("config")
                val config: String,
                @SerializedName("server_process_time")
                val serverProcessTime: String,
                @SerializedName("status")
                val status: String,
                @SerializedName("token")
                val token: Token
        ) {
            @Parcelize
            data class Data(
                    @SerializedName("addr_id")
                    val addrId: Int,
                    @SerializedName("addr_name")
                    val addrName: String,
                    @SerializedName("address_1")
                    val address1: String,
                    @SerializedName("address_2")
                    val address2: String,
                    @SerializedName("city")
                    val city: Int,
                    @SerializedName("city_name")
                    val cityName: String,
                    @SerializedName("country")
                    val country: String,
                    @SerializedName("district")
                    val district: Int,
                    @SerializedName("district_name")
                    val districtName: String?,
                    @SerializedName("is_active")
                    val isActive: Boolean,
                    @SerializedName("is_primary")
                    val isPrimary: Boolean,
                    @SerializedName("is_whitelist")
                    val isWhitelist: Boolean,
                    @SerializedName("latitude")
                    val latitude: String?,
                    @SerializedName("longitude")
                    val longitude: String?,
                    @SerializedName("phone")
                    val phone: String,
                    @SerializedName("postal_code")
                    val postalCode: String,
                    @SerializedName("province")
                    val province: Int,
                    @SerializedName("province_name")
                    val provinceName: String?,
                    @SerializedName("receiver_name")
                    val receiverName: String,
                    @SerializedName("status")
                    val status: Int
            ) : Parcelable
        }
    }
}
