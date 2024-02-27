package com.tokopedia.content.product.picker.seller.util

import android.content.Context
import com.tokopedia.content.common.util.throwable.isNetworkError
import com.tokopedia.content.product.picker.R
import java.text.ParseException

internal fun Context.getCustomErrorMessage(
    throwable: Throwable
): String? {
    val customMessage = when {
        throwable.isNetworkError -> getString(R.string.product_chooser_error_no_connection)
        throwable is ParseException -> getString(R.string.product_chooser_error_something_went_wrong)
        else -> throwable.message
    }
    return customMessage
}
