package com.tokopedia.play.broadcaster.view.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraUtils
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.controls.Facing
import com.otaliastudios.cameraview.controls.Flash
import com.otaliastudios.cameraview.gesture.Gesture
import com.otaliastudios.cameraview.gesture.GestureAction
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.imagepicker.common.util.ImageUtils
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.CameraTimerEnum
import com.tokopedia.play.broadcaster.util.permission.PermissionHelperImpl
import com.tokopedia.play.broadcaster.util.permission.PermissionResultListener
import com.tokopedia.play.broadcaster.util.permission.PermissionStatusHandler
import com.tokopedia.play.broadcaster.view.custom.PlayTimerCountDown
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.requestApplyInsetsWhenAttached
import com.tokopedia.play_common.view.updateMargins
import java.io.File

class PlayCoverCameraActivity : AppCompatActivity() {

    private var cameraTimerEnum: CameraTimerEnum = CameraTimerEnum.Immediate
    private var isTimerRunning = false

    private val cvCamera by lazy { findViewById<CameraView>(R.id.cv_camera) }
    private val tvCancel by lazy { findViewById<TextView>(R.id.tv_cancel) }
    private val ivShutter by lazy { findViewById<ImageView>(R.id.iv_shutter) }
    private val ivFlash by lazy { findViewById<ImageView>(R.id.iv_flash) }
    private val ivReverse by lazy { findViewById<ImageView>(R.id.iv_reverse) }
    private val tvTimer0 by lazy { findViewById<TextView>(R.id.tv_timer_0) }
    private val tvTimer5 by lazy { findViewById<TextView>(R.id.tv_timer_5) }
    private val tvTimer10 by lazy { findViewById<TextView>(R.id.tv_timer_10) }
    
    private val groupAction by lazy { findViewById<Group>(R.id.group_action) }
    private val countDownTimer by lazy { findViewById<PlayTimerCountDown>(R.id.countdown_timer) }

    private val permissionHelper by lazy { PermissionHelperImpl(this) }

    private val cameraListener = object : CameraListener() {
        override fun onPictureTaken(result: PictureResult) {
            saveToFile(result.data)
        }
    }

