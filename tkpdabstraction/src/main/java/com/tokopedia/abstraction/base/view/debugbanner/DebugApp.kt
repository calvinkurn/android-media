package com.tokopedia.abstraction.base.view.debugbanner

import android.app.Activity
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.R
import com.tokopedia.manageaddress.ui.debugbanner.DebugBannerView

/**
 * Created by irpan on 17/04/23.
 */
class DebugApp() {

    fun initDebug(activity: Activity, liveStatus: String) {
        val decorView = activity.window.decorView as ViewGroup
        val localBanner = Banner()

        var doubleClick = false

        val debugBannerView = DebugBannerView(activity).apply {
            updateText(liveStatus, localBanner.textColorRes)
            updateBannerColor(localBanner.bannerColorRes)
            bannerGravity = localBanner.bannerGravity

            setOnClickListener {
                if (doubleClick) {
                    // Code here when they double click

                    this.visibility = View.GONE
                }
                doubleClick = true
                Handler().postDelayed({ doubleClick = false }, 2000)
            }
        }

        val bannerSize = activity.resources.getDimension(R.dimen.banner_default_size_debug).toInt()
        val params = ViewGroup.MarginLayoutParams(bannerSize, bannerSize)
        decorView.addView(debugBannerView, params)
    }
}
