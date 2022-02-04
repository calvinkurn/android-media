package com.tokopedia.picker.ui.fragment.camera.component

import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common.component.UiComponent
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.picker.R
import com.tokopedia.picker.ui.PickerParam
import com.tokopedia.picker.ui.fragment.camera.recyclers.adapter.CameraSliderAdapter
import com.tokopedia.picker.ui.fragment.camera.recyclers.managers.SliderLayoutManager
import com.tokopedia.picker.ui.uimodel.MediaUiModel
import com.tokopedia.picker.ui.uimodel.internal.CameraSelectionMode
import com.tokopedia.picker.ui.widget.thumbnail.MediaThumbnailWidget
import com.tokopedia.picker.utils.anim.CameraButton.animStartRecording
import com.tokopedia.picker.utils.anim.CameraButton.animStopRecording
import com.tokopedia.picker.utils.toVideoDurationFormat
import com.tokopedia.unifycomponents.dpToPx
import com.tokopedia.unifyprinciples.Typography

class CameraControllerComponent(
    private val param: PickerParam,
    private val listener: Listener,
    parent: ViewGroup,
) : UiComponent(parent, R.id.uc_camera_controller)
    , ViewTreeObserver.OnScrollChangedListener
    , CameraSliderAdapter.Listener {

    private val adapter by lazy {
        CameraSliderAdapter(CameraSelectionMode.create(), this)
    }

    // camera mode slider
    private val lstCameraMode = findViewById<RecyclerView>(R.id.lst_camera_mode)

    // camera mode container
    private val cameraControl = findViewById<LinearLayout>(R.id.camera_control)

    // video duration
    private val txtCountDown = findViewById<Typography>(R.id.txt_countdown)
    private val txtMaxDuration = findViewById<Typography>(R.id.txt_max_duration)
    private val videoDurationContainer = findViewById<LinearLayout>(R.id.video_duration)

    // image thumbnail
    private val imgThumbnail = findViewById<MediaThumbnailWidget>(R.id.img_thumbnail)

    // action button
    private val btnTakeCamera = findViewById<View>(R.id.btn_take_camera)
    private val btnFlash = findViewById<ImageView>(R.id.btn_flash)
    private val btnFlip = findViewById<ImageView>(R.id.btn_flip)

    private var videoDurationTimer: CountDownTimer? = null

    init {
        setMaxDuration()

        btnTakeCamera.setOnClickListener {
            onTakeCamera()
        }

        btnFlash.setOnClickListener {
            listener.onFlashClicked()
        }

        btnFlip.setOnClickListener {
            if (listener.isFacingCameraIsFront()) {
                btnFlash.invisible()
            } else {
                btnFlash.show()
            }

            listener.onFlipClicked()
        }
    }

    override fun onCameraSliderItemClicked(view: View) {
        lstCameraMode.smoothScrollToPosition(
            lstCameraMode.getChildLayoutPosition(view)
        )
    }

    override fun onScrollChanged() {
        val position = getActiveCameraMode()

        if (position == RecyclerView.NO_POSITION) return

        listener.onCameraModeChanged(position)

        if (isPhotoMode()) {
            photoModeButtonState()
        } else if (isVideoMode()) {
            videoModeButtonState()
        }
    }

    override fun release() {
        resetVideoDuration()

        if (param.isIncludeVideoFile()) {
            lstCameraMode.viewTreeObserver.removeOnScrollChangedListener(this)
        }
    }

    fun setupView() {
        when {
            param.isIncludeVideoFile() -> {
                setupCameraSlider()
            }
            param.isOnlyVideoFile() -> {
                listener.onCameraModeChanged(VIDEO_MODE)
                videoModeButtonState()
            }
            else -> {
                listener.onCameraModeChanged(PHOTO_MODE)
                photoModeButtonState()
            }
        }
    }

    fun scrollToPhotoMode() {
        lstCameraMode.smoothScrollToPosition(PHOTO_MODE)
    }

    fun scrollToVideoMode() {
        lstCameraMode.smoothScrollToPosition(VIDEO_MODE)
    }

    fun setThumbnailPreview(model: MediaUiModel) {
        if (!param.isMultipleSelectionType()) return
        imgThumbnail.smallThumbnail(model)
    }

    fun removeThumbnailPreview() {
        imgThumbnail.removeWidget()
    }

    fun startRecording() {
        if (getActiveCameraMode() != VIDEO_MODE && !param.isIncludeVideoFile()) return

        btnTakeCamera.animStartRecording()
        videoDurationContainer.show()
        cameraControl.hide()
        lstCameraMode.hide()
    }

    fun stopRecording() {
        if (getActiveCameraMode() != VIDEO_MODE) return
        if (param.isIncludeVideoFile()) scrollToVideoMode()

        resetVideoDuration()
        btnTakeCamera.animStopRecording()
        videoDurationContainer.hide()
        cameraControl.show()
        lstCameraMode.show()
    }

    fun onVideoDurationChanged() {
        resetVideoDuration()

        val maxDuration = param.maxVideoDuration().toLong()

        videoDurationTimer = object : CountDownTimer(
            param.maxVideoDuration().toLong(),
            COUNTDOWN_INTERVAL
        ) {
            override fun onTick(millisUntilFinished: Long) {
                Handler(context.mainLooper).post {
                    val duration = (maxDuration - millisUntilFinished) / 1000
                    txtCountDown.text = duration.toVideoDurationFormat()
                }
            }

            override fun onFinish() {
                stopRecording()
            }
        }

        videoDurationTimer?.start()
    }

    fun isFlashSupported(value: Boolean) {
        if (value) btnFlash.show() else btnFlash.invisible()
    }

    fun hasFrontCamera(value: Boolean) {
        if (value) btnFlip.show() else btnFlip.invisible()
    }

    fun setFlashMode(isFlashOn: Boolean) {
        if (isFlashOn) {
            btnFlash.setImageResource(R.drawable.picker_ic_camera_flash_on)
        } else {
            btnFlash.setImageResource(R.drawable.picker_ic_camera_flash_off)
        }
    }

    private fun onTakeCamera() {
        if (isVideoMode() && listener.hasVideoAddedOnMediaSelection()) {
            listener.onShowToastMediaLimit()
            return
        }

        if (listener.hasReachedLimit()) {
            listener.onShowToastVideoLimit()
            return
        }

        listener.onTakeMediaClicked()
    }

    private fun setupCameraSlider() {
        lstCameraMode.show()
        lstCameraMode.viewTreeObserver.addOnScrollChangedListener(this)

        // Setting the padding such that the items will appear in the middle of the screen
        setPositionToCenterOfCameraMode()

        // layout manager & adapter
        lstCameraMode.layoutManager = SliderLayoutManager(context)
        lstCameraMode.adapter = adapter
    }

    private fun resetVideoDuration() {
        try {
            videoDurationTimer?.cancel()
            videoDurationTimer = null
        } catch (t: Throwable) {}
    }

    private fun isPhotoMode() = getActiveCameraMode() == PHOTO_MODE

    private fun isVideoMode() = getActiveCameraMode() == VIDEO_MODE

    private fun setMaxDuration() {
        txtMaxDuration.text = param.maxVideoDuration()
            .toLong()
            .toVideoDurationFormat()
    }

    private fun setPositionToCenterOfCameraMode() {
        val padding = (getScreenWidth() / 2 - HALF_SIZE_OF_CAMERA_MODE_ITEM.dpToPx()).toInt()
        lstCameraMode.setPadding(padding, 0, padding, 0)
    }

    private fun photoModeButtonState() {
        btnTakeCamera.setBackgroundResource(R.drawable.bg_picker_camera_take_photo)
    }

    private fun videoModeButtonState() {
        btnTakeCamera.setBackgroundResource(R.drawable.bg_picker_camera_take_video)
    }

    private fun getActiveCameraMode()
        = (lstCameraMode.layoutManager as LinearLayoutManager)
        .findLastCompletelyVisibleItemPosition()

    interface Listener {
        fun hasVideoAddedOnMediaSelection(): Boolean
        fun hasReachedLimit(): Boolean

        fun onShowToastVideoLimit()
        fun onShowToastMediaLimit()

        fun onCameraModeChanged(mode: Int)
        fun isFacingCameraIsFront(): Boolean
        fun onTakeMediaClicked()
        fun onFlashClicked()
        fun onFlipClicked()
    }

    companion object {
        private const val HALF_SIZE_OF_CAMERA_MODE_ITEM = 30f
        private const val COUNTDOWN_INTERVAL = 1000L
        private const val COUNTDOWN_PERIOD = 1L

        const val PHOTO_MODE = 0
        const val VIDEO_MODE = 1
    }

}