package com.tokopedia.picker.ui.fragment.camera.component

import android.view.ViewGroup
import android.widget.Space
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.LifecycleOwner
import com.otaliastudios.cameraview.*
import com.otaliastudios.cameraview.controls.Audio
import com.otaliastudios.cameraview.controls.Facing
import com.otaliastudios.cameraview.controls.Flash
import com.otaliastudios.cameraview.controls.Mode
import com.otaliastudios.cameraview.gesture.Gesture
import com.otaliastudios.cameraview.gesture.GestureAction
import com.otaliastudios.cameraview.size.AspectRatio
import com.otaliastudios.cameraview.size.Size
import com.otaliastudios.cameraview.size.SizeSelectors
import com.tokopedia.common.component.UiComponent
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.picker.R
import com.tokopedia.picker.ui.PickerParam
import com.tokopedia.picker.utils.MediaFileUtils
import com.tokopedia.picker.utils.exceptionHandler

class CameraPreviewComponent(
    private val param: PickerParam,
    private val listener: Listener,
    parent: ViewGroup,
) : UiComponent(parent, R.id.uc_camera_preview) {

    private val spaceToolBar = findViewById<Space>(R.id.space_toolbar)

    private val cameraView = findViewById<CameraView>(R.id.cameraView).apply {
        if (!param.ratioIsSquare()) {
            fullScreenCameraView()
        } else {
            squareRatioCameraView()
            squareRatioOfCameraSize()
        }
    }

    fun setupView(owner: LifecycleOwner) {
        cameraView.clearCameraListeners()
        cameraView.setLifecycleOwner(owner)
        cameraView.addCameraListener(cameraViewListener())
        cameraView.mapGesture(Gesture.TAP, GestureAction.AUTO_FOCUS)
    }

    fun onStartTakePicture() {
        cameraView.set(Mode.PICTURE)
        cameraView.takePictureSnapshot()
    }

    fun onStartTakeVideo() {
        cameraView.set(Mode.VIDEO)
        cameraView.set(Audio.ON)

        cameraView.takeVideoSnapshot(
            MediaFileUtils.createMediaFile(false),
            param.maxVideoDuration
        )
    }

    fun open() {
        exceptionHandler {
            cameraView.open()
        }
    }

    fun close() {
        exceptionHandler {
            cameraView.close()
        }
    }

    fun toggleFacing() {
        cameraView.toggleFacing()
    }

    fun stopVideo() {
        cameraView.stopVideo()
    }

    fun isFacingCameraIsFront() : Boolean {
        return cameraView.facing == Facing.FRONT
    }

    fun pictureSize(): Size? {
        return cameraView.pictureSize
    }

    fun isVideoMode() = cameraView.mode == Mode.VIDEO

    fun isTakingPicture() = cameraView.isTakingPicture

    fun isTakingVideo() = cameraView.isTakingVideo

    fun enableFlashTorch() {
        if (hasFlashFeatureOnCamera() && listener.isCameraFlashOn()) {
            cameraView.flash = Flash.TORCH
        }
    }

    fun hasFrontCamera() = cameraView
        .cameraOptions
        ?.supportedFacing
        ?.isNotEmpty() == true

    fun hasFlashFeatureOnCamera() = cameraView
        .cameraOptions
        ?.supportedFlash
        ?.contains(Flash.TORCH) == true

    private fun fullScreenCameraView() {
        spaceToolBar.hide()

        val parent = componentView() as ConstraintLayout

        ConstraintSet().apply {
            clone(parent)
            connect(
                R.id.cameraView,
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM,
                0
            )
        }.applyTo(parent)
    }

    private fun squareRatioCameraView() {
        spaceToolBar.show()

        val parent = componentView() as ConstraintLayout

        ConstraintSet().apply {
            clone(parent)
            setDimensionRatio(R.id.cameraView, CONSTRAINT_SQUARE_RATIO)
        }.applyTo(parent)
    }

    private fun CameraView.squareRatioOfCameraSize() {
        val sizeSelector = SizeSelectors.aspectRatio(AspectRatio.of(1, 1), 0f)
        this.setPictureSize(sizeSelector)
        this.setVideoSize(sizeSelector)
    }

    private fun cameraViewListener() = object : CameraListener() {
        override fun onCameraOpened(options: CameraOptions) {
            super.onCameraOpened(options)
            listener.onCameraOpened(options)
        }

        override fun onVideoRecordingStart() {
            super.onVideoRecordingStart()
            listener.onVideoRecordingStart()
        }

        override fun onVideoRecordingEnd() {
            super.onVideoRecordingEnd()
            disableFlashTorch()
            listener.onVideoRecordingEnd()
        }

        override fun onVideoTaken(result: VideoResult) {
            super.onVideoTaken(result)
            disableFlashTorch()
            listener.onVideoTaken(result)
        }

        override fun onPictureTaken(result: PictureResult) {
            super.onPictureTaken(result)
            disableFlashTorch()
            listener.onPictureTaken(result)
        }

        override fun onCameraError(exception: CameraException) {
            super.onCameraError(exception)
            disableFlashTorch()
        }
    }

    private fun disableFlashTorch() {
        if (hasFlashFeatureOnCamera() && listener.isCameraFlashOn()) {
            cameraView.flash = Flash.OFF
        }
    }

    override fun release() {
        exceptionHandler {
            cameraView.destroy()
        }
    }

    interface Listener {
        fun isCameraFlashOn(): Boolean
        fun onCameraOpened(options: CameraOptions)
        fun onVideoRecordingStart()
        fun onVideoRecordingEnd()
        fun onVideoTaken(result: VideoResult)
        fun onPictureTaken(result: PictureResult)
    }

    companion object {
        private const val CONSTRAINT_SQUARE_RATIO = "1:1"
    }

}