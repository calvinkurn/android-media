package com.tokopedia.analyticsdebugger.debugger.ui.fragment

import androidx.fragment.app.Fragment

import com.tokopedia.analyticsdebugger.debugger.di.AnalyticsDebuggerComponent
import com.tokopedia.analyticsdebugger.debugger.ui.AnalyticsDebugger

class AnalyticsDebuggerIrisSendFragment : BaseAnalyticsDebuggerFragment(), AnalyticsDebugger.View {

    override fun injectToFragment(component: AnalyticsDebuggerComponent) {
        presenter = component.gtmIrisSendPresenter
    }

    companion object {
        val TAG = AnalyticsDebuggerIrisSendFragment::class.java.canonicalName
        fun newInstance(): Fragment {
            return AnalyticsDebuggerIrisSendFragment()
        }
    }

}