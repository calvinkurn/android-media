package com.tokopedia.kyc_centralized.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.tokopedia.abstraction.base.view.activity.BaseStepperActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.listener.StepperListener
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.view.activity.UserIdentificationCameraActivity.Companion.createIntent
import com.tokopedia.kyc_centralized.view.activity.UserIdentificationFormActivity
import com.tokopedia.kyc_centralized.view.model.UserIdentificationStepperModel
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.user_identification_common.KYCConstant
import com.tokopedia.user_identification_common.analytics.UserIdentificationCommonAnalytics

/**
 * @author by alvinatin on 12/11/18.
 */
abstract class BaseUserIdentificationStepperFragment<T : UserIdentificationStepperModel> : BaseDaggerFragment() {
    protected var onboardingImage: LottieAnimationView? = null
    protected var title: TextView? = null
    protected var subtitle: TextView? = null
    protected var button: UnifyButton? = null
    protected var correctImage: ImageView? = null
    protected var wrongImage: ImageView? = null
    protected var analytics: UserIdentificationCommonAnalytics? = null
    protected var projectId = 0
    protected var stepperModel: T? = null
    private var stepperListener: StepperListener? = null

    private lateinit var remoteConfig: RemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (context is StepperListener) {
            stepperListener = context as StepperListener
        }
        if (arguments != null && savedInstanceState == null) {
            stepperModel = arguments?.getParcelable(BaseStepperActivity.STEPPER_MODEL_EXTRA)
        } else if (savedInstanceState != null) {
            stepperModel = savedInstanceState.getParcelable(EXTRA_KYC_STEPPER_MODEL)
        }
        if (activity != null) {
            projectId = activity?.intent?.getIntExtra(ApplinkConstInternalGlobal.PARAM_PROJECT_ID, -1)?: -1
            analytics = UserIdentificationCommonAnalytics.createInstance(projectId)
        }

        remoteConfig = FirebaseRemoteConfigImpl(context)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(EXTRA_KYC_STEPPER_MODEL, stepperModel)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_user_identification_form, container, false)
        initView(view)
        encryptImage()
        setContentView()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    protected open fun initView(view: View) {
        onboardingImage = view.findViewById(R.id.form_onboarding_image)
        title = view.findViewById(R.id.title)
        subtitle = view.findViewById(R.id.subtitle)
        button = view.findViewById(R.id.button)
        correctImage = view.findViewById(R.id.image_selfie_correct)
        wrongImage = view.findViewById(R.id.image_selfie_wrong)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == KYCConstant.REQUEST_CODE_CAMERA_FACE) {
                handleFaceImage(data)
            } else if (requestCode == KYCConstant.REQUEST_CODE_CAMERA_KTP) {
                handleKtpImage(data)
            }
        } else if (resultCode == KYCConstant.IS_FILE_IMAGE_TOO_BIG) {
            sendAnalyticErrorImageTooLarge(requestCode)
            NetworkErrorHelper.showRedSnackbar(activity, resources.getString(R.string.error_text_image_file_too_big))
        } else if (resultCode == KYCConstant.IS_FILE_IMAGE_NOT_EXIST) {
            NetworkErrorHelper.showRedSnackbar(activity, resources.getString(R.string.error_text_image_cant_be_accessed))
        } else if (resultCode == KYCConstant.IS_FILE_LIVENESS_IMAGE_NOT_EXIST) {
            NetworkErrorHelper.showRedSnackbar(activity, resources.getString(R.string.error_text_liveness_image_cant_be_accessed))
        } else if (resultCode == KYCConstant.NOT_SUPPORT_LIVENESS && requestCode == KYCConstant.REQUEST_CODE_CAMERA_FACE) {
            UserIdentificationFormActivity.isSupportedLiveness = false
            val intent = createIntent(context, UserIdentificationCameraFragment.PARAM_VIEW_MODE_FACE)
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_PROJECT_ID, projectId)
            startActivityForResult(intent, KYCConstant.REQUEST_CODE_CAMERA_FACE)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleFaceImage(data: Intent) {
        var faceFile = ""
        if(isKycSelfie) {
            stepperModel?.isLiveness = false
            faceFile = data.getStringExtra(KYCConstant.EXTRA_STRING_IMAGE_RESULT)?: ""
        } else {
            stepperModel?.isLiveness = true
            faceFile = data.getStringExtra(ApplinkConstInternalGlobal.PARAM_FACE_PATH)?: ""
        }
        stepperModel?.faceFile = faceFile.toEmptyStringIfNull()
        stepperListener?.goToNextPage(stepperModel)
    }

    private fun handleKtpImage(data: Intent) {
        val ktpFile = data.getStringExtra(KYCConstant.EXTRA_STRING_IMAGE_RESULT)
        stepperModel?.ktpFile = ktpFile.toEmptyStringIfNull()
        stepperListener?.goToNextPage(stepperModel)
    }

    private fun sendAnalyticErrorImageTooLarge(requestCode: Int) {
        when (requestCode) {
            KYCConstant.REQUEST_CODE_CAMERA_KTP -> analytics?.eventViewErrorImageTooLargeKtpPage()
            KYCConstant.REQUEST_CODE_CAMERA_FACE -> analytics?.eventViewErrorImageTooLargeSelfiePage()
            else -> {}
        }
    }

    protected val isKycSelfie: Boolean
        get() {
            try {
                if (UserIdentificationFormActivity.isSupportedLiveness) {
                    return remoteConfig.getBoolean(RemoteConfigKey.KYC_USING_SELFIE)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return true
        }

    abstract override fun initInjector()
    protected abstract fun setContentView()
    protected abstract fun encryptImage()

    companion object {
        const val EXTRA_KYC_STEPPER_MODEL = "kyc_stepper_model"
    }
}