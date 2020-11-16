package com.tokopedia.applink.user

import android.net.Uri
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_EMAIL
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_PHONE
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_USER_ID
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_SOURCE

object DeepLinkMapperUser {

    fun getInactivePhoneInternalAppLink(deepLink: String): String {
        val uri = Uri.parse(deepLink)
        val userId = uri.getQueryParameter(PARAM_USER_ID) ?: ""
        val phone = uri.getQueryParameter(PARAM_PHONE) ?: ""
        val email = uri.getQueryParameter(PARAM_EMAIL) ?: ""

        if (phone.isNotEmpty() || email.isNotEmpty()) {
            val params = mapOf(
                    PARAM_USER_ID to userId,
                    PARAM_PHONE to phone,
                    PARAM_EMAIL to email,
                    PARAM_SOURCE to "external"
            )
            return UriUtil.buildUriAppendParam(ApplinkConstInternalGlobal.CHANGE_INACTIVE_PHONE, params)
        }

        return ApplinkConstInternalGlobal.CHANGE_INACTIVE_PHONE
    }
}