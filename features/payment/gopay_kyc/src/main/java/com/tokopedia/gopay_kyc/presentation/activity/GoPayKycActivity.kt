package com.tokopedia.gopay_kyc.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.gopay_kyc.presentation.fragment.GoPayPlusKycBenefitFragment

class GoPayKycActivity : BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.hasExtra(IS_EXIT_KYC) == true)
            finish()

    }
    override fun getNewFragment() = GoPayPlusKycBenefitFragment.newInstance()
    override fun getScreenName() = null

    companion object {
        const val IS_EXIT_KYC = "exitFlow"
        fun getIntent(context: Context) = Intent(context, GoPayKycActivity::class.java)
    }
}