package com.tokopedia.otp.notif.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.otp.R
import com.tokopedia.otp.common.IOnBackPressed
import com.tokopedia.otp.common.abstraction.BaseOtpActivity
import com.tokopedia.otp.notif.view.fragment.SettingNotifFragment

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

    override fun setupLayout(savedInstanceState: Bundle?) {
        super.setupLayout(savedInstanceState)
        supportActionBar?.apply {
            title = getString(R.string.title_setting_push_notif)
            setDisplayShowTitleEnabled(true)
        }
    }

    override fun onBackPressed() {
        val fragment = this.supportFragmentManager.findFragmentById(R.id.parent_view)
        (fragment as? IOnBackPressed)?.onBackPressed()?.not()?.let {
            super.onBackPressed()
        }
    }
}