package com.tokopedia.talk.feature.sellersettings.common.activity


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.header.HeaderUnify
import com.tokopedia.talk.R

class TalkSellerSettingsActivity : BaseSimpleActivity() {

    override fun getParentViewResourceID(): Int {
        return R.id.talk_seller_settings_parent_view
    }

    override fun getLayoutRes() = R.layout.activity_talk_settings

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNavController()
    }

    private fun setupNavController() {
        val navController = findNavController(R.id.talk_seller_settings_parent_view)
        val listener = AppBarConfiguration.OnNavigateUpListener {
            navController.navigateUp()
        }

        findViewById<HeaderUnify>(R.id.talk_seller_settings_toolbar)?.let {
            setSupportActionBar(it)
        }

        val appBarConfiguration = AppBarConfiguration.Builder().setFallbackOnNavigateUpListener(listener).build()
        navController.setGraph(R.navigation.talk_seller_settings_navigation)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
    }
}