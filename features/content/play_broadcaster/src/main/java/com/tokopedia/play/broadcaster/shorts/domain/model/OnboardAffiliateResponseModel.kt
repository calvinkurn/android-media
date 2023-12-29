package com.tokopedia.play.broadcaster.shorts.domain.model

import com.google.gson.annotations.SerializedName

data class OnboardAffiliateResponseModel(
    @SerializedName("Data")
    val data: Data = Data()
) {
    data class Data(
        @SerializedName("Error")
        val error: Error = Error(),
        @SerializedName("Status")
        val status: Int = -1, // 0: fail, 1: success
    ) {
        data class Error(
            @SerializedName("ErrorType")
            val errorType: Int = -1, // 0: undefined, 1: toaster
            @SerializedName("Message")
            val message: String = "",
        )
    }
}
