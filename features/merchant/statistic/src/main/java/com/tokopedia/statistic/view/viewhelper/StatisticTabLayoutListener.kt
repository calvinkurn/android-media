package com.tokopedia.statistic.view.viewhelper

import com.google.android.material.tabs.TabLayout

/**
 * Created By @ilhamsuaib on 18/06/20
 */

fun TabLayout.setOnTabSelectedListener(callback: (tab: TabLayout.Tab) -> Unit) {
    this.clearOnTabSelectedListeners()
    this.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

        override fun onTabSelected(tab: TabLayout.Tab) {
            callback(tab)
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {

        }

        override fun onTabReselected(tab: TabLayout.Tab?) {

        }
    })
}