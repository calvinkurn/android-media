package com.tokopedia.loginregister.common.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author rival
 * @created on 20/02/2020
 */

data class DynamicBannerDataModel(
        @Expose @SerializedName("GetAuthBanner")
        var authBanner: GetAuthBanner = GetAuthBanner()
) {
    data class GetAuthBanner(
            @Expose @SerializedName("banner_img_url")
            val imgUrl : String = "",
            @Expose @SerializedName("success")
            val isSuccess : Boolean = false,
            @Expose @SerializedName("error_message")
            val errorMessage : String = ""
    )
}