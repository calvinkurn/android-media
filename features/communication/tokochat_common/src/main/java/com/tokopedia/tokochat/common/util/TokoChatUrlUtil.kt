package com.tokopedia.tokochat.common.util

import com.tokopedia.imageassets.TokopediaImageUrl

import com.tokopedia.url.TokopediaUrl

object TokoChatUrlUtil {
    /**
     * TNC
     */
    var TNC = "${TokopediaUrl.Companion.getInstance().WEB}terms#konten"

    /**
     * Image Urls
     */
    const val IV_MASKING_PHONE_NUMBER = TokopediaImageUrl.IV_MASKING_PHONE_NUMBER
    const val IC_TOKOFOOD_SOURCE = TokopediaImageUrl.IC_TOKOFOOD_SOURCE
    const val IMAGE_UNAVAILABLE_GENERAL_URL = TokopediaImageUrl.IMG_UNAVAILABLE_GENERAL
    const val IMAGE_TOKOCHAT_CONSENT = TokopediaImageUrl.IMG_TOKOCHAT_CONSENT

    /**
     * Typing GIF
     */
    const val TYPING_LIGHT_MODE = TokopediaImageUrl.GIF_TYPING_LIGHT
    const val TYPING_DARK_MODE = TokopediaImageUrl.GIF_TYPING_DARK

    /**
     * Censor Chat Urls
     */
    const val GUIDE_CHAT = TokopediaImageUrl.IMG_GUIDE_CHAT
}
