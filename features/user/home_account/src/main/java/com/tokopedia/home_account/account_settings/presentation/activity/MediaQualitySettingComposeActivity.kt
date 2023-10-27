package com.tokopedia.home_account.account_settings.presentation.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.header.compose.NestHeader
import com.tokopedia.header.compose.NestHeaderType
import com.tokopedia.home_account.R
import com.tokopedia.home_account.account_settings.presentation.uimodel.MediaQualityUIModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.nest.principles.ui.NestTheme

class MediaQualitySettingComposeActivity : BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.hide()
        setContent {
            NestTheme {
                Scaffold(topBar = {
                    NestHeader(
                        type = NestHeaderType.SingleLine(
                            title = stringResource(id = R.string.menu_account_title_quality_setting),
                            onBackClicked = { onBackClicked() }
                        )
                    )
                }, content = { padding ->
                        MediaQualitySettingScreen(
                            qualities = MediaQualityUIModel.settingsMenu(),
                            modifier = Modifier.padding(padding)
                        )
                    })
            }
        }
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    private fun onBackClicked() {
        finish()
    }
}
