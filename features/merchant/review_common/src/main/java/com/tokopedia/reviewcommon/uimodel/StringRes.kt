package com.tokopedia.reviewcommon.uimodel

import android.content.Context
import androidx.annotation.StringRes

data class StringRes(
    @StringRes val id: Int
) {
    fun getStringValue(context: Context, vararg params: Any): String {
        return try { context.getString(id, *params) } catch (_: Throwable) { "" }
    }
}
