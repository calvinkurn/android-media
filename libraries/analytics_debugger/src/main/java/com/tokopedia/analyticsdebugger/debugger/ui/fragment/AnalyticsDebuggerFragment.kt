package com.tokopedia.analyticsdebugger.debugger.ui.fragment

import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.tokopedia.analyticsdebugger.R

import com.tokopedia.analyticsdebugger.debugger.di.AnalyticsDebuggerComponent
import com.tokopedia.analyticsdebugger.debugger.ui.AnalyticsDebugger
import com.tokopedia.analyticsdebugger.validator.MainValidatorActivity

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_menu_delete) {
            presenter!!.deleteAll()
            return true
        } else if (item.itemId == R.id.action_menu_cav) {
            goToValidator()
            return true
        } else return super.onOptionsItemSelected(item)
    }

    private fun goToValidator() {
        context?.let {
            val intent = MainValidatorActivity.newInstance(it)
            startActivity(intent)
        }
    }

}
