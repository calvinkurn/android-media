package com.tokopedia.kyc_centralized.ui.tokoKyc.form

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.activity.BaseStepperActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.listener.StepperListener
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.KYC_TYPE_KTP_WITH_SELFIE
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.PARAM_KYC_TYPE
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.PARAM_PROJECT_ID
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.analytics.UserIdentificationCommonAnalytics
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.kyc_centralized.common.KYCConstant.LIVENESS_TAG
import com.tokopedia.kyc_centralized.common.KycServerLogger
import com.tokopedia.kyc_centralized.common.KycUrl
import com.tokopedia.kyc_centralized.common.KycUrl.SCAN_FACE_FAIL_GENERAL
import com.tokopedia.kyc_centralized.common.KycUrl.SCAN_FACE_FAIL_NETWORK
import com.tokopedia.kyc_centralized.data.model.KycData
import com.tokopedia.kyc_centralized.databinding.FragmentUserIdentificationFinalBinding
import com.tokopedia.kyc_centralized.di.UserIdentificationCommonComponent
import com.tokopedia.kyc_centralized.ui.tokoKyc.camera.UserIdentificationCameraActivity.Companion.createIntent
import com.tokopedia.kyc_centralized.ui.tokoKyc.camera.UserIdentificationCameraFragment
import com.tokopedia.kyc_centralized.ui.tokoKyc.form.KycUploadViewModel.Companion.KYC_IV_FACE_CACHE
import com.tokopedia.kyc_centralized.ui.tokoKyc.form.KycUploadViewModel.Companion.KYC_IV_KTP_CACHE
import com.tokopedia.kyc_centralized.ui.tokoKyc.form.UserIdentificationFormActivity.Companion.FILE_NAME_KYC
import com.tokopedia.kyc_centralized.ui.tokoKyc.form.stepper.BaseUserIdentificationStepperFragment
import com.tokopedia.kyc_centralized.ui.tokoKyc.form.stepper.UserIdentificationStepperModel
import com.tokopedia.kyc_centralized.ui.tokoKyc.info.UserIdentificationInfoFragment
import com.tokopedia.kyc_centralized.util.ImageEncryptionUtil
import com.tokopedia.kyc_centralized.util.KycUploadErrorCodeUtil.FAILED_ENCRYPTION
import com.tokopedia.kyc_centralized.util.KycUploadErrorCodeUtil.FILE_PATH_FACE_EMPTY
import com.tokopedia.kyc_centralized.util.KycUploadErrorCodeUtil.FILE_PATH_KTP_EMPTY
import com.tokopedia.media.loader.loadImage
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.file.FileUtil
import com.tokopedia.utils.lifecycle.autoClearedNullable
import timber.log.Timber
import javax.inject.Inject

/**
 * @author by alvinatin on 15/11/18.
 */
