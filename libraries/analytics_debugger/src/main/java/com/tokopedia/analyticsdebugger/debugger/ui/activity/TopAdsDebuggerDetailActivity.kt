package com.tokopedia.analyticsdebugger.debugger.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.analyticsdebugger.debugger.AnalyticsDebuggerConst.DATA_DETAIL
import com.tokopedia.analyticsdebugger.debugger.AnalyticsDebuggerConst.EVENT_NAME
import com.tokopedia.analyticsdebugger.debugger.ui.fragment.TopAdsDebuggerDetailFragment
import com.tokopedia.analyticsdebugger.debugger.ui.model.TopAdsDebuggerViewModel

class TopAdsDebuggerDetailActivity : BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent != null
                && intent.extras != null
                && !TextUtils.isEmpty(intent.extras!!.getString(EVENT_NAME))
                && supportActionBar != null) {
            supportActionBar!!.title = intent.extras!!.getString(EVENT_NAME)
        }
    }

    override fun getNewFragment(): Fragment? {
        return TopAdsDebuggerDetailFragment.newInstance(intent.extras ?: Bundle())
    }

    companion object {

        fun newInstance(context: Context, viewModel: TopAdsDebuggerViewModel): Intent {
            val intent = Intent(context, TopAdsDebuggerDetailActivity::class.java)
            intent.putExtra(DATA_DETAIL, viewModel)
            intent.putExtra(EVENT_NAME, "Topads Verificator")

            return intent
        }
    }
}
