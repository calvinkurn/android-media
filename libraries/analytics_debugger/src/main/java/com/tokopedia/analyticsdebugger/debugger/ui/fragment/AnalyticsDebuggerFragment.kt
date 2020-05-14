package com.tokopedia.analyticsdebugger.debugger.ui.fragment

import androidx.fragment.app.Fragment

import com.tokopedia.analyticsdebugger.debugger.di.AnalyticsDebuggerComponent
import com.tokopedia.analyticsdebugger.debugger.ui.AnalyticsDebugger

class AnalyticsDebuggerFragment : BaseAnalyticsDebuggerFragment(), AnalyticsDebugger.View {

    override fun injectToFragment(component: AnalyticsDebuggerComponent) {
        presenter = component.gtmPresenter
    }

    companion object {
        val TAG = AnalyticsDebuggerFragment::class.java.canonicalName
        fun newInstance(): Fragment {
            return AnalyticsDebuggerFragment()
        }
    }

}