class UserIdentificationFormFinalFragment : BaseDaggerFragment(),
    UserIdentificationFormActivity.Listener {
    private var viewBinding by autoClearedNullable<FragmentUserIdentificationFinalBinding>()
    private var stepperModel: UserIdentificationStepperModel? = null
    private var stepperListener: StepperListener? = null
    private var analytics: UserIdentificationCommonAnalytics? = null
    private var listRetake: ArrayList<Int> = arrayListOf()
    private var isSocketTimeoutException: Boolean = false
    private var kycType = ""
    private var projectId = 0

    private lateinit var remoteConfig: RemoteConfig

    private var retakeActionCode = NOT_RETAKE
    private var allowedSelfie = false

    @Inject
    lateinit var serverLogger: KycServerLogger
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val kycUploadViewModel by lazy { viewModelFragmentProvider.get(KycUploadViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (context is StepperListener) {
            stepperListener = context as StepperListener?
        }

        if (arguments != null && savedInstanceState == null) {
            stepperModel = arguments?.getParcelable(BaseStepperActivity.STEPPER_MODEL_EXTRA)
            kycType = arguments?.getString(PARAM_KYC_TYPE).orEmpty()
            projectId = arguments?.getInt(PARAM_PROJECT_ID).orZero()
        } else if (savedInstanceState != null) {
            stepperModel =
                savedInstanceState.getParcelable(BaseUserIdentificationStepperFragment.EXTRA_KYC_STEPPER_MODEL)
        }

        if (activity != null) {
            allowedSelfie = activity?.intent?.getBooleanExtra(
                UserIdentificationInfoFragment.ALLOW_SELFIE_FLOW_EXTRA,
                false
            ) ?: false
            analytics = UserIdentificationCommonAnalytics.createInstance(projectId)
        }

        remoteConfig = FirebaseRemoteConfigImpl(context)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(
            BaseUserIdentificationStepperFragment.EXTRA_KYC_STEPPER_MODEL,
            stepperModel
        )
        super.onSaveInstanceState(outState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentUserIdentificationFinalBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        encryptImage()
        setContentView()
        if (projectId == TRADE_IN_PROJECT_ID) //TradeIn project Id
            viewBinding?.uploadButton?.setText(R.string.upload_button_tradein)

        analytics?.eventViewFinalForm()
        initObserver()
    }

    private fun initObserver() {
        kycUploadViewModel.kycResponseLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    sendSuccessTimberLog()
                    setKycUploadResultView(it.data)
                }
                is Fail -> {
                    hideLoading()
                    showUploadError()
                    setFailedResult(it.throwable)
                    sendErrorTimberLog(it.throwable)
                }
            }
        }

        kycUploadViewModel.encryptImageLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    viewBinding?.uploadButton?.isEnabled = true
                    when (retakeActionCode) {
                        NOT_RETAKE -> {
                            uploadKycFiles(
                                isKtpFileUsingEncryption = true,
                                isFaceFileUsingEncryption = true
                            )
                        }
                        RETAKE_KTP -> {
                            retakeActionCode = RETAKE_KTP_AND_FACE
                            goToLivenessOrSelfie()
                        }
                        RETAKE_FACE -> {
                            uploadKycFiles(
                                isKtpFileUsingEncryption = false,
                                isFaceFileUsingEncryption = true
                            )
                        }
                        RETAKE_KTP_AND_FACE -> {
                            uploadKycFiles(
                                isKtpFileUsingEncryption = true,
                                isFaceFileUsingEncryption = true
                            )
                        }
                    }
                }
                is Fail -> {
                    ErrorHandler.getErrorMessage(
                        activity,
                        it.throwable,
                        ErrorHandler.Builder().apply {
                            className = UserIdentificationFormFinalFragment::class.java.name
                        }.build()
                    )
                    NetworkErrorHelper.showRedSnackbar(
                        activity,
                        context?.resources?.getString(R.string.error_text_image_fail_to_encrypt)
                    )
                    Timber.w(it.throwable, "$LIVENESS_TAG: ENCRYPT ERROR")
                }
            }
        }
    }

    private fun sendErrorTimberLog(throwable: Throwable) {
        Timber.w(throwable, "$LIVENESS_TAG: LIVENESS_UPLOAD_RESULT")
        if (!isKycSelfie) {
            serverLogger.livenessUploadResult(
                "ErrorUpload",
                stepperModel?.ktpFile.orEmpty(),
                stepperModel?.faceFile.orEmpty(),
                projectId.toString(),
                throwable
            )

        } else {
            serverLogger.selfieUploadResult(
                "ErrorUpload",
                stepperModel?.ktpFile.orEmpty(),
                stepperModel?.faceFile.orEmpty(),
                projectId.toString(),
                throwable
            )

            analytics?.eventClickUploadPhotosTradeIn("failed")
        }
    }

    private fun sendSuccessTimberLog() {
        analytics?.eventClickUploadPhotosTradeIn("success")

        serverLogger.kyUploadResult(
            "SuccessUpload",
            stepperModel?.ktpFile.orEmpty(),
            stepperModel?.faceFile.orEmpty(),
            projectId.toString(),
            isKycSelfie
        )
    }

    override fun initInjector() {
        getComponent(UserIdentificationCommonComponent::class.java).inject(this)
    }

    private fun openCameraView(viewMode: Int, requestCode: Int) {
        context?.let {
            val intent = if (viewMode == UserIdentificationCameraFragment.PARAM_VIEW_MODE_KTP) {
                createIntent(
                    it,
                    viewMode,
                    projectId,
                    useCropping = true,
                    useCompression = true
                )
            } else {
                createIntent(it, viewMode, projectId)
            }

            startActivityForResult(intent, requestCode)
        }
    }

    private fun openLivenessView() {
        val intent = RouteManager.getIntent(
            context,
            ApplinkConstInternalUserPlatform.KYC_LIVENESS,
            projectId.toString()
        )
        startActivityForResult(intent, KYCConstant.REQUEST_CODE_CAMERA_FACE)
    }

    private fun encryptImage() {
        if (isUsingEncrypt()) {
            viewBinding?.uploadButton?.isEnabled = false
            kycUploadViewModel.encryptImage(
                stepperModel?.faceFile.toEmptyStringIfNull(),
                KYC_IV_FACE_CACHE
            )
        }
    }

    private fun setContentView() {
        viewBinding?.userIdentificationFinalLoadingLayout?.visibility = View.GONE
        if (activity is UserIdentificationFormActivity) {
            (activity as UserIdentificationFormActivity)
                .updateToolbarTitle(getString(R.string.title_kyc_form_upload))
        }
    }

    private fun uploadKycFiles(
        isKtpFileUsingEncryption: Boolean,
        isFaceFileUsingEncryption: Boolean
    ) {
        showLoading()
        stepperModel?.let {
            if (isSocketTimeoutException) {
                isSocketTimeoutException = false
            }
            if (isUsingEncrypt()) {
                kycUploadViewModel.uploadImages(
                    it.ktpFile,
                    it.faceFile,
                    projectId.toString(),
                    isKtpFileUsingEncryption,
                    isFaceFileUsingEncryption
                )
            } else {
                kycUploadViewModel.uploadImages(
                    it.ktpFile,
                    it.faceFile,
                    projectId.toString(),
                    isKtpFileUsingEncryption = false,
                    isFaceFileUsingEncryption = false
                )
            }
        }
    }

    private fun setKycUploadResultView(data: KycData) {
        if (data.isSuccessRegister) {
            deleteTmpFile(deleteKtp = true, deleteFace = true)
            activity?.setResult(Activity.RESULT_OK)
            stepperListener?.finishPage()
        } else {
            hideLoading()
            var imageKtp = KycUrl.KTP_VERIF_OK
            var imageFace = KycUrl.FACE_VERIF_OK
            var colorKtp: Int? = context?.resources?.let {
                ResourcesCompat.getColor(
                    it,
                    com.tokopedia.unifyprinciples.R.color.Unify_N700_96,
                    null
                )
            }
            var colorFace: Int? = context?.resources?.let {
                ResourcesCompat.getColor(
                    it,
                    com.tokopedia.unifyprinciples.R.color.Unify_N700_96,
                    null
                )
            }

            listRetake = data.listRetake
            if (!listRetake.isNullOrEmpty()) {
                for (i in data.listRetake.indices) {
                    when (data.listRetake[i]) {
                        KYCConstant.KTP_RETAKE -> {
                            imageKtp = KycUrl.KTP_VERIF_FAIL
                            colorKtp = null
                            setKtpRetakeButtonListener()
                        }
                        KYCConstant.FACE_RETAKE -> {
                            imageFace = KycUrl.FACE_VERIF_FAIL
                            colorFace = null
                            setFaceRetakeButtonListener()
                        }
                    }
                }
                if (listRetake.size == 2) {
                    setKtpFaceRetakeButtonListener()
                }
            }
            setResultViews(
                imageKtp,
                imageFace,
                data.app.title,
                data.app.subtitle,
                colorKtp,
                colorFace,
                data.app.button,
                data.listMessage
            )
            if (activity is UserIdentificationFormActivity) {
                (activity as UserIdentificationFormActivity)
                    .updateToolbarTitle(getString(R.string.title_kyc_form_fail_verification))
            }
        }
    }

    private fun setKtpRetakeButtonListener() {
        viewBinding?.uploadButton?.setOnClickListener { v: View? ->
            analytics?.eventClickChangeKtpFinalFormPage()
            deleteTmpFile(deleteKtp = true, deleteFace = false)
            openCameraView(
                UserIdentificationCameraFragment.PARAM_VIEW_MODE_KTP,
                KYCConstant.REQUEST_CODE_CAMERA_KTP
            )
        }
    }

    private fun setFaceRetakeButtonListener() {
        viewBinding?.uploadButton?.setOnClickListener { v: View? ->
            analytics?.eventClickChangeSelfieFinalFormPage()
            retakeActionCode = RETAKE_FACE
            goToLivenessOrSelfie()
        }
    }

    private fun goToLivenessOrSelfie() {
        deleteTmpFile(deleteKtp = false, deleteFace = true)
        if (!isKycSelfie) {
            openLivenessView()
        } else {
            openCameraView(
                UserIdentificationCameraFragment.PARAM_VIEW_MODE_FACE,
                KYCConstant.REQUEST_CODE_CAMERA_FACE
            )
        }
    }

    private fun setKtpFaceRetakeButtonListener() {
        viewBinding?.uploadButton?.setOnClickListener { v: View? ->
            analytics?.eventClickChangeKtpSelfieFinalFormPage()
            deleteTmpFile(deleteKtp = true, deleteFace = true)
            openCameraView(
                UserIdentificationCameraFragment.PARAM_VIEW_MODE_KTP,
                KYCConstant.REQUEST_CODE_CAMERA_KTP
            )
        }
    }

    private fun setResultViews(
        urlKtp: String,
        urlFace: String,
        subtitleText: String,
        infoText: String,
        colorKtp: Int?,
        colorFace: Int?,
        buttonText: String,
        listMessage: ArrayList<String>?
    ) {
        viewBinding?.resultImageKtp?.loadImage(urlKtp)
        viewBinding?.resultImageFace?.loadImage(urlFace)
        if (colorKtp != null) {
            viewBinding?.resultTextKtp?.setTextColor(colorKtp)
        }
        if (colorFace != null) {
            viewBinding?.resultTextFace?.setTextColor(colorFace)
        }
        viewBinding?.textSubtitle?.gravity = Gravity.START
        viewBinding?.textSubtitle?.text = subtitleText
        viewBinding?.textInfo?.gravity = Gravity.START
        viewBinding?.textInfo?.text = infoText
        viewBinding?.uploadButton?.text = buttonText
        viewBinding?.layoutInfoBullet?.removeAllViewsInLayout()
        if (listMessage != null) {
            for (i in listMessage.indices) {
                viewBinding?.layoutInfoBullet?.let {
                    context?.let { context ->
                        (activity as UserIdentificationFormActivity).setTextViewWithBullet(
                            listMessage[i],
                            context,
                            it
                        )
                    }
                }
            }
        }
    }

    private val isKycSelfie: Boolean
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            retakeAction(requestCode, data)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun retakeAction(requestCode: Int, data: Intent) {
        when (requestCode) {
            KYCConstant.REQUEST_CODE_CAMERA_KTP -> {
                retakeActionCode = RETAKE_KTP
                stepperModel?.ktpFile =
                    data.getStringExtra(KYCConstant.EXTRA_STRING_IMAGE_RESULT).orEmpty()
                if (isUsingEncrypt()) {
                    kycUploadViewModel.encryptImage(
                        stepperModel?.ktpFile.orEmpty(),
                        KYC_IV_KTP_CACHE
                    )
                } else {
                    goToLivenessOrSelfie()
                }
            }
            KYCConstant.REQUEST_CODE_CAMERA_FACE -> {
                if (retakeActionCode != RETAKE_FACE) {
                    retakeActionCode = RETAKE_KTP_AND_FACE
                }
                stepperModel?.faceFile = if (!isKycSelfie) {
                    data.getStringExtra(ApplinkConstInternalGlobal.PARAM_FACE_PATH).orEmpty()
                } else {
                    data.getStringExtra(KYCConstant.EXTRA_STRING_IMAGE_RESULT).orEmpty()
                }
                if (isUsingEncrypt()) {
                    kycUploadViewModel.encryptImage(
                        stepperModel?.faceFile.orEmpty(),
                        KYC_IV_FACE_CACHE
                    )
                } else {
                    uploadKycFiles(
                        isKtpFileUsingEncryption = true,
                        isFaceFileUsingEncryption = true
                    )
                }
            }
            else -> {
            }
        }
    }

    override fun getScreenName(): String = ""

    private fun showLoading() {
        viewBinding?.layoutMain?.visibility = View.GONE
        viewBinding?.loaderUnify?.visibility = View.VISIBLE
        viewBinding?.layoutKycUploadError?.root?.visibility = View.GONE
    }

    private fun hideLoading() {
        viewBinding?.layoutMain?.visibility = View.VISIBLE
        viewBinding?.loaderUnify?.visibility = View.GONE
        viewBinding?.layoutKycUploadError?.root?.visibility = View.GONE
    }

    private fun showUploadError() {
        viewBinding?.layoutMain?.visibility = View.GONE
        viewBinding?.loaderUnify?.visibility = View.GONE
        viewBinding?.layoutKycUploadError?.root?.visibility = View.VISIBLE
    }

    fun clickBackAction() {
        if (isSocketTimeoutException) {
            analytics?.eventClickBackConnectionTimeout()
        } else {
            if (!isKycSelfie) {
                if (listRetake.size == 1) {
                    when (listRetake[0]) {
                        KYCConstant.KTP_RETAKE -> {
                            run { analytics?.eventClickBackChangeKtpFinalFormPage() }
                            run { analytics?.eventClickBackChangeSelfieFinalFormPage() }
                        }
                        KYCConstant.FACE_RETAKE -> {
                            analytics?.eventClickBackChangeSelfieFinalFormPage()
                        }
                    }
                } else if (listRetake.size == 2) {
                    analytics?.eventClickBackChangeKtpSelfieFinalFormPage()
                }
            } else {
                analytics?.eventClickBackFinalForm()
            }
        }
    }

    override fun trackOnBackPressed() {}

    private fun setFailedResult(throwable: Throwable) {
        val message = ErrorHandler.getErrorMessage(
            context,
            throwable,
            ErrorHandler.Builder().apply {
                className = UserIdentificationFormFinalFragment::class.java.name

            }.build()
        )

        when {
            message.contains("timeout") -> {
                isSocketTimeoutException = true
                analytics?.eventClickConnectionTimeout()
                setViews(
                    getString(R.string.kyc_upload_failed_title),
                    message,
                    SCAN_FACE_FAIL_NETWORK
                )
            }
            message.contains(FAILED_ENCRYPTION) -> {
                setViews(
                    getString(R.string.kyc_upload_failed_title),
                    getString(R.string.kyc_upload_failed_reason_encrypt),
                    SCAN_FACE_FAIL_GENERAL
                )
            }
            else -> {
                setViews(
                    getString(R.string.kyc_upload_failed_title),
                    message,
                    SCAN_FACE_FAIL_GENERAL
                )
            }
        }

        view?.findViewById<UnifyButton>(R.id.kyc_upload_error_button)?.setOnClickListener {
            when {
                message.contains(FAILED_ENCRYPTION) || message.contains(FILE_PATH_KTP_EMPTY) -> {
                    deleteTmpFile(deleteKtp = true, deleteFace = true)
                    stepperListener?.finishPage()
                }
                message.contains(FILE_PATH_FACE_EMPTY) -> {
                    retakeActionCode = RETAKE_FACE
                    goToLivenessOrSelfie()
                }
                !isKycSelfie -> {
                    retakeActionCode = RETAKE_FACE
                    openLivenessView()
                }
                else -> {
                    uploadKycFiles(
                        isKtpFileUsingEncryption = false,
                        isFaceFileUsingEncryption = false
                    )
                }
            }
        }
    }

    private fun setViews(failedReasonTitle: String, failedReason: String, failedImage: String) {
        view?.findViewById<Typography>(R.id.kyc_upload_error_title)?.text = failedReasonTitle
        view?.findViewById<Typography>(R.id.kyc_upload_error_subtitle)?.text = failedReason
        viewBinding?.layoutKycUploadError?.mainImage?.loadImage(failedImage)
    }

    fun deleteTmpFile(deleteKtp: Boolean, deleteFace: Boolean) {
        Timber.d("$LIVENESS_TAG: deleting ktp ($deleteKtp) face ($deleteFace)")
        if (deleteKtp && deleteFace) {
            FileUtil.deleteFolder(context?.externalCacheDir?.absolutePath + FILE_NAME_KYC)
        } else {
            if (deleteKtp) FileUtil.deleteFile(stepperModel?.ktpFile)
            if (deleteFace) FileUtil.deleteFile(stepperModel?.faceFile)
        }
    }

    private fun isUsingEncrypt(): Boolean {
        context?.let {
            return ImageEncryptionUtil.isUsingEncrypt(it)
        }
        return false
    }

    companion object {
        private const val TRADE_IN_PROJECT_ID = 4
        private const val NOT_RETAKE = 0
        private const val RETAKE_KTP = 1
        private const val RETAKE_FACE = 2
        private const val RETAKE_KTP_AND_FACE = 3

        fun createInstance(projectid: Int, kycType: String): Fragment {
            return UserIdentificationFormFinalFragment().apply {
                arguments = Bundle().apply {
                    putInt(PARAM_PROJECT_ID, projectid)
                    putString(PARAM_KYC_TYPE, kycType)
                }
            }
        }
    }
}
