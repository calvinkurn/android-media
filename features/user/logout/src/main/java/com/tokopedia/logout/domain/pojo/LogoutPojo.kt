package com.tokopedia.logout.domain.pojo

import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 5/30/18.
 */
data class LogoutPojo(
        @SerializedName("data")
        val data: Data? = null,
        @SerializedName("message_error")
        val message_error: List<String>? = ArrayList(),
        @SerializedName("message_status")
        val message_status: List<String>? = ArrayList()
)

data class Data(
        @SerializedName("is_logout")
        val is_logout: Boolean = false
)