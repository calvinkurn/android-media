package com.tokopedia.abstraction.base.view.debugbanner

import android.app.Activity
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.R

/**
 * Created by irpan on 17/04/23.
 */
class DebugBanner {


    /**
     * not show banner when liveStatus is Empty
     */
    fun initView(activity: Activity, liveStatus: String) {

        if (liveStatus.isNotEmpty()) {
            val decorView = activity.window.decorView as ViewGroup
            val localBanner = Banner()

            val debugBannerView = DebugBannerView(activity).apply {
                updateText(liveStatus, localBanner.textColorRes)
                if (liveStatus.contains("staging")) {
                    updateBannerColor(R.color.banner_debug_staging)
                } else {
                    updateBannerColor(R.color.banner_debug_beta)
                }
                bannerGravity = localBanner.bannerGravity
            }

            val bannerSize = activity.resources.getDimension(R.dimen.banner_default_size_debug).toInt()
            val params = ViewGroup.MarginLayoutParams(bannerSize, bannerSize)
            decorView.addView(debugBannerView, params)
        }
    }
}
