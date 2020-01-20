package com.tokopedia.globalnavwidget

import android.view.View

internal fun <T: View> T?.shouldShowWithAction(shouldShow: Boolean, action: (T) -> Unit) {
    if (this == null) return

    if (shouldShow) {
        this.visibility = View.VISIBLE
        action(this)
    } else {
        this.visibility = View.INVISIBLE
    }
}