package com.tokopedia.imagepicker_insta.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.AppCompatImageView
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.VideoResult
import com.otaliastudios.cameraview.controls.Control
import com.otaliastudios.cameraview.controls.Flash
import com.otaliastudios.cameraview.controls.Mode
import com.otaliastudios.cameraview.controls.PictureFormat
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.imagepicker_insta.R
import com.tokopedia.imagepicker_insta.views.CameraButton

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

        cameraView.pictureFormat = PictureFormat.JPEG
        cameraView.mode = Mode.VIDEO

        cameraButton.setOnClickListener {
            if(cameraView.isTakingVideo){
                stopVideo()
            }else{
                startVideo()
            }
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

            override fun onVideoTaken(result: VideoResult) {
                super.onVideoTaken(result)

            }
        })
    }

    fun startVideo(){
        cameraView.takeVideo(filesDir)
    }

    fun stopVideo(){
        cameraView.stopVideo()
    }

    fun capturePhoto(){
        cameraView.takePicture()
    }
}