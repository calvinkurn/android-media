package com.tokopedia.rechargeocr

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraUtils
import com.otaliastudios.cameraview.PictureResult
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.rechargeocr.analytics.RechargeCameraAnalytics
import com.tokopedia.rechargeocr.di.RechargeCameraInstance
import com.tokopedia.rechargeocr.util.RechargeOcrGqlQuery
import com.tokopedia.rechargeocr.viewmodel.RechargeUploadImageViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.image.ImageProcessingUtil
import com.tokopedia.utils.permission.PermissionCheckerHelper
import kotlinx.android.synthetic.main.fragment_recharge_camera.*
import java.io.File
import javax.inject.Inject

class RechargeCameraFragment : BaseDaggerFragment() {

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
        return inflater.inflate(R.layout.fragment_recharge_camera, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
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

        uploadImageviewModel.resultDataOcr.observe(viewLifecycleOwner, Observer { ocrData ->
            hideLoading()
            rechargeCameraAnalytics.scanIdCard(VALUE_TRACKING_OCR_SUCCESS)
            activity?.let {
                val intentReturn = Intent()
                intentReturn.putExtra(EXTRA_NUMBER_FROM_CAMERA_OCR, ocrData)
                it.setResult(Activity.RESULT_OK, intentReturn)
                it.finish()
            }
        })

        uploadImageviewModel.errorActionOcr.observe(viewLifecycleOwner, Observer {
            hideLoading()
            showCameraView()
            rechargeCameraAnalytics.scanIdCard(it)
            Toaster.build(layout_container, it, Snackbar.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
        })
    }

    private fun setupInfoCamera() {
        ocr_title.text = getString(R.string.ocr_title)
        ocr_subtitle.text = getString(R.string.ocr_subtitle)
    }

    private fun populateView() {
        image_button_shutter.setOnClickListener {
            getPermissionCamera()
        }

        cameraListener = object : CameraListener() {
            override fun onPictureTaken(result: PictureResult) {
                hideCameraButtonAndShowLoading()
                saveToFile(result.data)
            }
        }

        full_camera_view.addCameraListener(cameraListener)

        close_button.setOnClickListener {
            activity?.let {
                it.setResult(Activity.RESULT_CANCELED)
                it.finish()
            }
        }
    }

    private fun getPermissionCamera() {
        activity?.let {
            permissionCheckerHelper.checkPermissions(it, arrayOf(
                    PermissionCheckerHelper.Companion.PERMISSION_CAMERA,
                    PermissionCheckerHelper.Companion.PERMISSION_WRITE_EXTERNAL_STORAGE),
                    object : PermissionCheckerHelper.PermissionCheckListener {
                        override fun onPermissionDenied(permissionText: String) {
                            permissionCheckerHelper.onPermissionDenied(it, permissionText)
                        }

                        override fun onNeverAskAgain(permissionText: String) {
                            permissionCheckerHelper.onNeverAskAgain(it, permissionText)
                        }

                        override fun onPermissionGranted() {
                            full_camera_view.takePicture()
                        }
                    }, "")
        }
    }

    fun saveToFile(imageByte: ByteArray) {
        val mCaptureNativeSize = full_camera_view.pictureSize
        try {
            //rotate the bitmap using the library
            mCaptureNativeSize?.let {
                CameraUtils.decodeBitmap(imageByte, mCaptureNativeSize.width, mCaptureNativeSize.height) { bitmap ->
                    if (bitmap != null) {
                        val cameraResultFile = ImageProcessingUtil.writeImageToTkpdPath(bitmap, Bitmap.CompressFormat.JPEG)
                        if (cameraResultFile!= null) {
                            onSuccessImageTakenFromCamera(cameraResultFile)
                        }
                    }
                }
            }
        } catch (error: Throwable) {
            val cameraResultFile = ImageProcessingUtil.writeImageToTkpdPath(imageByte, Bitmap.CompressFormat.JPEG)
            if (cameraResultFile!= null) {
                onSuccessImageTakenFromCamera(cameraResultFile)
            }
        }
    }

    private fun onSuccessImageTakenFromCamera(cameraResultFile: File) {
        if (cameraResultFile.exists()) {
            ImageHandler.loadImageFromFile(context, full_image_preview, cameraResultFile)
            imagePath = cameraResultFile.absolutePath
            showImagePreview()
            uploadImageviewModel.uploadImageRecharge(imagePath,
                    RechargeOcrGqlQuery.rechargeCameraRecognition)
        } else {
            Toast.makeText(context, getString(R.string.ocr_default_error_message), Toast
                    .LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()
        showCameraView()
    }

    private fun hideLoading() {
        progress_bar.visibility = View.GONE
    }

    private fun showCameraView() {
        image_button_shutter.visibility = View.VISIBLE
        startCamera()
        full_image_preview.visibility = View.GONE
        full_camera_view.visibility = View.VISIBLE
    }

    private fun hideCameraButtonAndShowLoading() {
        progress_bar.visibility = View.VISIBLE
        image_button_shutter.visibility = View.GONE
        full_image_preview.visibility = View.GONE
    }

    private fun showImagePreview() {
        full_image_preview.visibility = View.VISIBLE
        full_camera_view.visibility = View.GONE
        full_camera_view.close()
        image_button_shutter.visibility = View.GONE
    }

    private fun startCamera() {
        try {
            full_camera_view.clearCameraListeners()
            if (::cameraListener.isInitialized) {
                full_camera_view.addCameraListener(cameraListener)
            }
            openCamera()
        } catch (e: Throwable) {
            // no-op
        }
    }

    private fun openCamera() {
        activity?.let {
            permissionCheckerHelper.checkPermissions(it, arrayOf(
                    PermissionCheckerHelper.Companion.PERMISSION_CAMERA,
                    PermissionCheckerHelper.Companion.PERMISSION_RECORD_AUDIO),
                    object : PermissionCheckerHelper.PermissionCheckListener {
                        override fun onPermissionDenied(permissionText: String) {
                            permissionCheckerHelper.onPermissionDenied(it, permissionText)
                        }

                        override fun onNeverAskAgain(permissionText: String) {
                            permissionCheckerHelper.onNeverAskAgain(it, permissionText)
                        }

                        override fun onPermissionGranted() {
                            full_camera_view.open()
                        }
                    }, "")
        }
    }

    override fun onPause() {
        super.onPause()
        try {
            full_camera_view.close()
        } catch (e: Throwable) {
            // no-op
        }
    }

    private fun destroyCamera() {
        try {
            full_camera_view.close()
            full_camera_view.destroy()
        } catch (e: Throwable) {
            // no-op
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyCamera()
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

        //TODO get this data from DigitalExtraParam
        private const val EXTRA_NUMBER_FROM_CAMERA_OCR = "EXTRA_NUMBER_FROM_CAMERA_OCR"

        private const val VALUE_TRACKING_OCR_SUCCESS = "success"

        fun newInstance(): Fragment {
            return RechargeCameraFragment()
        }
    }

}