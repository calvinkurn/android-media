package com.tokopedia.imagepicker_insta.activity

import android.content.Context
import android.content.Intent
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraMetadata.LENS_FACING_FRONT
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import com.otaliastudios.cameraview.*
import com.otaliastudios.cameraview.controls.Flash
import com.otaliastudios.cameraview.controls.Mode
import com.otaliastudios.cameraview.controls.PictureFormat
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.imagepicker_insta.R
import com.tokopedia.imagepicker_insta.util.CameraUtil
import com.tokopedia.imagepicker_insta.views.CameraButton
import com.tokopedia.imagepicker_insta.views.CameraButtonListener
import timber.log.Timber


class CameraActivity : BaseActivity() {

    lateinit var cameraView: CameraView
    lateinit var cameraButton: CameraButton
    lateinit var imageFlash: AppCompatImageView
    lateinit var imageSelfieCamera: AppCompatImageView

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, CameraActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.imagepicker_insta_camera_activity)

        cameraView = findViewById(R.id.camera_view)
        cameraButton = findViewById(R.id.camera_button)
        imageFlash = findViewById(R.id.image_flash)
        imageSelfieCamera = findViewById(R.id.image_selfie_camera)

        imageFlash.visibility = View.GONE
        imageSelfieCamera.visibility = View.GONE

        cameraView.pictureFormat = PictureFormat.JPEG
        cameraView.mode = Mode.PICTURE

        cameraView.flash = Flash.OFF

        setToolbar()
        setListeners()
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
                val file = CameraUtil.createMediaFile(this@CameraActivity)
                result.toFile(file) {
                    Timber.d("${CameraUtil.LOG_TAG} picture taken: ${it?.path}")
                }
            }

            override fun onVideoTaken(result: VideoResult) {
                super.onVideoTaken(result)
                Timber.d("${CameraUtil.LOG_TAG} video taken: ${result.file.path}")
            }

            override fun onCameraError(exception: CameraException) {
                super.onCameraError(exception)
                Timber.d("${CameraUtil.LOG_TAG} error: ${exception.reason}")
            }

            override fun onVideoRecordingStart() {
                super.onVideoRecordingStart()
            }

            override fun onVideoRecordingEnd() {
                super.onVideoRecordingEnd()
            }
        })
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

    fun stopRecordingVideo() {
        cameraView.stopVideo()
    }

    fun capturePhoto() {
        cameraView.takePicture()
    }
}