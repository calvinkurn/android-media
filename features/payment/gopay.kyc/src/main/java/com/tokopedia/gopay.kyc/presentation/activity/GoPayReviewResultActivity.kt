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
import com.tokopedia.gopay.kyc.presentation.bottomsheet.GoPayKycUploadFailedBottomSheet
import com.tokopedia.gopay.kyc.presentation.fragment.GoPayReviewAndUploadFragment
import com.tokopedia.gopay.kyc.presentation.fragment.GoPayUploadSuccessFragment
import com.tokopedia.gopay.kyc.presentation.listener.GoPayKycNavigationListener
import com.tokopedia.gopay.kyc.presentation.listener.GoPayKycReviewResultListener
import com.tokopedia.kotlin.extensions.view.gone
import kotlinx.android.synthetic.main.activity_gopay_ktp_layout.*
import javax.inject.Inject

class GoPayReviewResultActivity : BaseSimpleActivity(), HasComponent<GoPayKycComponent>,
    GoPayKycReviewResultListener, GoPayKycNavigationListener {

    @Inject
    lateinit var goPayKycAnalytics: dagger.Lazy<GoPayKycAnalytics>
    private var shouldOpenSuccessScreen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        kycComponent.inject(this)
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setupOldToolbar()
    }

    override fun getLayoutRes() = R.layout.activity_gopay_ktp_layout
    override fun getParentViewResourceID(): Int = R.id.kycFrameLayout

    override fun getNewFragment() = when {
            shouldOpenSuccessScreen -> {
                shouldOpenSuccessScreen = false
                GoPayUploadSuccessFragment.newInstance()
            }
            else -> GoPayReviewAndUploadFragment.newInstance(intent.extras)

        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            data?.let {
                if (requestCode == REQUEST_KTP_ACTIVITY && it.hasExtra(GoPayCameraKtpActivity.KTP_IMAGE_PATH)) {
                    val ktpPath = it.getStringExtra(GoPayCameraKtpActivity.KTP_IMAGE_PATH) ?: ""
                    (fragment as GoPayReviewAndUploadFragment).updateKtpImage(ktpPath)
                } else if (requestCode == REQUEST_KTP_SELFIE_ACTIVITY && it.hasExtra(
                        GoPayCameraKtpActivity.SELFIE_KTP_IMAGE_PATH
                    )
                ) {
                    val selfieKtpPath = it.getStringExtra(GoPayCameraKtpActivity.SELFIE_KTP_IMAGE_PATH) ?: ""
                    (fragment as GoPayReviewAndUploadFragment).updateSelfieKtpImage(selfieKtpPath)
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

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

    private fun initInjector() =
        DaggerGoPayKycComponent.builder()
            .baseAppComponent(
                (applicationContext as BaseMainApplication)
                    .baseAppComponent
            ).build()


    private val kycComponent: GoPayKycComponent by lazy { initInjector() }
    override fun getComponent() = kycComponent
    override fun getScreenName() = null
    override fun sendAnalytics(event: GoPayKycEvent) = goPayKycAnalytics.get().sentKycEvent(event)

    override fun showKycFailedBottomSheet(ktpPath: String, selfieKtpPath: String) =
        GoPayKycUploadFailedBottomSheet.show(ktpPath, selfieKtpPath,supportFragmentManager)

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

    override fun exitKycFlow() {
        val intent = RouteManager.getIntent(this, ApplinkConst.GOPAY_KYC)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(GoPayKycActivity.IS_EXIT_KYC, true)
        startActivity(intent)
    }

    override fun showKycSuccessScreen() {
        shouldOpenSuccessScreen = true
        ktpHeader.title = ""
        ktpHeader.subheaderView?.gone()
        inflateFragment()
    }

    companion object {
        const val REVIEW_TITLE = "Ringkasan Pengajuan"
        const val KTP_PATH = "ktp_path"
        const val SELFIE_KTP_PATH = "selfie_ktp_path"
        const val REQUEST_KTP_ACTIVITY = 1002
        const val REQUEST_KTP_SELFIE_ACTIVITY = 1003

        fun getIntent(context: Context, ktpPath: String, selfieKtpPath: String): Intent {
            val intent = Intent(context, GoPayReviewResultActivity::class.java)
            intent.putExtra(KTP_PATH, ktpPath)
            intent.putExtra(SELFIE_KTP_PATH, selfieKtpPath)
            return intent
        }
    }

}