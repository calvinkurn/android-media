package com.tokopedia.home_account.account_settings.presentation.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.home_account.account_settings.presentation.uimodel.MediaQualityUIModel
import com.tokopedia.kotlin.extensions.view.hide

class MediaQualitySettingComposeActivity : BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.hide()
        setContent {
            MediaQualitySettingScreen(qualities = MediaQualityUIModel.settingsMenu())
        }
    }

    override fun getNewFragment(): Fragment? {
        return null
    }
}
