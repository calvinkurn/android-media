package com.tokopedia.otp.notif.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.otp.R
import com.tokopedia.otp.common.IOnBackPressed
import com.tokopedia.otp.common.abstraction.BaseOtpActivity
import com.tokopedia.otp.notif.view.fragment.ResultNotifFragment

/**
 * Created by Ade Fulki on 14/09/20.
 */

class ResultNotifActivity : BaseOtpActivity() {

    override fun getNewFragment(): Fragment? {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return ResultNotifFragment.createInstance(bundle)
    }

    override fun onBackPressed() {
        val fragment = this.supportFragmentManager.findFragmentById(R.id.parent_view)
        (fragment as? IOnBackPressed)?.onBackPressed()?.not()?.let {
            super.onBackPressed()
        }
    }
}