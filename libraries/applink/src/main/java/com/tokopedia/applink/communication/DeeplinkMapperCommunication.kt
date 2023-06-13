package com.tokopedia.applink.communication

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.tokopedia.applink.FirebaseRemoteConfigInstance
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.internal.ApplinkConstInternalCommunication
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalOrder

object DeeplinkMapperCommunication {

    private const val CHAT_SETTINGS = "chatsettings"
    const val TOKOCHAT_REMOTE_CONFIG = "android_enable_tokochat"

    /**
     * Tokochat mapper with remote config
     */
    fun getRegisteredNavigationTokoChat(context: Context, deeplink: String): String {
        return if (isTokoChatRollenceActive(context)) {
            deeplink.replace(
                DeeplinkConstant.SCHEME_TOKOPEDIA_SLASH,
                ApplinkConstInternalCommunication.INTERNAL_COMMUNICATION + "/"
            )
        } else {
            ApplinkConstInternalOrder.UNIFY_ORDER_TOKOFOOD
        }
    }

    @VisibleForTesting
    fun isTokoChatRollenceActive(context: Context): Boolean {
        return try {
            FirebaseRemoteConfigInstance.get(context).getBoolean(
                TOKOCHAT_REMOTE_CONFIG,
                true
            )
        } catch (throwable: Throwable) {
            true
        }
    }

    /**
     * Bubble activation mapper
     */
    fun getRegisteredNavigationBubbleActivation(deeplink: String): String {
        return if (deeplink.contains(DeeplinkConstant.SCHEME_SELLERAPP)) {
            deeplink.replace(
                "${DeeplinkConstant.SCHEME_SELLERAPP}://$CHAT_SETTINGS/",
                "${ApplinkConstInternalMarketplace.INTERNAL_MARKETPLACE}/"
            )
        } else {
            deeplink.replace(
                "${DeeplinkConstant.SCHEME_TOKOPEDIA_SLASH}$CHAT_SETTINGS/",
                "${ApplinkConstInternalMarketplace.INTERNAL_MARKETPLACE}/"
            )
        }
    }
}
