package com.tokopedia.applink.sellerfeedback

import android.content.Context
import com.tokopedia.applink.FirebaseRemoteConfigInstance
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.remoteconfig.RemoteConfigKey

object DeeplinkMapperSellerFeedback {

    fun getSellerFeedbackInternalAppLink(context: Context): String {
        return if (getIsSellerFeedbackKmp(context)) {
            ApplinkConstInternalSellerapp.SELLER_FEEDBACK_KMP
        } else {
            ApplinkConstInternalSellerapp.SELLER_FEEDBACK
        }
    }

    private fun getIsSellerFeedbackKmp(context: Context): Boolean {
        val remoteConfigImpl = FirebaseRemoteConfigInstance.get(context)
        return remoteConfigImpl.getBoolean(RemoteConfigKey.SELLER_FEEDBACK_KMP)
    }
}
