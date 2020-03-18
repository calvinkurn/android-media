package com.tokopedia.topchat.chatroom.view.viewmodel

import com.tokopedia.attachproduct.resultmodel.ResultProduct
import com.tokopedia.chat_common.data.SendableViewModel
import com.tokopedia.chat_common.data.preview.ProductPreview
import com.tokopedia.chat_common.domain.SendWebsocketParam
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.factory.AttachmentPreviewFactory
import com.tokopedia.websocket.RxWebSocket
import okhttp3.Interceptor

class SendableProductPreview(
        val productPreview: ProductPreview
) : SendablePreview {

    val productId get() = productPreview.id
    val productUrl get() = productPreview.url

    override fun type(attachmentPreviewFactory: AttachmentPreviewFactory): Int {
        return attachmentPreviewFactory.type(this)
    }

    override fun notEnoughRequiredData(): Boolean {
        return productPreview.notEnoughRequiredData()
    }

    fun hasVariant(): Boolean {
        return !doesNotHaveVariant()
    }

    fun doesNotHaveVariant(): Boolean {
        return productPreview.doesNotHaveVariant()
    }

    fun hasColorVariant(): Boolean = productPreview.hasColorVariant()

    fun hasSizeVariant(): Boolean = productPreview.hasSizeVariant()

    private fun generateResultProduct(): ResultProduct {
        return ResultProduct(
                productPreview.id.toInt(),
                productPreview.url,
                productPreview.imageUrl,
                productPreview.price,
                productPreview.name
        )
    }

    override fun sendTo(messageId: String, opponentId: String, message: String, interceptors: List<Interceptor>) {
        val startTime = SendableViewModel.generateStartTime()
        val productPreviewParam = SendWebsocketParam.generateParamSendProductAttachment(
                messageId, generateResultProduct(), startTime, opponentId, productPreview, message
        )
        RxWebSocket.send(productPreviewParam, interceptors)
    }

}