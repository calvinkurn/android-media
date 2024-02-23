package com.tokopedia.analytics.byteio

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class AppLogFragmentLifecycleCallback : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        super.onFragmentStarted(fm, f)
        if (f is AppLogInterface) {
            AppLogAnalytics.pushPageData()
            AppLogAnalytics.putPageData(AppLogParam.PAGE_NAME, f.getPageName())
            if (f.isEnterFromWhitelisted()) {
                AppLogAnalytics.putPageData(AppLogParam.ENTER_FROM, f.getPageName())
            }
        }
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        super.onFragmentPaused(fm, f)
        if (f is AppLogInterface) {
            AppLogAnalytics.popPageData()
        }
    }
}
