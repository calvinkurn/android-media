package com.tokopedia.onboarding.listener

import android.support.v4.view.ViewPager
import android.view.View

/**
 * @author by stevenfredian on 15/05/19.
 */
class CustomAnimationPageTransformer : ViewPager.PageTransformer {

    override fun transformPage(page: View, position: Float) {
        if (page.tag is CustomAnimationPageTransformerDelegate) {
            val delegate = page.tag as CustomAnimationPageTransformerDelegate

            if (position == 0.0f) {
                // Page is selected
                delegate.onPageSelected()
            } else if (position <= -1.0f || position >= 1.0f) {
                // Page not visible to the user
                delegate.onPageInvisible(position)
            } else {
                // Page is being scrolled
                delegate.onPageScrolled(page, position)
            }
        }
    }

}
