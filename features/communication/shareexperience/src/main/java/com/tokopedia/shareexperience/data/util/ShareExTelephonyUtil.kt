package com.tokopedia.shareexperience.data.util

import android.content.Context
import android.provider.Telephony
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import javax.inject.Inject

open class ShareExTelephonyUtil @Inject constructor(
    @ApplicationContext private val context: Context
) {

    open fun getSMSPackageName(): String {
        return Telephony.Sms.getDefaultSmsPackage(context)
    }
}
