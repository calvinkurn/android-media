package com.tokopedia.applink.communication

import android.content.Context
import com.tokopedia.applink.FirebaseRemoteConfigInstance
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.internal.ApplinkConsInternalHome
import com.tokopedia.applink.internal.ApplinkConstInternalCommunication
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.user.session.UserSession
import timber.log.Timber

object DeeplinkMapperCommunication {

    private const val CHAT_SETTINGS = "chatsettings"

    const val UNIVERSAL_INBOX_ROLLENCE = "newInbox_rollout"
    private const val TOKOCHAT_REMOTE_CONFIG = "android_enable_tokochat"
    const val TOKOCHAT_LIST_REMOTE_CONFIG = "android_enable_tokochat_list"

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
            Timber.d(throwable)
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
            Timber.d(throwable)
            ""
        }
    }

    /**
     * User Session Util
     */
    fun isUserLoggedIn(context: Context): Boolean {
        val userSession = UserSession(context)
        return userSession.isLoggedIn
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

    fun getRegisteredNavigationTokoChatList(context: Context, deeplink: String): String {
        return if (isRemoteConfigActive(context, TOKOCHAT_LIST_REMOTE_CONFIG)) {
            ApplinkConstInternalCommunication.TOKOCHAT_LIST
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
    fun getRegisteredNavigationInbox(context: Context, deeplink: String): String {
        return if (isUserLoggedIn(context)) {
            val useUnivInbox = isABTestActive(UNIVERSAL_INBOX_ROLLENCE) == UNIVERSAL_INBOX_ROLLENCE
            return if (useUnivInbox) {
                ApplinkConstInternalCommunication.UNIVERSAL_INBOX
            } else {
                ApplinkConsInternalHome.HOME_INBOX
            }
        } else {
            ApplinkConstInternalUserPlatform.LOGIN
        }
    }
}
