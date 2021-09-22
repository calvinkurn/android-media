package com.tokopedia.imagepicker_insta.fragment

import android.graphics.Bitmap
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraMetadata
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.daasuu.mp4compose.FillMode
import com.daasuu.mp4compose.FillModeCustomItem
import com.daasuu.mp4compose.composer.Mp4Composer
import com.daasuu.mp4compose.logger.Logger
import com.otaliastudios.cameraview.*
import com.otaliastudios.cameraview.controls.Flash
import com.otaliastudios.cameraview.controls.Mode
import com.otaliastudios.cameraview.controls.PictureFormat
import com.otaliastudios.cameraview.size.Size
import com.otaliastudios.cameraview.size.SizeSelector
import com.otaliastudios.cameraview.size.SizeSelectors
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.imagepicker_insta.LiveDataResult
import com.tokopedia.imagepicker_insta.R
import com.tokopedia.imagepicker_insta.activity.CameraActivity
import com.tokopedia.imagepicker_insta.util.CameraUtil
import com.tokopedia.imagepicker_insta.viewmodel.CameraViewModel
import com.tokopedia.imagepicker_insta.views.CameraButton
import com.tokopedia.imagepicker_insta.views.CameraButtonListener
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.Toaster
import timber.log.Timber
import java.io.File
import java.util.*


class CameraFragment : Fragment() {

    lateinit var cameraView: CameraView
    lateinit var parent: View
    lateinit var cameraButton: CameraButton
    lateinit var imageFlash: AppCompatImageView
    lateinit var imageCaptureRegion: View
    lateinit var imageSelfieCamera: AppCompatImageView
    lateinit var loader: LoaderUnify

    lateinit var viewModel: CameraViewModel
    var sourceVideoFile: File? = null
    var cropVideoPath: String? = null
    val handler = Handler(Looper.getMainLooper())
    val timer = Timer()
    var isFlashOn = false

    val bitmapCallback = BitmapCallback {
        if (it != null) {
            cropBitmap(it)
        } else {
            showToast("Something went wrong in taking photos", Toaster.TYPE_ERROR)
            (activity as? CameraActivity)?.exitActivityOnError()
        }
    }
    val mp4ComposerListener = object : Mp4Composer.Listener {
        override fun onProgress(progress: Double) {}

        override fun onCurrentWrittenVideoTime(timeUs: Long) {}

        override fun onCompleted() {
            handler.post {
                loader.visibility = View.GONE
            }

            viewModel.deleteFile(sourceVideoFile)

            if (!cropVideoPath.isNullOrEmpty()) {
                (activity as? CameraActivity)?.exitActivityOnSuccess(Uri.parse(cropVideoPath))
            } else {
                (activity as? CameraActivity)?.exitActivityOnError()
            }

        }

        override fun onCanceled() {
            handler.post {
                loader.visibility = View.GONE
                showToast("Cropping Video cancelled", Toaster.TYPE_ERROR)
            }
            viewModel.deleteFile(sourceVideoFile)
            (activity as? CameraActivity)?.exitActivityOnError()
        }

        override fun onFailed(exception: Exception?) {
            handler.post {
                loader.visibility = View.GONE
                showToast("Cropping Video exception", Toaster.TYPE_ERROR)
            }

            viewModel.deleteFile(sourceVideoFile)
            (activity as? CameraActivity)?.exitActivityOnError()
            Timber.e(exception)

        }
    }
    val mp4ComposerLogger = object : Logger {
        override fun debug(tag: String?, message: String?) {}

        override fun error(tag: String?, message: String?, error: Throwable?) {}

        override fun warning(tag: String?, message: String?) {}
    }
    var mp4Composer: Mp4Composer? = null

