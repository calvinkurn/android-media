package com.tokopedia.picker.ui.fragment.camera.component

import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.otaliastudios.cameraview.controls.Flash
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.common.component.UiComponent
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.picker.R
import com.tokopedia.picker.ui.PickerParam
import com.tokopedia.picker.ui.fragment.camera.recyclers.adapter.CameraSliderAdapter
import com.tokopedia.picker.ui.fragment.camera.recyclers.managers.SliderLayoutManager
import com.tokopedia.picker.utils.pickerLoadImage
import com.tokopedia.unifycomponents.dpToPx
import com.tokopedia.unifyprinciples.Typography
import java.io.File

class CameraControllerComponent(
    private val param: PickerParam,
    private val listener: Listener,
    parent: ViewGroup,
) : UiComponent(parent, R.id.uc_camera_controller)
    , ViewTreeObserver.OnScrollChangedListener
    , CameraSliderAdapter.Listener {

    private val lstCameraMode = findViewById<RecyclerView>(R.id.lst_camera_mode)
    private val lblCountDown = findViewById<Typography>(R.id.lbl_countdown)
    private val btnTakeCamera = findViewById<View>(R.id.btn_take_camera)
    private val btnFlash = findViewById<IconUnify>(R.id.btn_flash)
    private val btnFlip = findViewById<ImageView>(R.id.btn_flip)
    private val imgPreview = findViewById<ImageView>(R.id.img_preview)

    private val camerasMode = listOf("PHOTO", "VIDEO")

    private val adapter by lazy {
        CameraSliderAdapter(camerasMode, this)
    }

    init {
        btnFlash.setOnClickListener {
            listener.onFlashClicked()
        }

        btnFlip.setOnClickListener {
            listener.onFlipClicked()
        }

        btnTakeCamera.setOnClickListener {
            listener.onTakeMediaClicked()
        }

        imgPreview.setOnClickListener {

        }
    }

    fun scrollToPhotoMode() {
        lstCameraMode.smoothScrollToPosition(PHOTO_MODE)
    }

    fun scrollToVideoMode() {
        lstCameraMode.smoothScrollToPosition(VIDEO_MODE)
    }

    fun setupFlashButtonState(isFlashNotEmpty: Boolean, invoke: () -> Unit) {
        btnFlash.shouldShowWithAction(isFlashNotEmpty) {
            invoke()
        }
    }

    fun setThumbnailPreview(file: File) {
        if (!param.isMultipleSelection) return

        imgPreview.pickerLoadImage(file.path)
    }

    fun startRecording() {
        if (getActiveCameraMode() != VIDEO_MODE) return

        lblCountDown.show()
        lstCameraMode.hide()
    }

    fun stopRecording() {
        if (getActiveCameraMode() != VIDEO_MODE) return

        lblCountDown.hide()
        lstCameraMode.show()
        scrollToVideoMode()
    }

    override fun onItemClicked(view: View) {
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
            lstCameraMode.viewTreeObserver.removeOnScrollChangedListener(this)
        }
    }

    fun setupView() {
        when {
            param.isIncludeVideo -> {
                setupCameraSlider()
            }

            param.isOnlyVideo -> {
                videoModeUiState()
                listener.onCameraModeChanged(VIDEO_MODE)
            }

            else -> {
                photoModeUiState()
                listener.onCameraModeChanged(PHOTO_MODE)
            }
        }
    }

    private fun isPhotoMode() = getActiveCameraMode() == PHOTO_MODE

    private fun isVideoMode() = getActiveCameraMode() == VIDEO_MODE

    private fun setupCameraSlider() {
        lstCameraMode.show()
        lstCameraMode.viewTreeObserver.addOnScrollChangedListener(this)

        // Setting the padding such that the items will appear in the middle of the screen
        setPositionToCenterOfCameraMode()

        // layout manager & adapter
        lstCameraMode.layoutManager = SliderLayoutManager(context)
        lstCameraMode.adapter = adapter
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

    private fun getActiveCameraMode() = (lstCameraMode.layoutManager as LinearLayoutManager)
        .findLastCompletelyVisibleItemPosition()

    // TODO
    fun setCameraFlashUIState(enum: Int) {
        val colorWhite = ContextCompat.getColor(
            context,
            com.tokopedia.unifyprinciples.R.color.Unify_Static_White
        )

        when (enum) {
            Flash.AUTO.ordinal -> btnFlash.setImageDrawable(
                MethodChecker.getDrawable(context, R.drawable.ic_picker_camera_flash)
            )
            Flash.ON.ordinal -> btnFlash.setImage(
                IconUnify.FLASH_ON,
                colorWhite,
                colorWhite,
                colorWhite,
                colorWhite
            )
            Flash.OFF.ordinal -> btnFlash.setImage(
                IconUnify.FLASH_OFF,
                colorWhite,
                colorWhite,
                colorWhite,
                colorWhite
            )
        }
    }

    interface Listener {
        fun onCameraModeChanged(mode: Int)
        fun onTakeMediaClicked()
        fun onFlashClicked()
        fun onFlipClicked()
    }

    companion object {
        private const val HALF_SIZE_OF_CAMERA_MODE_ITEM = 40f

        const val PHOTO_MODE = 0
        const val VIDEO_MODE = 1
    }

}