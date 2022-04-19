package com.tokopedia.referral.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.referral.R
import com.tokopedia.referral.view.fragment.ReferralPhoneNumberVerificationFragment

/**
 * * For navigate: use [ApplinkConstInternalGlobal.SETTING_REFERRAL_PHONE_VERIFICATION]
 */

class ReferralPhoneNumberVerificationActivity : BaseSimpleActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_referral_phone_number_verification
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    private fun initView() {
        addFragment(R.id.container, ReferralPhoneNumberVerificationFragment.newInstance())
    }

    private fun addFragment(containerViewId: Int, fragment: Fragment) {
        val fragmentTransaction = this.supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(containerViewId, fragment)
        fragmentTransaction.commit()
    }
}