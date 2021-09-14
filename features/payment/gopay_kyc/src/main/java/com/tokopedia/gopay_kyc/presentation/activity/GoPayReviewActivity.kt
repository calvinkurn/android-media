package com.tokopedia.gopay_kyc.presentation.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.gopay_kyc.R
import com.tokopedia.gopay_kyc.di.DaggerGoPayKycComponent
import com.tokopedia.gopay_kyc.di.GoPayKycComponent
import com.tokopedia.gopay_kyc.presentation.bottomsheet.GoPayKycUploadFailedBottomSheet
import com.tokopedia.gopay_kyc.presentation.fragment.GoPayReviewAndUploadFragment
import com.tokopedia.gopay_kyc.presentation.fragment.GoPayUploadSuccessFragment
import com.tokopedia.gopay_kyc.presentation.listener.GoPayKycOpenCameraListener
import com.tokopedia.gopay_kyc.presentation.listener.GoPayKycReviewListener
import com.tokopedia.gopay_kyc.viewmodel.GoPayKycImageUploadViewModel
import com.tokopedia.kotlin.extensions.view.gone
import kotlinx.android.synthetic.main.activity_gopay_ktp_layout.*
import javax.inject.Inject

class GoPayReviewActivity : BaseSimpleActivity(), HasComponent<GoPayKycComponent>,
    GoPayKycReviewListener, GoPayKycOpenCameraListener {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private var shouldOpenSuccessScreen = false

    private val viewModel: GoPayKycImageUploadViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory.get())
        viewModelProvider.get(GoPayKycImageUploadViewModel::class.java)
    }

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
            shouldOpenSuccessScreen -> {
                shouldOpenSuccessScreen = false
                GoPayUploadSuccessFragment.newInstance()
            }
            else -> {
                viewModel.ktpPath = intent.getStringExtra(KTP_PATH) ?: ""
                viewModel.selfieKtpPath = intent.getStringExtra(SELFIE_KTP_PATH) ?: ""
                GoPayReviewAndUploadFragment.newInstance(intent.extras)
            }
        }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            data?.let {
                if (requestCode == REQUEST_KTP_ACTIVITY && it.hasExtra(GoPayCameraKtpActivity.KTP_IMAGE_PATH)) {
                    viewModel.ktpPath =
                        it.getStringExtra(GoPayCameraKtpActivity.KTP_IMAGE_PATH) ?: ""
                    (fragment as GoPayReviewAndUploadFragment).updateKtpImage(viewModel.ktpPath)
                } else if (requestCode == REQUEST_KTP_SELFIE_ACTIVITY && it.hasExtra(
                        GoPayCameraKtpActivity.SELFIE_KTP_IMAGE_PATH
                    )
                ) {
                    viewModel.selfieKtpPath =
                        it.getStringExtra(GoPayCameraKtpActivity.SELFIE_KTP_IMAGE_PATH) ?: ""
                    (fragment as GoPayReviewAndUploadFragment).updateSelfieKtpImage(viewModel.selfieKtpPath)
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


    override fun showKycFailedBottomSheet() = GoPayKycUploadFailedBottomSheet
        .show(viewModel.ktpPath, viewModel.selfieKtpPath,supportFragmentManager)

    override fun uploadImageToServer() = viewModel.uploadImage()

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
            val intent = Intent(context, GoPayReviewActivity::class.java)
            intent.putExtra(KTP_PATH, ktpPath)
            intent.putExtra(SELFIE_KTP_PATH, selfieKtpPath)
            return intent
        }
    }

}