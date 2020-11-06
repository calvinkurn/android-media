package com.tokopedia.shop.settings.basicinfo.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.shop.settings.R

/**
 * Created by Zulfikar on 5/19/2016.
 * deeplink: SHOP_SETTING_INFO
 */
class ShopSettingsInfoActivity : BaseSimpleActivity() {

    companion object {
        @JvmStatic
        fun createIntent(context: Context) = Intent(context, ShopSettingsInfoActivity::class.java)
    }

    override fun getNewFragment(): Fragment? = null

    override fun getLayoutRes(): Int = R.layout.activity_shop_settings_info

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
        setupNavController()
    }

    private fun setupUI() {
        window.decorView.setBackgroundColor(Color.WHITE)
        findViewById<Toolbar>(R.id.toolbar)?.let {
            setSupportActionBar(it)
            supportActionBar?.setBackgroundDrawable(ContextCompat.getDrawable(this, android.R.color.transparent))
            it.title = getString(R.string.shop_settings_info)
        }
    }

    private fun setupNavController() {
        val navController = findNavController(R.id.parent_view)
        val listener = AppBarConfiguration.OnNavigateUpListener {
            navController.navigateUp()
        }

        val appBarConfiguration = AppBarConfiguration.Builder().setFallbackOnNavigateUpListener(listener).build()
        navController.setGraph(R.navigation.shop_settings_navigation)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
    }
}
