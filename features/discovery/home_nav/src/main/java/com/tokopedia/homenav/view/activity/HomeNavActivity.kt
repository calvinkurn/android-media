package com.tokopedia.homenav.view.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.homenav.R
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.setStatusBarColor

class HomeNavActivity: BaseSimpleActivity() {
    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBarColor(Color.WHITE)
        }

        findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)?.let {
            setSupportActionBar(it)
            it.navigationIcon = getResDrawable(R.drawable.ic_close_x_black)
        }
        setupNavigation()
    }

    override fun getLayoutRes() = R.layout.activity_main_nav


    override fun getParentViewResourceID(): Int = R.id.fragment_container

    fun setupNavigation() {
        val navController = findNavController(R.id.fragment_container)
        val listener = AppBarConfiguration.OnNavigateUpListener {
            navController.navigateUp()
        }

        val appBarConfiguration = AppBarConfiguration.Builder().setFallbackOnNavigateUpListener(listener).build()
        navController.setGraph(R.navigation.nav_graph, Bundle())
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
    }
}