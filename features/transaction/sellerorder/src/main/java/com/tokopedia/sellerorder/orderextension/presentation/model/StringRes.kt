package com.tokopedia.sellerorder.orderextension.presentation.model

import android.content.Context
import android.content.res.Resources
import androidx.annotation.StringRes

data class StringRes(
    @StringRes private val resId: Int
) {
    fun getString(context: Context?): String {
        return try {
            context?.resources?.getString(resId).orEmpty()
        } catch (_: Resources.NotFoundException) {
            ""
        }
    }
}
