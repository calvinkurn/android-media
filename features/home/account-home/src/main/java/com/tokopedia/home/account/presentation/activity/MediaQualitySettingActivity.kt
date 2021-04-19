package com.tokopedia.home.account.presentation.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.home.account.R
import com.tokopedia.home.account.presentation.fragment.setting.MediaQualitySettingFragment

class MediaQualitySettingActivity: BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.setBackgroundColor(Color.WHITE)
        super.onCreate(savedInstanceState)
        updateToolbarTitle()
    }

    override fun getNewFragment(): Fragment {
        return MediaQualitySettingFragment.createInstance(Bundle.EMPTY)
    }

    private fun updateToolbarTitle() {
        supportActionBar?.setTitle(R.string.image_quality_setting_screen)
    }

    companion object {
        fun getCallingIntent(context: Context?, extras: Bundle?): Intent? {
            val intent = Intent(context, MediaQualitySettingActivity::class.java)
            intent.putExtras(extras ?: Bundle())
            return intent
        }
    }
}