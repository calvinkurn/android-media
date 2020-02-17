package com.tokopedia.liveness.view.fragment

import ai.advance.common.entity.BaseResultEntity
import com.tokopedia.liveness.R
import com.tokopedia.liveness.analytics.LivenessDetectionAnalytics
import com.tokopedia.liveness.di.LivenessDetectionComponent
import ai.advance.liveness.lib.Detector
import ai.advance.liveness.lib.LivenessResult
import ai.advance.liveness.lib.LivenessView
import ai.advance.liveness.lib.impl.LivenessCallback
import ai.advance.liveness.lib.impl.LivenessGetFaceDataCallback
import com.tokopedia.liveness.utils.LivenessConstants
import com.tokopedia.liveness.view.BackgroundOverlay
import com.tokopedia.liveness.view.OnBackListener
import com.tokopedia.liveness.view.activity.LivenessFailedActivity
import com.tokopedia.liveness.view.viewmodel.LivenessDetectionViewModel
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieCompositionFactory
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.imagepicker.common.util.FileUtils
import com.tokopedia.imagepicker.common.util.ImageUtils
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import java.io.File
import java.net.SocketTimeoutException
import javax.inject.Inject

class LivenessFragment : BaseDaggerFragment(), Detector.DetectorInitCallback, LivenessCallback, OnBackListener {

    private var livenessView: LivenessView? = null
    private var tipLottieAnimationView: LottieAnimationView? = null
    private var tipTextView: TextView? = null
    private var loader: View? = null
    private var initProgressDialog: ProgressDialog? = null
    private var loadingLayout: View? = null
    private var mainLayout: View? = null
    private var bgOverlay: BackgroundOverlay? = null
    private var livenessWarnState: Detector.WarnCode? = null
    private var livenessActionState: Detector.DetectionType? = null
    private var tkpdProjectId: String? = null
    private var ktpPath: String = ""
    private var facePath: String = ""

