package com.tokopedia.kotlin.extensions.view

import com.google.android.material.tabs.TabLayout

/**
 * @author by milhamj on 16/01/19.
 */

fun TabLayout.onTabSelected(action: (TabLayout.Tab) -> Unit) {
    this.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab) = Unit
        override fun onTabUnselected(tab: TabLayout.Tab) = Unit
        override fun onTabSelected(tab: TabLayout.Tab) = action(tab)
    })
}

fun TabLayout.onTabReselected(action: (TabLayout.Tab) -> Unit) {
    this.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab) = action(tab)
        override fun onTabUnselected(tab: TabLayout.Tab) = Unit
        override fun onTabSelected(tab: TabLayout.Tab) = Unit

    })
}
