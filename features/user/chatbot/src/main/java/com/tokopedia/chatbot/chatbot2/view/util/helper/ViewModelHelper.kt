package com.tokopedia.chatbot.chatbot2.view.util.helper

import android.graphics.BitmapFactory
import android.net.Uri
import android.view.View
import com.tokopedia.chat_common.data.AttachmentType
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.chatbot.ChatbotConstant
import com.tokopedia.chatbot.ChatbotConstant.QUERY_SOURCE_TYPE
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.chatbot2.attachinvoice.domain.pojo.InvoiceLinkPojo
import com.tokopedia.chatbot.chatbot2.data.rejectreasons.DynamicAttachmentRejectReasons
import com.tokopedia.chatbot.chatbot2.data.submitoption.SubmitOptionInput
import com.tokopedia.chatbot.chatbot2.view.uimodel.chatactionbubble.ChatActionBubbleUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.helpfullquestion.HelpFullQuestionsUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.invoice.AttachInvoiceSingleUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.quickreply.QuickReplyUiModel
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ValidImageAttachment
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.url.TokopediaUrl
import java.io.File

fun generateInput(selectedValue: Long, model: HelpFullQuestionsUiModel?): SubmitOptionInput {
    val input = SubmitOptionInput()
    with(input) {
        caseChatID = model?.helpfulQuestion?.caseChatId ?: ""
        caseID = model?.helpfulQuestion?.caseId ?: ""
        messageID = model?.messageId ?: ""
        source = QUERY_SOURCE_TYPE
        value = selectedValue
    }
    return input
}

fun getEnvResoLink(link: String): String {
    var url = ""
    if (link.isNotEmpty() && link[0] == '/') {
        url = String.format(TokopediaUrl.getInstance().WEB + "%s", link.removeRange(0, 1))
    }
    return url
}

fun generateInvoice(
    invoiceLinkPojo: InvoiceLinkPojo,
    senderId: String,
    senderName: String
): com.tokopedia.chatbot.chatbot2.attachinvoice.data.uimodel.AttachInvoiceSentUiModel {
    return com.tokopedia.chatbot.chatbot2.attachinvoice.data.uimodel.AttachInvoiceSentUiModel.Builder()
        .withInvoiceAttributesResponse(invoiceLinkPojo)
        .withFromUid(senderId)
        .withFrom(senderName)
        .withAttachmentType(AttachmentType.Companion.TYPE_INVOICE_SEND)
        .withReplyTime(SendableUiModel.SENDING_TEXT)
        .withStartTime(SendableUiModel.generateStartTime())
        .withIsRead(false)
        .withIsDummy(true)
        .withIsSender(true)
        .build()
}

fun createAttachInvoiceSingleViewModel(hashMap: Map<String, String>): AttachInvoiceSingleUiModel {
    return AttachInvoiceSingleUiModel(
        typeString = "",
        type = 0,
        code = hashMap[ChatbotConstant.ChatbotUnification.CODE] ?: "",
        createdTime = hashMap[ChatbotConstant.ChatbotUnification.CREATE_TIME] ?: "",
        description = hashMap[ChatbotConstant.ChatbotUnification.DESCRIPTION] ?: "",
        url = hashMap[ChatbotConstant.ChatbotUnification.IMAGE_URL] ?: "",
        id = hashMap.get(ChatbotConstant.ChatbotUnification.ID).toLongOrZero(),
        imageUrl = hashMap[ChatbotConstant.ChatbotUnification.IMAGE_URL] ?: "",
        status = hashMap[ChatbotConstant.ChatbotUnification.STATUS] ?: "",
        statusId = hashMap[ChatbotConstant.ChatbotUnification.STATUS_ID].toLongOrZero(),
        title = hashMap[ChatbotConstant.ChatbotUnification.TITLE] ?: "",
        amount = hashMap[ChatbotConstant.ChatbotUnification.TOTAL_AMOUNT] ?: "",
        color = hashMap[ChatbotConstant.ChatbotUnification.STATUS_COLOR] ?: ""
    )
}

