package com.tokopedia.kyc_centralized.view.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.size.Size
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.PARAM_PROJECT_ID
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.analytics.UserIdentificationCommonAnalytics
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.kyc_centralized.common.KYCConstant.LIVENESS_TAG
import com.tokopedia.kyc_centralized.databinding.FragmentCameraFocusViewBinding
import com.tokopedia.kyc_centralized.view.activity.UserIdentificationFormActivity.Companion.FILE_NAME_KYC
import com.tokopedia.media.loader.loadImage
import com.tokopedia.utils.file.FileUtil
import com.tokopedia.utils.image.ImageProcessingUtil
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.permission.PermissionCheckerHelper
import com.tokopedia.utils.permission.request
import timber.log.Timber
import java.io.File

/**
 * @author by alvinatin on 12/11/18.
 */
class UserIdentificationCameraFragment : BaseDaggerFragment() {
    private var viewBinding by autoClearedNullable<FragmentCameraFocusViewBinding>()
    private var imagePath: String = ""
    private var mCaptureNativeSize: Size? = null
    private var analytics: UserIdentificationCommonAnalytics? = null
    private val permissionCheckerHelper = PermissionCheckerHelper()
    private var viewMode = 0
    private var cameraListener: CameraListener? = null

    override fun getScreenName(): String = ""
    override fun initInjector() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            viewMode = arguments?.getInt(ARG_VIEW_MODE, 1) ?: 1
        }
        analytics = UserIdentificationCommonAnalytics
            .createInstance(
                activity?.intent?.getIntExtra(PARAM_PROJECT_ID, 1) ?: 1
            )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentCameraFocusViewBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
        populateView()
    }

    private fun populateView() {
        viewBinding?.imageButtonShutter?.setOnClickListener {
            sendAnalyticClickShutter()
            checkPermission {
                hideCameraButtonAndShowLoading()
                viewBinding?.fullCameraView?.takePicture()
            }
        }
        viewBinding?.imageButtonFlip?.setOnClickListener {
            sendAnalyticClickFlipCamera()
            toggleCamera()
        }
        viewBinding?.recaptureButton?.setOnClickListener {
            sendAnalyticClickRecapture()
            showCameraView()
        }
        cameraListener = object : CameraListener() {
            override fun onPictureTaken(result: PictureResult) {
                checkPermission {
                    saveToFile(result)
                }
            }
        }
        cameraListener?.let { viewBinding?.fullCameraView?.addCameraListener(it) }
        viewBinding?.closeButton?.setOnClickListener { v: View? ->
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
        viewBinding?.nextButton?.setOnClickListener {
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

    private fun checkPermission(isGranted: () -> Unit = {}) {
        val listPermission = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            arrayOf(
                PermissionCheckerHelper.Companion.PERMISSION_CAMERA,
                PermissionCheckerHelper.Companion.PERMISSION_WRITE_EXTERNAL_STORAGE
            )
        } else {
            arrayOf(PermissionCheckerHelper.Companion.PERMISSION_CAMERA)
        }

        activity?.let {
            permissionCheckerHelper.request(it, listPermission) {
                isGranted.invoke()
            }
        }
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
            viewBinding?.fullCameraView?.clearCameraListeners()
            cameraListener?.let { viewBinding?.fullCameraView?.addCameraListener(it) }
            viewBinding?.fullCameraView?.open()
        } catch (e: Throwable) {
            // no-op
        }
    }

    private fun destroyCamera() {
        try {
            viewBinding?.fullCameraView?.close()
        } catch (e: Throwable) {
            // no-op
        }
    }

    fun saveToFile(result: PictureResult) {
        mCaptureNativeSize = viewBinding?.fullCameraView?.pictureSize
        val cameraResultFile =
            ImageProcessingUtil.getTokopediaPhotoPath(Bitmap.CompressFormat.JPEG, FILE_NAME_KYC)
        result.toFile(cameraResultFile) {
            if (it != null && it.exists()) {
                onSuccessImageTakenFromCamera(cameraResultFile)
            }
        }
    }

    private fun onSuccessImageTakenFromCamera(cameraResultFile: File) {
        if (cameraResultFile.exists()) {
            if (viewMode == PARAM_VIEW_MODE_KTP) {
                viewBinding?.imagePreviewKtp?.loadImage(cameraResultFile.absolutePath)
                viewBinding?.imagePreviewFace?.hide()
            } else if (viewMode == PARAM_VIEW_MODE_FACE) {
                viewBinding?.imagePreviewFace?.loadImage(cameraResultFile.absolutePath)
                viewBinding?.imagePreviewKtp?.hide()
            }
            imagePath = cameraResultFile.absolutePath
            Timber.d(
                "$LIVENESS_TAG: Successfully took an image. path: %s, size: %s",
                imagePath.substringAfterLast("/"),
                FileUtil.getFileSizeInKb(cameraResultFile)
            )
            showImagePreview()
        } else {
            Toast.makeText(context, getString(R.string.error_upload_image_kyc), Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun populateViewByViewMode() {
        when (viewMode) {
            PARAM_VIEW_MODE_KTP -> {
                viewBinding?.focusedViewKtp?.visibility = View.VISIBLE
                viewBinding?.focusedViewFace?.visibility = View.GONE
                viewBinding?.title?.setText(R.string.camera_ktp_title)
                viewBinding?.subtitle?.setText(R.string.camera_ktp_subtitle)
            }
            PARAM_VIEW_MODE_FACE -> {
                viewBinding?.focusedViewKtp?.visibility = View.GONE
                viewBinding?.focusedViewFace?.visibility = View.VISIBLE
                viewBinding?.title?.setText(R.string.camera_face_title)
                viewBinding?.subtitle?.setText(R.string.camera_face_subtitle)
                toggleCamera()
            }
        }
    }

    private fun showCameraView() {
        populateViewByViewMode()

        viewBinding?.fullCameraView?.visibility = View.VISIBLE
        viewBinding?.imageButtonShutter?.visibility = View.VISIBLE
        viewBinding?.imageButtonFlip?.visibility = View.VISIBLE
        startCamera()
        viewBinding?.loader?.visibility = View.GONE
        viewBinding?.imagePreviewKtp?.visibility = View.GONE
        viewBinding?.imagePreviewFace?.visibility = View.GONE
        viewBinding?.buttonLayout?.visibility = View.GONE
    }

    private fun showImagePreview() {
        when (viewMode) {
            PARAM_VIEW_MODE_KTP -> {
                viewBinding?.subtitle?.setText(R.string.camera_ktp_subtitle_preview)
                viewBinding?.imagePreviewKtp?.visibility = View.VISIBLE
                viewBinding?.imagePreviewFace?.visibility = View.GONE
            }
            PARAM_VIEW_MODE_FACE -> {
                viewBinding?.subtitle?.setText(R.string.camera_face_subtitle_preview)
                viewBinding?.imagePreviewKtp?.visibility = View.GONE
                viewBinding?.imagePreviewFace?.visibility = View.VISIBLE
            }
        }
        viewBinding?.fullCameraView?.visibility = View.GONE
        viewBinding?.imageButtonShutter?.visibility = View.GONE
        viewBinding?.imageButtonFlip?.visibility = View.GONE
        viewBinding?.loader?.visibility = View.GONE
        viewBinding?.focusedViewKtp?.visibility = View.GONE
        viewBinding?.focusedViewFace?.visibility = View.GONE
        destroyCamera()
        viewBinding?.buttonLayout?.visibility = View.VISIBLE
        sendAnalyticViewImagePreview()
    }

    private fun hideCameraButtonAndShowLoading() {
        viewBinding?.imageButtonShutter?.visibility = View.GONE
        viewBinding?.imageButtonFlip?.visibility = View.GONE
        viewBinding?.imagePreviewKtp?.visibility = View.GONE
        viewBinding?.imagePreviewFace?.visibility = View.GONE
        viewBinding?.buttonLayout?.visibility = View.GONE
        viewBinding?.loader?.visibility = View.VISIBLE
    }

    private fun toggleCamera() {
        if (isCameraVisible) {
            viewBinding?.fullCameraView?.toggleFacing()
        }
    }

    private fun isFileSizeQualified(filePath: String): Boolean {
        val file = File(filePath)
        val fileSize = (file.length() / DEFAULT_ONE_MEGABYTE).toString().toIntOrZero()
        return fileSize <= MAX_FILE_SIZE
    }

    private fun isFileExists(filePath: String): Boolean {
        val file = File(filePath)
        return file.exists()
    }

    private val isCameraVisible: Boolean
        get() = viewBinding?.fullCameraView != null &&
            viewBinding?.fullCameraView?.visibility == View.VISIBLE

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (context != null) {
            permissionCheckerHelper.onRequestPermissionsResult(
                context,
                requestCode,
                permissions,
                grantResults
            )
        }
    }

    companion object {
        const val ARG_VIEW_MODE = "view_mode"
        const val PARAM_VIEW_MODE_KTP = 1
        const val PARAM_VIEW_MODE_FACE = 2
        private const val DEFAULT_ONE_MEGABYTE: Long = 1024
        private const val MAX_FILE_SIZE = 15360
        fun createInstance(viewMode: Int, projectId: Int): Fragment {
            val fragment = UserIdentificationCameraFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_VIEW_MODE, viewMode)
            bundle.putInt(PARAM_PROJECT_ID, projectId)
            fragment.arguments = bundle
            return fragment
        }
    }
}
