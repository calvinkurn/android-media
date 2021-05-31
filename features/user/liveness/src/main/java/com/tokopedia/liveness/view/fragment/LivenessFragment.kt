package com.tokopedia.liveness.view.fragment

import ai.advance.common.entity.BaseResultEntity
import ai.advance.liveness.lib.Detector
import ai.advance.liveness.lib.LivenessResult
import ai.advance.liveness.lib.LivenessView
import ai.advance.liveness.lib.impl.LivenessCallback
import ai.advance.liveness.lib.impl.LivenessGetFaceDataCallback
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieCompositionFactory
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.liveness.R
import com.tokopedia.liveness.analytics.LivenessDetectionAnalytics
import com.tokopedia.liveness.di.LivenessDetectionComponent
import com.tokopedia.liveness.utils.LivenessConstants
import com.tokopedia.liveness.view.BackgroundOverlay
import com.tokopedia.liveness.view.OnBackListener
import com.tokopedia.liveness.view.activity.LivenessActivity
import com.tokopedia.liveness.view.activity.LivenessFailedActivity
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.utils.file.FileUtil
import com.tokopedia.utils.image.ImageProcessingUtil
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class LivenessFragment : BaseDaggerFragment(), Detector.DetectorInitCallback, LivenessCallback, OnBackListener {

    private var livenessView: LivenessView? = null
    private var tipLottieAnimationView: LottieAnimationView? = null
    private var tipTextView: TextView? = null
    private var initProgressDialog: ProgressDialog? = null
    private var mainLayout: View? = null
    private var bgOverlay: BackgroundOverlay? = null
    private var livenessWarnState: Detector.WarnCode? = null
    private var livenessActionState: Detector.DetectionType? = null
    private var facePath: String = ""

    @Inject
    lateinit var analytics: LivenessDetectionAnalytics

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_liveness, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        findViews()
        livenessView?.startDetection(this)
    }

    private fun onSuccessLiveness() {
        val intent = Intent()
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_FACE_PATH, facePath)
        activity?.setResult(RESULT_OK, intent)
        activity?.finish()
    }

    private fun findViews() {
        activity?.run {
            livenessView = findViewById(R.id.liveness_view)
            tipLottieAnimationView = findViewById(R.id.tip_lottie_animation_view)
            tipTextView = findViewById(R.id.tip_text_view)
            mainLayout = findViewById(R.id.main_layout)
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
        if(livenessActionState==null){
            tipTextView?.setText(strResId)
        }
    }

    private fun updateTipUIView(warnCode: Detector.WarnCode?) {
        livenessWarnState = warnCode
        if (warnCode != null) {
            when (warnCode) {
                Detector.WarnCode.FACEMISSING -> {
                    analytics.eventViewFaceInCenter()
                    changeTipTextView(R.string.liveness_no_people_face)
                }
                Detector.WarnCode.FACESMALL -> {
                    analytics.eventViewCloserFaceToScreen()
                    changeTipTextView(R.string.liveness_tip_move_closer)
                }
                Detector.WarnCode.FACELARGE -> {
                    changeTipTextView(R.string.liveness_tip_move_furthre)
                }
                Detector.WarnCode.FACENOTCENTER -> {
                    analytics.eventViewFaceInCenter()
                    changeTipTextView(R.string.liveness_move_face_center)
                }
                Detector.WarnCode.FACENOTFRONTAL -> {
                    changeTipTextView(R.string.liveness_frontal)
                }
                Detector.WarnCode.FACENOTSTILL, Detector.WarnCode.FACECAPTURE -> {
                    changeTipTextView(R.string.liveness_still)
                }
                Detector.WarnCode.WARN_MULTIPLEFACES -> {
                    analytics.eventViewMultipleFaces()
                    changeTipTextView(R.string.liveness_failed_reason_multipleface)
                }
                Detector.WarnCode.FACEINACTION -> {
                    livenessWarnState = null
                    showActionTipUIView()
                }
                else -> {}
            }
        }
    }

    private fun showActionTipUIView() {
        val currentDetectionType = livenessView?.currentDetectionType
        if (currentDetectionType != null && livenessActionState != currentDetectionType) {
            livenessActionState = currentDetectionType
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
                else -> {}
            }
            tipTextView?.setText(detectionNameId)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setLottieFile(getLottieFile(currentDetectionType))
            }
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
            activity?.run {
                showAlertDialog()
            }
        }
    }

    private fun showAlertDialog() {
        (activity as LivenessActivity).showNotSupportedDialog()
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

    override fun onDetectionSuccess() {
        analytics.eventSuccessdHeadDetection()
        livenessView?.getLivenessData(object : LivenessGetFaceDataCallback {

            override fun onGetFaceDataStart() {}

            override fun onGetFaceDataSuccess(entity: BaseResultEntity) {
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
            if(!isFileExists(facePath)) {
                setResult(LivenessConstants.KYC_LIVENESS_FILE_NOT_FOUND)
                finish()
            } else {
                onSuccessLiveness()
            }
        }
    }

    private fun setFailedResultData(failedType: Detector.DetectionFailedType) {
        if (activity != null) {
            val intent = Intent(activity, LivenessFailedActivity::class.java)
            when (failedType) {
                Detector.DetectionFailedType.BADNETWORK -> {
                    intent.putExtra(LivenessConstants.ARG_FAILED_TYPE, LivenessConstants.FAILED_BADNETWORK)
                }
                Detector.DetectionFailedType.TIMEOUT -> {
                    intent.putExtra(LivenessConstants.ARG_FAILED_TYPE, LivenessConstants.FAILED_TIMEOUT)
                }
                else -> {
                }
            }
            activity?.startActivityForResult(intent, RESULT_CANCELED)
        }
    }

    private fun saveToFile(mImageBitmap: Bitmap?): String {
        try {
            if(mImageBitmap != null) {
                val cameraResultFile = writeImageToTkpdPath(mImageBitmap)
                if (cameraResultFile.exists()) {
                    return cameraResultFile.absolutePath
                } else {
                    ServerLogger.log(Priority.P2, "LIVENESS_IMAGE_ERROR", mapOf("type" to "FailedImageFileNotFound", "absolutePath" to cameraResultFile.absolutePath))
                }
            } else {
                ServerLogger.log(Priority.P2, "LIVENESS_IMAGE_ERROR", mapOf("type" to "FailedImageNull"))
            }
        } catch (error: Throwable) {
            ServerLogger.log(Priority.P2, "LIVENESS_IMAGE_ERROR", mapOf("type" to "TryCatchSaveToFile", "stack_trace" to "$error"))
        }
        return ""
    }

    private fun isFileExists(filePath: String): Boolean {
        val file = File(filePath)
        return file.exists()
    }

    private fun writeImageToTkpdPath(bitmap: Bitmap): File {
        val cacheDir = File(context?.externalCacheDir, FileUtil.generateUniqueFileName() + ImageProcessingUtil.JPG_EXT)
        val cachePath = cacheDir.absolutePath
        val file = File(cachePath)
        if (file.exists()) {
            file.delete()
        }
        try {
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
        } catch (e: Throwable) {
            e.printStackTrace()
            ServerLogger.log(Priority.P2, "LIVENESS_IMAGE_ERROR",
                    mapOf("type" to "TryCatchWriteImageToTkpdPath", "cacheDir" to "$cacheDir", "cachePath" to cachePath, "fileExists" to "${file.exists()}", "stack_trace" to "$e"))
        }
        return file
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
        } else if (livenessActionState != null) {
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
    }

    companion object {
        fun newInstance(bundle: Bundle): LivenessFragment {
            val fragment = LivenessFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}