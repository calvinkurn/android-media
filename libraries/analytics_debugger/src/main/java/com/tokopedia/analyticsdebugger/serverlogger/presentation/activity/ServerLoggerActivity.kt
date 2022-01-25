package com.tokopedia.analyticsdebugger.serverlogger.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.serverlogger.presentation.fragment.ServerLoggerFragment

class ServerLoggerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_server_logger)
        inflateFragment(savedInstanceState)
    }

    private fun inflateFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.container,
                ServerLoggerFragment.newInstance(),
                ServerLoggerFragment.TAG
            ).commit()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(context: Context): Intent {
            return Intent(
                context,
                ServerLoggerActivity::class.java
            ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
}