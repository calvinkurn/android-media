package com.tokopedia.talk.feature.sellersettings.template.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ChatTemplatesAll(
        @SerializedName("buyerTemplate")
        @Expose
        val buyerTemplate: ChatTemplate = ChatTemplate(),
        @SerializedName("sellerTemplate")
        @Expose
        val sellerTemplate: ChatTemplate = ChatTemplate(),
)