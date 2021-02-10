package com.tokopedia.statistic.view.viewhelper

import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

/**
 * Created By @ilhamsuaib on 18/06/20
 */

interface StatisticTabLayoutListener : TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {

    override fun onTabReselected(tab: TabLayout.Tab) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab) {

    }

    override fun onTabSelected(tab: TabLayout.Tab) {

    }
}

fun TabLayout.setOnTabSelectedListener(callback: (tab: TabLayout.Tab) -> Unit) {
    this.addOnTabSelectedListener(object : StatisticTabLayoutListener {
        override fun onTabSelected(tab: TabLayout.Tab) {
            callback(tab)
        }
    })
}

fun ViewPager.addOnPageChanged(onChanged: (Int) -> Unit) {
    this.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

        }

        override fun onPageSelected(position: Int) {
            onChanged(position)
        }

        override fun onPageScrollStateChanged(state: Int) {

        }
    })
}