package com.tokopedia.applink.communication

import android.content.Context
import com.tokopedia.applink.FirebaseRemoteConfigInstance
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.internal.ApplinkConsInternalHome
import com.tokopedia.applink.internal.ApplinkConstInternalCommunication
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.remoteconfig.RemoteConfigInstance

object DeeplinkMapperCommunication {

    private const val CHAT_SETTINGS = "chatsettings"
    private const val KEY_ROLLENCE_UNIVERSAL_INBOX = "inbox_universal"
    private const val ROLLENCE_TYPE_A = "inbox_varA"
    private const val ROLLENCE_TYPE_B = "inbox_varB"

    const val TOKOCHAT_REMOTE_CONFIG = "android_enable_tokochat"

    /**
     * Remote Config util
     */
    private fun isRemoteConfigActive(
        context: Context,
        key: String,
        default: Boolean = true
    ): Boolean {
        return try {
            FirebaseRemoteConfigInstance.get(context).getBoolean(key, default)
        } catch (throwable: Throwable) {
            true
        }
    }

    /**
     * Rollence util
     */
    private fun isABTestActive(key: String): String {
        return try {
            RemoteConfigInstance
                .getInstance()
                .abTestPlatform.getString(
                    key,
                    ""
                )
        } catch (throwable: Throwable) {
            ""
        }
    }

    /**
     * Tokochat mapper with remote config
     */
    fun getRegisteredNavigationTokoChat(context: Context, deeplink: String): String {
        return if (isRemoteConfigActive(context, TOKOCHAT_REMOTE_CONFIG)) {
            deeplink.replace(
                DeeplinkConstant.SCHEME_TOKOPEDIA_SLASH,
                ApplinkConstInternalCommunication.INTERNAL_COMMUNICATION + "/"
            )
        } else {
            ApplinkConstInternalOrder.UNIFY_ORDER_TOKOFOOD
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

    /**
     * Inbox mapper with remote config
     */
    fun getRegisteredNavigationInbox(): String {
        val useUnivInbox = isABTestActive(KEY_ROLLENCE_UNIVERSAL_INBOX) == ROLLENCE_TYPE_A ||
            isABTestActive(KEY_ROLLENCE_UNIVERSAL_INBOX) == ROLLENCE_TYPE_B
        return if (useUnivInbox) {
            ApplinkConstInternalCommunication.UNIVERSAL_INBOX
        } else {
            ApplinkConsInternalHome.HOME_INBOX
        }
    }
}
