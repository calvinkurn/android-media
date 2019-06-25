package com.tokopedia.kotlin.extensions.view

import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton

fun FloatingActionButton.setAnchor(view:Int ) {
    var layoutParms = this.layoutParams as CoordinatorLayout.LayoutParams
    layoutParms.anchorId = view
    setLayoutParams(layoutParams)
}