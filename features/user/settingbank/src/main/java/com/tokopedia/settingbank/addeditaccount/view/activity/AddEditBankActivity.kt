package com.tokopedia.settingbank.addeditaccount.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.settingbank.view.activity.SettingBankActivity
import com.tokopedia.settingbank.banklist.view.fragment.SettingBankFragment

/**
 * @author by nisie on 6/21/18.
 */

class AddEditBankActivity : BaseSimpleActivity() {

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