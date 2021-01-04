package com.tokopedia.digital_deals.view.utils

import android.view.View
import com.tokopedia.unifycomponents.Toaster

object CustomWidthToaster {
    @JvmStatic
    fun showCustomeToaster(dimen: Int, view: View, message: String, ctaMessage: String, clickListener: View.OnClickListener){
        Toaster.toasterCustomCtaWidth = dimen
        Toaster.build(view, message, Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL,
               ctaMessage, clickListener).show()
    }
}