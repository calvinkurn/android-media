package com.tokopedia.analytics.byteio

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class AppLogFragmentLifecycleCallback: FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        val visibleFragment = fm.fragments.firstOrNull { it.isVisible }
        if (visibleFragment is AppLogInterface) {
            AppLogAnalytics.currentActivityName = visibleFragment.activity?.javaClass?.simpleName.orEmpty()
            AppLogAnalytics.currentPageName = visibleFragment.getPageName()
            AppLogAnalytics.updateAdsFragmentPageData(AppLogParam.PAGE_NAME, visibleFragment.getPageName())
        }
    }
}
