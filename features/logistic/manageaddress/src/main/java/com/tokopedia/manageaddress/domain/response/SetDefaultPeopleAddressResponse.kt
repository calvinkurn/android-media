package com.tokopedia.manageaddress.domain.response

import com.google.gson.annotations.SerializedName

data class SetDefaultPeopleAddressGqlResponse(
        @SerializedName("kero_set_default_address")
        val  response: SetDefaultPeopleAddressResponse = SetDefaultPeopleAddressResponse()
)

data class SetDefaultPeopleAddressResponse(
        @SerializedName("data")
        val data: DefaultPeopleAddressData = DefaultPeopleAddressData(),
        @SerializedName("status")
        val status: String = "",
        @SerializedName("config")
        val config: String = "",
        @SerializedName("server_process_time")
        val serverTime: String = ""
)

data class DefaultPeopleAddressData(
        @SerializedName("is_success")
        val success: Int = 0
)