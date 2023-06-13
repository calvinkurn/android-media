package com.tokopedia.tokopoints.view.util


import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import com.google.android.material.tabs.TabLayout

object TabUtil {
    fun wrapTabIndicatorToTitle(tabLayout: TabLayout?, externalMargin: Int, internalMargin: Int) {
        val tabStrip = tabLayout?.getChildAt(0)
        if (tabStrip is ViewGroup) {
            val childCount = tabStrip.childCount
            for (i in 0 until childCount) {
                val tabView = tabStrip.getChildAt(i)
                //set minimum width to 0 for instead for small texts, indicator is not wrapped as expected
                tabView.minimumWidth = 0
                // set padding to 0 for wrapping indicator as title
                tabView.setPadding(0, tabView.paddingTop, 0, tabView.paddingBottom)
                // setting custom margin between tabs
                if (tabView.layoutParams is MarginLayoutParams) {
                    val layoutParams = tabView.layoutParams as MarginLayoutParams
                    if (i == 0) {
                        // left
                        setMargin(layoutParams, externalMargin, internalMargin)
                    } else if (i == childCount - 1) {
                        // right
                        setMargin(layoutParams, internalMargin, externalMargin)
                    } else {
                        // internal
                        setMargin(layoutParams, internalMargin, internalMargin)
                    }
                }
            }
            tabLayout.requestLayout()
        }
    }

    private fun setMargin(layoutParams: MarginLayoutParams, start: Int, end: Int) {
        layoutParams.marginStart = start
        layoutParams.marginEnd = end
        layoutParams.leftMargin = start
        layoutParams.rightMargin = end
    }

    fun removedPaddingAtLast(tabLayout: TabLayout, internalMargin: Int) {
        val tabStrip = tabLayout.getChildAt(0)
        if (tabStrip is ViewGroup) {
            val childCount = tabStrip.childCount
            for (i in 0 until childCount) {
                val tabView = tabStrip.getChildAt(i)
                //set minimum width to 0 for instead for small texts, indicator is not wrapped as expected
                tabView.minimumWidth = 0
                // set padding to 0 for wrapping indicator as title
                tabView.setPadding(0, tabView.paddingTop, 0, tabView.paddingBottom)
                // setting custom margin between tabs
                if (tabView.layoutParams is MarginLayoutParams) {
                    val layoutParams = tabView.layoutParams as MarginLayoutParams
                    if (i == childCount - 1) {
                        setMargin(layoutParams, internalMargin, 0)
                    }
                }
            }
            tabLayout.requestLayout()
        }
    }
}
