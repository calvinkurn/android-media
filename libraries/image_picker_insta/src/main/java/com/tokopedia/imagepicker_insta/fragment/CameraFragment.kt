package com.tokopedia.imagepicker_insta.fragment

import android.graphics.Bitmap
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraMetadata
import android.net.Uri
import android.os.Bundle
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
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.imagepicker_insta.LiveDataResult
import com.tokopedia.imagepicker_insta.R
import com.tokopedia.imagepicker_insta.activity.CameraActivity
import com.tokopedia.imagepicker_insta.mediaImporter.VideoImporter
import com.tokopedia.imagepicker_insta.util.CameraUtil
import com.tokopedia.imagepicker_insta.viewmodel.CameraViewModel
import com.tokopedia.imagepicker_insta.views.CameraButton
import com.tokopedia.imagepicker_insta.views.CameraButtonListener
import com.tokopedia.unifycomponents.Toaster
import timber.log.Timber

class CameraFragment : Fragment() {

    lateinit var cameraView: CameraView
    lateinit var parent: View
    lateinit var cameraButton: CameraButton
    lateinit var imageFlash: AppCompatImageView
    lateinit var imageCaptureRegion: View
    lateinit var imageSelfieCamera: AppCompatImageView
    lateinit var viewModel: CameraViewModel
    var cropVideoPath: String? = null
    val bitmapCallback = BitmapCallback {
        if (it != null) {
            cropBitmap(it)
        } else {
            showToast("Something went wrong in taking photos", Toaster.TYPE_ERROR)
            (activity as? CameraActivity)?.exitActivityOnError()
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
            if (!cropVideoPath.isNullOrEmpty()) {
                showToast("Cropping Video Finished", Toaster.TYPE_NORMAL)
                (activity as? CameraActivity)?.exitActivityOnSuccess(Uri.parse(cropVideoPath))
            } else {
                showToast("Cropping Video Unknown error",Toaster.TYPE_ERROR)
                (activity as? CameraActivity)?.exitActivityOnError()
            }

        }

        override fun onCanceled() {
            showToast("Cropping Video cancelled", Toaster.TYPE_ERROR)
            Timber.d("NOOB, onCanceled")
            (activity as? CameraActivity)?.exitActivityOnError()
        }

        override fun onFailed(exception: Exception?) {
            showToast("Cropping Video exception", Toaster.TYPE_ERROR)
            (activity as? CameraActivity)?.exitActivityOnError()
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

    fun getLayout() = R.layout.imagepicker_insta_camera_fragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = LayoutInflater.from(context).inflate(getLayout(), container, false)
        initViews(v)
        initData()
        setListeners()
        return v
    }

    fun initData(){
        viewModel = ViewModelProviders.of(this)[CameraViewModel::class.java]
    }

    fun initViews(v: View) {
        cameraView = v.findViewById(R.id.camera_view)
        cameraButton = v.findViewById(R.id.camera_button)
        imageFlash = v.findViewById(R.id.image_flash)
        imageSelfieCamera = v.findViewById(R.id.image_selfie_camera)
        imageCaptureRegion = v.findViewById(R.id.capture_region)

        imageFlash.visibility = View.GONE
        imageSelfieCamera.visibility = View.GONE

        cameraView.pictureFormat = PictureFormat.JPEG
        cameraView.mode = Mode.PICTURE

        cameraView.flash = Flash.OFF
        cameraView.videoMaxDuration = VideoImporter.DURATION_MAX_LIMIT * 1000
    }

    fun showToast(message: String, toasterType: Int) {
        (activity as? CameraActivity)?.showToast(message, toasterType)
    }

    private fun setListeners() {

        viewModel.liveDataCropPhoto.observe(viewLifecycleOwner, {
            when (it.status) {
                LiveDataResult.STATUS.LOADING -> {
                    showToast("Cropping Photo, don't exit",Toaster.TYPE_NORMAL)
                }

                LiveDataResult.STATUS.SUCCESS -> {
                    if (it.data != null) {
                        (activity as? CameraActivity)?.exitActivityOnSuccess(it.data)
                    } else {
                        showToast("Something went wrong in getting uri", Toaster.TYPE_ERROR)
                        (activity as? CameraActivity)?.exitActivityOnError()
                    }
                }
                LiveDataResult.STATUS.ERROR -> {
                    showToast("Something went wrong in cropping",Toaster.TYPE_ERROR)
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

        cameraView.setLifecycleOwner(viewLifecycleOwner)
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
                showToast("Something went wrong", Toaster.TYPE_ERROR)
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
        context?.let {ctx ->
            showToast("Cropping Vide, please don't close the screen",Toaster.TYPE_NORMAL)
            val destinationPath = CameraUtil.createMediaFile(ctx, false).absolutePath
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
                val file = CameraUtil.createMediaFile(it, false)
                cameraView.takeVideo(file)
            }
        } catch (th: Throwable) {
            Timber.e(th)
        }
    }

    fun stopRecordingVideo() {
        cameraView.stopVideo()
    }

    fun capturePhoto() {
        cameraView.takePicture()
    }


    private fun cropBitmap(srcBitmap: Bitmap) {
        context?.let {
            val file = CameraUtil.createMediaFile(it)
            viewModel.cropPhoto(srcBitmap, imageCaptureRegion.y.toInt(), cameraView.width, file)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mp4Composer?.cancel()
    }
}