    fun getLayout() = R.layout.imagepicker_insta_camera_fragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = LayoutInflater.from(context).inflate(getLayout(), container, false)
        initViews(v)
        initData()
        setListeners()
        return v
    }

    fun initData() {
        viewModel = ViewModelProviders.of(this)[CameraViewModel::class.java]
    }

    fun initViews(v: View) {
        cameraView = v.findViewById(R.id.camera_view)
        cameraButton = v.findViewById(R.id.camera_button)
        imageFlash = v.findViewById(R.id.image_flash)
        imageSelfieCamera = v.findViewById(R.id.image_selfie_camera)
        imageCaptureRegion = v.findViewById(R.id.capture_region)
        loader = v.findViewById(R.id.loader)

        imageFlash.visibility = View.GONE
        imageSelfieCamera.visibility = View.GONE

        cameraView.pictureFormat = PictureFormat.JPEG
        cameraView.mode = Mode.PICTURE

        cameraView.flash = Flash.OFF
        isFlashOn = false

        imageFlash.setImageResource(R.drawable.imagepicker_insta_flash_off)
        cameraView.videoMaxDuration = CameraButton.MAX_VIDEO_RECORD_DURATION_IN_SECONDS * 1000
    }

    fun showToast(message: String, toasterType: Int) {
        (activity as? CameraActivity)?.showToast(message, toasterType)
    }

    private fun toggleFlash() {
        if (isFlashOn) {
            isFlashOn = false
            imageFlash.setImageResource(R.drawable.imagepicker_insta_flash_off)
        } else {
            isFlashOn = true
            imageFlash.setImageResource(R.drawable.imagepicker_insta_flash_on)
        }
    }

    private fun setListeners() {

        viewModel.liveDataCropPhoto.observe(viewLifecycleOwner, {
            when (it.status) {
                LiveDataResult.STATUS.LOADING -> {
                    loader.visibility = View.VISIBLE
                }

                LiveDataResult.STATUS.SUCCESS -> {
                    loader.visibility = View.GONE
                    if (it.data != null) {
                        (activity as? CameraActivity)?.exitActivityOnSuccess(it.data)
                    } else {
                        showToast("Something went wrong in getting uri", Toaster.TYPE_ERROR)
                        (activity as? CameraActivity)?.exitActivityOnError()
                    }
                }
                LiveDataResult.STATUS.ERROR -> {
                    loader.visibility = View.GONE
                    showToast("Something went wrong in cropping", Toaster.TYPE_ERROR)
                    (activity as? CameraActivity)?.exitActivityOnError()

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
                stopRecordingVideo()
            }
        }

        imageSelfieCamera.setOnClickListener {
            cameraView.toggleFacing()
        }

        imageFlash.setOnClickListener {
            toggleFlash()
        }

        cameraView.setLifecycleOwner(viewLifecycleOwner)
        cameraView.addCameraListener(object : CameraListener() {
            override fun onCameraOpened(options: CameraOptions) {
                super.onCameraOpened(options)
                checkForFlash(options)
                checkForFrontCamera()
            }

            override fun onPictureTaken(result: PictureResult) {
                super.onPictureTaken(result)
                disableFlashTorch()
                result.toBitmap(bitmapCallback)
            }

            override fun onVideoTaken(result: VideoResult) {
                super.onVideoTaken(result)
                disableFlashTorch()
                cropVideo(result)
            }

            override fun onCameraError(exception: CameraException) {
                super.onCameraError(exception)
                Timber.e(exception)
                disableFlashTorch()
                cameraButton.addTouchListener()
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
        context?.let { ctx ->
            loader.visibility = View.VISIBLE

            val destinationPath = CameraUtil.createMediaFile(ctx, false).absolutePath
            cropVideoPath = destinationPath

            val fillModeCustomItem = FillModeCustomItem(
                cameraView.scaleX,
                cameraView.rotation,
                0f,
                0f,
                result.size.width.toFloat(),
                result.size.height.toFloat()
            )
            sourceVideoFile = result.file

            mp4Composer = Mp4Composer(Uri.fromFile(result.file), destinationPath, ctx, mp4ComposerLogger)
                .size(cameraView.width, cameraView.width)
                .fillMode(FillMode.CUSTOM)
                .customFillMode(fillModeCustomItem)
                .listener(mp4ComposerListener)
                .start()
        }
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
            val manager = context?.getSystemService(BaseActivity.CAMERA_SERVICE) as? CameraManager
            manager?.cameraIdList?.forEach { cameraId ->
                val chars: CameraCharacteristics = manager.getCameraCharacteristics(cameraId)
                val facing = chars.get(CameraCharacteristics.LENS_FACING)
                if (facing == CameraMetadata.LENS_FACING_FRONT) {
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
            context?.let {
                val file = CameraUtil.createMediaFile(it, false, storeInCache = true)
                if (isFlashOn) {
                    enableFlashTorch()
                }
                cameraView.takeVideo(file)
            }
        } catch (th: Throwable) {
            Timber.e(th)
        }
    }

    fun stopRecordingVideo() {
        disableFlashTorch()
        cameraView.stopVideo()
    }

    fun enableFlashTorch() {
        val isSupportFlashTorch = cameraView.cameraOptions?.supportedFlash?.contains(Flash.TORCH) == true
        if (isSupportFlashTorch) {
            cameraView.flash = Flash.TORCH
        }
    }

    fun disableFlashTorch() {
        if (cameraView.cameraOptions?.supportedFlash?.contains(Flash.TORCH) == true && isFlashOn) {
            cameraView.flash = Flash.OFF
        }

    }

    fun capturePhoto() {

        if (isFlashOn) {
            enableFlashTorch()

            timer.schedule(object : TimerTask() {
                override fun run() {
                    cameraView.takePictureSnapshot()
                }
            }, 2000L)
        } else {
            cameraView.takePictureSnapshot()
        }
    }

    private fun cropBitmap(srcBitmap: Bitmap) {
        context?.let {
            val file = CameraUtil.createMediaFile(it)
            viewModel.cropPhoto(srcBitmap, imageCaptureRegion.y.toInt(), srcBitmap.width, file)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer.cancel()
        mp4Composer?.cancel()
        cameraButton.stopCountDown()
    }
}