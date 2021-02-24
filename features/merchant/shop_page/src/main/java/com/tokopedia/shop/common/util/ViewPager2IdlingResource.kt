package com.tokopedia.shop.common.util

import androidx.test.espresso.IdlingResource
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2

object ViewPager2IdlingResource {

    private var isIdle = true // Default to idle since we can't query the scroll state.
    private var resourceCallback: IdlingResource.ResourceCallback? = null
    var idlingResource : IdlingResource? = null

    fun setViewPagerIdlingResource(viewPager: ViewPager2, name: String) {
        idlingResource =  object : IdlingResource {
            override fun getName(): String {
                return name
            }

            override fun isIdleNow(): Boolean {
                return isIdle
            }

            override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
                resourceCallback = callback
            }
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                isIdle = (state == ViewPager.SCROLL_STATE_IDLE // Treat dragging as idle, or Espresso will block itself when swiping.
                        || state == ViewPager.SCROLL_STATE_DRAGGING)
                if (isIdle && resourceCallback != null) {
                    resourceCallback?.onTransitionToIdle()
                }
            }
        })
    }
}