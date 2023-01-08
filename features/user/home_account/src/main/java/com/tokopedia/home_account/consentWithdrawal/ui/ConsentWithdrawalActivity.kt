package com.tokopedia.home_account.consentWithdrawal.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.kotlin.extensions.view.toIntOrZero

class ConsentWithdrawalActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        return intent.data?.getQueryParameter(ApplinkConstInternalUserPlatform.GROUP_ID)?.let {
            ConsentWithdrawalFragment.createInstance(it.toIntOrZero())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTitle("Consent Withdrawal")
    }
}
