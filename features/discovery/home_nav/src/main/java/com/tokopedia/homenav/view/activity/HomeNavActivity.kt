package com.tokopedia.homenav.view.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.tokopedia.homenav.R
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.setStatusBarColor


class HomeNavActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_top, R.anim.nav_fade_out)
        setContentView(R.layout.activity_main_nav)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBarColor(Color.WHITE)
        }

        findViewById<Toolbar>(R.id.toolbar)?.let {
            setSupportActionBar(it)
            it.navigationIcon = getResDrawable(R.drawable.ic_close_x_black)
        }
        setupNavigation()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.nav_fade_in, R.anim.slide_bottom)
    }

    private fun setupNavigation() {
        val navController = findNavController(R.id.fragment_container)
        val listener = AppBarConfiguration.OnNavigateUpListener {
            navController.navigateUp()
        }

        val appBarConfiguration = AppBarConfiguration.Builder().setFallbackOnNavigateUpListener(listener).build()
        navController.setGraph(R.navigation.nav_graph, Bundle())
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
    }
}