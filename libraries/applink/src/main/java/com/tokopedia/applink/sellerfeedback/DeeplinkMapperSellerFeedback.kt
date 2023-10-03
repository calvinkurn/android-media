package com.tokopedia.applink.sellerfeedback

import android.content.Context
import com.tokopedia.applink.FirebaseRemoteConfigInstance
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.remoteconfig.RemoteConfigKey

object DeeplinkMapperSellerFeedback {

    fun getSellerFeedbackInternalAppLink(context: Context): String {
        val remoteConfigImpl = FirebaseRemoteConfigInstance.get(context)
        val isSellerFeedbackKmp = remoteConfigImpl.getBoolean(RemoteConfigKey.SELLER_FEEDBACK_KMP)
        return if (isSellerFeedbackKmp) {
            ApplinkConstInternalSellerapp.SELLER_FEEDBACK_KMP
        } else {
            ApplinkConstInternalSellerapp.SELLER_FEEDBACK
        }
    }
}
