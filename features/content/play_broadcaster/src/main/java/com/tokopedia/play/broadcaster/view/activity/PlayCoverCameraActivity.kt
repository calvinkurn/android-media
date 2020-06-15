package com.tokopedia.play.broadcaster.view.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraUtils
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.controls.Facing
import com.otaliastudios.cameraview.controls.Flash
import com.otaliastudios.cameraview.gesture.Gesture
import com.otaliastudios.cameraview.gesture.GestureAction
import com.tokopedia.imagepicker.common.util.ImageUtils
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.CameraTimerEnum
import kotlinx.android.synthetic.main.activity_play_cover_camera.*
import java.io.File
import java.util.*

class PlayCoverCameraActivity : AppCompatActivity() {

    private lateinit var cameraListener: CameraListener
    private var cameraTimerEnum: CameraTimerEnum = CameraTimerEnum.IMMEDIATE
    private var timeToCapture: Int = 0
    private var isTimerRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_cover_camera)
        initView()
    }

    override fun onResume() {
        super.onResume()
        if (isCameraPermissionGranted()) {
            cvPlayCameraView.open()
        } else {
            requestCameraPermission()
        }
    }

    override fun onPause() {
        super.onPause()
        cvPlayCameraView.close()
    }

    override fun onDestroy() {
        cvPlayCameraView.close()
        cvPlayCameraView.destroy()
        super.onDestroy()
    }

    private fun initView() {
        btnPlayCameraCancel.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        cameraListener = object : CameraListener() {
            override fun onPictureTaken(result: PictureResult) {
                saveToFile(result.data)
            }
        }

        cvPlayCameraView.addCameraListener(cameraListener)
        ivPlayCameraShutter.setOnClickListener {
            takePicture()
        }
        ivPlayCameraFlash.setOnClickListener {
            toggleFlash()
        }
        ivPlayCameraReverse.setOnClickListener {
            reverseCamera()
        }
        tvPlayCameraTimer0.setOnClickListener {
            setImmediateCapture()
        }
        tvPlayCameraTimer5.setOnClickListener {
            setTimerFiveSecondsCapture()
        }
        tvPlayCameraTimer10.setOnClickListener {
            setTimerTenSecondsCapture()
        }
        cvPlayCameraView.mapGesture(Gesture.PINCH, GestureAction.ZOOM)
        cvPlayCameraView.mapGesture(Gesture.TAP, GestureAction.AUTO_FOCUS)
    }

    private fun takePicture() {
        if (timeToCapture > 0) {
            showTimerLayout()
            val timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    runOnUiThread {
                        if (timeToCapture > 0)
                            showTimerLayout()
                    }
                    timeToCapture--
                    if (timeToCapture <= 0) {
                        runOnUiThread {
                            hideTimerLayout()
                        }
                        cvPlayCameraView.takePicture()
                        timeToCapture = cameraTimerEnum.seconds
                        timer.cancel()
                        timer.purge()
                    }
                }
            }, SECONDS_IN_MILIS, SECONDS_IN_MILIS)
        } else {
            cvPlayCameraView.takePicture()
        }
    }

    private fun saveToFile(imageByte: ByteArray) {
        val mCaptureNativeSize = cvPlayCameraView.pictureSize
        try {
            mCaptureNativeSize?.let {
                CameraUtils.decodeBitmap(imageByte, it.width, it.height) { bitmap ->
                    val cameraResultFile = ImageUtils.writeImageToTkpdPath(ImageUtils.DirectoryDef
                            .DIRECTORY_TOKOPEDIA_CACHE_CAMERA, imageByte, false)
                    onSuccessCaptureImageFromCamera(cameraResultFile)
                }
            }
        } catch (error: Throwable) {
            val cameraResultFile = ImageUtils.writeImageToTkpdPath(ImageUtils.DirectoryDef
                    .DIRECTORY_TOKOPEDIA_CACHE_CAMERA, imageByte, false)
            onSuccessCaptureImageFromCamera(cameraResultFile)
        }
    }

    private fun onSuccessCaptureImageFromCamera(cameraResultFile: File) {
        if (cameraResultFile.exists()) {
            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_IMAGE_URI, Uri.fromFile(cameraResultFile))
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun toggleFlash() {
        if (cvPlayCameraView.flash == Flash.OFF) {
            ivPlayCameraFlash.setImageDrawable(resources.getDrawable(R.drawable.ic_play_camera_off_flash))
            cvPlayCameraView.flash = Flash.ON
        } else {
            ivPlayCameraFlash.setImageDrawable(resources.getDrawable(R.drawable.ic_play_camera_on_flash))
            cvPlayCameraView.flash = Flash.OFF
        }
    }

    private fun reverseCamera() {
        if (cvPlayCameraView.facing == Facing.BACK) {
            cvPlayCameraView.facing = Facing.FRONT
        } else {
            cvPlayCameraView.facing = Facing.BACK
        }
    }

    private fun setImmediateCapture() {
        if (!isTimerRunning) {
            tvPlayCameraTimer0.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Neutral_N0))
            tvPlayCameraTimer5.setTextColor(resources.getColor(R.color.play_white_68))
            tvPlayCameraTimer10.setTextColor(resources.getColor(R.color.play_white_68))
            cameraTimerEnum = CameraTimerEnum.IMMEDIATE
            timeToCapture = cameraTimerEnum.seconds
        }
    }

    private fun setTimerFiveSecondsCapture() {
        if (!isTimerRunning) {
            tvPlayCameraTimer0.setTextColor(resources.getColor(R.color.play_white_68))
            tvPlayCameraTimer5.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Neutral_N0))
            tvPlayCameraTimer10.setTextColor(resources.getColor(R.color.play_white_68))
            cameraTimerEnum = CameraTimerEnum.FIVE_SECONDS
            timeToCapture = cameraTimerEnum.seconds
        }
    }

    private fun setTimerTenSecondsCapture() {
        if (!isTimerRunning) {
            tvPlayCameraTimer0.setTextColor(resources.getColor(R.color.play_white_68))
            tvPlayCameraTimer5.setTextColor(resources.getColor(R.color.play_white_68))
            tvPlayCameraTimer10.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Neutral_N0))
            cameraTimerEnum = CameraTimerEnum.TEN_SECONDS
            timeToCapture = cameraTimerEnum.seconds
        }
    }

    private fun showTimerLayout() {
        tvPlayCameraTimeToCapture.text = timeToCapture.toString()
        containerPlayCameraTimer.visibility = View.VISIBLE
    }

    private fun hideTimerLayout() {
        containerPlayCameraTimer.visibility = View.GONE
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),
                PERMISSION_CODE)
    }

    private fun isCameraPermissionGranted() =
            ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    companion object {
        const val EXTRA_IMAGE_URI = "EXTRA_IMAGE_URI"
        private const val SECONDS_IN_MILIS: Long = 1000

        private const val PERMISSION_CODE = 1010
    }

}
