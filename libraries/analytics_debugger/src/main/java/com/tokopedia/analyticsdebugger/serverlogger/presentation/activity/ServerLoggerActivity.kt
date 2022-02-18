package com.tokopedia.analyticsdebugger.serverlogger.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.serverlogger.presentation.fragment.ServerLoggerFragment

class ServerLoggerActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_server_logger)
        setupToolbar()
        setupNavigationController()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.subtitle = getString(com.tokopedia.analyticsdebugger.R.string.tokopedia_label)
    }

    private fun setupNavigationController() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_server_logger_fragment) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
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