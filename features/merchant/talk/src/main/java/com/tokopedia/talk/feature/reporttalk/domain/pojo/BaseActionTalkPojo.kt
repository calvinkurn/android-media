package com.tokopedia.talk.feature.reporttalk.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 9/7/18.
 */

data class BaseActionTalkPojo(
        @Expose
        @SerializedName("id")
        val id: String = "",
        @Expose
        @SerializedName("type")
        val type: String = "",
        @Expose
        @SerializedName("is_success")
        val is_success: Int = 0
)