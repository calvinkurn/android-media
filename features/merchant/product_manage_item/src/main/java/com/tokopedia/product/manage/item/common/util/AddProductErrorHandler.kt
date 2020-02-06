package com.tokopedia.product.manage.item.common.util

import android.text.TextUtils
import com.tokopedia.core.network.retrofit.exception.ResponseV4ErrorException

/**
 * @author by milhamj on 2020-02-06.
 */

object AddProductErrorHandler {
    fun getExceptionMessage(t: Throwable): String {
        return if (t is ResponseV4ErrorException && !TextUtils.isEmpty(t.errorList.firstOrNull())) {
            t.errorList.firstOrNull() ?: t.localizedMessage
        } else {
            t.localizedMessage
        }
    }
}