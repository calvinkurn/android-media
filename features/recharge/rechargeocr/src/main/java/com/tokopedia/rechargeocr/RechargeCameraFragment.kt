package com.tokopedia.rechargeocr

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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraUtils
import com.otaliastudios.cameraview.PictureResult
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.rechargeocr.analytics.RechargeCameraAnalytics
import com.tokopedia.rechargeocr.databinding.FragmentRechargeCameraBinding
import com.tokopedia.rechargeocr.di.RechargeCameraInstance
import com.tokopedia.rechargeocr.util.RechargeOcrGqlQuery
import com.tokopedia.rechargeocr.viewmodel.RechargeUploadImageViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.image.ImageProcessingUtil
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.permission.PermissionCheckerHelper
import java.io.File
import javax.inject.Inject

class RechargeCameraFragment : BaseDaggerFragment() {

    private var binding by autoClearedNullable<FragmentRechargeCameraBinding>()

    private lateinit var cameraListener: CameraListener
    private lateinit var uploadImageviewModel: RechargeUploadImageViewModel

    private var imagePath: String = ""

    @Inject
    lateinit var permissionCheckerHelper: PermissionCheckerHelper

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var rechargeCameraAnalytics: RechargeCameraAnalytics

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRechargeCameraBinding.inflate(LayoutInflater.from(context), container, false)
        return binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            val viewModelProvider = ViewModelProvider(it, viewModelFactory)
            uploadImageviewModel = viewModelProvider.get(RechargeUploadImageViewModel::class.java)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupInfoCamera()
        populateView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        uploadImageviewModel.resultDataOcr.observe(
            viewLifecycleOwner,
            Observer { ocrData ->
                when (ocrData) {
                    is Success -> {
                        hideLoading()
                        rechargeCameraAnalytics.scanIdCard(VALUE_TRACKING_OCR_SUCCESS)
                        activity?.let {
                            val intentReturn = Intent()
                            intentReturn.putExtra(EXTRA_NUMBER_FROM_CAMERA_OCR, ocrData.data)
                            it.setResult(Activity.RESULT_OK, intentReturn)
                            it.finish()
                        }
                    }

                    is Fail -> {
                        hideLoading()
                        showCameraView()
                        val throwableMessage = ErrorHandler.getErrorMessage(requireContext(), ocrData.throwable)
                        rechargeCameraAnalytics.scanIdCard(throwableMessage)
                        binding?.let {
                            Toaster.build(it.layoutContainer, throwableMessage, Snackbar.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
                        }
                    }
                }
            }
        )
    }

    private fun setupInfoCamera() {
        binding?.run {
            ocrTitle.text = getString(R.string.ocr_title)
            ocrSubtitle.text = getString(R.string.ocr_subtitle)
        }
    }

    private fun populateView() {
        binding?.run {
            imageButtonShutter.setOnClickListener {
                getPermissionCamera()
            }
            cameraListener = object : CameraListener() {
                override fun onPictureTaken(result: PictureResult) {
                    hideCameraButtonAndShowLoading()
                    saveToFile(result.data)
                }
            }
            fullCameraView.addCameraListener(cameraListener)
            closeButton.setOnClickListener {
                activity?.let {
                    it.setResult(Activity.RESULT_CANCELED)
                    it.finish()
                }
            }
        }
    }

