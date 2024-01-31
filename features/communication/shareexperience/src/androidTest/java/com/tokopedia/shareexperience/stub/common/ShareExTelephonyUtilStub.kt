package com.tokopedia.shareexperience.stub.common

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.shareexperience.data.util.ShareExTelephonyUtil
import javax.inject.Inject

class ShareExTelephonyUtilStub @Inject constructor(
    @ApplicationContext context: Context
) : ShareExTelephonyUtil(context) {

    override fun getSMSPackageName(): String {
        return "dummy.sms.package"
    }
}
