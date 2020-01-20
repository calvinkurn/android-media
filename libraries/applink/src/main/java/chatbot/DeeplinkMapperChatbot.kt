package chatbot

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal

object DeeplinkMapperChatbot {

    fun getChatbotDeeplink(deeplink: String): String {
        return deeplink.replace(ApplinkConst.CHAT_BOT, ApplinkConstInternalGlobal.CHAT_BOT)
    }
}