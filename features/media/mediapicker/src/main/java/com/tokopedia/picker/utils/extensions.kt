package com.tokopedia.picker.utils

import android.content.Context
import com.google.android.material.tabs.TabLayout

fun TabLayout?.addOnTabSelected(
    selected: (Int) -> Unit
) = this?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
    override fun onTabSelected(tab: TabLayout.Tab?) {
        tab?.position?.let { selected(it) }
    }
    override fun onTabReselected(tab: TabLayout.Tab?) {}
    override fun onTabUnselected(tab: TabLayout.Tab?) {}
})

fun Context.dimensionPixelOffsetOf(dimen: Int) = resources.getDimensionPixelOffset(dimen)

fun Context.dimensionOf(dimen: Int) = resources.getDimension(dimen)