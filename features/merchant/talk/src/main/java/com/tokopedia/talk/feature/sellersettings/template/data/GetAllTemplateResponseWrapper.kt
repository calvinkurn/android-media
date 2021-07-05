package com.tokopedia.talk.feature.sellersettings.template.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetAllTemplateResponseWrapper(
        @SerializedName("chatTemplatesAll")
        @Expose
        val chatTemplatesAll: ChatTemplatesAll = ChatTemplatesAll()
)