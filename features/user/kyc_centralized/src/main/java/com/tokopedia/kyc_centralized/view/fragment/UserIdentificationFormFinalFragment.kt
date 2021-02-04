package com.tokopedia.kyc_centralized.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseStepperActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.listener.StepperListener
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.data.model.response.KycData
import com.tokopedia.kyc_centralized.di.DaggerUserIdentificationCommonComponent
import com.tokopedia.kyc_centralized.util.KycUploadErrorCodeUtil
import com.tokopedia.kyc_centralized.view.activity.UserIdentificationCameraActivity.Companion.createIntent
import com.tokopedia.kyc_centralized.view.activity.UserIdentificationFormActivity
import com.tokopedia.kyc_centralized.view.listener.UserIdentificationUploadImage
import com.tokopedia.kyc_centralized.view.model.UserIdentificationStepperModel
import com.tokopedia.kyc_centralized.view.viewmodel.KycUploadViewModel
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user_identification_common.KYCConstant
import com.tokopedia.user_identification_common.KycCommonUrl
import com.tokopedia.user_identification_common.KycUrl
import com.tokopedia.user_identification_common.analytics.UserIdentificationCommonAnalytics
import kotlinx.android.synthetic.main.layout_kyc_upload_error.*
import com.tokopedia.utils.file.FileUtil
import timber.log.Timber
import java.net.SocketTimeoutException
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * @author by alvinatin on 15/11/18.
 */
class UserIdentificationFormFinalFragment : BaseDaggerFragment(), UserIdentificationUploadImage.View, UserIdentificationFormActivity.Listener {
    private var loadingLayout: ConstraintLayout? = null
    private var mainLayout: ConstraintLayout? = null
    private var errorUploadLayout: RelativeLayout? = null
    private var resultImageKtp: ImageView? = null
    private var resultImageFace: ImageView? = null
    private var resultTextKtp: TextView? = null
    private var resultTextFace: TextView? = null
    private var bulletTextLayout: LinearLayout? = null
    private var info: TextView? = null
    private var subtitle: TextView? = null
    private var uploadButton: TextView? = null
    private var stepperModel: UserIdentificationStepperModel? = null
    private var stepperListener: StepperListener? = null
    private var analytics: UserIdentificationCommonAnalytics? = null
    private var listRetake: ArrayList<Int> = arrayListOf()
    private var isSocketTimeoutException: Boolean = false

    private lateinit var remoteConfig: RemoteConfig

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val kycUploadViewModel by lazy { viewModelFragmentProvider.get(KycUploadViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (context is StepperListener) {
            stepperListener = context as StepperListener?
        }
        if (arguments != null && savedInstanceState == null) {
            stepperModel = arguments?.getParcelable(BaseStepperActivity.STEPPER_MODEL_EXTRA)
        } else if (savedInstanceState != null) {
            stepperModel = savedInstanceState.getParcelable(BaseUserIdentificationStepperFragment.EXTRA_KYC_STEPPER_MODEL)
        }
        if (activity != null) {
            analytics = UserIdentificationCommonAnalytics.createInstance(projectId)
        }

        remoteConfig = FirebaseRemoteConfigImpl(context)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(BaseUserIdentificationStepperFragment.EXTRA_KYC_STEPPER_MODEL, stepperModel)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_user_identification_final, container, false)
        initView(view)
        setContentView()
        if (projectId == 4) //TradeIn project Id
            uploadButton?.setText(R.string.upload_button_tradein)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        analytics?.eventViewFinalForm()
        initObserver()
    }

