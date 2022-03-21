package com.tokopedia.logisticCommon.data.response

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class KeroGetAddressResponse(

    @Expose
    @SerializedName("data")
    val data: Data = KeroGetAddressResponse.Data()
) {
    data class Data(

        @Expose
        @SerializedName("kero_get_address")
        val keroGetAddress: KeroGetAddress = KeroGetAddress()
    ) {
        data class KeroGetAddress(

            @Expose
            @SerializedName("data")
            val data: List<DetailAddressResponse> = emptyList(),

            @Expose
            @SerializedName("server_process_time")
            val serverProcessTime: String = "",

            @Expose
            @SerializedName("config")
            val config: String = "",

            @Expose
            @SerializedName("status")
            val status: String = ""
        ) {
            data class DetailAddressResponse(

                @Expose
                @SerializedName("country")
                val country: String = "",

                @Expose
                @SerializedName("is_active")
                val isActive: Boolean = false,

                @Expose
                @SerializedName("is_corner")
                val isCorner: Boolean = false,

                @Expose
                @SerializedName("is_state_chosen_address")
                val isStateChosenAddress: Boolean = false,

                @Expose
                @SerializedName("radio_button_checked")
                val radioButtonChecked: Boolean = false,

                @Expose
                @SerializedName("is_primary")
                val isPrimary: Boolean = false,

                @Expose
                @SerializedName("city")
                val city: Long = 0,

                @Expose
                @SerializedName("address_1")
                val address1: String = "",

                @Expose
                @SerializedName("address_2")
                val address2: String = "",

                @Expose
                @SerializedName("latitude")
                val latitude: String = "",

                @Expose
                @SerializedName("province_name")
                val provinceName: String = "",

                @Expose
                @SerializedName("addr_name")
                val addrName: String = "",

                @Expose
                @SerializedName("district_name")
                val districtName: String = "",

                @Expose
                @SerializedName("city_name")
                val cityName: String = "",

                @Expose
                @SerializedName("province")
                val province: Long = 0,

                @Expose
                @SerializedName("is_whitelist")
                val isWhitelist: Boolean = false,

                @Expose
                @SerializedName("phone")
                val phone: String = "",

                @Expose
                @SerializedName("district")
                val district: Long = 0,

                @Expose
                @SerializedName("receiver_name")
                val receiverName: String = "",

                @Expose
                @SerializedName("partner_name")
                val partnerName: String = "",

                @Expose
                @SerializedName("postal_code")
                val postalCode: String = "",

                @SuppressLint("Invalid Data Type")
                @Expose
                @SerializedName("addr_id")
                val addrId: Long = 0,

                @SuppressLint("Invalid Data Type")
                @Expose
                @SerializedName("partner_id")
                val partnerId: Long = 0,

                @Expose
                @SerializedName("status")
                val status: Int = 0,

                @Expose
                @SerializedName("type")
                val type: Int = 0,

                @Expose
                @SerializedName("state")
                val state: Int = 0,

                @Expose
                @SerializedName("state_detail")
                val stateDetail: String = "",

                @Expose
                @SerializedName("longitude")
                val longitude: String = ""
            )
        }
    }
}
