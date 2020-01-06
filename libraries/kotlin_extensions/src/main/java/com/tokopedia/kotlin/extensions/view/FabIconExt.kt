package com.tokopedia.kotlin.extensions.view

import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout

fun LinearLayout.setAnchor(view:Int ) {
    var layoutParms = this.layoutParams as CoordinatorLayout.LayoutParams
    layoutParms.anchorId = view
    setLayoutParams(layoutParams)
}
