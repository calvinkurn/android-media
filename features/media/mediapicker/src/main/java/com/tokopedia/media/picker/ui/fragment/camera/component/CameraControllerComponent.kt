package com.tokopedia.media.picker.ui.fragment.camera.component

import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.otaliastudios.cameraview.controls.Flash
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.R
import com.tokopedia.media.picker.ui.activity.main.PickerActivityListener
import com.tokopedia.media.picker.ui.fragment.camera.recyclers.adapter.CameraSliderAdapter
import com.tokopedia.media.picker.ui.fragment.camera.recyclers.managers.SliderLayoutManager
import com.tokopedia.media.picker.ui.uimodel.CameraSelectionUiModel
import com.tokopedia.media.picker.ui.widget.thumbnail.MediaThumbnailWidget
import com.tokopedia.media.picker.utils.anim.CameraButton.animStartRecording
import com.tokopedia.media.picker.utils.anim.CameraButton.animStopRecording
import com.tokopedia.picker.common.PickerParam
import com.tokopedia.picker.common.basecomponent.UiComponent
import com.tokopedia.picker.common.uimodel.MediaUiModel
import com.tokopedia.picker.common.utils.videoFormat
import com.tokopedia.picker.common.utils.visibleWithCondition
import com.tokopedia.unifycomponents.dpToPx
import com.tokopedia.unifyprinciples.Typography

class CameraControllerComponent(
    private val param: PickerParam,
    private val activityListener: PickerActivityListener?,
    private val controllerListener: Listener,
    parent: ViewGroup,
) : UiComponent(parent, R.id.uc_camera_controller)
    , ViewTreeObserver.OnScrollChangedListener
    , CameraSliderAdapter.Listener {

    private val adapter by lazy {
        CameraSliderAdapter(CameraSelectionUiModel.create(), this)
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

    init {
        setMaxDuration()

        btnTakeCamera.setOnClickListener {
            onTakeCamera()
        }

        btnFlash.setOnClickListener {
            controllerListener.onFlashClicked()
        }

        btnFlip.setOnClickListener {
            if (controllerListener.isFrontCamera()) {
                btnFlash.invisible()
            } else {
                btnFlash.show()
            }

            controllerListener.onFlipClicked()
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

        controllerListener.onCameraModeChanged(position)

        if (isPhotoMode()) {
            photoModeButtonState()
        } else if (isVideoMode()) {
            videoModeButtonState()
        }
    }

    override fun release() {
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
                controllerListener.onCameraModeChanged(VIDEO_MODE)
                videoModeButtonState()
            }
            else -> {
                controllerListener.onCameraModeChanged(PHOTO_MODE)
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
        imgThumbnail.setOnClickListener {
            controllerListener.onCameraThumbnailClicked()
        }
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

        btnTakeCamera.animStopRecording()
        videoDurationContainer.hide()
        cameraControl.show()
        lstCameraMode.show()
    }

    fun setVideoDuration(duration: String) {
        txtCountDown.text = duration
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

    fun isVideoMode() = getActiveCameraMode() == VIDEO_MODE

    private fun isPhotoMode() = getActiveCameraMode() == PHOTO_MODE

    private fun onTakeCamera() {
        if (isVideoMode() && activityListener?.isMinStorageThreshold() == true) {
            activityListener.onShowMinStorageThresholdToast()
            return
        }

        if (isVideoMode() && activityListener?.hasVideoLimitReached() == true) {
            activityListener.onShowVideoLimitReachedToast()
            return
        }

        if (activityListener?.hasMediaLimitReached() == true) {
            activityListener.onShowMediaLimitReachedToast()
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
        txtMaxDuration.text = param.maxVideoDuration()
            .toLong()
            .videoFormat()
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
        fun onCameraModeChanged(mode: Int)
        fun isFrontCamera(): Boolean

        fun onCameraThumbnailClicked()
        fun onTakeMediaClicked()
        fun onFlashClicked()
        fun onFlipClicked()
    }

    companion object {
        private const val HALF_SIZE_OF_CAMERA_MODE_ITEM = 30f

        const val PHOTO_MODE = 0
        const val VIDEO_MODE = 1
    }

}