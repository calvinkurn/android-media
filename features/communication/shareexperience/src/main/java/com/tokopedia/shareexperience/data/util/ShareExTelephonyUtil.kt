package com.tokopedia.shareexperience.data.util

import android.content.Context
import android.provider.Telephony
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.shareexperience.domain.util.ShareExLogger
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

open class ShareExTelephonyUtil @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userSession: UserSessionInterface
) {

    open fun getSMSPackageName(): String {
        return try {
            Telephony.Sms.getDefaultSmsPackage(context)
        } catch (throwable: Throwable) {
            ShareExLogger.logExceptionToServerLogger(
                throwable,
                userSession.deviceId,
                "getSMSPackageName"
            )
            ""
        }
    }
}
