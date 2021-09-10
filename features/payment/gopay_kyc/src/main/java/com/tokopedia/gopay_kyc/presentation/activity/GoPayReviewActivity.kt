package com.tokopedia.gopay_kyc.presentation.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.gopay_kyc.R
import com.tokopedia.gopay_kyc.presentation.fragment.GoPayReviewAndUploadFragment
import com.tokopedia.gopay_kyc.presentation.fragment.GoPayUploadSuccessFragment
import kotlinx.android.synthetic.main.activity_gopay_ktp_layout.*

class GoPayReviewActivity : BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setupOldToolbar()
    }

    override fun getLayoutRes() = R.layout.activity_gopay_ktp_layout

    override fun getParentViewResourceID(): Int = R.id.kycFrameLayout

    override fun getNewFragment() = GoPayReviewAndUploadFragment.newInstance()
    override fun getScreenName() = null

    private fun setupOldToolbar() {
        ktpHeader.isShowBackButton = true
        toolbar = ktpHeader
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(true)
        }
        ktpHeader.title = REVIEW_TITLE
    }

    fun showSuccessPage() {
        supportFragmentManager.beginTransaction()
            .add(
                R.id.kycFrameLayout,
                GoPayUploadSuccessFragment.newInstance(),
                GoPayUploadSuccessFragment.TAG
            )
            .commit()
    }

    companion object {
        const val REVIEW_TITLE = "Ringkasan Pengajuan"
        fun getIntent(context: Context): Intent {
            return Intent(context, GoPayReviewActivity::class.java)
        }
    }

}