package com.tokopedia.verification.notif.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.verification.common.abstraction.BaseOtpActivity
import com.tokopedia.verification.notif.view.fragment.SettingNotifFragment

/**
 * Created by Ade Fulki on 14/09/20.
 */

class SettingNotifActivity : BaseOtpActivity() {

    override fun getNewFragment(): Fragment? {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return SettingNotifFragment.createInstance(bundle)
    }
}
