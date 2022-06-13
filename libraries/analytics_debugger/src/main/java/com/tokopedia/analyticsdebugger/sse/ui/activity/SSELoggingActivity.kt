package com.tokopedia.analyticsdebugger.sse.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.tokopedia.analytics.debugger.ui.fragment.ApplinkDebuggerFragment
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.sse.ui.fragment.SSELoggingFragment

/**
 * Created By : Jonathan Darwin on November 09, 2021
 */
class SSELoggingActivity: AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sse_logging)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.subtitle = "Tokopedia"
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, SSELoggingFragment.newInstance(), SSELoggingFragment.TAG)
                .commit()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(context: Context): Intent {
            return Intent(context, SSELoggingActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
}