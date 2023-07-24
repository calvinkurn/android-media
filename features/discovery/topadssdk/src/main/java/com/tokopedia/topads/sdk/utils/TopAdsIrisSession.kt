package com.tokopedia.topads.sdk.utils

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.util.IRIS_SESSION_ID_KEY
import com.tokopedia.graphql.util.TOP_ADS_IRIS_SESSION_PREFERENCE
import com.tokopedia.iris.util.IrisSession
import javax.inject.Inject

class TopAdsIrisSession @Inject constructor(@ApplicationContext private val context: Context) {

    fun getSessionId(): String {
        val sharedPref = context.getSharedPreferences(TOP_ADS_IRIS_SESSION_PREFERENCE, Context.MODE_PRIVATE)
        if (sharedPref != null) {
            val editor = sharedPref.edit()
            editor.putString(IRIS_SESSION_ID_KEY, IrisSession(context).getSessionId())
            editor.apply()
        }
        return IrisSession(context).getSessionId()
    }
}
