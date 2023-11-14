package com.tokopedia.darkmodeconfig.view.activity

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.tokopedia.darkmodeconfig.common.DarkModeAnalytics
import com.tokopedia.darkmodeconfig.model.UiMode
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.constant.TkpdCache
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by @ilhamsuaib on 08/11/23.
 */

abstract class DarkModeActivity : BaseActivity() {

    protected val sharedPref: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(applicationContext)
    }

    protected fun applyAppTheme(mode: UiMode) {
        sendAnalytics(mode)
        saveDarkModeState(mode)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        AppCompatDelegate.setDefaultNightMode(mode.screenMode)
    }

    private fun sendAnalytics(mode: UiMode) {
        val isDarkModeOS = applicationContext.isDarkMode()
        DarkModeAnalytics.eventClickThemeSetting(mode, isDarkModeOS)
    }

    private fun saveDarkModeState(option: UiMode) {
        lifecycleScope.launch(Dispatchers.Default) {
            val editor = sharedPref.edit()
            editor.putInt(TkpdCache.Key.KEY_DARK_MODE_CONFIG_SCREEN_MODE, option.screenMode)
            editor.apply()
        }
    }
}