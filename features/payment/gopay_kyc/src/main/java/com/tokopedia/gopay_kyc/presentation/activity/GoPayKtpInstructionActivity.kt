package com.tokopedia.gopay_kyc.presentation.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.gopay_kyc.di.GoPayKycComponent

class GoPayKtpInstructionActivity : BaseSimpleActivity(), HasComponent<GoPayKycComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
    }

    override fun getLayoutRes() = R.layout.activity_gopay_ktp_layout

    override fun getParentViewResourceID(): Int = R.id.kycInstructionFrameLayout

    override fun getNewFragment() = null
    override fun getScreenName() = null

    override fun getComponent() = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setupOldToolbar(title: String) {
        upgradeGoPayHeader.isShowBackButton = true
        toolbar = upgradeGoPayHeader
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(true)
        }
        updateHeaderTitle(UPGRADE_GOPAY_TITLE)
    }

    companion object {
        const val UPGRADE_GOPAY_TITLE = "Upgrade GoPay Plus"
    }

}