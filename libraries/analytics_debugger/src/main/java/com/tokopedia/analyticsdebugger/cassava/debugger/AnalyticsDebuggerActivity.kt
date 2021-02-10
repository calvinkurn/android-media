package com.tokopedia.analyticsdebugger.cassava.debugger

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar

import com.tokopedia.analyticsdebugger.R

class AnalyticsDebuggerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analytics_debugger)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, AnalyticsDebuggerFragment.newInstance(), AnalyticsDebuggerFragment.TAG)
                    .commit()
        }
    }

    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, AnalyticsDebuggerActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
}
