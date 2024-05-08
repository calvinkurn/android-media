package com.tokopedia.analytics.byteio

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class AppLogFragmentLifecycleCallback: FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        val visibleFragment = fm.fragments.firstOrNull { it.isVisible }
        when {
            visibleFragment is IAdsLog -> {
                AppLogAnalytics.currentPageName = visibleFragment.getPageName()
                AppLogAnalytics.updateAdsFragmentPageData(AppLogParam.PAGE_NAME, visibleFragment.getPageName())
            }
            f is IAdsLog -> {
                AppLogAnalytics.currentPageName = f.getPageName()
                AppLogAnalytics.updateAdsFragmentPageData(AppLogParam.PAGE_NAME, f.getPageName())
            }
        }
    }
}
