package com.tokopedia.kyc_centralized.ui.tokoKyc.camera

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.PictureResult
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.PARAM_PROJECT_ID
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.databinding.FragmentCameraKtpBinding
import com.tokopedia.kyc_centralized.ui.tokoKyc.form.UserIdentificationFormActivity.Companion.FILE_NAME_KYC
import com.tokopedia.media.loader.clearImage
import com.tokopedia.media.loader.loadImage
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.kyc_centralized.analytics.UserIdentificationCommonAnalytics
import com.tokopedia.kyc_centralized.common.KYCConstant.EXTRA_USE_COMPRESSION
import com.tokopedia.kyc_centralized.common.KYCConstant.EXTRA_USE_CROPPING
import com.tokopedia.kyc_centralized.util.BitmapCroppingAndCompression
import com.tokopedia.kyc_centralized.util.BitmapProcessingListener
import com.tokopedia.utils.image.ImageProcessingUtil
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.io.File
import java.io.IOException
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * @author  : @rival [Rivaldy Firmansyah]
 * @team    : @android-minionkevin-dev
 * @since   : 3/01/2022
 * */
class CameraKtpFragment : BaseDaggerFragment(), CoroutineScope {

    private var viewBinding by autoClearedNullable<FragmentCameraKtpBinding>()
    private var imagePath: String = ""
    private var bitmapProcessing: BitmapCroppingAndCompression? = null

    private var isUseCropping: Boolean = false
    private var isUseCompression: Boolean = false
    private var projectId: Int = -1

    private var analytics: UserIdentificationCommonAnalytics? = null

    private val isCameraVisible: Boolean
        get() = viewBinding?.cameraView != null &&
                viewBinding?.cameraView?.visibility == View.VISIBLE

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    override fun getScreenName(): String = ""

    override fun initInjector() { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            isUseCropping = it.getBoolean(EXTRA_USE_CROPPING).orFalse()
            isUseCompression = it.getBoolean(EXTRA_USE_COMPRESSION).orFalse()
            projectId = it.getInt(PARAM_PROJECT_ID).orZero()
        }

        analytics = UserIdentificationCommonAnalytics.createInstance(projectId)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = FragmentCameraKtpBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        showCameraView()

        viewBinding?.let {
            bitmapProcessing = context?.let { context ->
                BitmapCroppingAndCompression(context, it.cameraView)
            }

            if (it.cameraView.isOpened) {
                analytics?.eventViewOpenCameraKtp()
            }
        }
    }

    private fun setupView() {
        viewBinding?.apply {
            title.text = getString(R.string.camera_ktp_title)
            subtitle.text = getString(R.string.camera_ktp_subtitle)

            imageButtonShutter.setOnClickListener {
                analytics?.eventClickShutterCameraKtp()
                hideCameraButtonAndShowLoading()
                cameraView.takePictureSnapshot()
            }

            imageButtonFlip.setOnClickListener {
                analytics?.eventClickFlipCameraKtp()
                toggleCamera()
            }

            recaptureButton.setOnClickListener {
                analytics?.eventClickRecaptureKtp()
                showCameraView()
            }

            closeButton.setOnClickListener {
                activity?.let {
                    if (isCameraVisible) {
                        analytics?.eventClickBackCameraKtp()
                    } else {
                        analytics?.eventClickCloseImagePreviewKtp()
                    }

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
                    analytics?.eventClickNextImagePreviewKtp()
                    it.finish()
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
            subtitle.text = getString(R.string.camera_ktp_subtitle)

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
        analytics?.eventViewImagePreviewKtp()
        viewBinding?.apply {
            subtitle.text = getString(R.string.camera_ktp_subtitle_preview)

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
        try {
            pictureResult.toBitmap { bitmap ->
                if (bitmap != null) {
                    if (isUseCropping && isUseCompression) {
                        viewBinding?.cropBorder?.let {
                            doCropAndCompress(bitmap, it)
                        }
                    } else if (isUseCropping && !isUseCompression) {
                        viewBinding?.cropBorder?.let {
                            doCropping(bitmap, it)
                        }
                    } else if (!isUseCropping && isUseCompression) {
                        doCompression(bitmap)
                    } else {
                        saveToFile(bitmap)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun doCropping(bitmap: Bitmap, frame: View) {
        try {
            bitmapProcessing?.doCropping(bitmap, frame, object : BitmapProcessingListener {
                override fun onBitmapReady(bitmap: Bitmap) {
                    val file = saveToFile(bitmap)
                    onSuccessSaveFile(file, bitmap)
                }

                override fun onFailed(originalBitmap: Bitmap, throwable: Throwable) {
                    throwable.printStackTrace()

                    val file = saveToFile(bitmap)
                    onSuccessSaveFile(file, bitmap)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun doCompression(bitmap: Bitmap) {
        try {
            bitmapProcessing?.doCompression(bitmap, object : BitmapProcessingListener {
                override fun onBitmapReady(bitmap: Bitmap) {
                    val file = saveToFile(bitmap)
                    onSuccessSaveFile(file, bitmap)
                }

                override fun onFailed(originalBitmap: Bitmap, throwable: Throwable) {
                    throwable.printStackTrace()

                    val file = saveToFile(bitmap)
                    onSuccessSaveFile(file, bitmap)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun doCropAndCompress(bitmap: Bitmap, frame: View) {
        try {
            bitmapProcessing?.doCropAndCompress(bitmap, frame, object : BitmapProcessingListener {
                override fun onBitmapReady(bitmap: Bitmap) {
                    val file = saveToFile(bitmap)
                    onSuccessSaveFile(file, bitmap)
                }

                override fun onFailed(originalBitmap: Bitmap, throwable: Throwable) {
                    throwable.printStackTrace()

                    val file = saveToFile(bitmap)
                    onSuccessSaveFile(file, bitmap)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun saveToFile(bitmap: Bitmap): File {
        val file = ImageProcessingUtil.writeImageToTkpdPath(bitmap, Bitmap.CompressFormat.JPEG, FILE_NAME_KYC)
        if (file?.exists() == true) return file
        else throw IOException("Failed save file")
    }

    private fun onSuccessSaveFile(file: File, bitmap: Bitmap) {

        val aspectRatio = "${bitmap.width}$DELIMITER_COLON${bitmap.height}"
        (viewBinding?.imagePreview?.layoutParams as ConstraintLayout.LayoutParams).dimensionRatio = aspectRatio

        viewBinding?.imagePreview?.apply {
            clearImage()
            loadImage(file.absolutePath) {
                useCache(false)
                setPlaceHolder(-1)
            }
        }
        imagePath = file.absolutePath
        showImagePreview()
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
        return getFileSizeInMb(file) <= MAX_FILE_SIZE
    }

    private fun getFileSizeInMb(file: File): Int {
        return (file.length() / DEFAULT_ONE_MEGABYTE).toString().toIntOrZero()
    }

    private fun listenerOnPictureTaken(result: (PictureResult) -> Unit): CameraListener {
        return object : CameraListener() {
            override fun onPictureTaken(pictureResult: PictureResult) {
                result.invoke(pictureResult)
            }
        }
    }

    companion object {
        private const val DEFAULT_ONE_MEGABYTE: Long = 1024
        private const val MAX_FILE_SIZE = 15360

        private const val DELIMITER_COLON = ":"

        // Bundle values :
        // viewMode: Int,
        // useCropping: Boolean = false,
        // useCompression: Boolean = false
        fun createInstance(bundle: Bundle): Fragment = CameraKtpFragment().apply {
            arguments = bundle
        }
    }
}
