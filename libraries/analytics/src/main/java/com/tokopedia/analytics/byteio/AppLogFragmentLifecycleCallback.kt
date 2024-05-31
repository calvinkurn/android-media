package com.tokopedia.analytics.byteio

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.analytics.byteio.topads.AppLogTopAds

class AppLogFragmentLifecycleCallback: FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        val visibleFragment = fm.fragments.firstOrNull { it.isVisible }
        when {
            visibleFragment is IAdsLog -> {
                AppLogTopAds.currentActivityName = visibleFragment.activity?.javaClass?.simpleName.orEmpty()
                AppLogTopAds.currentPageName = visibleFragment.getAdsPageName()
                AppLogTopAds.updateAdsFragmentPageData(visibleFragment.activity, AppLogParam.PAGE_NAME, visibleFragment.getAdsPageName())
            }
            f is IAdsLog -> {
                AppLogTopAds.currentActivityName = f.activity?.javaClass?.simpleName.orEmpty()
                AppLogTopAds.currentPageName = f.getAdsPageName()
                AppLogTopAds.updateAdsFragmentPageData(f.activity, AppLogParam.PAGE_NAME, f.getAdsPageName())
            }
        }
    }
}
