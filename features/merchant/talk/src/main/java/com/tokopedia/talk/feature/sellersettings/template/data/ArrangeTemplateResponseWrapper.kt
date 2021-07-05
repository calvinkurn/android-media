package com.tokopedia.talk.feature.sellersettings.template.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ArrangeTemplateResponseWrapper(
        @SerializedName("chatMoveTemplate")
        @Expose
        val chatMoveTemplate: TemplateMutationResult = TemplateMutationResult()
)