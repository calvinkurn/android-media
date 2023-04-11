package com.tokopedia.journeydebugger.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils

import androidx.fragment.app.Fragment

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

import com.tokopedia.journeydebugger.JourneyDebuggerConst.DATA_DETAIL
import com.tokopedia.journeydebugger.JourneyDebuggerConst.EVENT_NAME
import com.tokopedia.journeydebugger.ui.fragment.JourneyDebuggerDetailFragment
import com.tokopedia.journeydebugger.ui.model.JourneyDebuggerUIModel

class JourneyDebuggerDetailActivity : BaseSimpleActivity() {

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
        return JourneyDebuggerDetailFragment.newInstance(intent.extras ?: Bundle())
    }

    companion object {

        fun newInstance(context: Context, viewModel: JourneyDebuggerUIModel): Intent {
            val intent = Intent(context, JourneyDebuggerDetailActivity::class.java)
            intent.putExtra(DATA_DETAIL, viewModel)
            intent.putExtra(EVENT_NAME, "Journey Debugger")

            return intent
        }
    }
}
