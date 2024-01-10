package com.tokopedia.analyticsdebugger.debugger.ui.fragment

import android.widget.Toast
import androidx.fragment.app.Fragment

import com.tokopedia.analyticsdebugger.debugger.di.AnalyticsDebuggerComponent
import com.tokopedia.analyticsdebugger.debugger.ui.AnalyticsDebugger
import com.tokopedia.analyticsdebugger.debugger.ui.presenter.AnalyticsServerLogDebuggerPresenter
import com.tokopedia.analyticsdebugger.debugger.ui.presenter.ApplinkDebugger
import javax.inject.Inject

class ServerLogDebuggerFragment : BaseAnalyticsDebuggerFragment(), AnalyticsDebugger.View {

    override fun injectToFragment(component: AnalyticsDebuggerComponent) {
        presenter = component.serverLoggerpresenter
    }

    companion object {
        val TAG = ServerLogDebuggerFragment::class.java.canonicalName
        fun newInstance(): Fragment {
            return ServerLogDebuggerFragment()
        }
    }

    override fun showCount(count: Int) {
        // noop
    }
}