package com.tokopedia.play.broadcaster.view.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.controls.Facing
import com.otaliastudios.cameraview.controls.Flash
import com.otaliastudios.cameraview.gesture.Gesture
import com.otaliastudios.cameraview.gesture.GestureAction
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.common.ui.model.orUnknown
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.analytic.setup.cover.picker.PlayBroCoverPickerAnalytic
import com.tokopedia.play.broadcaster.di.DaggerActivityRetainedComponent
import com.tokopedia.play.broadcaster.di.PlayBroadcastModule
import com.tokopedia.play.broadcaster.ui.model.CameraTimerEnum
import com.tokopedia.play.broadcaster.ui.model.page.PlayBroPageSource
import com.tokopedia.play.broadcaster.util.delegate.retainedComponent
import com.tokopedia.play.broadcaster.util.permission.PermissionHelper
import com.tokopedia.play.broadcaster.util.permission.PermissionHelperImpl
import com.tokopedia.play.broadcaster.util.permission.PermissionResultListener
import com.tokopedia.play.broadcaster.util.permission.PermissionStatusHandler
import com.tokopedia.play.broadcaster.view.custom.PlayCameraView
import com.tokopedia.play.broadcaster.view.custom.PlayTimerCountDown
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.requestApplyInsetsWhenAttached
import com.tokopedia.play_common.view.updateMargins
import com.tokopedia.utils.image.ImageProcessingUtil
import java.io.File
import javax.inject.Inject

class PlayCoverCameraActivity : AppCompatActivity() {

    private val retainedComponent by retainedComponent {
        DaggerActivityRetainedComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .playBroadcastModule(PlayBroadcastModule(this))
            .build()
    }

    private var cameraTimerEnum: CameraTimerEnum = CameraTimerEnum.Immediate
    private var isTimerRunning = false

    private val account by lazy(LazyThreadSafetyMode.NONE) {
        (intent.getSerializableExtra(EXTRA_ACCOUNT) as? ContentAccountUiModel).orUnknown()
    }

    private val pageSource by lazy(LazyThreadSafetyMode.NONE) {
        PlayBroPageSource.getByValue(intent.getStringExtra(EXTRA_PAGE_SOURCE).orEmpty())
    }

    @Inject
    lateinit var analytic: PlayBroCoverPickerAnalytic

