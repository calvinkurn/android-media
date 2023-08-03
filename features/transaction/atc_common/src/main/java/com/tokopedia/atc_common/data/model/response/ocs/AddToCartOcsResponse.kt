package com.tokopedia.atc_common.data.model.response.ocs

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AddToCartOcsResponse(
    @SerializedName("error_message")
    @Expose
    val errorMessage: ArrayList<String> = arrayListOf(),

    @SerializedName("status")
    @Expose
    val status: String = "",

    @SerializedName("data")
    @Expose
    val data: OcsDataResponse = OcsDataResponse()
)
