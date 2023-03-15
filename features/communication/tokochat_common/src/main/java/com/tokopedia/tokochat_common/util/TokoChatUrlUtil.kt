package com.tokopedia.tokochat_common.util

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
    const val IMAGE_UNAVAILABLE_GENERAL_URL =
        "https://images.tokopedia.net/img/android/tokochat/tokochat_img_chat_unavailable_general.png"
    const val IMAGE_TOKOCHAT_CONSENT = TokopediaImageUrl.IMG_TOKOCHAT_CONSENT

    /**
     * Typing GIF
     */
    const val TYPING_LIGHT_MODE =
        "https://images.tokopedia.net/img/android/user/typing_motion_lightmode.gif"
    const val TYPING_DARK_MODE =
        "https://images.tokopedia.net/img/android/user/typing_motion_darkmode.gif"
}
