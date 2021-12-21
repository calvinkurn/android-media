package com.tokopedia.topchat.chattemplate.domain.pojo

import com.google.gson.annotations.SerializedName

data class TemplateDataWrapper(
    @SerializedName("data")
    val templateData: TemplateData = TemplateData()
)