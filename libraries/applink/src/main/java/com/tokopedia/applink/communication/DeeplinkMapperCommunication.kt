package com.tokopedia.applink.communication

import androidx.annotation.VisibleForTesting
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.internal.ApplinkConstInternalCommunication
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey

object DeeplinkMapperCommunication {
    fun getRegisteredNavigationTokoChat(deeplink: String): String {
        return if (isTokoChatRollenceActive()) {
            deeplink.replace(
                DeeplinkConstant.SCHEME_TOKOPEDIA_SLASH,
                ApplinkConstInternalCommunication.INTERNAL_COMMUNICATION + "/"
            )
        } else {
            ApplinkConstInternalOrder.UNIFY_ORDER_TOKOFOOD
        }
    }

    @VisibleForTesting
    fun isTokoChatRollenceActive(): Boolean {
        return try {
            RemoteConfigInstance
                .getInstance()
                .abTestPlatform.getString(
                    RollenceKey.KEY_ROLLENCE_TOKOCHAT,
                    ""
                ) == RollenceKey.KEY_ROLLENCE_TOKOCHAT
        } catch (throwable: Throwable) {
            true
        }
    }
}
