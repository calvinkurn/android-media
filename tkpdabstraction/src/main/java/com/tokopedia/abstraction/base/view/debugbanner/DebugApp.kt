package com.tokopedia.abstraction.base.view.debugbanner

import android.app.Activity
import android.view.ViewGroup
import com.tokopedia.abstraction.R
import com.tokopedia.manageaddress.ui.debugbanner.DebugBannerView

/**
 * Created by irpan on 17/04/23.
 */
class DebugApp() {

    fun initDebug(p0: Activity, liveStatus: String) {
        val decorView = p0.window.decorView as ViewGroup
        val localBanner = Banner()

        val debugBannerView = DebugBannerView(p0).apply {
            updateText(liveStatus, localBanner.textColorRes)
            updateBannerColor(localBanner.bannerColorRes)
            bannerGravity = localBanner.bannerGravity
        }
        val bannerSize = p0.resources.getDimension(R.dimen.banner_default_size_debug).toInt()
        val params = ViewGroup.MarginLayoutParams(bannerSize, bannerSize)
        decorView.addView(debugBannerView, params)
    }
}
