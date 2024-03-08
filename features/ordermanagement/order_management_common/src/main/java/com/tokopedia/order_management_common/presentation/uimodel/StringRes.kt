package com.tokopedia.order_management_common.presentation.uimodel

import android.content.Context
import androidx.annotation.StringRes
import com.tokopedia.kotlin.extensions.view.EMPTY

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
        } catch (_: Exception) {
            String.EMPTY
        }
    }
}
