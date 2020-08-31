package com.tokopedia.topchat.chatroom.view.viewmodel

import androidx.annotation.Keep
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject

@Keep
open class WebsocketAttachmentContract (
        val code: Int,
        val data: WebsocketAttachmentData
)

@Keep
open class WebsocketAttachmentData (
        val message_id: Int,
        val message: String,
        val source: String,
        val attachment_type: Int,
        val start_time: String,
        val payload: Any,
        var extras: Any = Any()
) {

    private fun createProductExtrasAttachments(attachments: List<SendablePreview>): JsonElement {
        val extrasProducts = JsonArray()
        attachments.forEach { attachment ->
            if (attachment is SendableProductPreview) {
                val product = JsonObject().apply {
                    addProperty("url", attachment.productUrl)
                    addProperty("product_id", attachment.productId.toInt())
                }
                extrasProducts.add(product)
            }
        }
        return JsonObject().apply {
            add("extras_product", extrasProducts)
        }
    }

    fun addExtrasAttachments(attachments: List<SendablePreview>) {
        extras = createProductExtrasAttachments(attachments)
    }

}