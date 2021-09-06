package com.tokopedia.gopay_kyc.presentation.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.gopay_kyc.di.GoPayKycComponent
import com.tokopedia.gopay_kyc.presentation.fragment.GoPayPlusKycBenefitFragment

class GoPayKycActivity : BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
    }
    override fun getNewFragment() = GoPayPlusKycBenefitFragment.newInstance()
    override fun getScreenName() = null

}