package com.tokopedia.chatbot.data.stickyactionbutton


import com.google.gson.annotations.SerializedName

data class StickyActionButtonPojo(
    @SerializedName("sticked_button_actions")
    val stickedButtonActions: List<StickedButtonAction?>?
) {
    data class StickedButtonAction(
        @SerializedName("invoice_ref_num")
        val invoiceRefNum: String?,
        @SerializedName("reply_text")
        val replyText: String?,
        @SerializedName("text")
        val text: String?
    )
}