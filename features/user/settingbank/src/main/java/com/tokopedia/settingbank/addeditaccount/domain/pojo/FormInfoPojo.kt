package com.tokopedia.settingbank.addeditaccount.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 2019-05-17.
 * ade.hadian@tokopedia.com
 */

data class FormInfoPojo(
        @Expose
        @SerializedName("param_name")
        val param_name: String? = "",
        @Expose
        @SerializedName("messages")
        val messages: String? = ""
)