package com.tokopedia.sellerorder.orderextension.presentation.model

import android.content.Context
import android.content.res.Resources
import androidx.annotation.StringRes

data class StringRes(
    @StringRes private val resId: Int,
    val params: List<Any> = emptyList()
) {
    fun getString(context: Context?): String {
        return try {
            context?.resources?.getString(resId).orEmpty()
        } catch (_: Resources.NotFoundException) {
            ""
        }
    }

    fun getStringWithDefaultValue(context: Context?): String {
        return try {
            context?.resources?.getString(resId, *params.toTypedArray()).orEmpty()
        } catch (_: Resources.NotFoundException) {
            ""
        }
    }
}
