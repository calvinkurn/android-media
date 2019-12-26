package com.tokopedia.rechargeocr

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.cameraview.CameraListener
import com.tokopedia.cameraview.CameraUtils
import com.tokopedia.cameraview.CameraView
import com.tokopedia.cameraview.PictureResult
import com.tokopedia.imagepicker.common.util.ImageUtils
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import com.tokopedia.rechargeocr.analytics.RechargeCameraAnalytics
import com.tokopedia.rechargeocr.di.RechargeCameraInstance
import com.tokopedia.rechargeocr.viewmodel.RechargeUploadImageViewModel
import com.tokopedia.rechargeocr.widget.FocusCameraView
import com.tokopedia.unifycomponents.Toaster
import java.io.File
import javax.inject.Inject

class RechargeCameraFragment : BaseDaggerFragment() {

    private lateinit var cameraView: CameraView
    private lateinit var fullImagePreview: ImageView
    private lateinit var focusCameraView: FocusCameraView
    private lateinit var closeBtn: ImageView
    private lateinit var title: TextView
    private lateinit var subtitle: TextView
    private lateinit var shutterBtn: FrameLayout
    private lateinit var loading: ProgressBar
    private lateinit var containerCamera: ConstraintLayout
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
        val view = inflater.inflate(R.layout.fragment_recharge_camera, container, false)
        cameraView = view.findViewById(R.id.full_camera_view)
        fullImagePreview = view.findViewById(R.id.full_image_preview)
        focusCameraView = view.findViewById(R.id.focused_camera_view)
        closeBtn = view.findViewById(R.id.close_button)
        title = view.findViewById(R.id.ocr_title)
        subtitle = view.findViewById(R.id.ocr_subtitle)
        shutterBtn = view.findViewById(R.id.image_button_shutter)
        loading = view.findViewById(R.id.progress_bar)
        containerCamera = view.findViewById(R.id.layout_container)
        return view
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
        showCameraView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        uploadImageviewModel.urlImage.observe(this, Observer {
            if (it.isNotEmpty()) {
                uploadImageviewModel.getResultOcr(GraphqlHelper.loadRawString(resources, R.raw.query_recharge_ocr), it)
            } else {
                hideLoading()
                showCameraView()
                Toaster.make(containerCamera, getString(R.string.ocr_error_card_unreadable), Snackbar.LENGTH_SHORT, Toaster.TYPE_ERROR)
            }
        })

        uploadImageviewModel.resultDataOcr.observe(this, Observer { ocrData ->
            hideLoading()
            rechargeCameraAnalytics.scanIdCard("")
            activity?.let {
                val intentReturn = Intent()
                intentReturn.putExtra(EXTRA_NUMBER_FROM_CAMERA_OCR, ocrData)
                it.setResult(Activity.RESULT_OK, intentReturn)
                it.finish()
            }
        })

        uploadImageviewModel.errorActionOcr.observe(this, Observer {
            hideLoading()
            showCameraView()
            rechargeCameraAnalytics.scanIdCard(it)
            Toaster.make(containerCamera, it, Snackbar.LENGTH_SHORT, Toaster.TYPE_ERROR)
        })
    }

    private fun setupInfoCamera() {
        title.text = getString(R.string.ocr_title)
        subtitle.text = getString(R.string.ocr_subtitle)
    }

    private fun populateView() {
        shutterBtn.setOnClickListener{
            getPermissionCamera()
        }

        cameraListener = object : CameraListener() {
            override fun onPictureTaken(result: PictureResult) {
                hideCameraButtonAndShowLoading()
                saveToFile(result.data)
            }
        }

        cameraView.addCameraListener(cameraListener)

        closeBtn.setOnClickListener {
            activity?.let {
                it.setResult(Activity.RESULT_CANCELED)
                it.finish()
            }
        }
    }

    private fun getPermissionCamera() {
        activity?.let {
            permissionCheckerHelper.checkPermission(it,
                    PermissionCheckerHelper.Companion.PERMISSION_CAMERA,
                    object : PermissionCheckerHelper.PermissionCheckListener {
                        override fun onPermissionDenied(permissionText: String) {

                        }

                        override fun onNeverAskAgain(permissionText: String) {

                        }

                        override fun onPermissionGranted() {
                            getPermissionExternalStorage()
                        }
                    }, "")
        }
    }

    private fun getPermissionExternalStorage() {
        activity?.let {
            permissionCheckerHelper.checkPermission(it,
                    PermissionCheckerHelper.Companion.PERMISSION_WRITE_EXTERNAL_STORAGE,
                    object : PermissionCheckerHelper.PermissionCheckListener {
                        override fun onPermissionDenied(permissionText: String) {

                        }

                        override fun onNeverAskAgain(permissionText: String) {

                        }

                        override fun onPermissionGranted() {
                            cameraView.takePicture()
                        }
                    }, "")
        }
    }

    fun saveToFile(imageByte: ByteArray) {
        val mCaptureNativeSize = cameraView.pictureSize
        try {
            //rotate the bitmap using the library
            mCaptureNativeSize?.let {
                CameraUtils.decodeBitmap(imageByte, mCaptureNativeSize.width, mCaptureNativeSize.height) { bitmap ->
                    val cameraResultFile = ImageUtils.writeImageToTkpdPath(ImageUtils
                            .DirectoryDef.DIRECTORY_TOKOPEDIA_CACHE_CAMERA, bitmap, false)
                    onSuccessImageTakenFromCamera(cameraResultFile)
                }
            }
        } catch (error: Throwable) {
            val cameraResultFile = ImageUtils.writeImageToTkpdPath(ImageUtils.DirectoryDef
                    .DIRECTORY_TOKOPEDIA_CACHE_CAMERA, imageByte, false)
            onSuccessImageTakenFromCamera(cameraResultFile)
        }
    }

    private fun onSuccessImageTakenFromCamera(cameraResultFile: File) {
        if (cameraResultFile.exists()) {
            ImageHandler.loadImageFromFile(context, fullImagePreview, cameraResultFile)
            imagePath = cameraResultFile.absolutePath
            showImagePreview()
            uploadImageviewModel.uploadImageRecharge(imagePath)
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
        loading.visibility = View.GONE
    }

    private fun showCameraView() {
        cameraView.visibility = View.VISIBLE
        shutterBtn.visibility = View.VISIBLE
        startCamera()
        fullImagePreview.visibility = View.GONE
    }

    private fun hideCameraButtonAndShowLoading() {
        loading.visibility = View.VISIBLE
        shutterBtn.visibility = View.GONE
        fullImagePreview.visibility = View.GONE
    }

    private fun showImagePreview() {
        cameraView.visibility = View.GONE
        shutterBtn.visibility = View.GONE
        destroyCamera()
        fullImagePreview.visibility = View.VISIBLE
    }

    private fun startCamera() {
        try {
            cameraView.clearCameraListeners()
            if (::cameraListener.isInitialized) {
                cameraView.addCameraListener(cameraListener)
            }
            cameraView.open()
        } catch (e: Throwable) {
            // no-op
        }
    }

    private fun destroyCamera() {
        try {
            cameraView.close()
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

        fun newInstance(): Fragment {
            return RechargeCameraFragment()
        }
    }

}