package com.tokopedia.topchat.chatroom.view.viewmodel

import com.tokopedia.chat_common.data.SendableViewModel
import com.tokopedia.chat_common.domain.SendWebsocketParam
import com.tokopedia.chat_common.domain.pojo.roommetadata.RoomMetaData
import com.tokopedia.chat_common.view.viewmodel.InvoiceViewModel
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.factory.AttachmentPreviewFactory
import okhttp3.Interceptor

class InvoicePreviewUiModel(
        id: String,
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
                statusId == INVALID_STATUS_ID ||
                status.isEmpty() ||
                totalPriceAmount.isEmpty()
    }

    override fun generatePreviewMessage(
        roomMetaData: RoomMetaData,
        message: String
    ): SendableViewModel {
        // TODO: implement with invoice
        return SendableViewModel(
            "", "", "", "", "", "",
            "", "", false, false, false, "",
            "", "", ""
        )
    }

    override fun type(attachmentPreviewFactory: AttachmentPreviewFactory): Int {
        return attachmentPreviewFactory.type(this)
    }

    override fun generateMsgObj(
        messageId: String,
        opponentId: String,
        message: String,
        listInterceptor: List<Interceptor>,
        userLocationInfo: LocalCacheModel,
        localId: String
    ): Any {
        val startTime = SendableViewModel.generateStartTime()
        return SendWebsocketParam.generateParamSendInvoiceAttachment(
                messageId,
                this,
                startTime,
                opponentId
        )
    }

    companion object {
        const val INVALID_ID = "-1"
        const val INVALID_STATUS_ID = -1
    }
}