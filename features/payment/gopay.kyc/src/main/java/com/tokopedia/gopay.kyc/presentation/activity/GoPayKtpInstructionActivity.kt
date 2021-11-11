package com.tokopedia.gopay.kyc.presentation.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.gopay.kyc.R
import com.tokopedia.gopay.kyc.analytics.GoPayKycAnalytics
import com.tokopedia.gopay.kyc.analytics.GoPayKycEvent
import com.tokopedia.gopay.kyc.di.DaggerGoPayKycComponent
import com.tokopedia.gopay.kyc.di.GoPayKycComponent
import com.tokopedia.gopay.kyc.presentation.fragment.GoPayPlusKtpInstructionsFragment
import com.tokopedia.gopay.kyc.presentation.fragment.GoPayPlusSelfieKtpInstructionsFragment
import com.tokopedia.gopay.kyc.presentation.listener.GoPayKycNavigationListener
import kotlinx.android.synthetic.main.activity_gopay_ktp_layout.*
import javax.inject.Inject

class GoPayKtpInstructionActivity : BaseSimpleActivity(),
    HasComponent<GoPayKycComponent>,
    GoPayKycNavigationListener {

    @Inject
    lateinit var goPayKycAnalytics: dagger.Lazy<GoPayKycAnalytics>

    private val kycComponent: GoPayKycComponent by lazy { initInjector() }
    private var shouldOpenSelfieKtpScreen = false
    private var shouldOpenReviewScreen = false
    private var ktpPath = ""
    private var selfieKtpPath = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        kycComponent.inject(this)
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setupOldToolbar()
    }

    override fun getLayoutRes() = R.layout.activity_gopay_ktp_layout
    override fun getParentViewResourceID(): Int = R.id.kycFrameLayout

    override fun getNewFragment() =
        when {
            shouldOpenSelfieKtpScreen -> GoPayPlusSelfieKtpInstructionsFragment.newInstance()
            else -> GoPayPlusKtpInstructionsFragment.newInstance()
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            data?.let {
                if (requestCode == REQUEST_KTP_ACTIVITY && it.hasExtra(GoPayCameraKtpActivity.KTP_IMAGE_PATH)) {
                    ktpPath = it.getStringExtra(GoPayCameraKtpActivity.KTP_IMAGE_PATH) ?: ""
                    shouldOpenSelfieKtpScreen = true
                } else if (requestCode == REQUEST_KTP_SELFIE_ACTIVITY && it.hasExtra(GoPayCameraKtpActivity.SELFIE_KTP_IMAGE_PATH)) {
                    selfieKtpPath = it.getStringExtra(GoPayCameraKtpActivity.SELFIE_KTP_IMAGE_PATH) ?: ""
                    shouldOpenReviewScreen = true
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onPostResume() {
        super.onPostResume()
        when {
            shouldOpenSelfieKtpScreen -> openSelfieKtpInstructionScreen()
            shouldOpenReviewScreen -> openKycReviewPage()
        }
    }

    override fun openKtpCameraScreen() {
        startActivityForResult(
            GoPayCameraKtpActivity.getIntent(this, false),
            REQUEST_KTP_ACTIVITY
        )
    }

    override fun openSelfieKtpCameraScreen() {
        startActivityForResult(
            GoPayCameraKtpActivity.getIntent(this, true),
            REQUEST_KTP_SELFIE_ACTIVITY
        )
    }

    private fun openSelfieKtpInstructionScreen() {
        ktpHeader.subtitle = SUBTITLE_STEP_2
        inflateFragment()
        shouldOpenSelfieKtpScreen = false
    }

    private fun openKycReviewPage() {
        startActivity(GoPayReviewResultActivity.getIntent(this, ktpPath, selfieKtpPath))
        shouldOpenReviewScreen = false
    }

    override fun exitKycFlow() {
        val intent = RouteManager.getIntent(this, ApplinkConst.GOPAY_KYC)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(GoPayKycActivity.IS_EXIT_KYC, true)
        startActivity(intent)
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

    override fun getScreenName() = null
    override fun getComponent() = kycComponent
    override fun sendAnalytics(event: GoPayKycEvent) = goPayKycAnalytics.get().sentKycEvent(event)

    private fun initInjector() =
        DaggerGoPayKycComponent.builder()
            .baseAppComponent(
                (applicationContext as BaseMainApplication)
                    .baseAppComponent
            ).build()

    companion object {
        const val UPGRADE_GOPAY_TITLE = "Upgrade GoPay Plus"
        const val SUBTITLE_STEP_1 = "Langkah 1 dari 2"
        const val SUBTITLE_STEP_2 = "Langkah 2 dari 2"
        const val REQUEST_KTP_ACTIVITY = 1000
        const val REQUEST_KTP_SELFIE_ACTIVITY = 1001

        fun getIntent(context: Context): Intent {
            return Intent(context, GoPayKtpInstructionActivity::class.java)
        }
    }
}