fun validateImageAttachment(uri: String?, maxFileSize: Int): ValidImageAttachment {
    if (uri == null) return ValidImageAttachment.OtherError
    val file = File(uri)
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFile(file.absolutePath, options)
    val imageHeight = options.outHeight
    val imageWidth = options.outWidth

    val fileSize = (file.length() / ChatbotConstant.ImageUpload.DEFAULT_ONE_MEGABYTE).toString().toIntOrZero()

    return if (imageHeight < ChatbotConstant.ImageUpload.MINIMUM_HEIGHT || imageWidth < ChatbotConstant.ImageUpload.MINIMUM_WIDTH) {
        return ValidImageAttachment.UnderSizedImage
    } else if (fileSize >= maxFileSize) {
        return ValidImageAttachment.OverSizedImage
    } else {
        return ValidImageAttachment.CorrectImage
    }
    return ValidImageAttachment.CorrectImage
}

fun getActionBubbleforNoTrasaction(view: View?): ChatActionBubbleUiModel {
    val text = view?.context?.getString(R.string.chatbot_text_for_no_transaction_found) ?: ""
    val value = view?.context?.getString(R.string.chatbot_text_for_no_transaction_found) ?: ""
    val action = view?.context?.getString(R.string.chatbot_action_text_for_no_transaction_found) ?: ""
    return ChatActionBubbleUiModel(text, value, action)
}

fun getValuesForArticleEntry(uri: Uri): Map<String, String> {
    return mapOf(
        ChatbotConstant.ChatbotUnification.ARTICLE_ID to getQueryParam(
            uri,
            ChatbotConstant.ChatbotUnification.ARTICLE_ID
        ),
        ChatbotConstant.ChatbotUnification.ARTICLE_TITLE to getQueryParam(
            uri,
            ChatbotConstant.ChatbotUnification.ARTICLE_TITLE
        ),
        ChatbotConstant.ChatbotUnification.CODE to getQueryParam(
            uri,
            ChatbotConstant.ChatbotUnification.CODE
        ),
        ChatbotConstant.ChatbotUnification.CREATE_TIME to getQueryParam(
            uri,
            ChatbotConstant.ChatbotUnification.CREATE_TIME
        ),
        ChatbotConstant.ChatbotUnification.DESCRIPTION to getQueryParam(
            uri,
            ChatbotConstant.ChatbotUnification.DESCRIPTION
        ),
        ChatbotConstant.ChatbotUnification.EVENT to getQueryParam(
            uri,
            ChatbotConstant.ChatbotUnification.EVENT
        ),
        ChatbotConstant.ChatbotUnification.ID to getQueryParam(
            uri,
            ChatbotConstant.ChatbotUnification.ID
        ),
        ChatbotConstant.ChatbotUnification.IMAGE_URL to getQueryParam(
            uri,
            ChatbotConstant.ChatbotUnification.IMAGE_URL
        ),
        ChatbotConstant.ChatbotUnification.IS_ATTACHED to getQueryParam(
            uri,
            ChatbotConstant.ChatbotUnification.IS_ATTACHED
        ),
        ChatbotConstant.ChatbotUnification.STATUS to getQueryParam(
            uri,
            ChatbotConstant.ChatbotUnification.STATUS
        ),
        ChatbotConstant.ChatbotUnification.STATUS_COLOR to getQueryParam(
            uri,
            ChatbotConstant.ChatbotUnification.STATUS_COLOR
        ),
        ChatbotConstant.ChatbotUnification.STATUS_ID to getQueryParam(
            uri,
            ChatbotConstant.ChatbotUnification.STATUS_ID
        ),
        ChatbotConstant.ChatbotUnification.TITLE to getQueryParam(
            uri,
            ChatbotConstant.ChatbotUnification.TITLE
        ),
        ChatbotConstant.ChatbotUnification.TOTAL_AMOUNT to getQueryParam(
            uri,
            ChatbotConstant.ChatbotUnification.TOTAL_AMOUNT
        ),
        ChatbotConstant.ChatbotUnification.USED_BY to getQueryParam(
            uri,
            ChatbotConstant.ChatbotUnification.USED_BY
        )
    )
}

private fun getQueryParam(uri: Uri, key: String): String {
    return uri.getQueryParameter(key).toBlankOrString()
}

fun DynamicAttachmentRejectReasons.toQuickReplyUiModel(): List<QuickReplyUiModel> {
    return this.helpfulQuestion.newQuickRepliesList.map {
        it.toQuickReplyUiModel()
    }
}

fun DynamicAttachmentRejectReasons.RejectReasonHelpfulQuestion.RejectReasonNewQuickReply.toQuickReplyUiModel(): QuickReplyUiModel {
    return QuickReplyUiModel(
        this.text,
        this.value,
        this.action
    )
}
