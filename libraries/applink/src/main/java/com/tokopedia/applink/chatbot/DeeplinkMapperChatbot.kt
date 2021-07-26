package com.tokopedia.applink.chatbot

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal

object DeeplinkMapperChatbot {

    fun getChatbotDeeplink(deeplink: String): String {
        if (deeplink.startsWith(ApplinkConst.CHAT_BOT, true))
            return deeplink.replace(ApplinkConst.CHAT_BOT, ApplinkConstInternalGlobal.CHAT_BOT)
        else if (deeplink.startsWith(ApplinkConst.TOP_CHAT, true)) {
            return deeplink.replace(ApplinkConst.TOP_CHAT, ApplinkConstInternalGlobal.CHAT_BOT)
        }
        return deeplink
    }
}