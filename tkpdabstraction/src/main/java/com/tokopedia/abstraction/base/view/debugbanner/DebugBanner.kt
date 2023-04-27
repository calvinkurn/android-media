package com.tokopedia.abstraction.base.view.debugbanner

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.TypedValue
import android.view.ViewGroup
import com.tokopedia.manageaddress.ui.debugbanner.DebugBannerView

class DebugBanner constructor(app: Application, private var banner: Banner) :
    Application.ActivityLifecycleCallbacks by ActivityEmptyLifecycleCallbacks() {

    companion object {

        fun init(application: Application, banner: Banner = Banner()) {
            DebugBanner(application, banner)
        }
    }

    init {
        app.registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityCreated(p0: Activity, savedInstanceState: Bundle?) {
        val decorView = p0.window.decorView as ViewGroup
        val localBanner = if (p0 is BannerView) {
            p0.createBanner()
        } else {
            banner
        }
        val debugBannerView = DebugBannerView(p0).apply {
            updateText(localBanner.bannerText, localBanner.textColorRes)
            updateBannerColor(localBanner.bannerColorRes)
            bannerGravity = localBanner.bannerGravity
        }
        val bannerSize = p0.resources.getDimension(
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 65f, p0.resources.displayMetrics).toInt()
        ).toInt()
        val params = ViewGroup.MarginLayoutParams(bannerSize, bannerSize)
        decorView.addView(debugBannerView, params)

//        //Small hack for pre lollipop devices,
//        //there is no problem for getting status bar height in this way,
//        //because we use it only for pre lollipop devices, and I know about windowInstets :D
//        if (!isAtLeastLollipop)
//            debugBannerView.translationY = activity.getStatusBarHeight()
    }
}
