package com.tokopedia.reviewcommon.uimodel

import android.content.Context
import androidx.annotation.StringRes

data class StringRes(
    @StringRes val id: Int,
    val params: List<Any> = emptyList()
) {
    fun getStringValue(context: Context): String {
        return try { context.getString(id) } catch (_: Throwable) { "" }
    }

    fun getStringValueWithCustomParam(context: Context, vararg params: Any): String {
        return try { context.getString(id, *params) } catch (_: Throwable) { "" }
    }

    fun getStringValueWithDefaultParam(context: Context): String {
        return try { context.getString(id, *params.toTypedArray()) } catch (_: Throwable) { "" }
    }
}
