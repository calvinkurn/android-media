package com.tokopedia.chatbot.util

import com.tokopedia.url.TokopediaUrl

object SecureImageUploadUrl {

    private val BASE_URL = TokopediaUrl.getInstance().CHAT

    private const val UPLOAD_SECURE_PATH = "/tc/v1/upload_secure"

    fun getUploadSecureUrl(): String {
        return BASE_URL + UPLOAD_SECURE_PATH
    }
}