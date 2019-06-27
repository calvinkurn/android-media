package com.tokopedia.kotlin.extensions.view

import android.support.design.widget.TabLayout

/**
 * @author by milhamj on 16/01/19.
 */

fun TabLayout.onTabSelected(action: (TabLayout.Tab) -> Unit) {
    this.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab) {
        }

        override fun onTabUnselected(tab: TabLayout.Tab) {
        }

        override fun onTabSelected(tab: TabLayout.Tab) {
            action(tab)
        }
    })
}