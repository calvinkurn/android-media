package com.tokopedia.tokopoints.view.util

import android.view.View
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.toPx

object ToasterHelper {

    fun showCouponClaimToast(message: String, view: View, bottomHeight: Int = 16) {
        if (message.isNotEmpty()) {
            view.let {
                Toaster.apply {
                    toasterCustomBottomHeight = bottomHeight.toPx()
                }.build(it, message, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
            }
        }
    }
}