    @Inject
    lateinit var analytics: LivenessDetectionAnalytics

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val livenessDetectionViewModel by lazy { viewModelProvider.get(LivenessDetectionViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_liveness, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        findViews()
        initData()
        initObserver()
        livenessView?.startDetection(this)
    }

    private fun initData() {
        arguments?.let {
            ktpPath = it.getString(ApplinkConstInternalGlobal.PARAM_KTP_PATH, "")
            tkpdProjectId = it.getInt(ApplinkConstInternalGlobal.PARAM_PROJECT_ID, 1).toString()
            facePath = it.getString(ApplinkConstInternalGlobal.PARAM_FACE_PATH, "")

            if (isFileExists(facePath)) {
                loadingLayout?.visibility = View.VISIBLE
                mainLayout?.visibility = View.GONE
                livenessDetectionViewModel.uploadImages(ktpPath, facePath, tkpdProjectId
                        ?: DEFAULT_ID)
            }
        }
    }

    private fun initObserver() {
        livenessDetectionViewModel.livenessResponseLiveData.observe(this, Observer {
            when (it) {
                is Success -> {
                    val intent = Intent()
                    intent.putExtra(ApplinkConst.Liveness.EXTRA_IS_SUCCESS_REGISTER, it.data.isSuccessRegister)
                    if (!it.data.isSuccessRegister) {
                        if (!it.data.listRetake.contains(FACE_RETAKE)) {
                            intent.putExtra(ApplinkConstInternalGlobal.PARAM_FACE_PATH, facePath)
                            FileUtils.deleteFileInTokopediaFolder(ktpPath)
                        } else {
                            FileUtils.deleteFileInTokopediaFolder(facePath)
                        }
                        intent.putIntegerArrayListExtra(ApplinkConst.Liveness.EXTRA_LIST_RETAKE, it.data.listRetake)
                        intent.putStringArrayListExtra(ApplinkConst.Liveness.EXTRA_LIST_MESSAGE, it.data.listMessage)
                        intent.putExtra(ApplinkConst.Liveness.EXTRA_TITLE, it.data.apps.title)
                        intent.putExtra(ApplinkConst.Liveness.EXTRA_SUBTITLE, it.data.apps.subtitle)
                        intent.putExtra(ApplinkConst.Liveness.EXTRA_BUTTON, it.data.apps.button)
                    } else {
                        FileUtils.deleteFileInTokopediaFolder(ktpPath)
                        FileUtils.deleteFileInTokopediaFolder(facePath)
                    }
                    activity?.setResult(RESULT_OK, intent)
                    activity?.finish()
                }
                is Fail -> {
                    when (it.throwable) {
                        is SocketTimeoutException -> {
                            setFailedResultData(Detector.DetectionFailedType.BADNETWORK)
                        }
                        else -> {
                            setFailedResultData(Detector.DetectionFailedType.GENERAL)
                        }
                    }
                }
            }
        })
    }

    private fun findViews() {
        activity?.run {
            livenessView = findViewById(R.id.liveness_view)
            tipLottieAnimationView = findViewById(R.id.tip_lottie_animation_view)
            tipTextView = findViewById(R.id.tip_text_view)
            loadingLayout = findViewById(R.id.loading_layout)
            mainLayout = findViewById(R.id.main_layout)
            loader = findViewById(R.id.loader)
            bgOverlay = findViewById(R.id.background_overlay)
            val mBackView = findViewById<View>(R.id.back_view_camera_activity)
            mBackView.setOnClickListener { onBackPressed() }
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(LivenessDetectionComponent::class.java).inject(this)
    }

    private fun changeTipTextView(strResId: Int) {
        tipTextView?.setText(strResId)
    }

    private fun updateTipUIView(warnCode: Detector.WarnCode?) {
        livenessWarnState = warnCode
        if (warnCode != null) {
            when (warnCode) {
                Detector.WarnCode.FACEMISSING -> {
                    analytics.eventViewFaceInCenter()
                    changeTipTextView(R.string.liveness_no_people_face)
                    livenessActionState = null
                }
                Detector.WarnCode.FACESMALL -> {
                    analytics.eventViewCloserFaceToScreen()
                    changeTipTextView(R.string.liveness_tip_move_closer)
                    livenessActionState = null
                }
                Detector.WarnCode.FACELARGE -> {
                    changeTipTextView(R.string.liveness_tip_move_furthre)
                    livenessActionState = null
                }
                Detector.WarnCode.FACENOTCENTER -> {
                    analytics.eventViewFaceInCenter()
                    changeTipTextView(R.string.liveness_move_face_center)
                    livenessActionState = null
                }
                Detector.WarnCode.FACENOTFRONTAL -> {
                    changeTipTextView(R.string.liveness_frontal)
                    livenessActionState = null
                }
                Detector.WarnCode.FACENOTSTILL, Detector.WarnCode.FACECAPTURE -> {
                    changeTipTextView(R.string.liveness_still)
                    livenessActionState = null
                }
                Detector.WarnCode.WARN_MULTIPLEFACES -> {
                    analytics.eventViewMultipleFaces()
                    changeTipTextView(R.string.liveness_failed_reason_multipleface)
                    livenessActionState = null
                }
                Detector.WarnCode.FACEINACTION -> {
                    livenessWarnState = null
                    showActionTipUIView()
                }
                else -> {
                }
            }
        }
    }

    private fun showActionTipUIView() {
        val currentDetectionType = livenessView?.currentDetectionType
        livenessActionState = currentDetectionType
        if (currentDetectionType != null) {
            var detectionNameId = 0
            when (currentDetectionType) {
                Detector.DetectionType.POS_YAW -> {
                    analytics.eventViewHeadDetection()
                    detectionNameId = R.string.liveness_pos_raw
                }
                Detector.DetectionType.MOUTH -> {
                    analytics.eventViewMouthDetection()
                    detectionNameId = R.string.liveness_mouse
                }
                Detector.DetectionType.BLINK -> {
                    analytics.eventViewBlinkDetection()
                    detectionNameId = R.string.liveness_blink
                }
                else -> {
                }
            }
            changeTipTextView(detectionNameId)
            setLottieFile(getLottieFile(currentDetectionType))
        }
    }

    override fun onDetach() {
        release()
        super.onDetach()
    }

    override fun onDetectorInitStart() {
        if (initProgressDialog != null) {
            initProgressDialog?.dismiss()
        }
        initProgressDialog = ProgressDialog(context)
        initProgressDialog?.setMessage(getString(R.string.liveness_auth_check))
        initProgressDialog?.setCanceledOnTouchOutside(false)
        initProgressDialog?.show()
    }

    fun release() {
        if (initProgressDialog != null) {
            initProgressDialog?.dismiss()
        }
        livenessView?.destroy()
    }

    override fun onDetectorInitComplete(isValid: Boolean, errorCode: String,
                                        message: String) {
        if (initProgressDialog != null) {
            initProgressDialog?.dismiss()
        }
        if (isValid) {
            updateTipUIView(null)
        } else {
            val errorMessage: String = if (LivenessView.NO_RESPONSE == errorCode) {
                getString(R.string.liveness_failed_reason_auth_failed)
            } else {
                message
            }
            activity?.run {
                showAlertDialog(errorMessage, this)
            }
        }
    }

    private fun showAlertDialog(message: String, activity: FragmentActivity) {
        val dialog = context?.let { DialogUnify(it, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE) }
        dialog?.setTitle(message)
        dialog?.setDescription(getString(R.string.default_error_unknown))
        dialog?.setOverlayClose(false)
        dialog?.setPrimaryCTAText(getString(R.string.exit))

        dialog?.setPrimaryCTAClickListener {
            activity.setResult(RESULT_OK)
            activity.finish()
            dialog.dismiss()
            Unit
        }
        dialog?.show()
    }

    private fun getLottieFile(detectionType: Detector.DetectionType?): String {
        var lottieFile = ""
        if (detectionType != null) {
            when (detectionType) {
                Detector.DetectionType.POS_YAW -> lottieFile = LivenessConstants.ANIMATION_YAW
                Detector.DetectionType.MOUTH -> lottieFile = LivenessConstants.ANIMATION_MOUTH
                Detector.DetectionType.BLINK -> lottieFile = LivenessConstants.ANIMATION_BLINK
                else -> {
                }
            }
        }
        return lottieFile
    }

    private fun setLottieFile(url: String) {
        tipLottieAnimationView?.cancelAnimation()
        tipLottieAnimationView?.clearAnimation()
        tipLottieAnimationView?.invalidate()

        val lottieCompositionLottieTask = LottieCompositionFactory.fromUrl(requireContext(), url)
        lottieCompositionLottieTask.addListener { result ->
            tipLottieAnimationView?.setComposition(result)
            tipLottieAnimationView?.repeatCount = ValueAnimator.INFINITE
            tipLottieAnimationView?.playAnimation()
        }
    }

    override fun onDetectionActionChanged() {
        bgOverlay?.changeColor()
        onDetectionActionSuccess()
    }

    private fun onDetectionActionSuccess() {
        if (livenessActionState != null) {
            when (livenessActionState) {
                Detector.DetectionType.BLINK -> {
                    analytics.eventSuccessdBlinkDetection()
                }
                Detector.DetectionType.MOUTH -> {
                    analytics.eventSuccessdMouthDetection()
                }
                else -> {
                }
            }
        }
    }

    /**
     * called by local liveness detection success
     */
    override fun onDetectionSuccess() {
        analytics.eventSuccessdHeadDetection()
        livenessView?.getLivenessData(object : LivenessGetFaceDataCallback {

            override fun onGetFaceDataStart() {
                loadingLayout?.visibility = View.VISIBLE
                mainLayout?.visibility = View.GONE
            }

            override fun onGetFaceDataSuccess(entity: BaseResultEntity) {
                // liveness detection success
                setSuccessResultData()
            }

            override fun onGetFaceDataFailed(entity: BaseResultEntity) {
                if (!entity.success && LivenessView.NO_RESPONSE == entity.code) {
                    setFailedResultData(Detector.DetectionFailedType.BADNETWORK)
                }
            }
        })
    }

    private fun setSuccessResultData() {
        activity?.run {
            val mImageBitmap = LivenessResult.livenessBitmap
            facePath = saveToFile(mImageBitmap)
            if (isFileExists(facePath) && isFileExists(ktpPath)) {
                livenessDetectionViewModel.uploadImages(ktpPath, facePath, tkpdProjectId
                        ?: DEFAULT_ID)
            } else {
                setResult(LivenessConstants.KYC_FILE_NOT_FOUND)
                finish()
            }
        }
    }

    private fun setFailedResultData(failedType: Detector.DetectionFailedType) {
        if (activity != null) {
            val intent = Intent(activity, LivenessFailedActivity::class.java)
            when (failedType) {
                Detector.DetectionFailedType.GENERAL -> {
                    intent.putExtra(ARG_FAILED_TYPE, LivenessConstants.FAILED_GENERAL)
                }
                Detector.DetectionFailedType.BADNETWORK -> {
                    intent.putExtra(ARG_FAILED_TYPE, LivenessConstants.FAILED_BADNETWORK)
                }
                Detector.DetectionFailedType.TIMEOUT -> {
                    intent.putExtra(ARG_FAILED_TYPE, LivenessConstants.FAILED_TIMEOUT)
                }
                else -> {
                }
            }
            activity?.startActivityForResult(intent, RESULT_CANCELED)
        }
    }

    private fun saveToFile(mImageBitmap: Bitmap?): String {
        try {
            val cameraResultFile = ImageUtils.writeImageToTkpdPath(ImageUtils
                    .DirectoryDef.DIRECTORY_TOKOPEDIA_CACHE_CAMERA, mImageBitmap, false)
            if (cameraResultFile.exists()) {
                return cameraResultFile.absolutePath
            } else {
                NetworkErrorHelper.showRedSnackbar(activity, resources.getString(R.string.liveness_failed_file_not_found))
            }
        } catch (error: Throwable) {
            NetworkErrorHelper.showRedSnackbar(activity, resources.getString(R.string.liveness_failed_file_not_found))
        }
        return ""
    }

    private fun isFileExists(filePath: String): Boolean {
        val file = File(filePath)
        return file.exists()
    }

    override fun onDetectionFrameStateChanged(warnCode: Detector.WarnCode) {
        if (isAdded) {
            updateTipUIView(warnCode)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onActionRemainingTimeChanged(remainingTimeMills: Long) {
        if (isAdded) {
            (remainingTimeMills / 1000).toInt()
        }
    }

    override fun onDetectionFailed(failedType: Detector.DetectionFailedType, detectionType: Detector.DetectionType) {
        if (isAdded) {
            when (failedType) {
                Detector.DetectionFailedType.TIMEOUT -> setFailedResultData(Detector.DetectionFailedType.TIMEOUT)
                Detector.DetectionFailedType.MUCHMOTION -> setFailedResultData(Detector.DetectionFailedType.MUCHMOTION)
                else -> {
                }
            }
        }
    }

    override fun trackOnBackPressed() {
        if (livenessActionState != null) {
            when (livenessActionState) {
                Detector.DetectionType.MOUTH -> {
                    analytics.eventClickBackMouthDetection()
                }
                Detector.DetectionType.BLINK -> {
                    analytics.eventClickBackBlinkDetection()
                }
                Detector.DetectionType.POS_YAW -> {
                    analytics.eventClickBackHeadDetection()
                }
                else -> {
                }
            }
        }

        if (livenessWarnState != null) {
            when (livenessWarnState) {
                Detector.WarnCode.FACESMALL -> {
                    analytics.eventClickBackCloserFaceToScreen()
                }
                Detector.WarnCode.FACENOTCENTER, Detector.WarnCode.FACEMISSING -> {
                    analytics.eventClickBackFaceInCenter()
                }
                Detector.WarnCode.WARN_MULTIPLEFACES -> {
                    analytics.eventClickBackMultipleFaces()
                }
                else -> {
                }
            }
        }
    }

    companion object {

        fun newInstance(bundle: Bundle): LivenessFragment {
            val fragment = LivenessFragment()
            fragment.arguments = bundle
            return fragment
        }

        const val FACE_RETAKE = 2
        const val ARG_FAILED_TYPE = "failed_type"
        const val DEFAULT_ID = "1"
    }


}
