package com.tokopedia.loginregister.common.domain.pojo

import com.google.gson.annotations.SerializedName

/**
 * @author rival
 * @created on 20/02/2020
 */

data class DynamicBannerDataModel(
    @SerializedName("GetBanner")
    var banner: GetBanner = GetBanner()
) {
    data class GetBanner(
        @SerializedName("URL")
        val imgUrl: String = "",
        @SerializedName("enable")
        val isEnable: Boolean = false,
        @SerializedName("message")
        val message: String = "",
        @SerializedName("error_message")
        val errorMessage: String = ""
    )
}
