package com.tokopedia.imagepicker_insta.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraMetadata.LENS_FACING_FRONT
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import com.daasuu.mp4compose.FillMode
import com.daasuu.mp4compose.FillModeCustomItem
import com.daasuu.mp4compose.composer.Mp4Composer
import com.daasuu.mp4compose.logger.Logger
import com.otaliastudios.cameraview.*
import com.otaliastudios.cameraview.controls.Flash
import com.otaliastudios.cameraview.controls.Mode
import com.otaliastudios.cameraview.controls.PictureFormat
import com.otaliastudios.cameraview.size.AspectRatio
import com.otaliastudios.cameraview.size.SizeSelectors
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.imagepicker_insta.LiveDataResult
import com.tokopedia.imagepicker_insta.R
import com.tokopedia.imagepicker_insta.mediaImporter.VideoImporter
import com.tokopedia.imagepicker_insta.models.BundleData
import com.tokopedia.imagepicker_insta.util.CameraUtil
import com.tokopedia.imagepicker_insta.viewmodel.CameraViewModel
import com.tokopedia.imagepicker_insta.views.CameraButton
import com.tokopedia.imagepicker_insta.views.CameraButtonListener
import com.tokopedia.unifycomponents.Toaster
import timber.log.Timber

class CameraActivity : BaseActivity() {

    lateinit var cameraView: CameraView
    lateinit var parent: View
    lateinit var cameraButton: CameraButton
    lateinit var imageFlash: AppCompatImageView
    lateinit var imageCaptureRegion: View
    lateinit var imageSelfieCamera: AppCompatImageView
    var applinkToNavigateAfterMediaCapture: String? = null
    lateinit var viewModel: CameraViewModel
    var cropVideoPath: String? = null
    val bitmapCallback = BitmapCallback {
        if (it != null) {
            cropBitmap(it)
        } else {
            showError("Something went wrong in taking photos")
            exitActivityOnError()
        }
    }
    val mp4ComposerListener = object : Mp4Composer.Listener {
        override fun onProgress(progress: Double) {
            Timber.d("NOOB, onProgess: $progress")
        }

        override fun onCurrentWrittenVideoTime(timeUs: Long) {
            Timber.d("NOOB, onCurrentWrittenVideoTime: $timeUs")
        }

        override fun onCompleted() {

            Timber.d("NOOB, onCompleted")
            if(!cropVideoPath.isNullOrEmpty()){
                showToast("Cropping Video Finished")
                exitActivityOnSuccess(Uri.parse(cropVideoPath))
            }else{
                showToast("Cropping Video Unknown error")
                exitActivityOnError()
            }

        }

        override fun onCanceled() {
            showError("Cropping Video cancelled")
            Timber.d("NOOB, onCanceled")
            exitActivityOnError()
        }

        override fun onFailed(exception: Exception?) {
            showError("Cropping Video exception")
            exitActivityOnError()
            exception?.printStackTrace()
            Timber.e("NOOB, ${exception?.message} ")

        }
    }
    val mp4ComposerLogger = object : Logger {
        override fun debug(tag: String?, message: String?) {
//            Timber.d("NOOB, Logger $message")
        }

        override fun error(tag: String?, message: String?, error: Throwable?) {
            Timber.e("NOOB, Logger $message")
        }

        override fun warning(tag: String?, message: String?) {
            Timber.w("NOOB, Logger $message")
        }
    }
    var mp4Composer: Mp4Composer? = null

