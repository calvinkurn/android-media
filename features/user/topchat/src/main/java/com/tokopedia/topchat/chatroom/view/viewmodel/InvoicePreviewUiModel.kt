package com.tokopedia.topchat.chatroom.view.viewmodel

import com.tokopedia.chat_common.data.AttachInvoiceSentUiModel
import com.tokopedia.chat_common.data.AttachmentType
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.chat_common.data.attachment.AttachmentId
import com.tokopedia.chat_common.domain.SendWebsocketParam
import com.tokopedia.chat_common.domain.pojo.roommetadata.RoomMetaData
import com.tokopedia.chat_common.view.viewmodel.InvoiceViewModel
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.factory.AttachmentPreviewFactory

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
    totalPriceAmount
), SendablePreview {

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
    ): SendableUiModel {
        return AttachInvoiceSentUiModel.Builder()
            .withRoomMetaData(roomMetaData)
            .withAttachmentId(AttachmentId.NOT_YET_GENERATED)
            .withAttachmentType(AttachmentType.Companion.TYPE_INVOICE_SEND)
            .withNeedSync(false)
            .withMsg(invoiceUrl)
            .withImageUrl(imageUrl)
            .withTotalAmount(totalPriceAmount)
            .withStatusId(statusId)
            .withStatus(status)
            .withInvoiceId(invoiceCode)
            .withInvoiceUrl(invoiceUrl)
            .withCreateTime(date)
            .build()
    }

    override fun type(attachmentPreviewFactory: AttachmentPreviewFactory): Int {
        return attachmentPreviewFactory.type(this)
    }

    override fun generateMsgObj(
        roomMetaData: RoomMetaData,
        message: String,
        userLocationInfo: LocalCacheModel,
        localId: String
    ): Any {
        val startTime = SendableUiModel.generateStartTime()
        val msgId = roomMetaData.msgId
        val toUid = roomMetaData.receiver.uid
        return SendWebsocketParam.generateParamSendInvoiceAttachment(
            msgId, this, startTime,
            toUid, localId
        )
    }

    companion object {
        const val INVALID_ID = "-1"
        const val INVALID_STATUS_ID = -1
    }
}