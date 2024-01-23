package com.tokopedia.order_management_common.presentation.uimodel

import android.content.Context
import android.content.res.Resources
import androidx.annotation.StringRes

data class StringRes(
    @StringRes private val resId: Int,
    val params: List<Any> = emptyList()
) {
    fun getString(context: Context?): String {
        return try {
            if (params.isNotEmpty()) {
                context?.resources?.getString(resId, *params.toTypedArray()).orEmpty()
            } else {
                context?.resources?.getString(resId).orEmpty()
            }
        } catch (_: Resources.NotFoundException) {
            ""
        }
    }
}
