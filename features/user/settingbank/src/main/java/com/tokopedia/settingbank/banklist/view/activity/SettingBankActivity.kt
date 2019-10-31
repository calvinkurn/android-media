package com.tokopedia.settingbank.banklist.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.settingbank.banklist.view.fragment.SettingBankFragment

/**
 * @author by nisie on 6/7/18.
 * For navigating to this class
 * {@link com.tokopedia.applink.internal.ApplinkConstInternalGlobal#SETTING_BANK}
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
}