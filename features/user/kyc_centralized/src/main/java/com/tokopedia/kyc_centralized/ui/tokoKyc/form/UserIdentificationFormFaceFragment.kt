package com.tokopedia.kyc_centralized.ui.tokoKyc.form

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.PARAM_KYC_TYPE
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.kyc_centralized.common.KycUrl
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.kyc_centralized.common.KYCConstant.PADDING_0_5F
import com.tokopedia.kyc_centralized.common.KYCConstant.PADDING_16
import com.tokopedia.kyc_centralized.common.KYCConstant.PADDING_ZERO
import com.tokopedia.kyc_centralized.di.UserIdentificationCommonComponent
import com.tokopedia.kyc_centralized.util.ImageEncryptionUtil
import com.tokopedia.kyc_centralized.ui.tokoKyc.camera.UserIdentificationCameraActivity.Companion.createIntent
import com.tokopedia.kyc_centralized.ui.tokoKyc.camera.UserIdentificationCameraFragment
import com.tokopedia.kyc_centralized.ui.tokoKyc.form.stepper.UserIdentificationStepperModel
import com.tokopedia.kyc_centralized.ui.tokoKyc.form.KycUploadViewModel.Companion.KYC_IV_KTP_CACHE
import com.tokopedia.kyc_centralized.ui.tokoKyc.form.stepper.BaseUserIdentificationStepperFragment
import com.tokopedia.media.loader.loadImage
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.file.FileUtil
import javax.inject.Inject

/**
 * @author by alvinatin on 09/11/18.
 */
class UserIdentificationFormFaceFragment :
    BaseUserIdentificationStepperFragment<UserIdentificationStepperModel>(),
    UserIdentificationFormActivity.Listener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val kycUploadViewModel by lazy { viewModelFragmentProvider.get(KycUploadViewModel::class.java) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        analytics?.eventViewSelfiePage(isKycSelfie)
        initObserver()
    }

    private fun initObserver() {
        kycUploadViewModel.encryptImageLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    viewBinding?.button?.isEnabled = true
                }
                is Fail -> {
                    ErrorHandler.getErrorMessage(
                        activity,
                        it.throwable,
                        ErrorHandler.Builder().apply {
                            className = UserIdentificationFormFaceFragment::class.java.name
                        }.build()
                    )
                    NetworkErrorHelper.showRedSnackbar(
                        activity,
                        context?.resources?.getString(R.string.error_text_image_fail_to_encrypt)
                            .orEmpty()
                    )
                }
            }
        }
    }

    override fun getScreenName(): String = ""

    override fun encryptImage() {
        context?.let {
            if (ImageEncryptionUtil.isUsingEncrypt(it)) {
                viewBinding?.button?.isEnabled = false
                kycUploadViewModel.encryptImage(
                    stepperModel?.ktpFile.toEmptyStringIfNull(),
                    KYC_IV_KTP_CACHE
                )
            }
        }
    }

    override fun setContentView() {
        if (isKycSelfie) {
            setKycSelfieViews()
        } else {
            setLivenessViews()
        }
    }

    private fun setKycSelfieViews() {
        viewBinding?.title?.setText(R.string.face_title_kyc)
        viewBinding?.subtitle?.setText(R.string.face_subtitle_kyc)
        viewBinding?.button?.setText(R.string.face_button_kyc)
        viewBinding?.button?.setOnClickListener { v: View? ->
            analytics?.eventClickNextSelfiePage(false)
            goToKycSelfie()
        }
        setExampleImages()
        viewBinding?.securityLayout?.hide()
        if (activity is UserIdentificationFormActivity) {
            (activity as UserIdentificationFormActivity)
                .updateToolbarTitle(getString(R.string.title_kyc_form_selfie))
        }
    }

    private fun setLivenessViews() {
        viewBinding?.title?.setText(R.string.face_title)
        viewBinding?.subtitle?.setText(R.string.face_subtitle)
        viewBinding?.layoutInfoBullet?.apply {
            addView(addTextWithBullet(getString(R.string.face_subtitle_body_1)))
            addView(addTextWithBullet(getString(R.string.face_subtitle_body_2)))
        }?.show()
        viewBinding?.button?.setText(R.string.face_button)
        viewBinding?.button?.setOnClickListener { v: View? ->
            analytics?.eventClickNextSelfiePage(true)
            goToKycLiveness()
        }
        viewBinding?.securityLayout?.show()

        setLottieAnimation()
    }

    private fun setLottieAnimation() {
        val lottieCompositionLottieTask =
            LottieCompositionFactory.fromUrl(requireContext(), KycUrl.SCAN_FACE)
        lottieCompositionLottieTask.addListener { result: LottieComposition? ->
            result?.let { viewBinding?.formOnboardingImage?.setComposition(it) }
            viewBinding?.formOnboardingImage?.repeatCount = ValueAnimator.INFINITE
            viewBinding?.formOnboardingImage?.playAnimation()
        }
    }

    private fun setExampleImages() {
        viewBinding?.formOnboardingImage?.apply {
            val scale = resources.displayMetrics.density
            setPadding(
                PADDING_ZERO,
                (PADDING_16 * scale + PADDING_0_5F).toInt(),
                PADDING_ZERO,
                PADDING_ZERO
            )
            loadImage(KycUrl.SCAN_SELFIE)
            show()
        }
    }

    private fun goToKycSelfie() {
        val intent = context?.let {
            createIntent(
                it,
                UserIdentificationCameraFragment.PARAM_VIEW_MODE_FACE,
                projectId
            )
        }
        startActivityForResult(intent, KYCConstant.REQUEST_CODE_CAMERA_FACE)
    }

    private fun goToKycLiveness() {
        val intent = RouteManager.getIntent(
            context,
            ApplinkConstInternalUserPlatform.KYC_LIVENESS,
            projectId.toString()
        )
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_KTP_PATH, stepperModel?.ktpFile)
        startActivityForResult(intent, KYCConstant.REQUEST_CODE_CAMERA_FACE)
    }

    override fun trackOnBackPressed() {
        FileUtil.deleteFile(stepperModel?.ktpFile)
        analytics?.eventClickBackSelfiePage(isKycSelfie)
    }

    companion object {
        fun createInstance(kycType: String): Fragment {
            return UserIdentificationFormFaceFragment().apply {
                arguments = Bundle().apply {
                    putString(PARAM_KYC_TYPE, kycType)
                }
            }
        }
    }

    override fun initInjector() {
        getComponent(UserIdentificationCommonComponent::class.java).inject(this)
    }
}
