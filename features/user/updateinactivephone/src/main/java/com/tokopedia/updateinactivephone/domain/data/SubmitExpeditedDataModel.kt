package com.tokopedia.updateinactivephone.domain.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SubmitExpeditedDataModel(
    @Expose
    @SerializedName("SubmitExpeditedInactivePhone")
    var submit: SubmitDataModel = SubmitDataModel(),
) {
    data class SubmitDataModel(
        @Expose
        @SerializedName("message_error")
        var errorMessage: MutableList<String> = mutableListOf(),
        @Expose
        @SerializedName("is_success")
        var isSuccess: Int = 0
    )
}