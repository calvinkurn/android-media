package com.tokopedia.tokochat.stub.common

import android.content.Context
import com.tokopedia.tokochat.common.util.TokoChatNetworkUtil

class TokoChatNetworkUtilStub : TokoChatNetworkUtil() {

    var isNetworkAvailable = true

    override fun isNetworkAvailable(context: Context): Boolean {
        return isNetworkAvailable
    }
}
