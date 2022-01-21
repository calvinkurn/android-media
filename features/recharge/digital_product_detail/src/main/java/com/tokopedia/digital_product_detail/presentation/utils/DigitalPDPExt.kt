package com.tokopedia.digital_product_detail.presentation.utils

import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs

fun AppBarLayout.setupDynamicAppBar(
    isErrorMessageShown: () -> Boolean,
    isInputEmpty: () -> Boolean,
    onCollapseAppBar: () -> Unit,
    onExpandAppBar: () -> Unit
) {
    addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
        var lastOffset = -1
        var lastIsCollapsed = false

        override fun onOffsetChanged(p0: AppBarLayout?, verticalOffSet: Int) {
            if (lastOffset == verticalOffSet) return

            lastOffset = verticalOffSet
            if (abs(verticalOffSet) >= totalScrollRange && !lastIsCollapsed) {
                if (!isErrorMessageShown() && !isInputEmpty()) {
                    //Collapsed
                    lastIsCollapsed = true
                    onCollapseAppBar()
                }
            } else if (verticalOffSet == 0 && lastIsCollapsed) {
                //Expanded
                lastIsCollapsed = false
                onExpandAppBar()
            }
        }
    })
}