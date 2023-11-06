package com.ilhamsuaib.darkmodeconfig.activity

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
import com.ilhamsuaib.darkmodeconfig.model.ThemeOptionUiModel
import com.ilhamsuaib.darkmodeconfig.screen.DarkModeConfigScreen
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.constant.TkpdCache
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

    private val sharedPref by lazy {
        PreferenceManager.getDefaultSharedPreferences(
            applicationContext
        )
    }
    private val themeOptions by getThemeOptions()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                    val options = themeOptions.collectAsStateWithLifecycle()
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

    private fun onOptionSelectedChanged(option: ThemeOptionUiModel) {
        val current = themeOptions.value.firstOrNull { it.isSelected }
        if (current?.screenMode == option.screenMode && option.isSelected) {
            return
        }

        themeOptions.update { options ->
            options.map {
                it.copy(isSelected = it.screenMode == option.screenMode)
            }
        }

        applyAppTheme(option)
    }

    private fun applyAppTheme(option: ThemeOptionUiModel) {
        saveDarkModeState(option)
        AppCompatDelegate.setDefaultNightMode(option.screenMode)
    }

    private fun saveDarkModeState(option: ThemeOptionUiModel) {
        lifecycleScope.launch(Dispatchers.Default) {
            val editor = sharedPref.edit()
            editor.putInt(TkpdCache.Key.KEY_DARK_MODE_CONFIG_SCREEN_MODE, option.screenMode)
            editor.apply()
        }
    }

    private fun getThemeOptions(): Lazy<MutableStateFlow<List<ThemeOptionUiModel>>> {
        return lazy {
            val defaultScreenMode = if (isDarkMode()) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
            val screenMode = sharedPref.getInt(
                TkpdCache.Key.KEY_DARK_MODE_CONFIG_SCREEN_MODE, defaultScreenMode
            )
            return@lazy MutableStateFlow(ThemeOptionUiModel.getOptionList(screenMode))
        }
    }

    private fun closePage() {
        finish()
    }
}
