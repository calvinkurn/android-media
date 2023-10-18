package com.tokopedia.logisticCommon.data.response

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class KeroGetAddressResponse(

    @SerializedName("data")
    val data: Data = KeroGetAddressResponse.Data()
) {
    data class Data(

        @SerializedName("kero_get_address")
        val keroGetAddress: KeroGetAddress = KeroGetAddress()
    ) {
        data class KeroGetAddress(

            @SerializedName("data")
            val data: List<DetailAddressResponse> = emptyList(),

            @SerializedName("server_process_time")
            val serverProcessTime: String = "",

            @SerializedName("config")
            val config: String = "",

            @SerializedName("status")
            val status: String = ""
        ) {
            data class DetailAddressResponse(

                @SerializedName("country")
                val country: String = "",

                @SerializedName("is_active")
                val isActive: Boolean = false,

                @SerializedName("is_corner")
                val isCorner: Boolean = false,

                @SerializedName("is_state_chosen_address")
                val isStateChosenAddress: Boolean = false,

                @SerializedName("radio_button_checked")
                val radioButtonChecked: Boolean = false,

                @SerializedName("is_primary")
                val isPrimary: Boolean = false,

                @SerializedName("city")
                val city: Long = 0,

                @SerializedName("address_1")
                val address1: String = "",

                @SerializedName("address_2")
                val address2: String = "",

                @SerializedName("latitude")
                val latitude: String = "",

                @SerializedName("province_name")
                val provinceName: String = "",

                @SerializedName("addr_name")
                val addrName: String = "",

                @SerializedName("district_name")
                val districtName: String = "",

                @SerializedName("city_name")
                val cityName: String = "",

                @SerializedName("province")
                val province: Long = 0,

                @SerializedName("is_whitelist")
                val isWhitelist: Boolean = false,

                @SerializedName("phone")
                val phone: String = "",

                @SerializedName("district")
                val district: Long = 0,

                @SerializedName("receiver_name")
                val receiverName: String = "",

                @SerializedName("partner_name")
                val partnerName: String = "",

                @SerializedName("postal_code")
                val postalCode: String = "",

                @SuppressLint("Invalid Data Type")
                @SerializedName("addr_id")
                val addrId: Long = 0,

                @SuppressLint("Invalid Data Type")
                @SerializedName("partner_id")
                val partnerId: Long = 0,

                @SerializedName("status")
                val status: Int = 0,

                @SerializedName("type")
                val type: Int = 0,

                @SerializedName("state")
                val state: Int = 0,

                @SerializedName("state_detail")
                val stateDetail: String = "",

                @SerializedName("longitude")
                val longitude: String = "",

                @SerializedName("address_detail_street")
                val addressDetailStreet: String = "",

                @SerializedName("address_detail_notes")
                val addressDetailNotes: String = ""
            )
        }
    }
}
