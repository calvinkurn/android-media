package com.tokopedia.home_account.ui.mediaquality

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.header.compose.NestHeader
import com.tokopedia.header.compose.NestHeaderType
import com.tokopedia.home_account.R
import com.tokopedia.media.loader.internal.MediaSettingPreferences
import com.tokopedia.nest.principles.ui.NestTheme
import kotlinx.coroutines.launch

class MediaQualitySettingActivity : BaseActivity() {

    private val settings by lazy { MediaSettingPreferences(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NestTheme {
                var selectedQuality by remember { mutableStateOf(settings.qualitySettings()) }
                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()

                Scaffold(
                    topBar = {
                        NestHeader(
                            type = NestHeaderType.SingleLine(
                                title = stringResource(id = R.string.menu_account_title_quality_setting),
                                onBackClicked = { onBackClicked() }
                            )
                        )
                    },
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    content = { padding ->
                        MediaQualitySettingScreen(
                            qualities = MediaQualityUIModel.settingsMenu(),
                            modifier = Modifier.padding(padding),
                            selectedIndex = selectedQuality,
                            onSelected = {
                                scope.launch {
                                    settings.setQualitySettings(it)
                                    val message = generateToastMessage(it)
                                    selectedQuality = settings.qualitySettings()
                                    snackbarHostState.showSnackbar(message)
                                }
                            }
                        )
                    }
                )
            }
        }
    }

    private fun generateToastMessage(quality: Int): String {
        return getString(
            when (quality) {
                2 -> R.string.image_quality_high_toast
                1 -> R.string.image_quality_low_toast
                else -> R.string.image_quality_auto_toast
            }
        )
    }

    private fun onBackClicked() {
        finish()
    }
}
