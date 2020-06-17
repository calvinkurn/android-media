package com.tokopedia.manageaddress.domain.response

import com.google.gson.annotations.SerializedName

data class DeletePeopleAddressResponse(
        @SerializedName("config")
        var config: String = ""
)