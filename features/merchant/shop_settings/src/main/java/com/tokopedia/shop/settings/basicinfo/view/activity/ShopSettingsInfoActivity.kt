package com.tokopedia.shop.settings.basicinfo.view.activity

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.header.HeaderUnify
import com.tokopedia.shop.settings.R

/**
 * Created by Zulfikar on 5/19/2016.
 * deeplink: SHOP_SETTING_INFO
 */
class ShopSettingsInfoActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? = null

    override fun getLayoutRes(): Int = R.layout.activity_shop_settings_info

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
        setupNavController()
    }

    private fun setupUI() {
        window.decorView.setBackgroundColor(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0))
        updateActivityToolbar()
    }

    private fun setupNavController() {
        val navController = findNavController(R.id.parent_view)
        val listener = AppBarConfiguration.OnNavigateUpListener {
            navController.navigateUp()
        }

        val appBarConfiguration = AppBarConfiguration.Builder().setFallbackOnNavigateUpListener(listener).build()
        navController.setGraph(R.navigation.shop_settings_navigation)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { _, _, _ ->
            updateActivityToolbar()
        }
    }

    private fun updateActivityToolbar() {
        findViewById<HeaderUnify>(R.id.header)?.let {
            setSupportActionBar(it)
            it.isShowShadow = true
            it.title = getString(R.string.shop_settings_info)
            // set to dark mode color support
            val color = ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N700)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                it.navigationIcon?.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_IN)
            }else{
                it.navigationIcon?.setColorFilter(color, PorterDuff.Mode.SRC_IN)
            }
        }
    }
}
