package com.tokopedia.picker.ui.fragment.camera.component

import android.annotation.SuppressLint
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
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.picker.R
import com.tokopedia.picker.ui.PickerParam
import com.tokopedia.picker.ui.PickerUiConfig
import com.tokopedia.picker.ui.fragment.camera.recyclers.adapter.CameraSliderAdapter
import com.tokopedia.picker.ui.fragment.camera.recyclers.managers.SliderLayoutManager
import com.tokopedia.picker.utils.DEFAULT_DURATION_LABEL
import com.tokopedia.picker.utils.anim.CameraButton.animStartRecording
import com.tokopedia.picker.utils.anim.CameraButton.animStopRecording
import com.tokopedia.picker.utils.pickerLoadImage
import com.tokopedia.picker.utils.videoDurationLabel
import com.tokopedia.unifycomponents.dpToPx
import com.tokopedia.unifyprinciples.Typography
import java.io.File
import java.util.*

class CameraControllerComponent(
    private val param: PickerParam,
    private val listener: Listener,
    parent: ViewGroup,
) : UiComponent(parent, R.id.uc_camera_controller)
    , ViewTreeObserver.OnScrollChangedListener
    , CameraSliderAdapter.Listener {

    private val adapter by lazy {
        CameraSliderAdapter(cameraModeList(), this)
    }

    // camera mode slider
    private val lstCameraMode = findViewById<RecyclerView>(R.id.lst_camera_mode)

    // video duration
    private val txtCountDown = findViewById<Typography>(R.id.txt_countdown)
    private val txtMaxDuration = findViewById<Typography>(R.id.txt_max_duration)
    private val videoDurationContainer = findViewById<LinearLayout>(R.id.video_duration)

    // image preview
    private val imgPreview = findViewById<ImageView>(R.id.img_preview)

    // action button
    private val btnTakeCamera = findViewById<View>(R.id.btn_take_camera).apply {
        setOnClickListener {
            onTakeCamera()
        }
    }

    private val btnFlash = findViewById<ImageView>(R.id.btn_flash).apply {
        setOnClickListener {
            listener.onFlashClicked()
        }
    }

    private val btnFlip = findViewById<ImageView>(R.id.btn_flip).apply {
        setOnClickListener {
            if (listener.isFrontFacingCamera()) {
                btnFlash.hide()
            } else {
                btnFlash.show()
            }

            listener.onFlipClicked()
        }
    }

    private var countDownTimer: Timer? = null
    private var countDownMills = 0L

    init {
        setMaxDuration()
    }

    override fun onCameraSliderItemClicked(view: View) {
        lstCameraMode.smoothScrollToPosition(
            lstCameraMode.getChildLayoutPosition(view)
        )
    }

    override fun onScrollChanged() {
        if (getActiveCameraMode() == RecyclerView.NO_POSITION) return
        listener.onCameraModeChanged(getActiveCameraMode())

        if (isPhotoMode()) {
            photoModeUiState()
        } else if (isVideoMode()) {
            videoModeUiState()
        }
    }

    override fun release() {
        if (param.isIncludeVideo) {
            lstCameraMode
                .viewTreeObserver
                .removeOnScrollChangedListener(this)
        }
    }

    fun setupView() {
        when {
            param.isIncludeVideo -> {
                setupCameraSlider()
            }
            param.isOnlyVideo -> {
                listener.onCameraModeChanged(VIDEO_MODE)
                videoModeUiState()
            }
            else -> {
                listener.onCameraModeChanged(PHOTO_MODE)
                photoModeUiState()
            }
        }
    }

    fun scrollToPhotoMode() {
        lstCameraMode.smoothScrollToPosition(PHOTO_MODE)
    }

    fun scrollToVideoMode() {
        lstCameraMode.smoothScrollToPosition(VIDEO_MODE)
    }

    fun setThumbnailPreview(file: File) {
        if (!param.isMultipleSelection) return
        imgPreview.pickerLoadImage(file.path)
        imgPreview.show()
    }

    fun removeThumbnailPreview() {
        imgPreview.hide()
    }

    fun startRecording() {
        if (getActiveCameraMode() != VIDEO_MODE && !param.isIncludeVideo) return

        btnTakeCamera.animStartRecording()
        videoDurationContainer.show()
        lstCameraMode.hide()
    }

    fun stopRecording() {
        if (getActiveCameraMode() != VIDEO_MODE) return
        if (param.isIncludeVideo) scrollToVideoMode()

        resetVideoDuration()
        countDownTimer?.cancel()
        btnTakeCamera.animStopRecording()
        videoDurationContainer.hide()
        lstCameraMode.show()
    }

    fun onObserveVideoDuration(invoke: (Long) -> Unit) {
        countDownTimer = Timer()

        countDownTimer?.schedule(object : TimerTask() {
            override fun run() {
                if (countDownMills.toInt() == param.maxVideoDuration) {
                    countDownTimer?.cancel()
                    return
                }

                invoke(countDownMills)
                countDownMills += COUNTDOWN_INTERVAL
            }
        }, COUNTDOWN_PERIOD.toLong(), COUNTDOWN_INTERVAL.toLong())
    }

    @SuppressLint("SetTextI18n")
    fun onChangeVideoDuration(duration: Long) {
        txtCountDown.text = "${videoDurationLabel(duration)} /"
    }

    fun isFlashSupported(value: Boolean) {
        btnFlash.showWithCondition(value)
    }

    fun hasFrontCamera(value: Boolean) {
        btnFlip.showWithCondition(value)
    }

    fun setFlashMode(isFlashOn: Boolean) {
        if (isFlashOn) {
            btnFlash.setImageResource(R.drawable.picker_ic_camera_flash_on)
        } else {
            btnFlash.setImageResource(R.drawable.picker_ic_camera_flash_off)
        }
    }

    private fun cameraModeList() = listOf(
        context.getString(R.string.picker_camera_picture_mode),
        context.getString(R.string.picker_camera_video_mode)
    )

    private fun onTakeCamera() {
        if (isVideoMode() && PickerUiConfig.hasAtLeastOneVideoOnGlobalSelection()) {
            listener.hasVideoAddedOnMediaSelection()
            return
        }

        if (PickerUiConfig.mediaSelectionList().size >= param.limit) {
            listener.hasReachedLimit()
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
        txtCountDown.text = DEFAULT_DURATION_LABEL
    }

    private fun isPhotoMode() = getActiveCameraMode() == PHOTO_MODE

    private fun isVideoMode() = getActiveCameraMode() == VIDEO_MODE

    private fun setMaxDuration() {
        txtMaxDuration.text = videoDurationLabel(param.maxVideoDuration.toLong())
    }

    private fun setPositionToCenterOfCameraMode() {
        val padding = (getScreenWidth() / 2 - HALF_SIZE_OF_CAMERA_MODE_ITEM.dpToPx()).toInt()
        lstCameraMode.setPadding(padding, 0, padding, 0)
    }

    private fun photoModeUiState() {
        btnTakeCamera.setBackgroundResource(R.drawable.bg_picker_camera_take_photo)
    }

    private fun videoModeUiState() {
        btnTakeCamera.setBackgroundResource(R.drawable.bg_picker_camera_take_video)
    }

    private fun getActiveCameraMode()
        = (lstCameraMode.layoutManager as LinearLayoutManager)
        .findLastCompletelyVisibleItemPosition()

    interface Listener {
        fun hasVideoAddedOnMediaSelection()
        fun hasReachedLimit()

        fun onCameraModeChanged(mode: Int)
        fun isFrontFacingCamera(): Boolean
        fun onTakeMediaClicked()
        fun onFlashClicked()
        fun onFlipClicked()
    }

    companion object {
        private const val HALF_SIZE_OF_CAMERA_MODE_ITEM = 40f
        private const val COUNTDOWN_INTERVAL = 1000
        private const val COUNTDOWN_PERIOD = 1

        const val PHOTO_MODE = 0
        const val VIDEO_MODE = 1
    }

}