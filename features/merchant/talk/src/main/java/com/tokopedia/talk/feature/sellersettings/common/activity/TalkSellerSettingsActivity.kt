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

    companion object {
        const val KEY_NAVIGATION_PARAM = "navigation"
    }

    private var navigationParam: String = ""

    override fun getParentViewResourceID(): Int {
        return R.id.talk_seller_settings_parent_view
    }

    override fun getLayoutRes() = R.layout.activity_talk_settings

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDataFromApplink()
        setupNavController()
    }

    private fun setupNavController() {
        val navController = findNavController(R.id.talk_seller_settings_parent_view)
        val listener = AppBarConfiguration.OnNavigateUpListener {
            navController.navigateUp()
        }

        val bundle = Bundle().apply {
            putString(KEY_NAVIGATION_PARAM, navigationParam)
        }

        findViewById<HeaderUnify>(R.id.talk_seller_settings_toolbar)?.let {
            setSupportActionBar(it)
            it.setNavigationOnClickListener {
                onBackPressed()
            }
        }

        val appBarConfiguration = AppBarConfiguration.Builder().setFallbackOnNavigateUpListener(listener).build()
        navController.setGraph(R.navigation.talk_seller_settings_navigation, bundle)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
    }

    private fun getDataFromApplink() {
        val uri = intent.data
        navigationParam = uri?.getQueryParameter(KEY_NAVIGATION_PARAM) ?: ""
    }
}