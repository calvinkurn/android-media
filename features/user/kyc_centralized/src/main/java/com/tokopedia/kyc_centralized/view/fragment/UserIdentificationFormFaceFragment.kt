package com.tokopedia.kyc_centralized.view.fragment

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.di.DaggerUserIdentificationCommonComponent
import com.tokopedia.kyc_centralized.view.activity.UserIdentificationCameraActivity.Companion.createIntent
import com.tokopedia.kyc_centralized.view.activity.UserIdentificationFormActivity
import com.tokopedia.kyc_centralized.view.viewmodel.UserIdentificationStepperModel
import com.tokopedia.user_identification_common.KYCConstant
import com.tokopedia.user_identification_common.KycUrl

/**
 * @author by alvinatin on 09/11/18.
 */
class UserIdentificationFormFaceFragment : BaseUserIdentificationStepperFragment<UserIdentificationStepperModel>(), UserIdentificationFormActivity.Listener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        analytics?.eventViewSelfiePage()
    }

    override fun getScreenName(): String = ""

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
        button?.setText(R.string.face_button)
        button?.setOnClickListener { v: View? ->
            analytics?.eventClickNextSelfiePage()
            goToKycLiveness()
        }
        setLottieAnimation()
        if (activity is UserIdentificationFormActivity) {
            (activity as UserIdentificationFormActivity)
                    .updateToolbarTitle(getString(R.string.title_kyc_form_face))
        }
    }

    private fun setLottieAnimation() {
        val lottieCompositionLottieTask = LottieCompositionFactory.fromUrl(requireContext(), KycUrl.SCAN_FACE)
        lottieCompositionLottieTask.addListener { result: LottieComposition? ->
            result?.let { onboardingImage?.setComposition(it) }
            onboardingImage?.repeatCount = ValueAnimator.INFINITE
            onboardingImage?.playAnimation()
        }
    }

    private fun setExampleImages() {
        correctImage?.visibility = View.VISIBLE
        wrongImage?.visibility = View.VISIBLE
        ImageHandler.LoadImage(correctImage, KycUrl.SELFIE_OK)
        ImageHandler.LoadImage(wrongImage, KycUrl.SELFIE_FAIL)
    }

    private fun goToKycSelfie() {
        val intent = createIntent(context, UserIdentificationCameraFragment.PARAM_VIEW_MODE_FACE)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_PROJECT_ID, projectId)
        startActivityForResult(intent, KYCConstant.REQUEST_CODE_CAMERA_FACE)
    }

    private fun goToKycLiveness() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.LIVENESS_DETECTION)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_KTP_PATH, stepperModel?.ktpFile)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_PROJECT_ID, projectId)
        startActivityForResult(intent, KYCConstant.REQUEST_CODE_CAMERA_FACE)
    }

    override fun trackOnBackPressed() {
        analytics?.eventClickBackSelfiePage()
    }

    override fun initInjector() {
        if (activity != null) {
            val daggerUserIdentificationComponent = DaggerUserIdentificationCommonComponent.builder()
                    .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                    .build()
            daggerUserIdentificationComponent.inject(this)
        }
    }

    companion object {
        fun createInstance(): Fragment {
            val fragment: Fragment = UserIdentificationFormFaceFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}