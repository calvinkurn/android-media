package com.tokopedia.kyc_centralized.view.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.otaliastudios.cameraview.*
import com.otaliastudios.cameraview.size.Size
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kyc_centralized.R
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.UnifyImageButton
import com.tokopedia.user_identification_common.KYCConstant
import com.tokopedia.user_identification_common.analytics.UserIdentificationCommonAnalytics
import com.tokopedia.utils.image.ImageProcessingUtil
import com.tokopedia.utils.permission.PermissionCheckerHelper
import com.tokopedia.utils.permission.PermissionCheckerHelper.PermissionCheckListener
import java.io.File

/**
 * @author by alvinatin on 12/11/18.
 */
class UserIdentificationCameraFragment : TkpdBaseV4Fragment() {
    private var container: View? = null
    private var cameraView: CameraView? = null
    private var closeButton: UnifyImageButton? = null
    private var title: TextView? = null
    private var subtitle: TextView? = null
    private var focusedFaceView: View? = null
    private var focusedKtpView: View? = null
    private var shutterButton: View? = null
    private var loading: View? = null
    private var switchCamera: View? = null
    private var imagePreview: ImageUnify? = null
    private var buttonLayout: View? = null
    private var reCaptureButton: View? = null
    private var nextButton: UnifyButton? = null
    private var imagePath: String = ""
    private var mCaptureNativeSize: Size? = null
    private var analytics: UserIdentificationCommonAnalytics? = null
    private var permissionCheckerHelper: PermissionCheckerHelper? = null
    private var viewMode = 0
    private var cameraListener: CameraListener? = null

