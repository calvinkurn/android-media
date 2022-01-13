package com.tokopedia.kyc_centralized.view.fragment.camera

import android.app.Activity
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.PictureResult
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.view.activity.UserIdentificationFormActivity.Companion.FILE_NAME_KYC
import com.tokopedia.user_identification_common.KYCConstant
import com.tokopedia.utils.image.ImageProcessingUtil
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.io.File

import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.kyc_centralized.databinding.FragmentCameraCroppingBinding
import com.tokopedia.media.loader.clearImage
import com.tokopedia.media.loader.loadImage
import kotlin.math.roundToInt
import com.otaliastudios.cameraview.CameraView

/**
 * @author  : @rival [Rivaldy Firmansyah]
 * @team    : @android-minionkevin-dev
 * @since   : 3/01/2022
 * */
class CameraWithCroppingFragment : BaseDaggerFragment() {

    private var viewBinding by autoClearedNullable<FragmentCameraCroppingBinding>()

    private var viewMode = 0
    private var imagePath: String = ""

    private val isCameraVisible: Boolean
        get() = viewBinding?.cameraView != null &&
                viewBinding?.cameraView?.visibility == View.VISIBLE

    override fun getScreenName(): String = ""

    override fun initInjector() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = FragmentCameraCroppingBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        showCameraView()
    }

    private fun setupView() {
        viewBinding?.apply {
            imageButtonShutter.setOnClickListener {
                hideCameraButtonAndShowLoading()
                cameraView.takePicture()
            }

            imageButtonFlip.setOnClickListener {
                toggleCamera()
            }

            recaptureButton.setOnClickListener {
                showCameraView()
            }

            closeButton.setOnClickListener {
                activity?.let {
                    it.setResult(Activity.RESULT_CANCELED)
                    it.finish()
                }
            }

            nextButton.setOnClickListener {
                activity?.let {
                    if (isFileExists(imagePath)) {
                        if (isFileSizeQualified(imagePath)) {
                            val intent = Intent()
                            intent.putExtra(KYCConstant.EXTRA_STRING_IMAGE_RESULT, imagePath)
                            it.setResult(Activity.RESULT_OK, intent)
                        } else {
                            it.setResult(KYCConstant.IS_FILE_IMAGE_TOO_BIG)
                        }
                    } else {
                        it.setResult(KYCConstant.IS_FILE_IMAGE_NOT_EXIST)
                    }
                    it.finish()
                }
            }

            populateViewByViewMode()
        }
    }

    private fun populateViewByViewMode() {
        when (viewMode) {
            PARAM_VIEW_MODE_KTP -> {
                viewBinding?.apply {
                    title.text = getString(R.string.camera_ktp_title)
                    subtitle.text = getString(R.string.camera_ktp_subtitle)
                }
            }
            PARAM_VIEW_MODE_FACE -> {
                viewBinding?.apply {
                    title.text = getString(R.string.camera_face_title)
                    subtitle.text = getString(R.string.camera_face_subtitle)
                    toggleCamera()
                }
            }
        }
    }

    private fun toggleCamera() {
        if (isCameraVisible) {
            viewBinding?.cameraView?.toggleFacing()
        }
    }

    private fun showCameraView() {
        viewBinding?.apply {
            imageButtonShutter.show()
            imageButtonFlip.show()
            cropBorder.show()
            progressBar.hide()
            imagePreview.hide()
            buttonLayout.hide()
        }
        startCamera()
    }

    private fun showImagePreview() {
        viewBinding?.apply {
            cameraView.apply {
                close()
                hide()
            }
            imageButtonShutter.hide()
            imageButtonFlip.hide()
            progressBar.hide()
            imagePreview.show()
            buttonLayout.show()
            cropBorder.hide()
        }
    }

    private fun hideCameraButtonAndShowLoading() {
        viewBinding?.apply {
            imageButtonShutter.hide()
            imageButtonFlip.hide()
            imagePreview.hide()
            buttonLayout.hide()
            progressBar.show()
        }
    }

    private fun startCamera() {
        viewBinding?.cameraView?.apply {
            if (isOpened) {
                close()
            }

            clearCameraListeners()
            addCameraListener(listenerOnPictureTaken {
                onSuccessCaptureImage(it)
                this.close()
                this.hide()
            })
            open()
            show()
        }
    }

    private fun onSuccessCaptureImage(pictureResult: PictureResult) {
        pictureResult.toBitmap {
            if (it != null) {
                try {
                    saveToFile(it)
                    saveToFile(doCroppingWithTemplate(it))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun saveToFile(bitmap: Bitmap) {
        val cameraResultFile = ImageProcessingUtil.writeImageToTkpdPath(bitmap, Bitmap.CompressFormat.JPEG, FILE_NAME_KYC)
        if (cameraResultFile?.exists() == true) {
            viewBinding?.imagePreview?.apply {
                clearImage()
                loadImage(cameraResultFile.absolutePath) {
                    useCache(false)
                    setPlaceHolder(-1)
                }
            }
            imagePath = cameraResultFile.absolutePath
            showImagePreview()
        } else {
            Toast.makeText(context, getString(R.string.error_upload_image_kyc), Toast.LENGTH_LONG).show()
        }
    }

    override fun onStop() {
        super.onStop()
        viewBinding?.cameraView?.close()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding?.cameraView?.destroy()
    }

    private fun isFileExists(filePath: String): Boolean {
        val file = File(filePath)
        return file.exists()
    }

    private fun isFileSizeQualified(filePath: String): Boolean {
        val file = File(filePath)
        val fileSize = (file.length() / DEFAULT_ONE_MEGABYTE).toString().toInt()
        return fileSize <= MAX_FILE_SIZE
    }

    private fun listenerOnPictureTaken(result: (PictureResult) -> Unit): CameraListener {
        return object : CameraListener() {
            override fun onPictureTaken(pictureResult: PictureResult) {
                result.invoke(pictureResult)
            }
        }
    }

    private fun doCroppingWithTemplate(bitmap: Bitmap): Bitmap {
        val framePadding = 100

        val bitmapW = bitmap.width.toFloat()
        val bitmapH = bitmap.height.toFloat()

        val camera = viewBinding?.cameraView as CameraView
        val cameraW = camera.width.toFloat()
        val cameraH = camera.height.toFloat()
        val cameraLeft = camera.left.toFloat()
        val cameraTop = camera.top.toFloat()

        val diffScaleW = cameraW / bitmapW
        val diffScaleH = cameraH / bitmapH

        val frame = viewBinding?.cropBorder as View
        var frameW = frame.width.toFloat()
        frameW = if (frameW > bitmapW) bitmapW else frameW
        var frameH = frame.height.toFloat()
        frameH = if (frameH > bitmapH) bitmapH else frameH

        var frameLeft = frame.left.toFloat()
        frameLeft = if (frameLeft < 0) 0F else frameLeft
        var frameTop = frame.top.toFloat()
        frameTop = if (frameTop < 0) 0F else frameTop

        var offsetX: Float = (frameLeft / diffScaleW) - (cameraLeft / diffScaleW) - framePadding
        offsetX = if (offsetX > bitmapW) frameLeft else offsetX
        var offsetY: Float = (frameTop / diffScaleH) - (cameraTop / diffScaleH) - framePadding
        offsetY = if (offsetY > bitmapH) frameTop else offsetY

        var newW: Float = (frameW / diffScaleW) + (framePadding * 2).toFloat() // 2 (add padding on left & right)
        newW = when {
            newW > bitmapW -> bitmapW
            newW + offsetX > bitmapW -> newW - offsetX
            else -> newW
        }
        var newH: Float = (frameH / diffScaleH) + (framePadding * 2).toFloat() // 2 (add padding on top & bottom)
        newH = when {
            newH > bitmapH -> bitmapH
            newH + offsetY > bitmapH -> newH - offsetY
            else -> newH
        }

        return Bitmap.createBitmap(
                bitmap,
                offsetX.roundToInt(),
                offsetY.roundToInt(),
                newW.roundToInt(),
                newH.roundToInt()
        )
    }

    companion object {
        private const val ARG_VIEW_MODE = "view_mode"
        private const val PARAM_VIEW_MODE_KTP = 1
        private const val PARAM_VIEW_MODE_FACE = 2
        private const val DEFAULT_ONE_MEGABYTE: Long = 1024
        private const val MAX_FILE_SIZE = 15360

        fun createInstance(viewMode: Int): Fragment = CameraWithCroppingFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_VIEW_MODE, viewMode)
            }
        }
    }
}