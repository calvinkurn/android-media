package com.tokopedia.sellerhome.view.viewhelper

import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.view.viewhelper.lottiebottomnav.LottieBottomNav
import kotlinx.android.synthetic.main.activity_sah_seller_home.*
import kotlin.math.abs

class SellerHomePreDrawListener(
    resources: Resources?,
    private val sahRootLayout: ViewGroup?
) : ViewTreeObserver.OnPreDrawListener {
    private val keyboardHeightEstimation = resources?.getDimension(R.dimen.sah_keyboard_height_estimation).orZero()

    private val navBarShadow = sahRootLayout?.findViewById<View>(R.id.navBarShadow)
    private val sahBottomNav = sahRootLayout?.findViewById<LottieBottomNav>(R.id.sahBottomNav)

    override fun onPreDraw(): Boolean {
        return sahRootLayout.let {
            if (it == null || keyboardHeightEstimation == 0f) {
                navBarShadow?.show()
                sahBottomNav?.show()
                true
            } else {
                val screenHeight = getScreenHeight()
                val sahRootLayoutHeight = it.measuredHeight
                val showNavBar = abs(screenHeight - sahRootLayoutHeight) <= keyboardHeightEstimation
                if (showNavBar) {
                    // Postpone the show logic so that the bottom nav will show after root layout become
                    // full screen to prevent glitch then continue the drawing process
                    it.post {
                        navBarShadow?.show()
                        sahBottomNav?.show()
                    }
                    true
                } else {
                    if (navBarShadow?.isVisible != true && sahBottomNav?.isVisible != true) {
                        // Continue the drawing process because bottom nav is already hidden
                        true
                    } else {
                        // To prevent glitch showing keyboard, cancel current drawing process and hide
                        // the bottom nav first
                        navBarShadow?.gone()
                        sahBottomNav?.gone()
                        false
                    }
                }
            }
        }
    }
}