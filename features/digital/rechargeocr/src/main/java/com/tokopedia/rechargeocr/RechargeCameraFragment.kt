package com.tokopedia.rechargeocr

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.cameraview.CameraListener
import com.tokopedia.cameraview.CameraUtils
import com.tokopedia.cameraview.CameraView
import com.tokopedia.cameraview.PictureResult
import com.tokopedia.imagepicker.common.util.ImageUtils
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import com.tokopedia.rechargeocr.data.RechargeUploadImageData
import com.tokopedia.rechargeocr.data.RechargeUploadImageResponse
import com.tokopedia.rechargeocr.di.RechargeCameraInstance
import com.tokopedia.rechargeocr.viewmodel.RechargeUploadImageViewModel
import com.tokopedia.rechargeocr.widget.FocusCameraView
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
    private lateinit var cameraListener: CameraListener
    private lateinit var recaptureBtn: RelativeLayout
    private lateinit var uploadImageviewModel: RechargeUploadImageViewModel

    private var imagePath: String = ""

    @Inject
    lateinit var permissionCheckerHelper: PermissionCheckerHelper
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

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
        recaptureBtn = view.findViewById(R.id.recapture_button)
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

    private fun setupInfoCamera() {
        title.text = getString(R.string.ocr_title)
        subtitle.text = getString(R.string.ocr_subtitle)
    }

    private fun populateView() {
        shutterBtn.setOnClickListener(View.OnClickListener {
            activity?.let {
                permissionCheckerHelper.checkPermission(it,
                        PermissionCheckerHelper.Companion.PERMISSION_CAMERA,
                        object : PermissionCheckerHelper.PermissionCheckListener {
                            override fun onPermissionDenied(permissionText: String) {

                            }

                            override fun onNeverAskAgain(permissionText: String) {

                            }

                            override fun onPermissionGranted() {
                                hideCameraButtonAndShowLoading()
                                cameraView.takePicture()
                            }
                        }, "")
            }
        })

        cameraListener = object : CameraListener() {

            override fun onPictureTaken(result: PictureResult) {
                activity?.let {
                    permissionCheckerHelper.checkPermission(it,
                            PermissionCheckerHelper.Companion.PERMISSION_WRITE_EXTERNAL_STORAGE,
                            object : PermissionCheckerHelper.PermissionCheckListener {
                                override fun onPermissionDenied(permissionText: String) {

                                }

                                override fun onNeverAskAgain(permissionText: String) {

                                }

                                override fun onPermissionGranted() {
                                    saveToFile(result.data)
                                }
                            }, "")
                }
            }
        }

        cameraView.addCameraListener(cameraListener)

        recaptureBtn.setOnClickListener {
            showCameraView()
        }

        closeBtn.setOnClickListener {
            activity?.let {
                it.setResult(Activity.RESULT_CANCELED)
                it.finish()
            }
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
            //TODO hit to backend to get url
            uploadImageviewModel.uploadImageRecharge(imagePath, this::onSuccessUploadImage,
                    this::onErrorUploadImage)
        } else {
            Toast.makeText(context, "Terjadi kesalahan, silahkan coba lagi", Toast
                    .LENGTH_LONG).show()
        }
    }

    private fun onSuccessUploadImage(rechargeUploadImageData: RechargeUploadImageData) {
        Toast.makeText(activity, rechargeUploadImageData.picSrc, Toast.LENGTH_SHORT).show()
    }

    private fun onErrorUploadImage(throwable: Throwable) {
        Toast.makeText(activity, throwable.message, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        showCameraView()
    }

    private fun showCameraView() {
        cameraView.visibility = View.VISIBLE
        shutterBtn.visibility = View.VISIBLE
        startCamera()
        loading.visibility = View.GONE
        fullImagePreview.visibility = View.GONE
        recaptureBtn.visibility = View.GONE
    }

    private fun hideCameraButtonAndShowLoading() {
        shutterBtn.visibility = View.GONE
        fullImagePreview.visibility = View.GONE
        recaptureBtn.visibility = View.GONE
        loading.visibility = View.VISIBLE
    }

    private fun showImagePreview() {
        cameraView.visibility = View.GONE
        shutterBtn.visibility = View.GONE
        loading.visibility = View.GONE
        destroyCamera()
        fullImagePreview.visibility = View.VISIBLE
        recaptureBtn.visibility = View.VISIBLE
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

        fun newInstance(): Fragment {
            return RechargeCameraFragment()
        }
    }

}