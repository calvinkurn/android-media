package com.tokopedia.analytics.byteio

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class AppLogFragmentLifecycleCallback: FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        val visibleFragment = fm.fragments.firstOrNull { it.isVisible }
        when {
            visibleFragment is IAdsLog -> {
                AppLogAnalytics.currentActivityName = visibleFragment.activity?.javaClass?.simpleName.orEmpty()
                AppLogAnalytics.currentPageName = visibleFragment.getAdsPageName()
                AppLogAnalytics.updateAdsFragmentPageData(visibleFragment.activity, AppLogParam.PAGE_NAME, visibleFragment.getAdsPageName())
            }
            f is IAdsLog -> {
                AppLogAnalytics.currentActivityName = f.activity?.javaClass?.simpleName.orEmpty()
                AppLogAnalytics.currentPageName = f.getAdsPageName()
                AppLogAnalytics.updateAdsFragmentPageData(f.activity, AppLogParam.PAGE_NAME, f.getAdsPageName())
            }
        }
    }
}