    private fun initObserver() {
        kycUploadViewModel.kycResponseLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            hideLoading()
            when (it) {
                is Success -> {
                    setKycUploadResultView(it.data)
                    sendSuccessTimberLog()
                }
                is Fail -> {
                    showUploadError()
                    setFailedResult(it.throwable)
                    sendErrorTimberLog(it.throwable)
                }
            }
        })
    }

    private fun sendErrorTimberLog(throwable: Throwable) {
        if(!isKycSelfie) {
            Timber.w("P2#LIVENESS_UPLOAD_RESULT#'ErrorUpload';ktpPath='${stepperModel?.ktpFile}';facePath='${stepperModel?.faceFile}';tkpdProjectId='$projectId';stack_trace='${throwable}'")
        } else {
            Timber.w("P2#SELFIE_UPLOAD_RESULT#'ErrorUpload';ktpPath='${stepperModel?.ktpFile}';facePath='${stepperModel?.faceFile}';tkpdProjectId='$projectId';stack_trace='${throwable}'")
            analytics?.eventClickUploadPhotosTradeIn("failed")
        }
    }

    private fun sendSuccessTimberLog() {
        if(isKycSelfie) {
            Timber.w("P2#KYC_SELFIE_UPLOAD_RESULT#'SuccessUpload';" +
                    "ktpPath='" + stepperModel?.ktpFile + "';" +
                    "facePath='" + stepperModel?.faceFile + "';" +
                    "tkpdProjectId='" + projectId + "';")
            analytics?.eventClickUploadPhotosTradeIn("success")
        }
    }

    override fun initInjector() {
        if (activity != null) {
            val daggerUserIdentificationComponent = DaggerUserIdentificationCommonComponent.builder()
                    .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                    .build()
            daggerUserIdentificationComponent.inject(this)
        }
    }

    private fun openCameraView(viewMode: Int, requestCode: Int) {
        val intent = createIntent(context, viewMode)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_PROJECT_ID, projectId)
        startActivityForResult(intent, requestCode)
    }

    private fun openLivenessView() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.LIVENESS_DETECTION)
        startActivityForResult(intent, KYCConstant.REQUEST_CODE_CAMERA_FACE)
    }

    private fun setContentView() {
        loadingLayout?.visibility = View.GONE
        if (isKycSelfie) {
            hideLoading()
            setKycSelfieView()
        } else {
            uploadKycFiles()
        }
        if (activity is UserIdentificationFormActivity) {
            (activity as UserIdentificationFormActivity)
                    .updateToolbarTitle(getString(R.string.title_kyc_form_upload))
        }
    }

    private fun uploadKycFiles() {
        showLoading()
        stepperModel?.let {
            if(isSocketTimeoutException) {
                isSocketTimeoutException = false
            }
            kycUploadViewModel.uploadImages(it.ktpFile, it.faceFile, projectId.toString())
        }
    }

    private fun setKycSelfieView() {
        if (activity is UserIdentificationFormActivity) {
            (activity as UserIdentificationFormActivity)
                    .updateToolbarTitle(getString(R.string.title_kyc_form_upload))
        }
        setResultViews(KycUrl.KTP_VERIF_OK, KycUrl.FACE_VERIF_OK, "", getString(R.string.form_final_info),
                ResourcesCompat.getColor(resources, com.tokopedia.unifyprinciples.R.color.Unify_N700_96, null),
                ResourcesCompat.getColor(resources, com.tokopedia.unifyprinciples.R.color.Unify_N700_96, null),
                getString(R.string.upload_button), null)
        generateLink()
        uploadButton?.setOnClickListener { v: View? ->
            analytics?.eventClickUploadPhotos()
            uploadKycFiles()
        }
    }

    private fun setKycUploadResultView(data: KycData) {
        if(data.isSuccessRegister) {
            deleteTmpFile()
            activity?.setResult(Activity.RESULT_OK)
            stepperListener?.finishPage()
        } else {
            var imageKtp = KycUrl.KTP_VERIF_OK
            var imageFace = KycUrl.FACE_VERIF_OK
            var colorKtp: Int? = ResourcesCompat.getColor(resources, com.tokopedia.unifyprinciples.R.color.Unify_N700_96, null)
            var colorFace: Int? = ResourcesCompat.getColor(resources, com.tokopedia.unifyprinciples.R.color.Unify_N700_96, null)
            listRetake = data.listRetake
            if(!listRetake.isNullOrEmpty()) {
                for(i in data.listRetake.indices) {
                    when(data.listRetake[i]) {
                        KYCConstant.KTP_RETAKE -> {
                            imageKtp = KycUrl.KTP_VERIF_FAIL
                            colorKtp = null
                            setKtpUploadButtonListener()
                        }
                        KYCConstant.FACE_RETAKE -> {
                            imageFace = KycUrl.FACE_VERIF_FAIL
                            colorFace = null
                            setFaceUploadButtonListener()
                        }
                    }
                }
                if (listRetake.size == 2) {
                    setKtpFaceUploadButtonListener()
                }
            }
            setResultViews(imageKtp, imageFace, data.app.title, data.app.subtitle, colorKtp, colorFace, data.app.button, data.listMessage)
            if (activity is UserIdentificationFormActivity) {
                (activity as UserIdentificationFormActivity)
                    .updateToolbarTitle(getString(R.string.title_kyc_form_fail_verification))
            }
        }
    }

    private fun setKtpUploadButtonListener() {
        uploadButton?.setOnClickListener { v: View? ->
            analytics?.eventClickChangeKtpFinalFormPage()
            openCameraView(UserIdentificationCameraFragment.PARAM_VIEW_MODE_KTP, KYCConstant.REQUEST_CODE_CAMERA_KTP)
        }
    }

    private fun setFaceUploadButtonListener() {
        uploadButton?.setOnClickListener { v: View? ->
            analytics?.eventClickChangeSelfieFinalFormPage()
            goToLivenessOrSelfie()
        }
    }

    private fun goToLivenessOrSelfie() {
        if(!isKycSelfie) {
            openLivenessView()
        } else {
            openCameraView(UserIdentificationCameraFragment.PARAM_VIEW_MODE_FACE, KYCConstant.REQUEST_CODE_CAMERA_FACE)
        }
    }

    private fun setKtpFaceUploadButtonListener() {
        uploadButton?.setOnClickListener { v: View? ->
            analytics?.eventClickChangeKtpSelfieFinalFormPage()
            openCameraView(UserIdentificationCameraFragment.PARAM_VIEW_MODE_KTP, KYCConstant.REQUEST_CODE_CAMERA_KTP)
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
        ImageHandler.LoadImage(resultImageKtp, urlKtp)
        ImageHandler.LoadImage(resultImageFace, urlFace)
        if (colorKtp != null) {
            resultTextKtp?.setTextColor(colorKtp)
        }
        if (colorFace != null) {
            resultTextFace?.setTextColor(colorFace)
        }
        subtitle?.gravity = Gravity.LEFT
        subtitle?.text = subtitleText
        info?.gravity = Gravity.LEFT
        info?.text = infoText
        uploadButton?.text = buttonText
        bulletTextLayout?.removeAllViewsInLayout()
        if (listMessage != null) {
            for (i in listMessage.indices) {
                bulletTextLayout?.let {
                    context?.let { context ->
                        (activity as UserIdentificationFormActivity).setTextViewWithBullet(listMessage[i], context, it)
                    }
                }
            }
        }
    }

    private val isKycSelfie: Boolean
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

    private fun initView(view: View) {
        loadingLayout = view.findViewById(R.id.user_identification_final_loading_layout)
        mainLayout = view.findViewById(R.id.layout_main)
        resultImageKtp = view.findViewById(R.id.result_image_ktp)
        resultImageFace = view.findViewById(R.id.result_image_face)
        resultTextKtp = view.findViewById(R.id.result_text_ktp)
        resultTextFace = view.findViewById(R.id.result_text_face)
        bulletTextLayout = view.findViewById(R.id.layout_info_bullet)
        subtitle = view.findViewById(R.id.text_subtitle)
        info = view.findViewById(R.id.text_info)
        uploadButton = view.findViewById(R.id.upload_button)
        errorUploadLayout = view.findViewById(R.id.layout_kyc_upload_error)
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
                stepperModel?.ktpFile = data.getStringExtra(KYCConstant.EXTRA_STRING_IMAGE_RESULT).toEmptyStringIfNull()
                goToLivenessOrSelfie()
            }
            KYCConstant.REQUEST_CODE_CAMERA_FACE -> {
                if(!isKycSelfie) {
                    stepperModel?.faceFile = data.getStringExtra(ApplinkConstInternalGlobal.PARAM_FACE_PATH).toEmptyStringIfNull()
                    uploadKycFiles()
                } else {
                    stepperModel?.faceFile = data.getStringExtra(KYCConstant.EXTRA_STRING_IMAGE_RESULT).toEmptyStringIfNull()
                    setKycSelfieView()
                }
            }
            else -> {
            }
        }
    }

    override fun getScreenName(): String = ""

    private fun generateLink() {
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                analytics?.eventClickTermsFinalFormPage()
                RouteManager.route(activity, KycCommonUrl.APPLINK_TERMS_AND_CONDITION)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_G400)
            }
        }
        val infoText = SpannableString(info?.text)
        val linked = resources.getString(R.string.terms_and_condition)
        val startIndex = info?.text.toString().indexOf(linked)
        infoText.setSpan(clickableSpan, startIndex, startIndex + linked.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        info?.highlightColor = Color.TRANSPARENT
        info?.movementMethod = LinkMovementMethod.getInstance()
        info?.setText(infoText, TextView.BufferType.SPANNABLE)
    }

    override val getContext: Context?
        get() = context

    override fun showLoading() {
        mainLayout?.visibility = View.GONE
        loadingLayout?.visibility = View.VISIBLE
        errorUploadLayout?.visibility = View.GONE
    }

    override fun hideLoading() {
        mainLayout?.visibility = View.VISIBLE
        loadingLayout?.visibility = View.GONE
        errorUploadLayout?.visibility = View.GONE
    }

    private fun showUploadError() {
        mainLayout?.visibility = View.GONE
        loadingLayout?.visibility = View.GONE
        errorUploadLayout?.visibility = View.VISIBLE
    }

    fun clickBackAction() {
        if(isSocketTimeoutException) {
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
        val errorCode = KycUploadErrorCodeUtil.getErrorCode(throwable)
        when (throwable) {
            is SocketTimeoutException -> {
                isSocketTimeoutException = true
                setViews(getString(R.string.kyc_upload_failed_reason_bad_network_title),
                        getString(R.string.kyc_upload_failed_reason_bad_network),
                        com.tokopedia.kyc_centralized.KycUrl.SCAN_FACE_FAIL_NETWORK)
            }
            else -> {
                setViews(getString(R.string.kyc_upload_failed_reason_general_title),
                        "${getString(R.string.kyc_upload_failed_reason_general)} ($errorCode)",
                        com.tokopedia.kyc_centralized.KycUrl.SCAN_FACE_FAIL_GENERAL)
            }
        }
        kyc_upload_error_button?.setOnClickListener {
            if(!isKycSelfie) {
                analytics?.eventClickConnectionTimeout()
                openLivenessView()
            } else {
                uploadKycFiles()
            }
        }
    }

    private fun setViews(failedReasonTitle: String, failedReason: String, failedImage: String){
        kyc_upload_error_title?.text = failedReasonTitle
        kyc_upload_error_subtitle?.text = failedReason
        main_image?.let {
            ImageHandler.LoadImage(main_image, failedImage)
        }
    }

    fun deleteTmpFile() {
        FileUtil.deleteFile(stepperModel?.ktpFile)
        FileUtil.deleteFile(stepperModel?.faceFile)
    }

    companion object {
        private var projectId = 0
        fun createInstance(projectid: Int): Fragment {
            val fragment = UserIdentificationFormFinalFragment()
            projectId = projectid
            return fragment
        }
    }
}