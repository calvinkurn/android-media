package com.tokopedia.topchat.chatroom.view.uimodel.autoreply

import android.text.SpannableString
import android.text.Spanned
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import timber.log.Timber

data class TopChatAutoReplyUiModel(
    @SerializedName("type")
    val title: String,

    @SerializedName("title")
    private val type: String,

    @SerializedName("message")
    private val message: String
) {
    fun getIcon(): Int? {
        return when (type) {
            WELCOME_MESSAGE -> null
            PRODUCT_MESSAGE -> IconUnify.PRODUCT
            DELIVERY_MESSAGE -> IconUnify.COURIER
            OPERATIONAL_MESSAGE -> IconUnify.CLOCK
            "Produk" -> IconUnify.PRODUCT // TODO: REMOVE DUMMY
            else -> null
        }
    }

    fun getMessage(): Spanned {
        return try {
            MethodChecker.fromHtml(message)
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
