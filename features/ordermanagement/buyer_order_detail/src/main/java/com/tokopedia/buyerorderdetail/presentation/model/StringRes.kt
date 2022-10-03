package com.tokopedia.buyerorderdetail.presentation.model

import android.content.Context
import android.content.res.Resources
import androidx.annotation.StringRes
import java.io.Serializable

data class StringRes(
    @StringRes private val resId: Int
) : Serializable {
    fun getString(context: Context?): String {
        return try {
            context?.getString(resId).orEmpty()
        } catch (_: Resources.NotFoundException) {
            ""
        }
    }
}
