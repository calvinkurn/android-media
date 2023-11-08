package com.ilhamsuaib.darkmodeconfig.view.activity

//noinspection MissingResourceImportAlias
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
import com.ilhamsuaib.darkmodeconfig.R
import com.ilhamsuaib.darkmodeconfig.common.DarkModeAnalytics
import com.ilhamsuaib.darkmodeconfig.common.PrefKey
import com.ilhamsuaib.darkmodeconfig.model.UiMode
import com.ilhamsuaib.darkmodeconfig.view.screen.DarkModeConfigScreen
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.constant.TkpdCache
import com.tokopedia.header.compose.NestHeader
import com.tokopedia.header.compose.NestHeaderType
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.utils.lifecycle.collectAsStateWithLifecycle
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Created by @ilhamsuaib on 03/11/23.
 */

class DarkModeConfigActivity : BaseActivity() {

    private val sharedPref by lazy {
        PreferenceManager.getDefaultSharedPreferences(
            applicationContext
        )
    }
    private val uiModeOptions by getUiModeOptions()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        saveHasOpenConfigState()
        setContent {
            NestTheme {
                Scaffold(
                    topBar = {
                        NestHeader(
                            modifier = Modifier.fillMaxWidth(),
                            type = NestHeaderType.SingleLine().copy(
                                title = stringResource(id = R.string.dmc_set_theme),
                                onBackClicked = ::closePage
                            )
                        )
                    }
                ) { padding ->
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

        sendAnalytics(mode)
        applyAppTheme(mode)
    }

    private fun sendAnalytics(mode: UiMode) {
        DarkModeAnalytics.eventClickThemeSetting(mode)
    }

    private fun applyAppTheme(option: UiMode) {
        saveDarkModeState(option)
        AppCompatDelegate.setDefaultNightMode(option.screenMode)
    }

    private fun saveDarkModeState(option: UiMode) {
        lifecycleScope.launch(Dispatchers.Default) {
            val editor = sharedPref.edit()
            editor.putInt(TkpdCache.Key.KEY_DARK_MODE_CONFIG_SCREEN_MODE, option.screenMode)
            editor.apply()
        }
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

    private fun closePage() {
        finish()
    }
}
