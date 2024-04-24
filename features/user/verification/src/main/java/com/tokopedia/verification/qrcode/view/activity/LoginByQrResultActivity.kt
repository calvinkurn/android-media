package com.tokopedia.verification.qrcode.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.verification.R
import com.tokopedia.verification.common.IOnBackPressed
import com.tokopedia.verification.common.abstraction.BaseOtpActivity
import com.tokopedia.verification.qrcode.view.fragment.LoginByQrResultFragment

class LoginByQrResultActivity : BaseOtpActivity() {

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return LoginByQrResultFragment.createInstance(bundle)
    }

    override fun onBackPressed() {
        val fragment = this.supportFragmentManager.findFragmentById(R.id.parent_view)
        (fragment as? IOnBackPressed)?.onBackPressed()?.not()?.let {
            super.onBackPressed()
        }
    }
}