    private val cvCamera by lazy { findViewById<PlayCameraView>(R.id.cv_camera) }
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
        inject()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_cover_camera)
        initView()
        setupView()
        setupInsets()
    }

    override fun onStart() {
        super.onStart()
        tvCancel.requestApplyInsetsWhenAttached()
        ivFlash.requestApplyInsetsWhenAttached()
        analytic.openCameraScreenToAddCover(account, pageSource)
    }

    override fun onResume() {
        super.onResume()
        setLayoutFullScreen()
        openCamera()
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
        cvCamera.setListener(object : PlayCameraView.Listener {
            override fun onCameraInstantiated() {
                openCamera()
            }
        })

        tvCancel.setOnClickListener {
            analytic.clickCancelOnCameraPage(account, pageSource)
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        cvCamera.addCameraListener(cameraListener)
        ivShutter.setOnClickListener {
            takePicture()
            analytic.clickCaptureFromCameraPage(account, pageSource)
        }
        ivFlash.setOnClickListener {
            toggleFlash()
        }
        ivReverse.setOnClickListener {
            reverseCamera()
            analytic.clickSwitchCameraOnCameraPage(account, pageSource)
        }
        tvTimer0.setOnClickListener {
            setImmediateCapture()
            analytic.clickTimerCameraOnCameraPage(account, pageSource, CameraTimerEnum.Immediate.seconds)
        }
        tvTimer5.setOnClickListener {
            setTimerFiveSecondsCapture()
            analytic.clickTimerCameraOnCameraPage(account, pageSource, CameraTimerEnum.Five.seconds)
        }
        tvTimer10.setOnClickListener {
            setTimerTenSecondsCapture()
            analytic.clickTimerCameraOnCameraPage(account, pageSource, CameraTimerEnum.Ten.seconds)
        }
        cvCamera.mapGesture(Gesture.PINCH, GestureAction.ZOOM)
        cvCamera.mapGesture(Gesture.TAP, GestureAction.AUTO_FOCUS)
    }

    private fun setupView() {
        requestRequiredPermission()
    }

    private fun inject() {
        retainedComponent.inject(this)
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

    private fun openCamera() {
        if (isRequiredPermissionGranted()) cvCamera.open()
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

                countDownTimer.startCountDown(
                    animationProcess,
                    object : PlayTimerCountDown.Listener {
                        override fun onTick(milisUntilFinished: Long) {
                        }

                        override fun onFinish() {
                            cvCamera.takePicture()
                            countDownTimer.gone()
                        }
                    }
                )
            }
        }
    }

    private fun saveToFile(imageByte: ByteArray) {
        val cameraResultFile = ImageProcessingUtil.writeImageToTkpdPath(imageByte, Bitmap.CompressFormat.JPEG)
        if (cameraResultFile != null) {
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
            tvTimer0.setTextColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_Static_White))
            tvTimer5.setTextColor(MethodChecker.getColor(this, R.color.play_dms_white_68))
            tvTimer10.setTextColor(MethodChecker.getColor(this, R.color.play_dms_white_68))
            cameraTimerEnum = CameraTimerEnum.Immediate
        }
    }

    private fun setTimerFiveSecondsCapture() {
        if (!isTimerRunning) {
            tvTimer0.setTextColor(MethodChecker.getColor(this, R.color.play_dms_white_68))
            tvTimer5.setTextColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_Static_White))
            tvTimer10.setTextColor(MethodChecker.getColor(this, R.color.play_dms_white_68))
            cameraTimerEnum = CameraTimerEnum.Five
        }
    }

    private fun setTimerTenSecondsCapture() {
        if (!isTimerRunning) {
            tvTimer0.setTextColor(MethodChecker.getColor(this, R.color.play_dms_white_68))
            tvTimer5.setTextColor(MethodChecker.getColor(this, R.color.play_dms_white_68))
            tvTimer10.setTextColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_Static_White))
            cameraTimerEnum = CameraTimerEnum.Ten
        }
    }

    private fun requestRequiredPermission() {
        permissionHelper.requestMultiPermissionsFullFlow(
                permissions = arrayOf(Manifest.permission.CAMERA, PermissionHelper.READ_EXTERNAL_STORAGE),
                requestCode = REQUEST_CODE_PERMISSION,
                permissionResultListener = object : PermissionResultListener {
                    override fun onRequestPermissionResult(): PermissionStatusHandler {
                        return {
                            if (isAllGranted()) cvCamera.open()
                        }
                    }

                override fun onShouldShowRequestPermissionRationale(permissions: Array<String>, requestCode: Int): Boolean {
                    return false
                }
            }
        )
    }

    private fun isRequiredPermissionGranted() = permissionHelper.isAllPermissionsGranted(
            arrayOf(
                Manifest.permission.CAMERA,
                PermissionHelper.READ_EXTERNAL_STORAGE
            )
    )

    companion object {
        const val EXTRA_IMAGE_URI = "EXTRA_IMAGE_URI"

        private const val EXTRA_PAGE_SOURCE = "EXTRA_PAGE_SOURCE"
        private const val EXTRA_ACCOUNT = "EXTRA_ACCOUNT"

        private const val REQUEST_CODE_PERMISSION = 1010

        fun getIntent(
            context: Context,
            pageSource: PlayBroPageSource,
            account: ContentAccountUiModel,
        ): Intent {
            return Intent(context, PlayCoverCameraActivity::class.java).apply {
                putExtra(EXTRA_PAGE_SOURCE, pageSource.value)
                putExtra(EXTRA_ACCOUNT, account)
            }
        }
    }
}
