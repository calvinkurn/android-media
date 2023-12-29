package com.tokopedia.kyc_centralized.ui.tokoKyc.form.stepper

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.style.BulletSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.tokopedia.abstraction.base.view.activity.BaseStepperActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.listener.StepperListener
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.KYC_TYPE_KTP_WITH_SELFIE
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.PARAM_KYC_TYPE
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.PARAM_PROJECT_ID
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.kyc_centralized.databinding.FragmentUserIdentificationFormBinding
import com.tokopedia.kyc_centralized.ui.tokoKyc.camera.UserIdentificationCameraActivity.Companion.createIntent
import com.tokopedia.kyc_centralized.ui.tokoKyc.camera.UserIdentificationCameraFragment
import com.tokopedia.kyc_centralized.ui.tokoKyc.form.UserIdentificationFormActivity
import com.tokopedia.kyc_centralized.ui.tokoKyc.info.UserIdentificationInfoFragment
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.lifecycle.autoClearedNullable

/**
 * @author by alvinatin on 12/11/18.
 */
abstract class BaseUserIdentificationStepperFragment<T : UserIdentificationStepperModel> :
    BaseDaggerFragment() {

    protected var viewBinding by autoClearedNullable<FragmentUserIdentificationFormBinding>()
    protected var projectId = 0
    protected var stepperModel: T? = null

    private var kycType = ""
    private var stepperListener: StepperListener? = null
    private var allowedSelfie = false

    abstract var remoteConfig: RemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (context is StepperListener) {
            stepperListener = context as StepperListener
        }

        if (arguments != null && savedInstanceState == null) {
            stepperModel = arguments?.getParcelable(BaseStepperActivity.STEPPER_MODEL_EXTRA)
            kycType = arguments?.getString(PARAM_KYC_TYPE).orEmpty()
        } else if (savedInstanceState != null) {
            stepperModel = savedInstanceState.getParcelable(EXTRA_KYC_STEPPER_MODEL)
        }

        if (activity != null) {
            projectId =
                activity?.intent?.getIntExtra(PARAM_PROJECT_ID, -1)
                    ?: -1
            allowedSelfie = activity?.intent?.getBooleanExtra(
                UserIdentificationInfoFragment.ALLOW_SELFIE_FLOW_EXTRA,
                false
            ) ?: false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(EXTRA_KYC_STEPPER_MODEL, stepperModel)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentUserIdentificationFormBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        encryptImage()
        setContentView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == KYCConstant.REQUEST_CODE_CAMERA_FACE) {
                handleFaceImage(data)
            } else if (requestCode == KYCConstant.REQUEST_CODE_CAMERA_KTP) {
                handleKtpImage(data)
            }
        } else if (resultCode == KYCConstant.IS_FILE_IMAGE_TOO_BIG) {
            NetworkErrorHelper.showRedSnackbar(
                activity,
                context?.resources?.getString(R.string.error_text_image_file_too_big).orEmpty()
            )
        } else if (resultCode == KYCConstant.IS_FILE_IMAGE_NOT_EXIST) {
            NetworkErrorHelper.showRedSnackbar(
                activity,
                context?.resources?.getString(R.string.error_text_image_cant_be_accessed).orEmpty()
            )
        } else if (resultCode == KYCConstant.IS_FILE_LIVENESS_IMAGE_NOT_EXIST) {
            NetworkErrorHelper.showRedSnackbar(
                activity,
                context?.resources?.getString(R.string.error_text_liveness_image_cant_be_accessed)
                    .orEmpty()
            )
        } else if (resultCode == KYCConstant.NOT_SUPPORT_LIVENESS && requestCode == KYCConstant.REQUEST_CODE_CAMERA_FACE) {
            handleDeviceIsNotSupported(projectId)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleDeviceIsNotSupported(projectId: Int) {
        if (projectId == GOCICIL_PROJECT_ID) {
            NetworkErrorHelper.showRedSnackbar(
                activity,
                context?.resources?.getString(R.string.error_liveness_is_not_supported)
                    .orEmpty())
        } else {
            UserIdentificationFormActivity.isSupportedLiveness = false
            val intent = createIntent(
                requireContext(),
                UserIdentificationCameraFragment.PARAM_VIEW_MODE_FACE,
                projectId
            )
            startActivityForResult(intent, KYCConstant.REQUEST_CODE_CAMERA_FACE)
        }
    }

    private fun handleFaceImage(data: Intent) {
        var faceFile = ""
        if (isKycSelfie) {
            stepperModel?.isLiveness = false
            faceFile = data.getStringExtra(KYCConstant.EXTRA_STRING_IMAGE_RESULT) ?: ""
        } else {
            stepperModel?.isLiveness = true
            faceFile = data.getStringExtra(ApplinkConstInternalGlobal.PARAM_FACE_PATH) ?: ""
        }
        stepperModel?.faceFile = faceFile.toEmptyStringIfNull()
        stepperListener?.goToNextPage(stepperModel)
    }

    private fun handleKtpImage(data: Intent) {
        val ktpFile = data.getStringExtra(KYCConstant.EXTRA_STRING_IMAGE_RESULT)
        stepperModel?.ktpFile = ktpFile.toEmptyStringIfNull()
        stepperListener?.goToNextPage(stepperModel)
    }

    protected val isKycSelfie: Boolean
        get() {
            try {
                if (allowedSelfie || kycType == KYC_TYPE_KTP_WITH_SELFIE) return true
                if (UserIdentificationFormActivity.isSupportedLiveness) {
                    return remoteConfig.getBoolean(RemoteConfigKey.KYC_USING_SELFIE)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return true
        }

    protected fun addText(text: String): Typography? {
        return context?.let {
            Typography(it).apply {
                this.setType(Typography.BODY_2)
                this.text = text
                this.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }
        }
    }

    protected fun addTextWithBullet(text: String): Typography? {
        return context?.let {
            Typography(it).apply {
                val radius = DP_4.dpToPx(resources.displayMetrics)
                val gapWidth = DP_12.dpToPx(resources.displayMetrics)
                val margin = DP_8.dpToPx(resources.displayMetrics)
                val span = SpannableString(text)
                val color =
                    MethodChecker.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_NN200)

                val bulletSpan: BulletSpan = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    BulletSpan(gapWidth, color, radius)
                } else {
                    BulletSpan(gapWidth, color)
                }

                span.setSpan(bulletSpan, 0, text.length, 0)

                setMargins(this, 0, 0, 0, margin)
                this.setType(Typography.BODY_2)
                this.text = span
                this.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }
        }
    }

    private fun setMargins(view: View, left: Int, top: Int, right: Int, bottom: Int) {
        if (view.layoutParams is ViewGroup.MarginLayoutParams) {
            val p = view.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(left, top, right, bottom)
            view.requestLayout()
        }
    }

    abstract override fun initInjector()
    protected abstract fun setContentView()
    protected abstract fun encryptImage()

    companion object {
        private const val DP_4 = 4
        private const val DP_8 = 8
        private const val DP_12 = 12

        private val GOCICIL_PROJECT_ID = 21

        const val EXTRA_KYC_STEPPER_MODEL = "kyc_stepper_model"
    }
}