    companion object {

        val REQUEST_CODE = 213
        fun getIntent(context: Context, fileUriList: List<Uri>, applinkToNavigateAfterMediaCapture: String?): Intent {
            val uriList = ArrayList<String>()
            fileUriList.forEach {
                uriList.add(it.toString())
            }
            val intent = Intent(context, CameraActivity::class.java)
            intent.putExtra(BundleData.URIS, uriList)
            intent.putExtra(BundleData.APPLINK_AFTER_CAMERA_CAPTURE, applinkToNavigateAfterMediaCapture)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.imagepicker_insta_camera_activity)
        viewModel = ViewModelProviders.of(this)[CameraViewModel::class.java]

        readIntentData()

        parent = findViewById(R.id.parent)
        cameraView = findViewById(R.id.camera_view)
        cameraButton = findViewById(R.id.camera_button)
        imageFlash = findViewById(R.id.image_flash)
        imageSelfieCamera = findViewById(R.id.image_selfie_camera)
        imageCaptureRegion = findViewById(R.id.capture_region)

        imageFlash.visibility = View.GONE
        imageSelfieCamera.visibility = View.GONE

        cameraView.pictureFormat = PictureFormat.JPEG
        cameraView.mode = Mode.PICTURE

        cameraView.flash = Flash.OFF
        cameraView.videoMaxDuration = VideoImporter.DURATION_MAX_LIMIT * 1000

        setToolbar()
        setListeners()
    }

    private fun handleCaptureSize() {
        val width = SizeSelectors.minWidth(cameraView.width)
        val height = SizeSelectors.minHeight(cameraView.width)
        val dimensions = SizeSelectors.and(width, height)
        val ratio = SizeSelectors.aspectRatio(AspectRatio.of(1, 1), 0f)
        val result = SizeSelectors.or(
            SizeSelectors.and(ratio, dimensions),  // Try to match both constraints
            ratio,  // If none is found, at least try to match the aspect ratio
            SizeSelectors.biggest() // If none is found, take the biggest
        )
        cameraView.setVideoSize(result)
        cameraView.setPreviewStreamSize(result)
        cameraView.setPictureSize(result)
    }

    fun readIntentData() {
        applinkToNavigateAfterMediaCapture = intent.getStringExtra(BundleData.APPLINK_AFTER_CAMERA_CAPTURE)
    }

