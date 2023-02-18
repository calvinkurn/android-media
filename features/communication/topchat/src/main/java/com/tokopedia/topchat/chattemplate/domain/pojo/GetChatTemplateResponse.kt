package com.tokopedia.topchat.chattemplate.domain.pojo

import com.google.gson.annotations.SerializedName

data class GetChatTemplateResponse(
    @SerializedName("chatTemplatesAll")
    val chatTemplatesAll: GetChatTemplate = GetChatTemplate()
)

data class GetChatTemplate(
    @SerializedName("buyerTemplate")
    val buyerTemplate: TopchatChatTemplates = TopchatChatTemplates(),

    @SerializedName("sellerTemplate")
    val sellerTemplate: TopchatChatTemplates = TopchatChatTemplates()
)

data class TopchatChatTemplates(
    @SerializedName("isEnable")
    val isEnable: Boolean = false,

    @SerializedName("IsEnableSmartReply")
    val isEnableSmartReply: Boolean = false,

    @SerializedName("IsSeller")
    val isSeller: Boolean = false,

    @SerializedName("templates")
    val templates: ArrayList<String> = arrayListOf()
)
