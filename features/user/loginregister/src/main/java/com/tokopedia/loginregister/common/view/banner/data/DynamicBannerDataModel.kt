package com.tokopedia.loginregister.common.view.banner.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author rival
 * @created on 20/02/2020
 */

data class DynamicBannerDataModel(
        @Expose @SerializedName("GetBanner")
        var banner: GetBanner = GetBanner()
) {
    data class GetBanner(
            @Expose @SerializedName("URL")
            val imgUrl : String = "",
            @Expose @SerializedName("enable")
            val isEnable : Boolean = false,
            @Expose @SerializedName("message")
            val message : String = "",
            @Expose @SerializedName("error_message")
            val errorMessage : String = ""
    )
}