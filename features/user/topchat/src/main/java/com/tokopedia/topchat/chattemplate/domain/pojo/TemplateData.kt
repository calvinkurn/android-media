package com.tokopedia.topchat.chattemplate.domain.pojo

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

/**
 * Created by stevenfredian on 11/27/17.
 */
class TemplateData {
    @SerializedName("is_enable")
    @Expose
    var isIsEnable = false

    @SerializedName("templates")
    @Expose
    var templates: List<String> = listOf()

    @SerializedName("is_success")
    @Expose
    var isSuccess = false
}