package com.tokopedia.liveness.view

import ai.advance.liveness.lib.Detector
import ai.advance.liveness.lib.Detector.WarnCode.*
import ai.advance.liveness.lib.LivenessResult
import ai.advance.liveness.lib.LivenessView
import ai.advance.liveness.lib.http.entity.ResultEntity
import ai.advance.liveness.lib.impl.LivenessCallback
import ai.advance.liveness.lib.impl.LivenessGetFaceDataCallback
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.lottie.LottieCompositionFactory
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.PARAM_PROJECT_ID
import com.tokopedia.liveness.R
import com.tokopedia.liveness.analytics.LivenessDetectionAnalytics
import com.tokopedia.liveness.databinding.FragmentRevampLivenessBinding
import com.tokopedia.liveness.di.LivenessDetectionComponent
import com.tokopedia.liveness.utils.LivenessConstants
import com.tokopedia.liveness.utils.LivenessDetectionLogTracker
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.utils.file.FileUtil
import com.tokopedia.utils.image.ImageProcessingUtil
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class LivenessFragment : BaseDaggerFragment(),
    OnBackListener,
    LivenessCallback,
    LivenessGetFaceDataCallback {

    @Inject
    lateinit var analytics: LivenessDetectionAnalytics

    private var viewBinding by autoClearedNullable<FragmentRevampLivenessBinding>()

    private var livenessActionState: Detector.DetectionType? = null
    private var livenessWarnState: Detector.WarnCode? = null
    private var loaderDialog: LoaderDialog? = null

    private var projectId = ""

    override fun getScreenName(): String = "Livenesss"

    override fun initInjector() {
        getComponent(LivenessDetectionComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            projectId = it.getString(PARAM_PROJECT_ID).orEmpty()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewBinding = FragmentRevampLivenessBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    @SuppressLint("DeprecatedMethod")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.livenessView?.setLivenssCallback(this)

        viewBinding?.buttonBack?.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    @SuppressLint("DeprecatedMethod")
    override fun onResume() {
        super.onResume()
        viewBinding?.livenessView?.onResume()
    }

    override fun onDetectorInitStart() {
        if (loaderDialog != null) {
            loaderDialog?.dialog?.dismiss()
        }

        loaderDialog = context?.let { LoaderDialog(it) }
        loaderDialog?.apply {
            setLoadingText(getString(R.string.liveness_auth_check))
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
        }?.show()
    }

    override fun onDetectorInitComplete(isValid: Boolean, errorCode: String?, message: String?) {
        if (loaderDialog != null) {
            loaderDialog?.dialog?.dismiss()
        }

        if (!isValid) {
            alertDialogDeviceNotSupported()

            LivenessDetectionLogTracker.sendLog(
                LivenessDetectionLogTracker.LogType.LIBRARY,
                this::class.simpleName.orEmpty(),
                throwable = Throwable("[$errorCode] $message")
            )
        } else {
            updateTipUIView(null)
        }
    }

    override fun onDetectionSuccess() {
        sendTrackerDetectionSuccess(livenessActionState)
        viewBinding?.livenessView?.getLivenessData(this)
    }

    override fun onDetectionFrameStateChanged(warnCode: Detector.WarnCode?) {
        if (isAdded) {
            updateTipUIView(warnCode)
        }
    }

    override fun onActionRemainingTimeChanged(remainingTimeMills: Long) {}

    override fun onDetectionFailed(
        detectionFailedType: Detector.DetectionFailedType?,
        detectionType: Detector.DetectionType?,
    ) {
        LivenessDetectionLogTracker.sendLog(
            LivenessDetectionLogTracker.LogType.FAILED,
            this::class.simpleName.orEmpty(),
            detectionFailedType = detectionFailedType,
            detectionType = detectionType
        )

        if (isAdded && detectionFailedType != null) {
            setFailedResultData(detectionFailedType)
        }
    }

    override fun onDetectionActionChanged() {
        viewBinding?.backgroundOverlay?.changeColor()
        sendTrackerDetectionSuccess(livenessActionState)
    }

    /*
    * WARNING!!!
    * when this code created, tokopedia only use 3 DetectionType,
    * there are:
    * 1. Detector.DetectionType.BLINK
    * 2. Detector.DetectionType.MOUTH
    * 3. Detector.DetectionType.POS_YAW
    *
    * if in the future, there is a change in the rules, we must add it at the code logic
    * */
    @Suppress("NON_EXHAUSTIVE_WHEN")
    private fun sendTrackerDetectionSuccess(type: Detector.DetectionType?) {
        when(type) {
            Detector.DetectionType.BLINK -> {
                analytics.eventSuccessBlinkDetection(projectId, true)
            }
            Detector.DetectionType.MOUTH -> {
                analytics.eventSuccessMouthDetection(projectId, true)
            }
            Detector.DetectionType.POS_YAW -> {
                analytics.eventSuccessHeadDetection(projectId, true)
            }
        }
    }

    private fun updateTipUIView(warnCode: Detector.WarnCode?) {
        livenessWarnState = warnCode
        if (warnCode != null) {
            when (warnCode) {
                FACEMISSING -> {
                    analytics.eventViewFaceInCenter(projectId,
                        false,
                        getString(R.string.liveness_no_people_face))
                    changeTipTextView(R.string.liveness_no_people_face)
                }
                FACESMALL -> {
                    analytics.eventViewCloserFaceToScreen(projectId,
                        false,
                        getString(R.string.liveness_tip_move_closer))
                    changeTipTextView(R.string.liveness_tip_move_closer)
                }
                FACELARGE -> {
                    changeTipTextView(R.string.liveness_tip_move_furthre)
                }
                FACENOTCENTER -> {
                    analytics.eventViewFaceInCenter(projectId,
                        false,
                        getString(R.string.liveness_move_face_center))
                    changeTipTextView(R.string.liveness_move_face_center)
                }
                FACENOTFRONTAL -> {
                    changeTipTextView(R.string.liveness_frontal)
                }
                FACENOTSTILL, FACECAPTURE -> {
                    changeTipTextView(R.string.liveness_still)
                }
                WARN_MULTIPLEFACES -> {
                    analytics.eventViewMultipleFaces(projectId,
                        false,
                        getString(R.string.liveness_failed_reason_multipleface))
                    changeTipTextView(R.string.liveness_failed_reason_multipleface)
                }
                FACEINACTION -> {
                    livenessWarnState = null
                    showActionTipUIView()
                }
                else -> {}
            }
        }
    }

    @SuppressLint("DeprecatedMethod")
    private fun showActionTipUIView() {
        val currentDetectionType = viewBinding?.livenessView?.currentDetectionType
        if (currentDetectionType != null && livenessActionState != currentDetectionType) {
            livenessActionState = currentDetectionType
            var detectionNameId = 0
            when (currentDetectionType) {
                Detector.DetectionType.POS_YAW -> {
                    analytics.eventViewHeadDetection(projectId, true)
                    detectionNameId = R.string.liveness_pos_raw
                }
                Detector.DetectionType.MOUTH -> {
                    analytics.eventViewMouthDetection(projectId, true)
                    detectionNameId = R.string.liveness_mouse
                }
                Detector.DetectionType.BLINK -> {
                    analytics.eventViewBlinkDetection(projectId, true)
                    detectionNameId = R.string.liveness_blink
                }
                else -> {}
            }
            viewBinding?.textTipView?.setText(detectionNameId)
            setLottieFile(getLottieFile(currentDetectionType))
        }
    }

    private fun changeTipTextView(strResId: Int) {
        if (livenessActionState == null) {
            viewBinding?.textTipView?.setText(strResId)
        }
    }

    private fun alertDialogDeviceNotSupported() {
        activity?.let {
            (it as LivenessActivity).alertDialogDeviceNotSupported()
        }
    }

    private fun getLottieFile(detectionType: Detector.DetectionType?): String {
        var lottieFile = ""
        if (detectionType != null) {
            when (detectionType) {
                Detector.DetectionType.POS_YAW -> lottieFile = LivenessConstants.ANIMATION_YAW
                Detector.DetectionType.MOUTH -> lottieFile = LivenessConstants.ANIMATION_MOUTH
                Detector.DetectionType.BLINK -> lottieFile = LivenessConstants.ANIMATION_BLINK
                else -> {}
            }
        }
        return lottieFile
    }

    private fun setLottieFile(url: String) {
        viewBinding?.lottieLivenessTips?.apply {
            cancelAnimation()
            clearAnimation()
            invalidate()

            val lottieCompositionLottieTask =
                LottieCompositionFactory.fromUrl(requireContext(), url)
            lottieCompositionLottieTask.addListener { result ->
                setComposition(result)
            }
            repeatCount = ValueAnimator.INFINITE
            playAnimation()
        }
    }

    override fun trackOnBackPressed() {
        if (livenessWarnState != null) {
            when (livenessWarnState) {
                FACESMALL -> {
                    analytics.eventClickBackCloserFaceToScreen(projectId)
                }
                FACENOTCENTER, FACEMISSING -> {
                    analytics.eventClickBackFaceInCenter(projectId)
                }
                WARN_MULTIPLEFACES -> {
                    analytics.eventClickBackMultipleFaces(projectId)
                }
                else -> {}
            }
        } else if (livenessActionState != null) {
            when (livenessActionState) {
                Detector.DetectionType.MOUTH -> {
                    analytics.eventClickBackMouthDetection(projectId)
                }
                Detector.DetectionType.BLINK -> {
                    analytics.eventClickBackBlinkDetection(projectId)
                }
                Detector.DetectionType.POS_YAW -> {
                    analytics.eventClickBackHeadDetection(projectId)
                }
                else -> {}
            }
        }
    }

    override fun onGetFaceDataStart() {}

    @SuppressLint("DeprecatedMethod")
    override fun onGetFaceDataSuccess(resultEntity: ResultEntity?) {
        try {
            activity?.let {
                val bitmap = LivenessResult.getLivenessBitmap()
                val cameraResultFile = writeImageToTkpdPath(bitmap)
                if (cameraResultFile.exists()) {
                    setResult(true, cameraResultFile)
                } else {
                    LivenessDetectionLogTracker.sendLogImageProcess(
                        LivenessDetectionLogTracker.ImageFailedProcessType.FailedImageFileNotFound,
                        Throwable("FailedImageFileNotFound")
                    )
                    setResult(false)
                }
            }
        } catch (e: Exception) {
            LivenessDetectionLogTracker.sendLogImageProcess(
                LivenessDetectionLogTracker.ImageFailedProcessType.TryCatchSaveToFile,
                e
            )
        }
    }

    override fun onGetFaceDataFailed(resultEntity: ResultEntity?) {
        if (resultEntity?.success == false && resultEntity.code == LivenessView.NO_RESPONSE) {
            LivenessDetectionLogTracker.sendLog(
                LivenessDetectionLogTracker.LogType.LIBRARY,
                this::class.simpleName.orEmpty(),
                message = "[${resultEntity.code}] ${resultEntity.message}",
                throwable = resultEntity.exception
            )

            setFailedResultData(Detector.DetectionFailedType.TIMEOUT)
        }
    }

    private fun setResult(isSuccess: Boolean, file: File? = null) {
        activity?.apply {
            if (isSuccess && file != null && file.exists()) {

                LivenessDetectionLogTracker.sendLog(
                    LivenessDetectionLogTracker.LogType.SUCCESS,
                    this::class.simpleName.orEmpty()
                )

                setResult(Activity.RESULT_OK, Intent().apply {
                    putExtra(ApplinkConstInternalGlobal.PARAM_FACE_PATH, file.absolutePath)
                })
                finish()
            } else {
                setResult(LivenessConstants.KYC_LIVENESS_FILE_NOT_FOUND)
                finish()
            }
        }
    }

    private fun setFailedResultData(detectionFailedType: Detector.DetectionFailedType) {
        val bundle = Bundle()
        bundle.putSerializable(LivenessConstants.ARG_FAILED_TYPE, detectionFailedType)
        bundle.putString(PARAM_PROJECT_ID, projectId)
        activity?.let {
            val fragment = LivenessErrorFragment.newInstance(bundle)
            (it as LivenessActivity).replaceFragment(fragment)
        }
    }

    override fun onDetach() {
        super.onDetach()
        if (loaderDialog != null && loaderDialog?.dialog?.isShowing == true) {
            loaderDialog?.dialog?.dismiss()
        }
    }

    private fun getTkpdCacheDir(): File {
        val cacheDir = File(context?.externalCacheDir, FILE_NAME_KYC)
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }
        return cacheDir
    }

    private fun writeImageToTkpdPath(bitmap: Bitmap): File {
        val cacheDir = getTkpdCacheDir()
        val fileName = FileUtil.generateUniqueFileName() + ImageProcessingUtil.JPG_EXT
        val cacheFile = File(cacheDir, fileName)
        val cachePath = cacheFile.absolutePath
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
            LivenessDetectionLogTracker.sendLogImageProcess(
                LivenessDetectionLogTracker.ImageFailedProcessType.TryCatchWriteImageToTkpdPath,
                e,
                cachePath,
                file
            )
        }

        return file
    }

    @SuppressLint("DeprecatedMethod")
    override fun onDestroy() {
        viewBinding?.livenessView?.onDestroy()
        super.onDestroy()
    }

    companion object {
        private const val FILE_NAME_KYC = "/KYC"

        fun newInstance(bundle: Bundle): LivenessFragment {
            val fragment = LivenessFragment().apply {
                arguments = bundle
            }
            return fragment
        }
    }

}
