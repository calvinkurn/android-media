package com.tokopedia.analyticsdebugger.debugger.ui.fragment

import androidx.fragment.app.Fragment

import com.tokopedia.analyticsdebugger.debugger.di.AnalyticsDebuggerComponent
import com.tokopedia.analyticsdebugger.debugger.ui.AnalyticsDebugger

class AnalyticsDebuggerGtmErrorFragment : BaseAnalyticsDebuggerFragment(), AnalyticsDebugger.View {

    override fun injectToFragment(component: AnalyticsDebuggerComponent) {
        presenter = component.gtmErrorPresenter
    }

    companion object {
        val TAG = AnalyticsDebuggerGtmErrorFragment::class.java.canonicalName
        fun newInstance(): Fragment {
            return AnalyticsDebuggerGtmErrorFragment()
        }
    }

}