    fun setToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val toolbarNavIcon: AppCompatImageView = findViewById(R.id.toolbar_nav_icon)
        toolbarNavIcon.setOnClickListener {
            finish()
        }

    }

    private fun setListeners() {

        viewModel.liveDataCropPhoto.observe(this, {
            when (it.status) {
                LiveDataResult.STATUS.LOADING -> {
                    showToast("Cropping Photo, don't exit")
                }

                LiveDataResult.STATUS.SUCCESS -> {
                    if (it.data != null) {
                        exitActivityOnSuccess(it.data)
                    } else {
                        showError("Something went wrong in getting uri")
                        exitActivityOnError()
                    }
                }
                LiveDataResult.STATUS.ERROR -> {
                    showError("Something went wrong in cropping")
                    exitActivityOnError()

                }
            }
        })
        cameraButton.cameraButtonListener = object : CameraButtonListener {
            override fun onClick() {
                cameraView.mode = Mode.PICTURE

                capturePhoto()
            }

            override fun onLongClickStart() {
                cameraView.mode = Mode.VIDEO

                if (!cameraView.isTakingVideo) {
                    startRecordingVideo()
                }
            }

            override fun onLongClickEnd() {

                if (cameraView.isTakingVideo) {
                    stopRecordingVideo()
                }
                cameraView.mode = Mode.PICTURE
            }

        }

        imageSelfieCamera.setOnClickListener {
            cameraView.toggleFacing()
        }

        imageFlash.setOnClickListener {
            if (cameraView.flash == Flash.ON) {
                cameraView.flash = Flash.OFF
                imageFlash.setImageResource(R.drawable.imagepicker_insta_flash_on)
            } else {
                cameraView.flash = Flash.ON
                imageFlash.setImageResource(R.drawable.imagepicker_insta_flash_off)
            }

        }

        cameraView.setLifecycleOwner(this)
        cameraView.addCameraListener(object : CameraListener() {
            override fun onCameraOpened(options: CameraOptions) {
                super.onCameraOpened(options)
                checkForFlash(options)
                checkForFrontCamera()
            }

            override fun onPictureTaken(result: PictureResult) {
                super.onPictureTaken(result)
                Timber.d("${CameraUtil.LOG_TAG} picture taken:")
                result.toBitmap(bitmapCallback)
            }

            override fun onVideoTaken(result: VideoResult) {
                super.onVideoTaken(result)
                Timber.d("${CameraUtil.LOG_TAG} video taken: ${result.file.path}")
                cropVideo(result)
            }

            override fun onCameraError(exception: CameraException) {
                super.onCameraError(exception)
                Timber.d("${CameraUtil.LOG_TAG} error: ${exception.reason}")
                showError("Something went wrong")
            }

            override fun onVideoRecordingStart() {
                super.onVideoRecordingStart()
            }

            override fun onVideoRecordingEnd() {
                super.onVideoRecordingEnd()
            }
        })
    }

    /**
     * TODO Rahul Ensure second recording will only start when mp4Composer is finished
     * */
    private fun cropVideo(result: VideoResult) {
        showToast("Cropping Vide, please don't close the screen")
        val destinationPath = CameraUtil.createMediaFile(this, false).absolutePath
        cropVideoPath = destinationPath
        Timber.d("NOOB, Cropped Video : $cropVideoPath")


        val fillModeCustomItem = FillModeCustomItem(
            cameraView.scaleX,
            cameraView.rotation,
            0f,
            0f,
            result.size.width.toFloat(),
            result.size.height.toFloat()
        )
        mp4Composer = Mp4Composer(Uri.fromFile(result.file), destinationPath, this, mp4ComposerLogger)
            .size(cameraView.width, cameraView.width)
            .fillMode(FillMode.CUSTOM)
            .customFillMode(fillModeCustomItem)
            .listener(mp4ComposerListener)
            .start()

    }

    private fun cropBitmap(srcBitmap: Bitmap) {
        val file = CameraUtil.createMediaFile(this@CameraActivity)
        viewModel.cropPhoto(srcBitmap, imageCaptureRegion.y.toInt(), cameraView.width, file)
    }

    private fun afterMediaIsCaptured(uri: Uri) {
        if (!applinkToNavigateAfterMediaCapture.isNullOrEmpty()) {
            val finalApplink = CameraUtil.createApplinkToSendFileUris(applinkToNavigateAfterMediaCapture!!, arrayListOf(uri))
            RouteManager.route(this, finalApplink)
        } else {
            finish()
        }
    }

    private fun showError(message: String) {
        Toaster.build(parent, message, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
    }

    private fun showToast(message: String) {
        Toaster.build(parent, message, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
    }

    fun checkForFlash(cameraOptions: CameraOptions) {
        val isSupportFlash = cameraOptions.supportedFlash.contains(Flash.ON)
        if (isSupportFlash) {
            imageFlash.visibility = View.VISIBLE
        } else {
            imageFlash.visibility = View.GONE
        }
    }

    fun checkForFrontCamera() {
        var hasFrontCamera = false
        try {
            val manager = getSystemService(CAMERA_SERVICE) as? CameraManager
            manager?.cameraIdList?.forEach { cameraId ->
                val chars: CameraCharacteristics = manager.getCameraCharacteristics(cameraId)
                val facing = chars.get(CameraCharacteristics.LENS_FACING)
                if (facing == LENS_FACING_FRONT) {
                    hasFrontCamera = true
                    return@forEach
                }

            }
        } catch (th: Throwable) {
            Timber.e(th)
        }

        if (!hasFrontCamera) {
            imageSelfieCamera.visibility = View.GONE
        } else {
            imageSelfieCamera.visibility = View.VISIBLE
        }
    }

    fun startRecordingVideo() {
        try {
            val file = CameraUtil.createMediaFile(this, false)
            cameraView.takeVideo(file)
        } catch (th: Throwable) {
            Timber.e(th)
        }
    }


    fun exitActivityOnError() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    private fun exitActivityOnSuccess(uri: Uri) {
        setResult(Activity.RESULT_OK, CameraUtil.getIntentfromFileUris(arrayListOf(uri)))
        afterMediaIsCaptured(uri)
    }

    fun stopRecordingVideo() {
        cameraView.stopVideo()
    }

    fun capturePhoto() {
        cameraView.takePicture()
    }

    override fun onDestroy() {
        super.onDestroy()
        mp4Composer?.cancel()
    }
}