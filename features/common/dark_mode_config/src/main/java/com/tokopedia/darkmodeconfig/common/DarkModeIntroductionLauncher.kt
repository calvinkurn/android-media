package com.tokopedia.darkmodeconfig.common

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentManager
import androidx.preference.PreferenceManager
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.darkmodeconfig.view.bottomsheet.DarkModeIntroBottomSheet
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.resources.isDarkMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext
import com.tokopedia.darkmodeconfig.R as darkmodeconfigR

/**
 * Created by @ilhamsuaib on 07/11/23.
 */

object DarkModeIntroductionLauncher : CoroutineScope {

    private const val EXTRA_SHOW_TOASTER = "extra_show_toaster"
    private val MIN_DAYS_MILLIS_AFTER_FIRST_OPEN = TimeUnit.DAYS.toMillis(3)

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    @JvmStatic
    fun withToaster(intent: Intent, view: View): DarkModeIntroductionLauncher {
        val showToaster = intent.getBooleanExtra(EXTRA_SHOW_TOASTER, false).orFalse()
        val isDarkMode = view.context.isDarkMode()
        if (showToaster && isDarkMode) {
            showToaster(view)
        }
        return this
    }

    /**
     * Rules to show the introduction popup :
     * 1. User activating the dark theme system/OS, and
     * 2. Still using light theme in Tokopedia app, and
     * 3. 3 days opened Tokopedia App, and
     * 4. Is logged in, and
     * 5. Never opened the introduction popup, and
     * 6. Never opened the dark mode config page, and
     * 7. Is enabled by remote config, and
     * 8. Is not forced to light mode by remote config
     * */
    fun launch(context: Context, fm: FragmentManager, isLoggedIn: Boolean) {
        launch {
            val sharedPref = getSharedPref(context.applicationContext)
            val isDarkModeOS = context.applicationContext.isDarkMode()
            val isLightModeApp = !isDarkModeApp()
            val isEligibleTimeRange = has3DaysOpenedApp(sharedPref)
            val neverOpenedPopup = !hasOpenedPopup(sharedPref)
            val neverOpenedConfigPage = hasOpenedConfigPage(sharedPref)
            val shouldShowPopup = isEligibleTimeRange && isLightModeApp && isDarkModeOS &&
                isLoggedIn && neverOpenedPopup && neverOpenedConfigPage
            if (shouldShowPopup) {
                val isAllowedByRemoteConfig = isAllowedByRemoteConfig(context)
                if (isAllowedByRemoteConfig) {
                    markAsOpenedPopup(sharedPref)
                    withContext(Dispatchers.Main) {
                        showPupUp(context, fm)
                    }
                }
            }
        }
    }

    private fun isAllowedByRemoteConfig(context: Context): Boolean {
        val remoteConfig: RemoteConfig = FirebaseRemoteConfigImpl(context.applicationContext)
        val isEnabled = remoteConfig.getBoolean(RemoteConfigKey.ENABLE_DARK_MODE_INTRO, false)
        val isNotForceLightMode = !remoteConfig.getBoolean(RemoteConfigKey.FORCE_LIGHT_MODE, true)
        return isEnabled && isNotForceLightMode
    }

    private fun hasOpenedConfigPage(sharedPref: SharedPreferences): Boolean {
        return sharedPref.getBoolean(PrefKey.HAS_OPENED_CONFIG_PAGE, true)
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
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    private fun isDarkModeApp(): Boolean {
        val currentNightMode = AppCompatDelegate.getDefaultNightMode()
        return currentNightMode == AppCompatDelegate.MODE_NIGHT_YES
    }

    private fun showPupUp(context: Context, fm: FragmentManager) {
        DarkModeIntroBottomSheet.getInstance(fm)
            .show(fm, onApplyConfig = {
                requestDarkModeToaster(context)
            })
    }

    private fun requestDarkModeToaster(context: Context) {
        val intent = RouteManager.getIntent(context, ApplinkConst.HOME)
        intent.putExtra(EXTRA_SHOW_TOASTER, true)
        context.startActivity(intent)
    }

    private fun showToaster(view: View) {
        val context = view.context.applicationContext
        val message = context.getString(darkmodeconfigR.string.dmc_apply_dark_mode_toaster_message)
        val cta = context.getString(darkmodeconfigR.string.dmc_apply_dark_mode_toaster_cta)
        Toaster.toasterCustomBottomHeight = context.dpToPx(96).toInt()
        Toaster.build(
            view,
            message,
            Toaster.LENGTH_SHORT,
            Toaster.TYPE_NORMAL,
            cta
        ).show()
    }
}
