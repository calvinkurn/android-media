package com.tokopedia.logisticCommon.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class KeroEditAddressResponse(

    @Expose
    @SerializedName("data")
    val data: Data = Data()
) {
    data class Data(

        @Expose
        @SerializedName("kero_edit_address")
        val keroEditAddress: KeroEditAddress = KeroEditAddress(),
    ) {
        data class KeroEditAddress(

            @Expose
            @SerializedName("data")
            val data: KeroEditAddressSuccessResponse = KeroEditAddressSuccessResponse(),

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
            data class KeroEditAddressSuccessResponse(

                @Expose
                @SerializedName("is_success")
                val isSuccess: Int = 0
            )
        }
    }
}
