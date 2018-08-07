package com.tokopedia.changepassword.domain.pojo
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
/**
 * @author by nisie on 7/25/18.
 */

data class ChangePasswordPojo(
        @Expose
        @SerializedName("data")
        val data: Data? = null,
        @Expose
        @SerializedName("message_error")
        val message_error: List<String>? = ArrayList(),
        @Expose
        @SerializedName("message_status")
        val message_status: List<String>? = ArrayList()
)

data class Data(
        @Expose
        @SerializedName("is_success")
        val is_success: Int = 0
)