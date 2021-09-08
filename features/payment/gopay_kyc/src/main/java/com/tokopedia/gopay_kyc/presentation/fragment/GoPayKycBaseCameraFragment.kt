package com.tokopedia.gopay_kyc.presentation.fragment

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.FrameLayout
import androidx.constraintlayout.widget.Group
import androidx.core.app.ActivityCompat
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraOptions
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.size.Size
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

abstract class GoPayKycBaseCameraFragment : BaseDaggerFragment() {

    protected var cameraView: CameraView? = null
    protected var isCameraOpen = false
    protected var mCapturingPicture = false
    private var mCaptureNativeSize: Size? = null
    protected var capturedImageView: ImageUnify? = null
    protected var shutterImageView: ImageUnify? = null
    protected var reverseCamera: IconUnify? = null
    protected var retakeButton: UnifyButton? = null
    protected var cameraControlLayout: Group? = null
    protected var reviewPhotoLayout: Group? = null
    protected var ktpInstructionText: Typography? = null
    protected var cameraLayout: FrameLayout? = null

    private val cameraListener = object : CameraListener() {
        override fun onCameraOpened(options: CameraOptions) {
            isCameraOpen = true
        }

        override fun onCameraClosed() {
            super.onCameraClosed()
            isCameraOpen = false
        }

        override fun onPictureTaken(result: PictureResult) {
            try {
                generateImage(result.data)
                mCapturingPicture = false
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun generateImage(data: ByteArray) {
        // process Photo
        hideCameraProp()
        resetCapture()
    }

    abstract fun setCaptureInstruction()
    abstract fun setVerificationInstruction()

    private fun reInitCamera() {
        cameraView?.open()
        cameraLayout?.visible()
        capturedImageView?.gone()
        cameraControlLayout?.visible()
        reviewPhotoLayout?.gone()
        setCaptureInstruction()
    }

    private fun hideCameraProp() {
        cameraView?.close()
        cameraLayout?.gone()
        capturedImageView?.visible()
        cameraControlLayout?.gone()
        reviewPhotoLayout?.visible()
        setVerificationInstruction()
    }

    fun initListeners() {
        shutterImageView?.setOnClickListener { capturePicture() }
        reverseCamera?.setOnClickListener { toggleCamera() }
        retakeButton?.setOnClickListener { reInitCamera() }
        cameraView?.addCameraListener(cameraListener)
    }

    private fun capturePicture() {
        if (mCapturingPicture || !isCameraOpen) return
        showLoading()
        mCapturingPicture = true
        mCaptureNativeSize = cameraView?.pictureSize
        cameraView?.takePicture()
    }

    protected open fun toggleCamera() {
        if (mCapturingPicture) return
        cameraView?.toggleFacing()
    }


    protected open fun showLoading() {
        if (isAdded) {
            //progressDialog.show()
        }
    }

    protected open fun hideLoading() {
        if (isAdded) {
            //progressDialog.dismiss()
        }
    }

    private fun resetCapture() {
        mCapturingPicture = false
        mCaptureNativeSize = null
        //
        hideLoading()
    }

    private fun onVisible() {
        if (activity?.isFinishing == true) {
            return
        }
        context?.let {
            val permission = Manifest.permission.CAMERA
            if (ActivityCompat.checkSelfPermission(
                    it,
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startCamera()
            }

        }

    }

    private fun startCamera() {
        try {
            cameraView?.clearCameraListeners();
            cameraView?.addCameraListener(cameraListener);
            cameraView?.open();
        } catch (e: Throwable) {
            e.printStackTrace();

        }
    }

    @TargetApi(23)
    override fun onAttach(context: Context) {
        super.onAttach(context)
        onAttachActivity(context)
    }

    @SuppressWarnings("deprecation")
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachActivity(activity)
        }
    }

    override fun onResume() {
        super.onResume()
        onVisible()
    }

    override fun onDestroy() {
        hideLoading()
        cameraView?.close()
        super.onDestroy()
    }

    companion object {
        private const val IMAGE_QUALITY = 95
        private const val FOLDER_NAME = "extras"
        private const val FILE_EXTENSIONS = ".jpg"
    }

    override fun getScreenName() = null
    override fun initInjector() {}
}