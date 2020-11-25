package com.tokopedia.analyticsdebugger.debugger.ui.fragment

import android.widget.Toast
import androidx.fragment.app.Fragment

import com.tokopedia.analyticsdebugger.debugger.di.AnalyticsDebuggerComponent
import com.tokopedia.analyticsdebugger.debugger.ui.AnalyticsDebugger

class AnalyticsDebuggerIrisSaveFragment : BaseAnalyticsDebuggerFragment(), AnalyticsDebugger.View {

    override fun injectToFragment(component: AnalyticsDebuggerComponent) {
        presenter = component.gtmIrisSavePresenter
    }

    companion object {
        val TAG = AnalyticsDebuggerIrisSaveFragment::class.java.canonicalName
        fun newInstance(): Fragment {
            return AnalyticsDebuggerIrisSaveFragment()
        }
    }

    override fun showCount(count: Int) {
        Toast.makeText(context, "Total Data Save $count", Toast.LENGTH_LONG).show()
    }
}