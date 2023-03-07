package com.tokopedia.journeydebugger.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

import com.tokopedia.journeydebugger.R
import com.tokopedia.journeydebugger.ui.fragment.JourneyDebuggerFragment

class JourneyDebuggerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journey_debugger)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.subtitle = "Tokopedia"
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, JourneyDebuggerFragment.newInstance(), JourneyDebuggerFragment.TAG)
                    .commit()
        }
    }

    companion object {

        fun newInstance(context: Context): Intent {
            return Intent(context, JourneyDebuggerActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
}
