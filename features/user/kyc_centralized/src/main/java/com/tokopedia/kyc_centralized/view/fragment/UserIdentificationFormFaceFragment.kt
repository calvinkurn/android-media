package com.tokopedia.kyc_centralized.view.fragment

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.di.DaggerUserIdentificationCommonComponent
import com.tokopedia.kyc_centralized.di.UserIdentificationCommonComponent
import com.tokopedia.kyc_centralized.util.ImageEncryptionUtil
import com.tokopedia.kyc_centralized.view.activity.UserIdentificationCameraActivity.Companion.createIntent
import com.tokopedia.kyc_centralized.view.activity.UserIdentificationFormActivity
import com.tokopedia.kyc_centralized.view.model.UserIdentificationStepperModel
import com.tokopedia.kyc_centralized.view.viewmodel.KycUploadViewModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.kyc_centralized.view.viewmodel.KycUploadViewModel.Companion.KYC_IV_KTP_CACHE
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user_identification_common.KYCConstant
import com.tokopedia.user_identification_common.KycUrl
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
    private val viewModelFragmentProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val kycUploadViewModel by lazy { viewModelFragmentProvider.get(KycUploadViewModel::class.java) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        analytics?.eventViewSelfiePage()
        initObserver()
    }

    private fun initObserver() {
        kycUploadViewModel.encryptImageLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {
                when (it) {
                    is Success -> {
                        button?.isEnabled = true
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
                            resources.getString(R.string.error_text_image_fail_to_encrypt)
                        )
                    }
                }
            })
    }

    override fun getScreenName(): String = ""

    override fun encryptImage() {
        context?.let {
            if (ImageEncryptionUtil.isUsingEncrypt(it)) {
                button?.isEnabled = false
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
        title?.setText(R.string.face_title_kyc)
        subtitle?.setText(R.string.face_subtitle_kyc)
        button?.setText(R.string.face_button_kyc)
        button?.setOnClickListener { v: View? ->
            analytics?.eventClickNextSelfiePage()
            goToKycSelfie()
        }
        setExampleImages()
        onboardingImage?.visibility = View.GONE
        if (activity is UserIdentificationFormActivity) {
            (activity as UserIdentificationFormActivity)
                .updateToolbarTitle(getString(R.string.title_kyc_form_selfie))
        }
    }

    private fun setLivenessViews() {
        title?.setText(R.string.face_title)
        subtitle?.setText(R.string.face_subtitle)
        bulletTextLayout?.apply {
            addView(addTextWithBullet(getString(R.string.face_subtitle_body_1)))
            addView(addTextWithBullet(getString(R.string.face_subtitle_body_2)))
        }?.show()
        button?.setText(R.string.face_button)
        button?.setOnClickListener { v: View? ->
            analytics?.eventClickNextSelfiePage()
            goToKycLiveness()
        }

        setLottieAnimation()
    }

    private fun setLottieAnimation() {
        val lottieCompositionLottieTask =
            LottieCompositionFactory.fromUrl(requireContext(), KycUrl.SCAN_FACE)
        lottieCompositionLottieTask.addListener { result: LottieComposition? ->
            result?.let { onboardingImage?.setComposition(it) }
            onboardingImage?.repeatCount = ValueAnimator.INFINITE
            onboardingImage?.playAnimation()
        }
    }

    private fun setExampleImages() {
        correctImage?.visibility = View.VISIBLE
        wrongImage?.visibility = View.VISIBLE
        correctImage?.loadImage(KycUrl.SELFIE_OK)
        wrongImage?.loadImage(KycUrl.SELFIE_FAIL)
    }

    private fun goToKycSelfie() {
        val intent = createIntent(context, UserIdentificationCameraFragment.PARAM_VIEW_MODE_FACE)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_PROJECT_ID, projectId)
        startActivityForResult(intent, KYCConstant.REQUEST_CODE_CAMERA_FACE)
    }

    private fun goToKycLiveness() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.LIVENESS_DETECTION, projectId.toString())
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_KTP_PATH, stepperModel?.ktpFile)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_PROJECT_ID, projectId)
        startActivityForResult(intent, KYCConstant.REQUEST_CODE_CAMERA_FACE)
    }

    override fun trackOnBackPressed() {
        FileUtil.deleteFile(stepperModel?.ktpFile)
        analytics?.eventClickBackSelfiePage()
    }

    companion object {
        fun createInstance(): Fragment {
            val fragment: Fragment = UserIdentificationFormFaceFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun initInjector() {
        getComponent(UserIdentificationCommonComponent::class.java).inject(this)
    }
}