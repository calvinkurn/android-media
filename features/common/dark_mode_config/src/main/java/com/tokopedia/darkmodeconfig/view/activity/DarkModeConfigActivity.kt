package com.tokopedia.darkmodeconfig.view.activity

//noinspection MissingResourceImportAlias
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.constant.TkpdCache
import com.tokopedia.darkmodeconfig.R
import com.tokopedia.darkmodeconfig.common.DarkModeAnalytics
import com.tokopedia.darkmodeconfig.common.PrefKey
import com.tokopedia.darkmodeconfig.common.Utils
import com.tokopedia.darkmodeconfig.model.UiMode
import com.tokopedia.darkmodeconfig.view.screen.DarkModeConfigScreen
import com.tokopedia.header.compose.NestHeader
import com.tokopedia.header.compose.NestHeaderType
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.utils.lifecycle.collectAsStateWithLifecycle
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Created by @ilhamsuaib on 03/11/23.
 */

class DarkModeConfigActivity : BaseActivity() {

    private val uiModeOptions by getUiModeOptions()
    private val sharedPref: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        saveHasOpenConfigState()
        setContent {
            NestTheme {
                Scaffold(topBar = {
                    NestHeader(
                        modifier = Modifier.fillMaxWidth(),
                        type = NestHeaderType.SingleLine(
                            title = stringResource(id = R.string.dmc_set_theme),
                            onBackClicked = ::closePage
                        )
                    )
                }) { padding ->
                    val options = uiModeOptions.collectAsStateWithLifecycle()
                    DarkModeConfigScreen(
                        modifier = Modifier.padding(padding),
                        options = options.value,
                        onSelectedChanged = {
                            onOptionSelectedChanged(it)
                        }
                    )
                }
            }
        }
    }

    override fun getScreenName(): String {
        return SCREEN_NAME
    }

    private fun saveHasOpenConfigState() {
        lifecycleScope.launch(Dispatchers.Default) {
            val spe = sharedPref.edit()
            spe.putBoolean(PrefKey.HAS_OPENED_CONFIG_PAGE, true)
            spe.apply()
        }
    }

    private fun onOptionSelectedChanged(mode: UiMode) {
        val current = uiModeOptions.value.firstOrNull { it.isSelected }
        if (current?.screenMode == mode.screenMode && mode.isSelected) {
            return
        }

        uiModeOptions.update { options ->
            options.map {
                it.copyModel(isSelected = it.screenMode == mode.screenMode)
            }
        }

        applyAppTheme(mode)
    }

    private fun getUiModeOptions(): Lazy<MutableStateFlow<List<UiMode>>> {
        return lazy {
            val defaultScreenMode = if (isDarkMode()) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
            val screenMode = sharedPref.getInt(
                TkpdCache.Key.KEY_DARK_MODE_CONFIG_SCREEN_MODE, defaultScreenMode
            )
            return@lazy MutableStateFlow(UiMode.getOptionList(screenMode))
        }
    }

    private fun applyAppTheme(mode: UiMode) {
        saveDarkModeState(mode)
        sendAnalytics(mode)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        AppCompatDelegate.setDefaultNightMode(mode.screenMode)
    }

    private fun sendAnalytics(mode: UiMode) {
        val isDarkModeOS = applicationContext.isDarkMode()
        DarkModeAnalytics.eventClickThemeSetting(mode, isDarkModeOS)
    }

    private fun saveDarkModeState(mode: UiMode) {
        val isDarkModeOs = applicationContext.isDarkMode()
        lifecycleScope.launch(Dispatchers.Default) {
            val editor = sharedPref.edit()
            editor.putInt(TkpdCache.Key.KEY_DARK_MODE_CONFIG_SCREEN_MODE, mode.screenMode)
            editor.putBoolean(
                TkpdCache.Key.KEY_DARK_MODE,
                Utils.getIsDarkModeStatus(mode, isDarkModeOs)
            )
            editor.apply()
        }
    }

    private fun closePage() {
        finish()
    }

    companion object {
        private const val SCREEN_NAME = "DarkModeConfigActivity"
    }
}
