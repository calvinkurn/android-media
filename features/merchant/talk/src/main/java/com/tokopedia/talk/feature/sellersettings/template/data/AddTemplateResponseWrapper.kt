package com.tokopedia.talk.feature.sellersettings.template.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AddTemplateResponseWrapper(
        @SerializedName("chatAddTemplate")
        @Expose
        val chatAddTemplate: TemplateMutationResult = TemplateMutationResult()
)