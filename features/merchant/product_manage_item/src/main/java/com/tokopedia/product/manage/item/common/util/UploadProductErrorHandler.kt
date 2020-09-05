package com.tokopedia.product.manage.item.common.util

import android.text.TextUtils
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.network.data.model.response.ResponseV4ErrorException

/**
 * @author by milhamj on 2020-02-06.
 */

object UploadProductErrorHandler {
    @JvmStatic
    fun getExceptionMessage(t: Throwable): String {
        return if (t is ResponseV4ErrorException && !TextUtils.isEmpty(t.errorList.firstOrNull())) {
            t.errorList.firstOrNull() ?: t.localizedMessage
        } else {
            t.localizedMessage
        }
    }

    @JvmStatic
    fun logExceptionToCrashlytics(t: Throwable) {
        try {
            FirebaseCrashlytics.getInstance().recordException(t)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }
}