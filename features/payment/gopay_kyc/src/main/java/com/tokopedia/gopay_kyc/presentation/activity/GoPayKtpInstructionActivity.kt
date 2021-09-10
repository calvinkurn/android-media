package com.tokopedia.gopay_kyc.presentation.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.gopay_kyc.R
import com.tokopedia.gopay_kyc.presentation.bottomsheet.GoPayKycUploadFailedBottomSheet
import com.tokopedia.gopay_kyc.presentation.fragment.GoPayPlusKtpInstructionsFragment
import com.tokopedia.gopay_kyc.presentation.fragment.GoPayPlusSelfieKtpInstructionsFragment
import com.tokopedia.gopay_kyc.presentation.fragment.GoPayReviewAndUploadFragment
import com.tokopedia.gopay_kyc.presentation.fragment.GoPayUploadSuccessFragment
import com.tokopedia.gopay_kyc.presentation.listener.GoPayKycFlowListener
import com.tokopedia.kotlin.extensions.view.gone
import kotlinx.android.synthetic.main.activity_gopay_ktp_layout.*

class GoPayKtpInstructionActivity : BaseSimpleActivity(), GoPayKycFlowListener {

    private var shouldOpenSelfieKtpScreen = false
    private var shouldOpenReviewScreen = false
    private var shouldOpenSuccessScreen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setupOldToolbar()
    }

    override fun getLayoutRes() = R.layout.activity_gopay_ktp_layout
    override fun getParentViewResourceID(): Int = R.id.kycFrameLayout

    override fun getNewFragment() =
        when {
            shouldOpenSelfieKtpScreen -> GoPayPlusSelfieKtpInstructionsFragment.newInstance()
            shouldOpenReviewScreen -> GoPayReviewAndUploadFragment.newInstance()
            shouldOpenSuccessScreen -> GoPayUploadSuccessFragment.newInstance()
            else -> GoPayPlusKtpInstructionsFragment.newInstance()
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            data?.let {
                if (it.hasExtra(GoPayCameraKtpActivity.KTP_IMAGE_PATH)) {
                    shouldOpenSelfieKtpScreen = true
                } else if (it.hasExtra(GoPayCameraKtpActivity.SELFIE_KTP_IMAGE_PATH)) {
                    shouldOpenReviewScreen = true
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onPostResume() {
        super.onPostResume()
        when {
            shouldOpenSelfieKtpScreen -> openSelfieKtpCameraScreen()
            shouldOpenReviewScreen -> openKycReviewPage()
            shouldOpenSuccessScreen -> openKycSuccessScreen()
        }
    }

    private fun openKycSuccessScreen() {
        ktpHeader.title = ""
        ktpHeader.subtitle = ""
        ktpHeader.subheaderView?.gone()
        inflateFragment()
        shouldOpenSuccessScreen = false
    }

    private fun openSelfieKtpCameraScreen() {
        ktpHeader.subtitle = SUBTITLE_STEP_2
        inflateFragment()
        shouldOpenSelfieKtpScreen = false
    }

    private fun openKycReviewPage() {
        ktpHeader.title = REVIEW_TITLE
        ktpHeader.subheaderView?.gone()
        inflateFragment()
        shouldOpenReviewScreen = false
    }

    private fun setupOldToolbar() {
        ktpHeader.isShowBackButton = true
        toolbar = ktpHeader
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(true)
        }
        ktpHeader.title = UPGRADE_GOPAY_TITLE
        ktpHeader.subtitle = SUBTITLE_STEP_1
    }

    companion object {
        const val UPGRADE_GOPAY_TITLE = "Upgrade GoPay Plus"
        const val REVIEW_TITLE = "Ringkasan Pengajuan"
        const val SUBTITLE_STEP_1 = "Langkah 1 dari 2"
        const val SUBTITLE_STEP_2 = "Langkah 2 dari 2"
        fun getIntent(context: Context): Intent {
            return Intent(context, GoPayKtpInstructionActivity::class.java)
        }
    }

    override fun showKycSuccessScreen() {
        shouldOpenSuccessScreen = true
        openKycSuccessScreen()
    }

    override fun showKycFailedBottomSheet() {
        GoPayKycUploadFailedBottomSheet.show(Bundle(), supportFragmentManager)
    }

    override fun getScreenName() = null

    override fun exitKycFlow() {
        val intent = GoPayKycActivity.getIntent(this)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(GoPayKycActivity.IS_EXIT_KYC, true)
        startActivity(intent)
    }
}