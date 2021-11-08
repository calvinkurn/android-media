package com.tokopedia.topchat.chatroom.domain.pojo.headerctamsg


import com.google.gson.annotations.SerializedName

data class LocationStock(
    @SerializedName("address_id")
    var addressId: String = "",
    @SerializedName("district_id")
    var districtId: String = "",
    @SerializedName("latlon")
    var latlon: String = "",
    @SerializedName("postal_code")
    var postalCode: String = ""
)