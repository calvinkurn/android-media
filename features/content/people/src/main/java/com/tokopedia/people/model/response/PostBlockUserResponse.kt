package com.tokopedia.people.model.response

import com.google.gson.annotations.SerializedName

/**
 * Created by kenny.hadisaputra on 28/11/22
 */
data class PostBlockUserResponse(
    @SerializedName("feedXProfileBlockUser")
    val data: Data = Data(),
) {

    data class Data(
        @SerializedName("success")
         val success: Boolean = false,
    )
}
