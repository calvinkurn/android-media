package com.tokopedia.gopay_kyc.presentation.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.gopay_kyc.R
import com.tokopedia.gopay_kyc.di.GoPayKycComponent
import com.tokopedia.gopay_kyc.presentation.fragment.GoPayPlusKtpInstructionsFragment
import kotlinx.android.synthetic.main.activity_gopay_ktp_layout.*

class GoPayKtpInstructionActivity : BaseSimpleActivity(), HasComponent<GoPayKycComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setupOldToolbar()
    }

    override fun getLayoutRes() = R.layout.activity_gopay_ktp_layout

    override fun getParentViewResourceID(): Int = R.id.kycInstructionFrameLayout

    override fun getNewFragment() = GoPayPlusKtpInstructionsFragment.newInstance()
    override fun getScreenName() = null

    override fun getComponent() = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setupOldToolbar() {
        upgradeGoPayHeader.isShowBackButton = true
        toolbar = upgradeGoPayHeader
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(true)
        }
        upgradeGoPayHeader.title = UPGRADE_GOPAY_TITLE
        upgradeGoPayHeader.subtitle = SUBTITLE_STEP_1
    }

    companion object {
        const val UPGRADE_GOPAY_TITLE = "Upgrade GoPay Plus"
        const val SUBTITLE_STEP_1 = "Langkah 1 dari 2"
        fun getIntent(context: Context): Intent {
            return Intent(context, GoPayKtpInstructionActivity::class.java)
        }
    }

}