package com.tokopedia.settingbank.banklist.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.settingbank.banklist.view.fragment.DebugSettingBankFragment

class DebugSettingBankActivity : SettingBankActivity() {

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return DebugSettingBankFragment()
    }
}