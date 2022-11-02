package com.tokopedia.buyerorderdetail.presentation.model

import android.content.Context
import androidx.annotation.StringRes

data class StringRes(
    @StringRes val id: Int,
    val params: List<Any> = emptyList()
) {
    fun getStringValue(context: Context?): String {
        return try { context?.getString(id).orEmpty() } catch (_: Throwable) { "" }
    }

    fun getStringValueWithCustomParam(context: Context?, vararg params: Any): String {
        return try { context?.getString(id, *params).orEmpty() } catch (_: Throwable) { "" }
    }

    fun getStringValueWithDefaultParam(context: Context?): String {
        return try { context?.getString(id, *params.toTypedArray()).orEmpty() } catch (_: Throwable) { "" }
    }
}
