package com.tokopedia.topchat.chatroom.view.viewmodel

import com.tokopedia.attachproduct.resultmodel.ResultProduct
import com.tokopedia.chat_common.data.SendableViewModel
import com.tokopedia.chat_common.domain.SendWebsocketParam
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.factory.AttachmentPreviewFactory
import com.tokopedia.websocket.RxWebSocket
import okhttp3.Interceptor

class ProductPreviewViewModel(
        val id: String,
        val imageUrl: String,
        val name: String,
        val price: String,
        val colorVariant: String,
        val colorHexVariant: String,
        val sizeVariant: String,
        val url: String
) : PreviewViewModel {

    override fun type(attachmentPreviewFactory: AttachmentPreviewFactory): Int {
        return attachmentPreviewFactory.type(this)
    }

    fun notEnoughRequiredData(): Boolean {
        return name.isEmpty() || imageUrl.isEmpty() || price.isEmpty() || id.isEmpty()
    }

    fun doesNotHaveVariant(): Boolean {
        return colorVariant.isEmpty() && sizeVariant.isEmpty()
    }

    fun hasColorVariant(): Boolean = colorVariant.isNotEmpty() && colorHexVariant.isNotEmpty()

    fun hasSizeVariant(): Boolean = sizeVariant.isNotEmpty()

    private fun generateResultProduct(): ResultProduct {
        return ResultProduct(id.toInt(), url, imageUrl, price, name)
    }

    override fun sendTo(messageId: String, opponentId: String, interceptors: List<Interceptor>) {
        val startTime = SendableViewModel.generateStartTime()
        val productPreviewParam = SendWebsocketParam.generateParamSendProductAttachment(
                messageId,
                generateResultProduct(),
                startTime,
                opponentId
        )
        RxWebSocket.send(productPreviewParam, interceptors)
    }

}