package com.tokopedia.gifting.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetAddOnResponse {
    @SerializedName("GetAddOnByID")
    @Expose
    var getAddOnByID: GetAddOnByID = GetAddOnByID()
}

class GetAddOnByID {
    @SerializedName("Error")
    @Expose
    var error: Error = Error()

    @SerializedName("StaticInfo")
    @Expose
    var staticInfo: StaticInfo = StaticInfo()

    @SerializedName("AddOnByIDResponse")
    @Expose
    var addOnByIDResponse: List<Addon> = emptyList()
}