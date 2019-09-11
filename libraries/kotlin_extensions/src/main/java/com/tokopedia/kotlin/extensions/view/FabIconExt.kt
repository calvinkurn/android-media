package com.tokopedia.kotlin.extensions.view

import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton

fun FloatingActionButton.setAnchor(view:Int ) {
    var layoutParms = this.layoutParams as CoordinatorLayout.LayoutParams
    layoutParms.anchorId = view
    setLayoutParams(layoutParams)
}
