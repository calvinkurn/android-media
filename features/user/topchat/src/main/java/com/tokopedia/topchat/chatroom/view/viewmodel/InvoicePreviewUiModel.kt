package com.tokopedia.topchat.chatroom.view.viewmodel

import com.tokopedia.chat_common.data.SendableViewModel
import com.tokopedia.chat_common.domain.SendWebsocketParam
import com.tokopedia.chat_common.view.viewmodel.InvoiceViewModel
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.factory.AttachmentPreviewFactory
import com.tokopedia.websocket.RxWebSocket
import okhttp3.Interceptor

class InvoicePreviewUiModel(
        id: Int,
        invoiceCode: String,
        productName: String,
        date: String,
        imageUrl: String,
        invoiceUrl: String,
        statusId: Int,
        status: String,
        totalPriceAmount: String
) : InvoiceViewModel(
        id,
        invoiceCode,
        productName,
        date,
        imageUrl,
        invoiceUrl,
        statusId,
        status,
        totalPriceAmount), SendablePreview {

    fun enoughRequiredData(): Boolean {
        return !notEnoughRequiredData()
    }

    override fun notEnoughRequiredData(): Boolean {
        return id == INVALID_ID ||
                invoiceCode.isEmpty() ||
                productName.isEmpty() ||
                date.isEmpty() ||
                imageUrl.isEmpty() ||
                invoiceUrl.isEmpty() ||
                statusId == INVALID_ID ||
                status.isEmpty() ||
                totalPriceAmount.isEmpty()
    }

    override fun type(attachmentPreviewFactory: AttachmentPreviewFactory): Int {
        return attachmentPreviewFactory.type(this)
    }

    override fun sendTo(messageId: String, opponentId: String, message: String, listInterceptor: List<Interceptor>) {
        val startTime = SendableViewModel.generateStartTime()
        val invoicePreviewParam = SendWebsocketParam.generateParamSendInvoiceAttachment(
                messageId,
                this,
                startTime,
                opponentId
        )
        RxWebSocket.send(invoicePreviewParam, listInterceptor)
    }

    companion object {
        const val INVALID_ID = -1
    }
}