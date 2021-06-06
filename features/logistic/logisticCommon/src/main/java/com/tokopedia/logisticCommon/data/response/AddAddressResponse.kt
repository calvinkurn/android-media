package com.tokopedia.logisticCommon.data.response

import com.google.gson.annotations.SerializedName

data class AddAddressResponse (
        @SerializedName("kero_add_address")
        val keroAddAddress: KeroAddAddress = KeroAddAddress()
)

data class KeroAddAddress(
        @SerializedName("data")
        val data: DataAddAddress = DataAddAddress(),

        @SerializedName("server_process_time")
        val serverProcessTime: String = "",

        @SerializedName("config")
        val config: String = "",

        @SerializedName("status")
        val status: String = ""
)

data class DataAddAddress(
        @SerializedName("is_success")
        val isSuccess: Int = 0,

        @SerializedName("addr_id")
        val addrId: Int = 0
)