package com.tokopedia.talk.feature.sellersettings.template.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EnableTemplateResponseWrapper(
        @SerializedName("chatToggleTemplate")
        @Expose
        val chatToggleTemplate: TemplateMutationResult = TemplateMutationResult()
)