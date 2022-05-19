package com.tokopedia.sellerorder.orderextension.presentation.model

import android.content.Context
import android.content.res.Resources
import androidx.annotation.DimenRes
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.orZero

data class DimenRes(
    @DimenRes private val resId: Int
) {
    fun getDimen(context: Context?): Float {
        return try {
            context?.resources?.getDimension(resId).orZero()
        } catch (_: Resources.NotFoundException) {
            Float.ZERO
        }
    }
}
