package ai.advance.liveness.fragment

import ai.advance.common.entity.BaseResultEntity
import ai.advance.liveness.BackgroundOverlay
import ai.advance.liveness.LivenessConstants
import ai.advance.liveness.OnBackListener
import ai.advance.liveness.R
import ai.advance.liveness.activity.LivenessFailedActivity
import ai.advance.liveness.analytics.LivenessDetectionAnalytics
import ai.advance.liveness.lib.Detector
import ai.advance.liveness.lib.LivenessResult
import ai.advance.liveness.lib.LivenessView
import ai.advance.liveness.lib.impl.LivenessCallback
import ai.advance.liveness.lib.impl.LivenessGetFaceDataCallback
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieCompositionFactory
import com.tokopedia.imagepicker.common.util.ImageUtils
import java.io.File

class LivenessFragment : Fragment(), Detector.DetectorInitCallback, LivenessCallback, OnBackListener {

    private var mLivenessView: LivenessView? = null
    private var mTipLottieAnimationView: LottieAnimationView? = null
    private var mTipTextView: TextView? = null
    private var mProgressLayout: View? = null
    private var mInitProgressDialog: ProgressDialog? = null
    private var bgOverlay: BackgroundOverlay? = null

    private var livenessWarnState: Detector.WarnCode? = null
    private var livenessActionState: Detector.DetectionType? = null

