package com.tokopedia.reviewcommon.uimodel

import android.content.Context
import androidx.annotation.StringRes

data class StringRes(
    @StringRes val id: Int
) {
    fun getStringValue(context: Context): String {
        return try {
            context.getString(id)
        } catch (t: Throwable) {
            ""
        }
    }
}
