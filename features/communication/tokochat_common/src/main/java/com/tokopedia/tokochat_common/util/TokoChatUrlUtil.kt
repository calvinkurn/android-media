package com.tokopedia.tokochat_common.util

import com.tokopedia.url.TokopediaUrl

object TokoChatUrlUtil {
    /**
     * TNC
     */
    var TNC = "${TokopediaUrl.Companion.getInstance().WEB}terms#konten"

    /**
     * Typing GIF
     */
    const val TYPING_LIGHT_MODE =
        "https://images.tokopedia.net/img/android/user/typing_motion_lightmode.gif"
    const val TYPING_DARK_MODE =
        "https://images.tokopedia.net/img/android/user/typing_motion_darkmode.gif"
}
