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
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseStepperActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.listener.StepperListener
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.imagepicker.common.util.FileUtils
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.di.DaggerUserIdentificationCommonComponent
import com.tokopedia.kyc_centralized.view.activity.UserIdentificationCameraActivity.Companion.createIntent
import com.tokopedia.kyc_centralized.view.activity.UserIdentificationFormActivity
import com.tokopedia.kyc_centralized.view.listener.UserIdentificationUploadImage
import com.tokopedia.kyc_centralized.view.model.UserIdentificationStepperModel
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.user_identification_common.KYCConstant
import com.tokopedia.user_identification_common.KycCommonUrl
import com.tokopedia.user_identification_common.KycUrl
import com.tokopedia.user_identification_common.analytics.UserIdentificationCommonAnalytics
import com.tokopedia.user_identification_common.subscriber.GetKtpStatusSubscriber.GetKtpStatusListener
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * @author by alvinatin on 15/11/18.
 */
class UserIdentificationFormFinalFragment : BaseDaggerFragment(), UserIdentificationUploadImage.View, GetKtpStatusListener, UserIdentificationFormActivity.Listener {
    private var loadingLayout: ConstraintLayout? = null
    private var mainLayout: ConstraintLayout? = null
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

    @Inject
    lateinit var presenter: UserIdentificationUploadImage.Presenter

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
            analytics = UserIdentificationCommonAnalytics.createInstance(activity?.intent?.getIntExtra(ApplinkConstInternalGlobal.PARAM_PROJECT_ID, 1)?: 1)
        }
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
        hideLoading()
        analytics?.eventViewFinalForm()
    }

    override fun initInjector() {
        if (activity != null) {
            val daggerUserIdentificationComponent = DaggerUserIdentificationCommonComponent.builder()
                    .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                    .build()
            daggerUserIdentificationComponent.inject(this)
            presenter?.attachView(this)
        }
    }

    private fun openCameraView(viewMode: Int, requestCode: Int) {
        val intent = createIntent(context, viewMode)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_PROJECT_ID, projectId)
        startActivityForResult(intent, requestCode)
    }

    private fun openLivenessView() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.LIVENESS_DETECTION)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_PROJECT_ID, projectId)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_KTP_PATH, stepperModel?.ktpFile)
        if (stepperModel?.listRetake?.contains(2) == false) {
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_FACE_PATH, stepperModel?.faceFile)
        }
        startActivityForResult(intent, KYCConstant.REQUEST_CODE_CAMERA_FACE)
    }

    private fun setContentView() {
        loadingLayout?.visibility = View.GONE
        if (isKycSelfie) {
            setKycSelfieView()
        } else {
            setKycLivenessView()
        }
    }

    private fun setKycSelfieView() {
        if (activity is UserIdentificationFormActivity) {
            (activity as UserIdentificationFormActivity)
                    .updateToolbarTitle(getString(R.string.title_kyc_form_upload))
        }
        setResultViews(KycUrl.KTP_VERIF_OK, KycUrl.FACE_VERIF_OK, "", getString(R.string.form_final_info),
                ResourcesCompat.getColor(resources, R.color.kyc_centralized_f531353b, null),
                ResourcesCompat.getColor(resources, R.color.kyc_centralized_f531353b, null),
                getString(R.string.upload_button))
        generateLink()
        uploadButton?.setOnClickListener { v: View? ->
            analytics?.eventClickUploadPhotos()
            checkKtp()
        }
    }

    private fun setKycLivenessView() {
        if(!stepperModel?.listRetake.isNullOrEmpty()) {
            listRetake = stepperModel?.listRetake ?: arrayListOf()
        }
        var imageKtp = KycUrl.KTP_VERIF_OK
        var imageFace = KycUrl.FACE_VERIF_OK
        var colorKtp: Int? = ResourcesCompat.getColor(resources, R.color.kyc_centralized_f531353b, null)
        var colorFace: Int? = ResourcesCompat.getColor(resources, R.color.kyc_centralized_f531353b, null)
        if (!listRetake.isNullOrEmpty()) {
            for (i in listRetake.indices) {

                when (listRetake[i]) {
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
        stepperModel?.titleText?.let {titleText ->
            stepperModel?.subtitleText?.let { subtitleText ->
                stepperModel?.buttonText?.let { buttonText ->
                    setResultViews(imageKtp, imageFace, titleText, subtitleText, colorKtp, colorFace, buttonText) }
            }
        }
        if (activity is UserIdentificationFormActivity) {
            (activity as UserIdentificationFormActivity)
                    .updateToolbarTitle(getString(R.string.title_kyc_form_fail_verification))
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
            openLivenessView()
        }
    }

    private fun setKtpFaceUploadButtonListener() {
        uploadButton?.setOnClickListener { v: View? ->
            analytics?.eventClickChangeKtpSelfieFinalFormPage()
            openCameraView(UserIdentificationCameraFragment.PARAM_VIEW_MODE_KTP, KYCConstant.REQUEST_CODE_CAMERA_KTP)
        }
    }

    private fun setResultViews(urlKtp: String, urlFace: String, subtitleText: String, infoText: String, colorKtp: Int?, colorFace: Int?, buttonText: String) {
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
        val listMessage = stepperModel?.listMessage
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
                    return RemoteConfigInstance.getInstance().abTestPlatform.getString(KYCConstant.KYC_AB_KEYWORD) != KYCConstant.KYC_AB_KEYWORD
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return true
        }

    private fun checkKtp() {
        showLoading()
        presenter.checkKtp(stepperModel?.ktpFile)
    }

    private fun uploadImage() {
        showLoading()
        presenter.uploadImage(stepperModel, projectId)
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
    }

    private fun getLivenessResult(data: Intent) {
        val isSuccessRegister = data.getBooleanExtra(ApplinkConst.Liveness.EXTRA_IS_SUCCESS_REGISTER, false)
        if (!isSuccessRegister) {
            stepperModel?.listRetake = data.getIntegerArrayListExtra(ApplinkConst.Liveness.EXTRA_LIST_RETAKE)
            stepperModel?.listMessage = data.getStringArrayListExtra(ApplinkConst.Liveness.EXTRA_LIST_MESSAGE)
            stepperModel?.titleText = data.getStringExtra(ApplinkConst.Liveness.EXTRA_TITLE)
            stepperModel?.subtitleText = data.getStringExtra(ApplinkConst.Liveness.EXTRA_SUBTITLE)
            stepperModel?.buttonText = data.getStringExtra(ApplinkConst.Liveness.EXTRA_BUTTON)
            val ft = fragmentManager?.beginTransaction()
            ft?.detach(this)?.attach(this)?.commit()
        } else {
            activity?.setResult(Activity.RESULT_OK)
            stepperListener?.finishPage()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            retakeAction(requestCode, data)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun retakeAction(requestCode: Int, data: Intent) {
        if (!isKycSelfie) {
            when (requestCode) {
                KYCConstant.REQUEST_CODE_CAMERA_KTP -> {
                    stepperModel?.ktpFile = data.getStringExtra(KYCConstant.EXTRA_STRING_IMAGE_RESULT)
                    openLivenessView()
                }
                KYCConstant.REQUEST_CODE_CAMERA_FACE -> {
                    stepperModel?.faceFile = data.getStringExtra(ApplinkConstInternalGlobal.PARAM_FACE_PATH)
                    getLivenessResult(data)
                }
                else -> {
                }
            }
        } else {
            val imagePath = data.getStringExtra(KYCConstant.EXTRA_STRING_IMAGE_RESULT)
            if (requestCode == KYCConstant.REQUEST_CODE_CAMERA_KTP) {
                stepperModel?.ktpFile = imagePath
                analytics?.eventClickUploadPhotos()
                checkKtp()
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
                ds.color = resources.getColor(R.color.kyc_centralized_42b549)
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

    override fun onSuccessUpload() {
        hideLoading()
        Timber.w("P2#KYC_SELFIE_UPLOAD_RESULT#'SuccessUpload';" +
                "ktpPath='" + stepperModel?.ktpFile + "';" +
                "facePath='" + stepperModel?.faceFile + "';" +
                "tkpdProjectId='" + projectId + "';")
        activity?.setResult(Activity.RESULT_OK)
        analytics?.eventClickUploadPhotosTradeIn("success")
        stepperListener?.finishPage()
    }

    override fun onErrorUpload(error: String?) {
        hideLoading()
        if (activity is UserIdentificationFormActivity) {
            (activity as UserIdentificationFormActivity?)?.showError(error, NetworkErrorHelper.RetryClickedListener { uploadImage() })
            analytics?.eventClickUploadPhotosTradeIn("failed")
        }
    }

    override val ktpStatusListener: GetKtpStatusListener
        get() = this

    override fun showLoading() {
        mainLayout?.visibility = View.GONE
        loadingLayout?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        mainLayout?.visibility = View.VISIBLE
        loadingLayout?.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detachView()
    }

    private fun showKtpInvalidView() {
        uploadButton?.setOnClickListener { v: View? ->
            analytics?.eventClickChangeKtpFinalFormPage()
            openCameraView(UserIdentificationCameraFragment.PARAM_VIEW_MODE_KTP, KYCConstant.REQUEST_CODE_CAMERA_KTP)
        }
        setResultViews(KycUrl.KTP_VERIF_FAIL, KycUrl.FACE_VERIF_OK, getString(R.string.kyc_ktp_fail_face_ok_verification_subtitle),
                getString(R.string.kyc_ktp_fail_face_ok_verification_info),
                null, ResourcesCompat.getColor(resources, R.color.kyc_centralized_f531353b, null),
                getString(R.string.kyc_ktp_fail_face_ok_button))
        bulletTextLayout?.let {
            context?.let { it1 ->
                (activity as UserIdentificationFormActivity)
                    .setTextViewWithBullet(getString(R.string.kyc_ktp_fail_face_ok_info_1), it1, it)
            }
        }
        bulletTextLayout?.let {
            context?.let { it1 ->
                (activity as UserIdentificationFormActivity)
                    .setTextViewWithBullet(getString(R.string.kyc_ktp_fail_face_ok_info_2), it1, it)
            }
        }
    }

    fun clickBackAction() {
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
        FileUtils.deleteFileInTokopediaFolder(stepperModel?.ktpFile)
        FileUtils.deleteFileInTokopediaFolder(stepperModel?.faceFile)
    }

    override fun trackOnBackPressed() {}
    override fun onErrorGetKtpStatus(throwable: Throwable) {
        hideLoading()
        val errMsg = ErrorHandler.getErrorMessage(activity, throwable)
        (activity as UserIdentificationFormActivity).showError(errMsg, NetworkErrorHelper.RetryClickedListener { checkKtp() })
    }

    override fun onKtpInvalid(message: String?) {
        hideLoading()
        showKtpInvalidView()
    }

    override fun onKtpValid() {
        hideLoading()
        uploadImage()
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