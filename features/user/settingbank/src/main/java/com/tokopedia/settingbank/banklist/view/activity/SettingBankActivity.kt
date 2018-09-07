package com.tokopedia.settingbank.banklist.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.settingbank.banklist.view.fragment.SettingBankFragment

/**
 * @author by nisie on 6/7/18.
 */
class SettingBankActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return SettingBankFragment()
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, SettingBankActivity::class.java)
        }
    }

    object DeeplinkIntents {
        @JvmStatic
        @DeepLink(ApplinkConst.SETTING_BANK)
        fun defaultIntent(context: Context, extras: Bundle): Intent {
            return Intent(context, SettingBankActivity::class.java)
        }
    }
}