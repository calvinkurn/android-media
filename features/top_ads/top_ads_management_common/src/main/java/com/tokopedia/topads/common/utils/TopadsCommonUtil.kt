package com.tokopedia.topads.common.utils

import android.view.View
import com.tokopedia.unifycomponents.Toaster

object TopadsCommonUtil {

    fun View.showErrorAutoAds(error: String) {
        Toaster.build(
            view = this,
            text = error,
            type = Toaster.TYPE_ERROR
        ).show()
    }
}