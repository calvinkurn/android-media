package com.tokopedia.topchat.chatroom.view.viewmodel

import com.tokopedia.chat_common.data.SendableViewModel
import com.tokopedia.chat_common.domain.SendWebsocketParam
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.factory.AttachmentPreviewFactory
import com.tokopedia.websocket.RxWebSocket
import okhttp3.Interceptor

data class InvoicePreviewViewModel(
        val id: String,
        val invoiceCode: String,
        val productName: String,
        val date: String,
        val imageUrl: String,
        val invoiceUrl: String,
        val statusId: String,
        val status: String,
        val totalPriceAmount: String
) : PreviewViewModel {

    override fun notEnoughRequiredData(): Boolean {
        return id.isEmpty() ||
                invoiceCode.isEmpty() ||
                productName.isEmpty() ||
                date.isEmpty() ||
                imageUrl.isEmpty() ||
                invoiceUrl.isEmpty() ||
                statusId.isEmpty() ||
                status.isEmpty() ||
                totalPriceAmount.isEmpty()
    }

    override fun type(attachmentPreviewFactory: AttachmentPreviewFactory): Int {
        return attachmentPreviewFactory.type(this)
    }

    override fun sendTo(messageId: String, opponentId: String, listInterceptor: List<Interceptor>) {
        val startTime = SendableViewModel.generateStartTime()
        val invoicePreviewParam = SendWebsocketParam.generateParamSendInvoiceAttachment(
                messageId,
                startTime,
                opponentId
        )
        RxWebSocket.send(invoicePreviewParam, listInterceptor)
    }
}