package com.tokopedia.topads.sdk.utils

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.iris.util.IrisSession
import javax.inject.Inject

class TopAdsIrisSession @Inject constructor(@ApplicationContext private val context: Context) {
    fun getSessionId(): String {
        return IrisSession(context).getSessionId()
    }
}