package com.tokopedia.videorecorder.utils

import android.support.v4.view.ViewPager

/**
 * Created by isfaaghyth on 02/04/19.
 * github: @isfaaghyth
 */
class PageChangeCallback(val it: (position: Int) -> Unit): ViewPager.OnPageChangeListener {

    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) {
        it.invoke(position)
    }
}