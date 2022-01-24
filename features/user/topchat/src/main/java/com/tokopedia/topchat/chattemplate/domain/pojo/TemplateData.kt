package com.tokopedia.topchat.chattemplate.domain.pojo

import com.google.gson.annotations.SerializedName

/**
 * Created by stevenfredian on 11/27/17.
 */
class TemplateData {
    @SerializedName("is_enable")
    var isIsEnable = false

    @SerializedName("templates")
    var templates: List<String> = listOf()

    @SerializedName("is_success")
    var isSuccess = false
}