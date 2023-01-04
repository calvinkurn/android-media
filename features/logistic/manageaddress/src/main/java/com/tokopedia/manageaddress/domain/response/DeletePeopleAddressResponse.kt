package com.tokopedia.manageaddress.domain.response

import com.google.gson.annotations.SerializedName

data class DeletePeopleAddressGqlResponse(
    @SerializedName("kero_remove_address")
    val response: DeletePeopleAddressResponse = DeletePeopleAddressResponse()
)

data class DeletePeopleAddressResponse(
    @SerializedName("data")
    val data: DeletePeopleAddressData = DeletePeopleAddressData(),
    @SerializedName("status")
    val status: String = "",
    @SerializedName("config")
    val config: String = "",
    @SerializedName("server_process_time")
    val serverTime: String = ""
)

data class DeletePeopleAddressData(
    @SerializedName("is_success")
    val success: Int = 0
)