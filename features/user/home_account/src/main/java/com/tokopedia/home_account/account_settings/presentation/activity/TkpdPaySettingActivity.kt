package com.tokopedia.home_account.account_settings.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.home_account.account_settings.presentation.activity.TkpdPaySettingActivity
import com.tokopedia.home_account.account_settings.presentation.fragment.setting.TkpdPaySettingFragment

class TkpdPaySettingActivity : BaseSimpleActivity() {


    override fun getNewFragment(): Fragment {
        return TkpdPaySettingFragment.createInstance()
    }

    companion object {
        fun createIntent(context: Context?): Intent {
            return Intent(context, TkpdPaySettingActivity::class.java)
        }
    }
}