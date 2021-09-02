package com.tokopedia.imagepicker_insta.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Size
import androidx.appcompat.widget.AppCompatImageView
//import androidx.camera.core.ImageCapture
//import androidx.camera.core.VideoCapture
//import androidx.camera.core.impl.ImageCaptureConfig
//import androidx.camera.core.impl.OptionsBundle
//import androidx.camera.core.impl.VideoCaptureConfig
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.controls.Flash
import com.otaliastudios.cameraview.controls.PictureFormat
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.imagepicker_insta.R

class CameraXActivity : BaseActivity() {
    lateinit var cameraView: CameraView
    lateinit var imageCapture: AppCompatImageView
    lateinit var imageFlash: AppCompatImageView
    lateinit var imageSelfieCamera: AppCompatImageView

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, CameraXActivity::class.java)
        }
    }

    fun setConfig(){
//        val c = android.hardware.camera2.CameraDevice()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.imagepicker_insta_camera_activity)

        cameraView = findViewById(R.id.camera_view)
        imageCapture = findViewById(R.id.image_capture)
        imageFlash = findViewById(R.id.image_flash)
        imageSelfieCamera = findViewById(R.id.image_selfie_camera)

        cameraView.pictureFormat = PictureFormat.JPEG
//        cameraView.setPreviewStreamSize()

        imageCapture.setOnClickListener {
            cameraView.takePicture()
        }

        imageSelfieCamera.setOnClickListener {
            cameraView.toggleFacing()
        }

        imageFlash.setOnClickListener {
            if (cameraView.flash == Flash.ON) {
                cameraView.flash = Flash.ON
            } else {
                cameraView.flash = Flash.OFF
            }
        }

        cameraView.setLifecycleOwner(this)
        cameraView.addCameraListener(object : CameraListener() {
            override fun onPictureTaken(result: PictureResult) {
                super.onPictureTaken(result)
            }
        })
    }
}