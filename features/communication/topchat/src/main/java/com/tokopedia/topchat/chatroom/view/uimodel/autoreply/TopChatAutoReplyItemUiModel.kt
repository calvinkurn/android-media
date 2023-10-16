package com.tokopedia.topchat.chatroom.view.uimodel.autoreply

import android.text.SpannableString
import android.text.Spanned
import com.google.gson.annotations.SerializedName
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.utils.htmltags.HtmlUtil
import timber.log.Timber

data class TopChatAutoReplyItemUiModel(
    @SerializedName("title")
    val title: String,

    @SerializedName("type")
    val type: String,

    @SerializedName("message")
    private val message: String
) {
    fun getIcon(): Int? {
        return when (type) {
            WELCOME_MESSAGE -> null
            PRODUCT_MESSAGE -> IconUnify.PRODUCT
            DELIVERY_MESSAGE -> IconUnify.COURIER
            OPERATIONAL_MESSAGE -> IconUnify.CLOCK
            else -> IconUnify.BELL // default
        }
    }

    fun getMessage(): Spanned {
        return try {
            HtmlUtil.fromHtml(message)
        } catch (throwable: Throwable) {
            Timber.d(throwable)
            SpannableString(message) // Return raw string
        }
    }

    companion object {
        private const val WELCOME_MESSAGE = "welcoming_message"
        private const val PRODUCT_MESSAGE = "product_message"
        private const val DELIVERY_MESSAGE = "delivery_message"
        private const val OPERATIONAL_MESSAGE = "operational_message"
    }
}
