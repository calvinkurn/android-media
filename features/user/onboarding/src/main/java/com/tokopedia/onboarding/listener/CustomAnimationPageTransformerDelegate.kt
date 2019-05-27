package com.tokopedia.onboarding.listener

import android.view.View

interface CustomAnimationPageTransformerDelegate {

    /**
     * The page has been selected and is fully opaque. Useful if
     * you want to animate Views.
     */
    fun onPageSelected()

    /**
     * The page is being scrolled. Use the event to change View properties,
     * such as alpha, scale and transition, and create an engaging animation.
     * @param page View of the scrolling page.
     * @param position A position indicative of the scroll position. If
     * position is greater than zero, the page being scrolled
     * is positioned after the currently selected page. If
     * position is smaller than zero, it is positioned before.
     */
    fun onPageScrolled(page: View, position: Float)

    /**
     * The page is invisible to the user. This can be useful
     * to clean up or reset Views.
     * @param position
     */
    fun onPageInvisible(position: Float)

}