    private var systemUiVisibility: Int
        get() = window.decorView.systemUiVisibility
        set(value) {
            window.decorView.systemUiVisibility = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_cover_camera)
        initView()
        setupView()
        setupInsets()
    }

    override fun onResume() {
        super.onResume()
        setLayoutFullScreen()
        if (isRequiredPermissionGranted()) cvCamera.open()
    }

    override fun onStart() {
        super.onStart()
        tvCancel.requestApplyInsetsWhenAttached()
        ivFlash.requestApplyInsetsWhenAttached()
    }

    override fun onPause() {
        super.onPause()
        cvCamera.close()
    }

    override fun onDestroy() {
        cvCamera.close()
        cvCamera.destroy()
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)) return
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun initView() {
        tvCancel.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        cvCamera.addCameraListener(cameraListener)
        ivShutter.setOnClickListener {
            takePicture()
        }
        ivFlash.setOnClickListener {
            toggleFlash()
        }
        ivReverse.setOnClickListener {
            reverseCamera()
        }
        tvTimer0.setOnClickListener {
            setImmediateCapture()
        }
        tvTimer5.setOnClickListener {
            setTimerFiveSecondsCapture()
        }
        tvTimer10.setOnClickListener {
            setTimerTenSecondsCapture()
        }
        cvCamera.mapGesture(Gesture.PINCH, GestureAction.ZOOM)
        cvCamera.mapGesture(Gesture.TAP, GestureAction.AUTO_FOCUS)
    }

    private fun setupView() {
        requestRequiredPermission()
    }

    private fun setupInsets() {
        tvCancel.doOnApplyWindowInsets { v, insets, _, margin ->
            val marginLayoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
            val newTopMargin = margin.top + insets.systemWindowInsetTop
            if (marginLayoutParams.topMargin != newTopMargin) {
                marginLayoutParams.updateMargins(top = newTopMargin)
                v.parent.requestLayout()
            }
        }

        ivFlash.doOnApplyWindowInsets { v, insets, _, margin ->
            val marginLayoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
            val newBottomMargin = margin.bottom + insets.systemWindowInsetBottom
            if (marginLayoutParams.bottomMargin != newBottomMargin) {
                marginLayoutParams.updateMargins(bottom = newBottomMargin)
                v.parent.requestLayout()
            }
        }
    }

    private fun setLayoutFullScreen() {
        systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    }

    private fun takePicture() {
        when (cameraTimerEnum) {
            CameraTimerEnum.Immediate -> cvCamera.takePicture()
            else -> {
                countDownTimer.visible()
                groupAction.gone()

                val animationProcess = PlayTimerCountDown.AnimationProperty.Builder()
                        .setTotalCount(cameraTimerEnum.seconds)
                        .build()

                countDownTimer.startCountDown(animationProcess, object : PlayTimerCountDown.Listener {
                    override fun onTick(milisUntilFinished: Long) {

                    }

                    override fun onFinish() {
                        cvCamera.takePicture()
                        countDownTimer.gone()
                    }
                })
            }
        }
    }

    private fun saveToFile(imageByte: ByteArray) {
        val mCaptureNativeSize = cvCamera.pictureSize
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
        if (cvCamera.flash == Flash.OFF) {
            ivFlash.setImageDrawable(MethodChecker.getDrawable(this, R.drawable.ic_play_camera_on_flash))
            cvCamera.flash = Flash.ON
        } else {
            ivFlash.setImageDrawable(MethodChecker.getDrawable(this, R.drawable.ic_play_camera_off_flash))
            cvCamera.flash = Flash.OFF
        }
    }

    private fun reverseCamera() {
        if (cvCamera.facing == Facing.BACK) {
            cvCamera.facing = Facing.FRONT
        } else {
            cvCamera.facing = Facing.BACK
        }
    }

    private fun setImmediateCapture() {
        if (!isTimerRunning) {
            tvTimer0.setTextColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Neutral_N0))
            tvTimer5.setTextColor(MethodChecker.getColor(this, R.color.play_white_68))
            tvTimer10.setTextColor(MethodChecker.getColor(this, R.color.play_white_68))
            cameraTimerEnum = CameraTimerEnum.Immediate
        }
    }

    private fun setTimerFiveSecondsCapture() {
        if (!isTimerRunning) {
            tvTimer0.setTextColor(MethodChecker.getColor(this, R.color.play_white_68))
            tvTimer5.setTextColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Neutral_N0))
            tvTimer10.setTextColor(MethodChecker.getColor(this, R.color.play_white_68))
            cameraTimerEnum = CameraTimerEnum.Five
        }
    }

    private fun setTimerTenSecondsCapture() {
        if (!isTimerRunning) {
            tvTimer0.setTextColor(MethodChecker.getColor(this, R.color.play_white_68))
            tvTimer5.setTextColor(MethodChecker.getColor(this, R.color.play_white_68))
            tvTimer10.setTextColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Neutral_N0))
            cameraTimerEnum = CameraTimerEnum.Ten
        }
    }

    private fun requestRequiredPermission() {
        permissionHelper.requestMultiPermissionsFullFlow(
                permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                requestCode = REQUEST_CODE_PERMISSION,
                permissionResultListener = object : PermissionResultListener {
                    override fun onRequestPermissionResult(): PermissionStatusHandler.() -> Unit {
                        return {
                            if (isAllGranted()) cvCamera.open()
                        }
                    }

                    override fun shouldShowRequestPermissionRationale(permissions: Array<String>, requestCode: Int): Boolean {
                        return false
                    }
                }
        )
    }

    private fun isRequiredPermissionGranted() = permissionHelper.isAllPermissionsGranted(
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
    )

    companion object {
        const val EXTRA_IMAGE_URI = "EXTRA_IMAGE_URI"

        private const val REQUEST_CODE_PERMISSION = 1010
    }

}