    private fun getPermissionCamera() {
        activity?.let {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                permissionCheckerHelper.checkPermissions(
                    it,
                    arrayOf(
                        PermissionCheckerHelper.Companion.PERMISSION_CAMERA,
                        PermissionCheckerHelper.Companion.PERMISSION_WRITE_EXTERNAL_STORAGE
                    ),
                    object : PermissionCheckerHelper.PermissionCheckListener {
                        override fun onPermissionDenied(permissionText: String) {
                            permissionCheckerHelper.onPermissionDenied(it, permissionText)
                        }

                        override fun onNeverAskAgain(permissionText: String) {
                            permissionCheckerHelper.onNeverAskAgain(it, permissionText)
                        }

                        override fun onPermissionGranted() {
                            takePicture()
                        }
                    },
                    ""
                )
            } else {
                takePicture()
            }
        }
    }

    private fun takePicture() {
        binding?.fullCameraView?.takePicture()
    }

    fun saveToFile(imageByte: ByteArray) {
        val mCaptureNativeSize = binding?.fullCameraView?.pictureSize
        try {
            // rotate the bitmap using the library
            mCaptureNativeSize?.let {
                CameraUtils.decodeBitmap(imageByte, mCaptureNativeSize.width, mCaptureNativeSize.height) { bitmap ->
                    if (bitmap != null) {
                        binding?.fullImagePreview?.setImageBitmap(bitmap)
                        val cameraResultFile = ImageProcessingUtil.writeImageToTkpdPath(bitmap, Bitmap.CompressFormat.JPEG)
                        if (cameraResultFile != null) {
                            onSuccessImageTakenFromCamera(cameraResultFile)
                        }
                    }
                }
            }
        } catch (error: Throwable) {
            val cameraResultFile = ImageProcessingUtil.writeImageToTkpdPath(imageByte, Bitmap.CompressFormat.JPEG)
            if (cameraResultFile != null) {
                onSuccessImageTakenFromCamera(cameraResultFile)
                if (cameraResultFile.exists()) {
                    binding?.fullImagePreview?.let {
                        ImageHandler.loadImageFromFile(context, it, cameraResultFile)
                    }
                }
            }
        }
    }

    private fun onSuccessImageTakenFromCamera(cameraResultFile: File) {
        if (cameraResultFile.exists()) {
            imagePath = cameraResultFile.absolutePath
            showImagePreview()
            uploadImageviewModel.uploadImageRecharge(
                imagePath,
                RechargeOcrGqlQuery.rechargeCameraRecognition
            )
        } else {
            val throwableMessage = MessageErrorException(getString(R.string.ocr_default_error_message))
            Toast.makeText(
                context,
                ErrorHandler.getErrorMessage(requireContext(), throwableMessage),
                Toast
                    .LENGTH_LONG
            ).show()
        }
    }

    override fun onResume() {
        super.onResume()
        showCameraView()
        startCamera()
    }

    private fun hideLoading() {
        binding?.progressBar?.visibility = View.GONE
    }

    private fun showCameraView() {
        binding?.run {
            imageButtonShutter.visibility = View.VISIBLE
            fullImagePreview.visibility = View.GONE
            fullCameraView.visibility = View.VISIBLE
        }
    }

    private fun hideCameraButtonAndShowLoading() {
        binding?.run {
            progressBar.visibility = View.VISIBLE
            imageButtonShutter.visibility = View.GONE
            fullImagePreview.visibility = View.GONE
        }
    }

    private fun showImagePreview() {
        binding?.run {
            fullImagePreview.visibility = View.VISIBLE
            fullCameraView.visibility = View.GONE
            imageButtonShutter.visibility = View.GONE
        }
    }

    private fun startCamera() {
        try {
            binding?.fullCameraView?.clearCameraListeners()
            if (::cameraListener.isInitialized) {
                binding?.fullCameraView?.addCameraListener(cameraListener)
            }
            openCamera()
        } catch (e: Throwable) {
            // no-op
        }
    }

    private fun openCamera() {
        activity?.let {
            permissionCheckerHelper.checkPermissions(
                it,
                arrayOf(
                    PermissionCheckerHelper.Companion.PERMISSION_CAMERA,
                    PermissionCheckerHelper.Companion.PERMISSION_RECORD_AUDIO
                ),
                object : PermissionCheckerHelper.PermissionCheckListener {
                    override fun onPermissionDenied(permissionText: String) {
                        permissionCheckerHelper.onPermissionDenied(it, permissionText)
                    }

                    override fun onNeverAskAgain(permissionText: String) {
                        permissionCheckerHelper.onNeverAskAgain(it, permissionText)
                    }

                    override fun onPermissionGranted() {
                        binding?.fullCameraView?.open()
                    }
                },
                ""
            )
        }
    }

    override fun onPause() {
        super.onPause()
        try {
            binding?.fullCameraView?.close()
        } catch (e: Throwable) {
            // no-op
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            binding?.fullCameraView?.destroy()
        } catch (e: Throwable) {
            // no-op
        }
    }

    override fun getScreenName(): String {
        return RechargeCameraFragment::javaClass.name
    }

    override fun initInjector() {
        activity?.let {
            val rechargeCameraComponent = RechargeCameraInstance.getComponent(it.application)
            rechargeCameraComponent.inject(this)
        }
    }

    companion object {

        // TODO get this data from DigitalExtraParam
        private const val EXTRA_NUMBER_FROM_CAMERA_OCR = "EXTRA_NUMBER_FROM_CAMERA_OCR"

        private const val VALUE_TRACKING_OCR_SUCCESS = "success"

        fun newInstance(): Fragment {
            return RechargeCameraFragment()
        }
    }
}
