package com.tokopedia.analyticsdebugger.validator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.debugger.ui.fragment.AnalyticsDebuggerFragment

class AnalyticsValidatorActivity  : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analytics_validator)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.subtitle = "Tokopedia Client Analytics Validator"
//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                    .add(R.id.container, AnalyticsDebuggerFragment.newInstance(), AnalyticsDebuggerFragment.TAG)
//                    .commit()
//        }
    }

    companion object {

        fun newInstance(context: Context): Intent {
            return Intent(context, AnalyticsValidatorActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
}