    private var projectId = -1
    private lateinit var analytics: LivenessDetectionAnalytics

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_liveness, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        analytics = LivenessDetectionAnalytics().createInstance(projectId)
        findViews()
        initData()
    }

    private fun initData() {
        mLivenessView?.startDetection(this)
    }

    private fun findViews() {
        val activity = activity
        if (activity != null) {
            mLivenessView = activity.findViewById(R.id.liveness_view)
            mTipLottieAnimationView = activity.findViewById(R.id.tip_lottie_animation_view)
            mTipTextView = activity.findViewById(R.id.tip_text_view)
            mProgressLayout = activity.findViewById(R.id.progress_layout)
            bgOverlay = activity.findViewById(R.id.background_overlay)
            val mBackView = activity.findViewById<View>(R.id.back_view_camera_activity)
            mBackView.setOnClickListener { activity.onBackPressed() }
        }
    }

    private fun changeTipTextView(strResId: Int) {
        mTipTextView?.setText(strResId)
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
//                    showActionTipUIView()
                }
                else -> {}
            }
        }
    }

    private fun showActionTipUIView() {
        val currentDetectionType = mLivenessView?.currentDetectionType
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
                else -> {}
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
        if (mInitProgressDialog != null) {
            mInitProgressDialog?.dismiss()
        }
        mInitProgressDialog = ProgressDialog(context)
        mInitProgressDialog?.setMessage(getString(R.string.liveness_auth_check))
        mInitProgressDialog?.setCanceledOnTouchOutside(false)
        mInitProgressDialog?.show()
    }

    fun release() {
        if (mInitProgressDialog != null) {
            mInitProgressDialog?.dismiss()
        }
        mLivenessView?.destroy()
    }

    override fun onDetectorInitComplete(isValid: Boolean, errorCode: String,
                                        message: String) {
        if (mInitProgressDialog != null) {
            mInitProgressDialog?.dismiss()
        }
        if (isValid) {
            updateTipUIView(null)
        } else {
            val errorMessage: String = if (LivenessView.NO_RESPONSE == errorCode) {
                getString(R.string.liveness_failed_reason_auth_failed)
            } else {
                message
            }
            val activity = activity
            if (activity != null) {
                AlertDialog.Builder(activity).setMessage(errorMessage).setPositiveButton(R.string.liveness_perform) { dialog, _ ->
                    LivenessResult.errorMsg = errorMessage
                    val fragmentActivity = getActivity()
                    dialog.dismiss()
                    if (fragmentActivity != null) {
                        fragmentActivity.setResult(RESULT_OK)
                        fragmentActivity.finish()
                    }
                }.create().show()
            }
        }
    }

    private fun getLottieFile(detectionType: Detector.DetectionType?): String {
        var lottieFile = ""
        if (detectionType != null) {
            when (detectionType) {
                Detector.DetectionType.POS_YAW -> lottieFile = "https://ecs7.tokopedia.net/lottie/turning_the_head_to_right_and_left.json"
                Detector.DetectionType.MOUTH -> lottieFile = "https://ecs7.tokopedia.net/lottie/mouth_open_and_close.json"
                Detector.DetectionType.BLINK -> lottieFile = "https://ecs7.tokopedia.net/lottie/blink_interaction.json"
                else -> {}
            }
        }
        return lottieFile
    }

    private fun setLottieFile(url: String) {
        mTipLottieAnimationView?.cancelAnimation()
        mTipLottieAnimationView?.clearAnimation()
        mTipLottieAnimationView?.invalidate()

        val lottieCompositionLottieTask = LottieCompositionFactory.fromUrl(requireContext(), url)
        lottieCompositionLottieTask.addListener { result ->
            mTipLottieAnimationView?.setComposition(result)
            mTipLottieAnimationView?.repeatCount = ValueAnimator.INFINITE
            mTipLottieAnimationView?.playAnimation()
        }
    }

    override fun onDetectionActionChanged() {
        bgOverlay?.changeColor()
        onDetectionActionSuccess()
        showActionTipUIView()
    }

    private fun onDetectionActionSuccess(){
        if(livenessActionState != null){
            when (livenessActionState) {
                Detector.DetectionType.BLINK -> {
                    analytics.eventSuccessdBlinkDetection()
                }
                Detector.DetectionType.MOUTH -> {
                    analytics.eventSuccessdMouthDetection()
                }
                else -> {}
            }
        }
    }

    /**
     * called by local liveness detection success
     * 活体检测成功时会执行该方法
     */
    override fun onDetectionSuccess() {
        analytics.eventSuccessdHeadDetection()
        mLivenessView?.getLivenessData(object : LivenessGetFaceDataCallback {

            override fun onGetFaceDataStart() {
                mProgressLayout?.visibility = View.VISIBLE
                mLivenessView?.visibility = View.GONE
                mTipLottieAnimationView?.visibility = View.GONE
                mTipTextView?.visibility = View.GONE
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
        val activity = activity
        if (activity != null) {
            val mImageBitmap = LivenessResult.livenessBitmap
            val imagePath = saveToFile(mImageBitmap)
            if (isFileExists(imagePath)) {
                val intent = Intent()
                intent.putExtra("image_result", imagePath)
                activity.setResult(RESULT_OK, intent)
            } else {
                activity.setResult(-7)
            }
            activity.finish()
        }
    }

    private fun setFailedResultData(failedType: Detector.DetectionFailedType) {
        if (activity != null) {
            val intent = Intent(activity, LivenessFailedActivity::class.java)
            when (failedType) {
                Detector.DetectionFailedType.BADNETWORK -> {
                    intent.putExtra("failed_type", LivenessConstants.FAILED_BADNETWORK)
                }
                Detector.DetectionFailedType.TIMEOUT -> {
                    intent.putExtra("failed_type", LivenessConstants.FAILED_TIMEOUT)
                }
                else -> {}
            }
            activity?.startActivityForResult(intent, 0)
        }
    }

    private fun saveToFile(mImageBitmap: Bitmap?): String {
        try {
            val cameraResultFile = ImageUtils.writeImageToTkpdPath(ImageUtils
                    .DirectoryDef.DIRECTORY_TOKOPEDIA_CACHE_CAMERA, mImageBitmap, false)
            if (cameraResultFile.exists()) {
                return cameraResultFile.absolutePath
            } else {
                Toast.makeText(context, "Terjadi kesalahan, silahkan coba lagi", Toast
                        .LENGTH_LONG).show()
            }
        } catch (error: Throwable) {
            Toast.makeText(context, "Terjadi kesalahan, silahkan coba lagi", Toast
                    .LENGTH_LONG).show()
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
                Detector.DetectionFailedType.WEAKLIGHT -> {
                    //changeTipTextView(R.string.liveness_weak_light);
                }
                Detector.DetectionFailedType.STRONGLIGHT -> {
                    //changeTipTextView(R.string.liveness_too_light);
                }
                else -> when (failedType) {
                    Detector.DetectionFailedType.TIMEOUT -> setFailedResultData(Detector.DetectionFailedType.TIMEOUT)
                    Detector.DetectionFailedType.MUCHMOTION -> setFailedResultData(Detector.DetectionFailedType.MUCHMOTION)
                    else -> {}
                }
            }
        }
    }

    override fun trackOnBackPressed() {
        if(livenessActionState != null){
            when(livenessActionState){
                Detector.DetectionType.MOUTH -> {
                    analytics.eventClickBackMouthDetection()
                }
                Detector.DetectionType.BLINK -> {
                    analytics.eventClickBackBlinkDetection()
                }
                Detector.DetectionType.POS_YAW -> {
                    analytics.eventClickBackHeadDetection()
                }
                else -> {}
            }
        }

        if(livenessWarnState != null){
            when(livenessWarnState){
                Detector.WarnCode.FACESMALL -> {
                    analytics.eventClickBackCloserFaceToScreen()
                }
                Detector.WarnCode.FACENOTCENTER, Detector.WarnCode.FACEMISSING -> {
                    analytics.eventClickBackFaceInCenter()
                }
                Detector.WarnCode.WARN_MULTIPLEFACES -> {
                    analytics.eventClickBackMultipleFaces()
                }
                else -> {}
            }
        }
    }

    companion object {

        fun newInstance(): LivenessFragment {
            return LivenessFragment()
        }
    }


}
