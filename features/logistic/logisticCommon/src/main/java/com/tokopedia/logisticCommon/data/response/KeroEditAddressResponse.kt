package com.tokopedia.logisticCommon.data.response

import com.google.gson.annotations.SerializedName

data class KeroEditAddressResponse(

    @SerializedName("data")
    val data: Data = Data()
) {
    data class Data(

        @SerializedName("kero_edit_address")
        val keroEditAddress: KeroEditAddress = KeroEditAddress()
    ) {
        data class KeroEditAddress(

            @SerializedName("data")
            val data: KeroEditAddressSuccessResponse = KeroEditAddressSuccessResponse(),

            @SerializedName("server_process_time")
            val serverProcessTime: String = "",

            @SerializedName("config")
            val config: String = "",

            @SerializedName("status")
            val status: String = ""
        ) {
            data class KeroEditAddressSuccessResponse(

                @SerializedName("is_success")
                val isSuccess: Int = 0,

                @SerializedName("is_state_chosen_address_changed")
                val isStateChosenAddressChanged: Boolean = false,

                @SerializedName("chosen_address")
                var chosenAddressData: KeroAddrStateChosenAddressData = KeroAddrStateChosenAddressData(),
                @SerializedName("tokonow")
                var tokonow: KeroAddressRespTokonow = KeroAddressRespTokonow()
            )
        }
    }
}
