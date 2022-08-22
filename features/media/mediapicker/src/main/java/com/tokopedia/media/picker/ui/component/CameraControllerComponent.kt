package com.tokopedia.media.picker.ui.component

import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.otaliastudios.cameraview.controls.Flash
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.R
import com.tokopedia.media.picker.ui.fragment.camera.CameraMode
import com.tokopedia.picker.common.mapper.humanize
import com.tokopedia.media.picker.ui.activity.picker.PickerActivityContract
import com.tokopedia.media.picker.ui.adapter.CameraSliderAdapter
import com.tokopedia.media.picker.ui.adapter.managers.SliderLayoutManager
import com.tokopedia.media.picker.ui.uimodel.CameraSelectionUiModel
import com.tokopedia.media.picker.ui.widget.thumbnail.MediaThumbnailWidget
import com.tokopedia.media.picker.utils.anim.CameraButton.animStartRecording
import com.tokopedia.media.picker.utils.anim.CameraButton.animStopRecording
import com.tokopedia.picker.common.PickerParam
import com.tokopedia.picker.common.basecomponent.UiComponent
import com.tokopedia.picker.common.uimodel.MediaUiModel
import com.tokopedia.unifycomponents.dpToPx
import com.tokopedia.unifyprinciples.Typography

class CameraControllerComponent(
    private val param: PickerParam,
    private val activityContract: PickerActivityContract?,
    private val controllerListener: Listener,
    parent: ViewGroup,
) : UiComponent(parent, R.id.uc_camera_controller), ViewTreeObserver.OnScrollChangedListener,
    CameraSliderAdapter.Listener {

    private val adapterData = CameraSelectionUiModel.create()

    private val adapter by lazy {
        CameraSliderAdapter(adapterData, this)
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

    // index of camera mode based on rv position
    private var cameraModeIndex = 0

    private var videoDurationTimer: CountDownTimer? = null

    init {
        setMaxDuration()

        btnTakeCamera.setOnClickListener {
            onTakeCamera()
        }

        btnFlash.setOnClickListener {
            controllerListener.onFlashClicked()
        }

        btnFlip.setOnClickListener {
            controllerListener.onFlipClicked()

            if (controllerListener.isFrontCamera()) {
                btnFlash.invisible()
            } else {
                btnFlash.show()
            }
        }
    }

    override fun onCameraSliderItemClicked(view: View) {
        val cameraIndex = lstCameraMode.getChildLayoutPosition(view)
        lstCameraMode.smoothScrollToPosition(cameraIndex)
        setCameraModeSelected(CameraMode.to(cameraIndex))
    }

    override fun onScrollChanged() {
        val position = getActiveCameraMode()
        if (position == RecyclerView.NO_POSITION) return

        val cameraMode = CameraMode.to(position)
        controllerListener.onCameraModeChanged(cameraMode)

        if (isPhotoMode()) {
            photoModeButtonState()
            setCameraModeSelected(CameraMode.Photo)
        } else if (isVideoMode()) {
            videoModeButtonState()
            setCameraModeSelected(CameraMode.Video)
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
                controllerListener.onCameraModeChanged(CameraMode.Video)
                videoModeButtonState()
            }
            else -> {
                controllerListener.onCameraModeChanged(CameraMode.Photo)
                photoModeButtonState()
            }
        }
    }

    fun scrollToPhotoMode() {
        lstCameraMode.smoothScrollToPosition(CameraMode.Photo.value)
    }

    fun scrollToVideoMode() {
        lstCameraMode.smoothScrollToPosition(CameraMode.Video.value)
    }

    fun setThumbnailPreview(model: MediaUiModel) {
        if (!param.isMultipleSelectionType()) return
        imgThumbnail.smallThumbnail(model) {
            controllerListener.onThumbnailLoaded()
        }
        imgThumbnail.setOnClickListener {
            controllerListener.onCameraThumbnailClicked()
        }
    }

    fun removeThumbnailPreview() {
        imgThumbnail.removeWidget()
    }

    fun startRecording() {
        if (getActiveCameraMode() != CameraMode.Video.value && !param.isIncludeVideoFile()) return

        btnTakeCamera.animStartRecording()
        videoDurationContainer.show()
        cameraControl.hide()
        lstCameraMode.hide()
    }

    fun stopRecording() {
        if (getActiveCameraMode() != CameraMode.Video.value) return
        if (param.isIncludeVideoFile()) scrollToVideoMode()

        resetVideoDuration()

        btnTakeCamera.animStopRecording()
        videoDurationContainer.hide()
        cameraControl.show()
        lstCameraMode.show()
    }

    fun isFlashSupported(isShown: Boolean) {
        btnFlash.visibleWithCondition(isShown)
    }

    fun hasFrontCamera(isShown: Boolean) {
        btnFlip.visibleWithCondition(isShown)
    }

    fun setFlashMode(state: Int) {
        btnFlash.setImageResource(
            when (state) {
                Flash.ON.ordinal -> R.drawable.picker_ic_camera_flash_on
                Flash.OFF.ordinal -> R.drawable.picker_ic_camera_flash_off
                else -> R.drawable.picker_ic_camera_flash_auto
            }
        )
    }

    fun onVideoDurationChanged() {
        resetVideoDuration()

        val maxDuration = param.maxVideoDuration()

        videoDurationTimer = object : CountDownTimer(
            param.maxVideoDuration().toLong(),
            COUNTDOWN_INTERVAL
        ) {
            override fun onTick(milis: Long) {
                Handler(Looper.getMainLooper()).post {
                    val duration = (maxDuration - milis).toInt()
                    txtCountDown.text = duration.humanize()
                }
            }

            override fun onFinish() {
                stopRecording()
            }
        }

        videoDurationTimer?.start()
    }

    fun isVideoMode() = getActiveCameraMode() == CameraMode.Video.value

    private fun isPhotoMode() = getActiveCameraMode() == CameraMode.Photo.value

    private fun resetVideoDuration() {
        try {
            videoDurationTimer?.cancel()
            videoDurationTimer = null
        } catch (t: Throwable) {
        }
    }

    private fun onTakeCamera() {
        if (isVideoMode() && activityContract?.hasVideoLimitReached() == true) {
            activityContract.onShowVideoLimitReachedCameraToast()
            return
        }

        if (activityContract?.hasMediaLimitReached() == true) {
            activityContract.onShowMediaLimitReachedCameraToast()
            return
        }

        controllerListener.onTakeMediaClicked()
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

    private fun setMaxDuration() {
        txtMaxDuration.text = param.maxVideoDuration().humanize()
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

    private fun setCameraModeSelected(mode: CameraMode){
        val cameraIndex = CameraMode.to(mode)

        if (cameraIndex == cameraModeIndex) return
        updateCameraModeRecyclerItem(cameraModeIndex, false)
        cameraModeIndex = cameraIndex
        updateCameraModeRecyclerItem(cameraModeIndex, true)
    }

    private fun updateCameraModeRecyclerItem(index: Int, recyclerItemState: Boolean) {
        adapterData[index].isSelected = recyclerItemState
        adapter.notifyItemChanged(index)
    }

    private fun getActiveCameraMode(): Int {
        return if (param.isOnlyVideoFile()) {
            CameraMode.Video.value
        } else {
            (lstCameraMode.layoutManager as LinearLayoutManager)
                .findLastCompletelyVisibleItemPosition()
        }
    }

    interface Listener {
        fun onCameraModeChanged(mode: CameraMode)
        fun isFrontCamera(): Boolean

        fun onCameraThumbnailClicked()
        fun onTakeMediaClicked()
        fun onFlashClicked()
        fun onFlipClicked()
        fun onThumbnailLoaded()
    }

    companion object {
        private const val HALF_SIZE_OF_CAMERA_MODE_ITEM = 30f
        private const val COUNTDOWN_INTERVAL = 1000L
    }

}