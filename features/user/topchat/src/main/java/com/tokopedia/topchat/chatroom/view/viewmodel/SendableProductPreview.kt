package com.tokopedia.topchat.chatroom.view.viewmodel

import com.tokopedia.attachcommon.data.ResultProduct
import com.tokopedia.chat_common.data.SendableViewModel
import com.tokopedia.chat_common.data.preview.ProductPreview
import com.tokopedia.chat_common.domain.SendWebsocketParam
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.factory.AttachmentPreviewFactory
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
                productPreview.id,
                productPreview.url,
                productPreview.imageUrl,
                productPreview.price,
                productPreview.name
        )
    }

    override fun generateMsgObj(
            messageId: String,
            opponentId: String,
            message: String,
            listInterceptor: List<Interceptor>
    ): Any {
        val startTime = SendableViewModel.generateStartTime()
        return SendWebsocketParam.generateParamSendProductAttachment(
                messageId, generateResultProduct(), startTime, opponentId, productPreview, message
        )
    }

}