package com.tokopedia.circular_view_pager.presentation.widgets.circularViewPager

interface CircularPageChangeListener {
    fun onPageScrolled(position: Int)
    fun onPageScrollStateChanged(state: Int)
}