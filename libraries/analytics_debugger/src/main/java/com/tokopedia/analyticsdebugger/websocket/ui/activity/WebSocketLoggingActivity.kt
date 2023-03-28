package com.tokopedia.analyticsdebugger.websocket.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.WebSocketLogPageSource

/**
 * Created By : Jonathan Darwin on December 03, 2021
 */
class WebSocketLoggingActivity: AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_websocket_logging)

        // get page source
        val pageSource = intent?.getStringExtra(EXTRA_PAGE_SOURCE).orEmpty()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_websocket_logging_fragment) as NavHostFragment
        navController = navHostFragment.navController
        navController.setGraph(R.navigation.websocket_logging_nav, Bundle().apply {
            putString(EXTRA_PAGE_SOURCE, pageSource)
        })

        appBarConfiguration = AppBarConfiguration(navController.graph)
        findViewById<Toolbar>(R.id.websocket_logging_toolbar).apply {
            setSupportActionBar(this)
            subtitle = pageSource
        }
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val EXTRA_PAGE_SOURCE = "page_source"

        @JvmStatic
        fun newInstance(context: Context, pageSource: WebSocketLogPageSource): Intent {
            return Intent(context, WebSocketLoggingActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                putExtra(EXTRA_PAGE_SOURCE, pageSource.value)
            }
        }
    }
}
