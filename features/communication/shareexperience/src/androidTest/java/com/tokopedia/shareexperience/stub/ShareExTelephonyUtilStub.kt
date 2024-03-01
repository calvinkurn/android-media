package com.tokopedia.shareexperience.stub

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.shareexperience.data.util.ShareExTelephonyUtil
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ShareExTelephonyUtilStub @Inject constructor(
    @ApplicationContext context: Context,
    userSession: UserSessionInterface
) : ShareExTelephonyUtil(context, userSession) {

    override fun getSMSPackageName(): String {
        return "dummy.sms.package"
    }
}
