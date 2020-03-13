package com.tokopedia.analyticsdebugger.debugger.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.debugger.ui.fragment.AnalyticsDebuggerIrisSendFragment

class AnalyticsIrisSendDebuggerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analytics_debugger)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.subtitle = "Tokopedia"
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, AnalyticsDebuggerIrisSendFragment.newInstance(), AnalyticsDebuggerIrisSendFragment.TAG)
                    .commit()
        }
    }

    companion object {

        fun newInstance(context: Context): Intent {
            return Intent(context, AnalyticsIrisSendDebuggerActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
}
