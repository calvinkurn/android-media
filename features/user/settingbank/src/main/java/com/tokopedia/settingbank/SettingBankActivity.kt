package com.tokopedia.settingbank

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

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
        open fun createIntent(context: Context): Intent {
            return Intent(context, SettingBankActivity::class.java)
        }
    }
}