package com.tokopedia.usercomponents.explicit.domain.model

import com.google.gson.annotations.SerializedName

data class UpdateStateParam(
    @SerializedName("template")
    val template: TemplateParam = TemplateParam()
)

data class TemplateParam(
    @SerializedName("name")
    var name: String = "",

    @SerializedName("active")
    val active: Boolean = false
)