    override fun getScreenName(): String = ""

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachActivity(activity)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionCheckerHelper = PermissionCheckerHelper()
        if (arguments != null) {
            viewMode = arguments?.getInt(ARG_VIEW_MODE, 1) ?: 1
        }
        analytics = UserIdentificationCommonAnalytics.createInstance(activity?.intent?.getIntExtra(ApplinkConstInternalGlobal.PARAM_PROJECT_ID, 1)
                ?: 1)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_camera_focus_view, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View) {
        container = view.findViewById(R.id.container)
        cameraView = view.findViewById(R.id.full_camera_view)
        closeButton = view.findViewById(R.id.close_button)
        title = view.findViewById(R.id.title)
        subtitle = view.findViewById(R.id.subtitle)
        imagePreview = view.findViewById(R.id.full_image_preview)
        focusedFaceView = view.findViewById(R.id.focused_view_face)
        focusedKtpView = view.findViewById(R.id.focused_view_ktp)
        shutterButton = view.findViewById(R.id.image_button_shutter)
        switchCamera = view.findViewById(R.id.image_button_flip)
        buttonLayout = view.findViewById(R.id.button_layout)
        reCaptureButton = view.findViewById(R.id.recapture_button)
        nextButton = view.findViewById(R.id.next_button)
        loading = view.findViewById(R.id.progress_bar)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateView()
    }

    private fun populateView() {
        container?.setBackgroundResource(com.tokopedia.unifyprinciples.R.color.Unify_N700)
        shutterButton?.setOnClickListener { v: View? ->
            sendAnalyticClickShutter()
            val fragment: Fragment = this@UserIdentificationCameraFragment
            permissionCheckerHelper?.checkPermission(fragment,
                    PermissionCheckerHelper.Companion.PERMISSION_CAMERA, object : PermissionCheckListener {
                override fun onPermissionDenied(permissionText: String) {}
                override fun onNeverAskAgain(permissionText: String) {}
                override fun onPermissionGranted() {
                    hideCameraButtonAndShowLoading()
                    cameraView?.takePicture()
                }
            }, "")
        }
        switchCamera?.setOnClickListener { v: View? ->
            sendAnalyticClickFlipCamera()
            toggleCamera()
        }
        reCaptureButton?.setOnClickListener { v: View? ->
            sendAnalyticClickRecapture()
            showCameraView()
        }
        cameraListener = object : CameraListener() {

            override fun onPictureTaken(result: PictureResult) {
                val fragment: Fragment = this@UserIdentificationCameraFragment
                permissionCheckerHelper?.checkPermission(fragment,
                        PermissionCheckerHelper.Companion.PERMISSION_WRITE_EXTERNAL_STORAGE,
                        object : PermissionCheckListener {
                            override fun onPermissionDenied(permissionText: String) {}
                            override fun onNeverAskAgain(permissionText: String) {}
                            override fun onPermissionGranted() {
                                saveToFile(result.data)
                            }
                        }, "")
            }
        }
        cameraListener?.let { cameraView?.addCameraListener(it) }
        closeButton?.setOnClickListener { v: View? ->
            if (activity != null) {
                activity?.setResult(Activity.RESULT_CANCELED)
            }
            if (isCameraVisible) {
                sendAnalyticClickBackCamera()
            } else {
                sendAnalyticClickCloseImagePreview()
            }
            activity?.finish()
        }
        nextButton?.setOnClickListener {
            if (activity != null) {
                if (isFileExists(imagePath)) {
                    if (isFileSizeQualified(imagePath)) {
                        val intent = Intent()
                        intent.putExtra(KYCConstant.EXTRA_STRING_IMAGE_RESULT, imagePath)
                        activity?.setResult(Activity.RESULT_OK, intent)
                    } else {
                        activity?.setResult(KYCConstant.IS_FILE_IMAGE_TOO_BIG)
                    }
                } else {
                    activity?.setResult(KYCConstant.IS_FILE_IMAGE_NOT_EXIST)
                }
            }
            sendAnalyticClickNext()
            activity?.finish()
        }
        populateViewByViewMode()
        showCameraView()
        sendAnalyticOpenCamera()
    }

    private fun sendAnalyticOpenCamera() {
        when (viewMode) {
            PARAM_VIEW_MODE_KTP -> analytics?.eventViewOpenCameraKtp()
            PARAM_VIEW_MODE_FACE -> analytics?.eventViewOpenCameraSelfie()
            else -> {
            }
        }
    }

    private fun sendAnalyticClickBackCamera() {
        when (viewMode) {
            PARAM_VIEW_MODE_KTP -> analytics?.eventClickBackCameraKtp()
            PARAM_VIEW_MODE_FACE -> analytics?.eventClickBackCameraSelfie()
            else -> {
            }
        }
    }

    private fun sendAnalyticClickShutter() {
        when (viewMode) {
            PARAM_VIEW_MODE_KTP -> analytics?.eventClickShutterCameraKtp()
            PARAM_VIEW_MODE_FACE -> analytics?.eventClickShutterCameraSelfie()
            else -> {
            }
        }
    }

    private fun sendAnalyticClickFlipCamera() {
        when (viewMode) {
            PARAM_VIEW_MODE_KTP -> analytics?.eventClickFlipCameraKtp()
            PARAM_VIEW_MODE_FACE -> analytics?.eventClickFlipCameraSelfie()
            else -> {
            }
        }
    }

    private fun sendAnalyticViewImagePreview() {
        when (viewMode) {
            PARAM_VIEW_MODE_KTP -> analytics?.eventViewImagePreviewKtp()
            PARAM_VIEW_MODE_FACE -> analytics?.eventViewImagePreviewSelfie()
            else -> {
            }
        }
    }

    private fun sendAnalyticClickCloseImagePreview() {
        when (viewMode) {
            PARAM_VIEW_MODE_KTP -> analytics?.eventClickCloseImagePreviewKtp()
            PARAM_VIEW_MODE_FACE -> analytics?.eventClickCloseImagePreviewSelfie()
            else -> {
            }
        }
    }

    private fun sendAnalyticClickRecapture() {
        when (viewMode) {
            PARAM_VIEW_MODE_KTP -> analytics?.eventClickRecaptureKtp()
            PARAM_VIEW_MODE_FACE -> analytics?.eventClickRecaptureSelfie()
            else -> {
            }
        }
    }

    private fun sendAnalyticClickNext() {
        when (viewMode) {
            PARAM_VIEW_MODE_KTP -> analytics?.eventClickNextImagePreviewKtp()
            PARAM_VIEW_MODE_FACE -> analytics?.eventClickNextImagePreviewSelfie()
            else -> {
            }
        }
    }

    override fun onPause() {
        super.onPause()
        destroyCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyCamera()
    }

    override fun onResume() {
        super.onResume()
        showCameraView()
    }

    private fun startCamera() {
        try {
            cameraView?.clearCameraListeners()
            cameraListener?.let { cameraView?.addCameraListener(it) }
            cameraView?.open()
        } catch (e: Throwable) {
            // no-op
        }
    }

    private fun destroyCamera() {
        try {
            cameraView?.close()
        } catch (e: Throwable) {
            // no-op
        }
    }

    fun saveToFile(imageByte: ByteArray) {
        mCaptureNativeSize = cameraView?.pictureSize
        try {
            //rotate the bitmap using the library
            CameraUtils.decodeBitmap(imageByte, mCaptureNativeSize?.width
                    ?: 0, mCaptureNativeSize?.height ?: 0) { bitmap: Bitmap? ->
                if (bitmap != null) {
                    val cameraResultFile = ImageProcessingUtil.writeImageToTkpdPath(bitmap, Bitmap.CompressFormat.JPEG)
                    if (cameraResultFile!= null) {
                        onSuccessImageTakenFromCamera(cameraResultFile)
                    }
                }
            }
        } catch (error: Throwable) {
            val cameraResultFile = ImageProcessingUtil.writeImageToTkpdPath(imageByte, Bitmap.CompressFormat.JPEG)
            if (cameraResultFile != null) {
                onSuccessImageTakenFromCamera(cameraResultFile)
            }
        }
    }

    private fun onSuccessImageTakenFromCamera(cameraResultFile: File) {
        if (cameraResultFile.exists()) {
            ImageHandler.loadImageFromFile(context, imagePreview, cameraResultFile)
            imagePath = cameraResultFile.absolutePath
            showImagePreview()
        } else {
            Toast.makeText(context, getString(R.string.error_upload_image_kyc), Toast.LENGTH_LONG).show()
        }
    }

    private fun populateViewByViewMode() {
        when (viewMode) {
            PARAM_VIEW_MODE_KTP -> {
                focusedKtpView?.visibility = View.VISIBLE
                focusedFaceView?.visibility = View.GONE
                title?.setText(R.string.camera_ktp_title)
                subtitle?.setText(R.string.camera_ktp_subtitle)
            }
            PARAM_VIEW_MODE_FACE -> {
                focusedKtpView?.visibility = View.GONE
                focusedFaceView?.visibility = View.VISIBLE
                title?.setText(R.string.camera_face_title)
                subtitle?.setText(R.string.camera_face_subtitle)
                toggleCamera()
            }
        }
    }

    private fun showCameraView() {
        cameraView?.visibility = View.VISIBLE
        shutterButton?.visibility = View.VISIBLE
        switchCamera?.visibility = View.VISIBLE
        startCamera()
        loading?.visibility = View.GONE
        imagePreview?.visibility = View.GONE
        buttonLayout?.visibility = View.GONE
    }

    private fun showImagePreview() {
        cameraView?.visibility = View.GONE
        shutterButton?.visibility = View.GONE
        switchCamera?.visibility = View.GONE
        loading?.visibility = View.GONE
        destroyCamera()
        imagePreview?.visibility = View.VISIBLE
        buttonLayout?.visibility = View.VISIBLE
        sendAnalyticViewImagePreview()
    }

    private fun hideCameraButtonAndShowLoading() {
        shutterButton?.visibility = View.GONE
        switchCamera?.visibility = View.GONE
        imagePreview?.visibility = View.GONE
        buttonLayout?.visibility = View.GONE
        loading?.visibility = View.VISIBLE
    }

    private fun toggleCamera() {
        if (isCameraVisible) {
            cameraView?.toggleFacing()
        }
    }

    private fun isFileSizeQualified(filePath: String): Boolean {
        val file = File(filePath)
        val fileSize = (file.length() / DEFAULT_ONE_MEGABYTE).toString().toInt()
        return fileSize <= MAX_FILE_SIZE
    }

    private fun isFileExists(filePath: String): Boolean {
        val file = File(filePath)
        return file.exists()
    }

    private val isCameraVisible: Boolean
        get() = cameraView != null &&
                cameraView?.visibility == View.VISIBLE

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (context != null) {
            permissionCheckerHelper?.onRequestPermissionsResult(context, requestCode, permissions,
                    grantResults)
        }
    }

    companion object {
        const val ARG_VIEW_MODE = "view_mode"
        const val PARAM_VIEW_MODE_KTP = 1
        const val PARAM_VIEW_MODE_FACE = 2
        private const val DEFAULT_ONE_MEGABYTE: Long = 1024
        private const val MAX_FILE_SIZE = 15360
        fun createInstance(viewMode: Int): Fragment {
            val fragment = UserIdentificationCameraFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_VIEW_MODE, viewMode)
            fragment.arguments = bundle
            return fragment
        }
    }
}