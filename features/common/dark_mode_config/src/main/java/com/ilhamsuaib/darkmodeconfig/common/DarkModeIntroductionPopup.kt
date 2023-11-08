package com.ilhamsuaib.darkmodeconfig.common

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.utils.resources.isDarkMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

/**
 * Created by @ilhamsuaib on 07/11/23.
 */

object DarkModeIntroductionPopup : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    private val MIN_DAYS_MILLIS_AFTER_FIRST_OPEN = TimeUnit.DAYS.toMillis(3)

    /**
     * Rules to show the introduction popup :
     * 1. User activating the dark theme system/OS, and
     * 2. Still using light theme in Tokopedia app, and
     * 3. 3 days opened Tokopedia App, and
     * 4. Is logged in
     * 5. Never opened the introduction popup
     * 6. Never opened the dark mode config page
     * */
    fun requestLaunch(context: Context, isLoggedIn: Boolean) {
        launch {
            val sharedPref = getSharedPref(context)
            val isDarkModeOS = context.applicationContext.isDarkMode()
            val isLightModeApp = !isDarkModeApp()
            val isEligibleTimeRange = has3DaysOpenedApp(sharedPref)
            val neverOpenedPopup = !hasOpenedPopup(sharedPref)
            val neverOpenedConfigPage = hasOpenedConfigPage(sharedPref)
            if (isEligibleTimeRange && isLightModeApp &&
                isDarkModeOS && isLoggedIn && neverOpenedPopup && neverOpenedConfigPage
            ) {
                markAsOpenedPopup(sharedPref)
                withContext(Dispatchers.Main) {
                    showPupUp(context)
                }
            }
        }
    }

    private fun hasOpenedConfigPage(sharedPref: SharedPreferences): Boolean {
        return sharedPref.getBoolean(PrefKey.HAS_OPENED_CONFIG_PAGE, true)
    }

    private fun showPupUp(context: Context) {
        RouteManager.route(context, ApplinkConstInternalGlobal.DARK_MODE_INTRO)
    }

    private fun hasOpenedPopup(sharedPref: SharedPreferences): Boolean {
        return sharedPref.getBoolean(PrefKey.HAS_OPENED_POPUP, false)
    }

    private fun markAsOpenedPopup(sharedPref: SharedPreferences) {
        val spe = sharedPref.edit()
        spe.putBoolean(PrefKey.HAS_OPENED_POPUP, true)
        spe.apply()
    }

    private fun has3DaysOpenedApp(sharedPref: SharedPreferences): Boolean {
        val firstOpenTime = sharedPref.getLong(PrefKey.KEY_FIRST_OPEN, 0L)
        if (firstOpenTime <= 0L) {
            saveFirstOpen(sharedPref)
            return false
        }

        val currentTime = System.currentTimeMillis()
        return currentTime.minus(firstOpenTime) >= MIN_DAYS_MILLIS_AFTER_FIRST_OPEN
    }

    private fun saveFirstOpen(sharedPref: SharedPreferences) {
        val currentTime = System.currentTimeMillis()
        val spe = sharedPref.edit()
        spe.putLong(PrefKey.KEY_FIRST_OPEN, currentTime)
        spe.apply()
    }

    private fun getSharedPref(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
    }

    private fun isDarkModeApp(): Boolean {
        val currentNightMode = AppCompatDelegate.getDefaultNightMode()
        return currentNightMode == AppCompatDelegate.MODE_NIGHT_YES
    }
}