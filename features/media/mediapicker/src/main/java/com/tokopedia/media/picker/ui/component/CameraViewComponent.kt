package com.tokopedia.media.picker.ui.component

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
import com.otaliastudios.cameraview.size.SizeSelectors
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.R
import com.tokopedia.media.picker.utils.exceptionHandler
import com.tokopedia.picker.common.PickerParam
import com.tokopedia.picker.common.basecomponent.UiComponent
import java.io.File

class CameraViewComponent(
    private val param: PickerParam,
    private val listener: Listener,
    parent: ViewGroup,
) : UiComponent(parent, R.id.uc_camera_preview) {

    private val spaceToolBar = findViewById<Space>(R.id.space_toolbar)

    private val cameraView = findViewById<CameraView>(R.id.cameraView).apply {
        if (param.ratioIsSquare()) {
            customRatioCameraView(SQUARE_RATIO)
            squareRatioOfCameraSize()
        } else {
            fullScreenCameraView()
        }
    }

    // flash collection
    private var flashList = arrayListOf<Flash>()
    private var activeFlash = Flash.OFF
    private var flashIndex = 0

    fun setupView(owner: LifecycleOwner) {
        cameraView.clearCameraListeners()
        cameraView.setLifecycleOwner(owner)
        cameraView.addCameraListener(cameraViewListener())
        cameraView.mapGesture(Gesture.TAP, GestureAction.AUTO_FOCUS)
    }

    fun initCameraFlash() {
        if (cameraView.cameraOptions == null) return
        if (flashList.isNotEmpty()) return

        val supportedFlashes = cameraView.cameraOptions?.supportedFlash?: return

        for (flash: Flash in supportedFlashes) {
            if (flash != Flash.TORCH) {
                flashList.add(flash)
            }
        }
    }

    fun setCameraFlashIndex() {
        flashIndex = (flashIndex + 1) % flashList.size
    }

    fun cameraFlash(): Flash? {
        if (flashIndex < 0 || flashList.size <= flashIndex) return null

        activeFlash = flashList[flashIndex]

        // prevent the flash mode is TORCH
        if (activeFlash.ordinal == Flash.TORCH.ordinal) {
            flashIndex = (flashIndex + 1) % flashList.size
            activeFlash = flashList[flashIndex]
        }

        cameraView.set(activeFlash)

        return activeFlash
    }

    fun onStartTakePicture() {
        cameraView.takePicture()
    }

    fun onStartTakeVideo(file: File?) {
        if (file == null) return

        cameraView.set(Audio.ON)
        cameraView.takeVideoSnapshot(file, param.maxVideoDuration())
    }

    fun onPictureMode() {
        cameraView.set(Mode.PICTURE)
    }

    fun onVideoMode() {
        cameraView.set(Mode.VIDEO)
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

    fun enableFlashTorch() {
        if (hasFlashFeatureOnCamera() && isFlashOn()) {
            cameraView.flash = Flash.TORCH
        }
    }

    fun isFacingCameraIsFront() = cameraView.facing == Facing.FRONT

    fun pictureSize() = cameraView.pictureSize

    fun isTakingPicture() = cameraView.isTakingPicture

    fun isTakingVideo() = cameraView.isTakingVideo

    fun hasFlashFeatureOnCamera() = flashList.size > 1

    fun hasFrontCamera() = cameraView
        .cameraOptions
        ?.supportedFacing
        ?.isNotEmpty() == true

    private fun isFlashOn() = activeFlash == Flash.ON

    private fun fullScreenCameraView() {
        spaceToolBar.hide()

        val parent = container() as ConstraintLayout

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

    @Suppress("SameParameterValue") // for now
    private fun customRatioCameraView(ratio: String) {
        spaceToolBar.show()

        val parent = container() as ConstraintLayout

        ConstraintSet().apply {
            clone(parent)
            setDimensionRatio(R.id.cameraView, ratio)
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
            listener.onVideoRecordingEnd()
            disableFlashTorch()
        }

        override fun onVideoTaken(result: VideoResult) {
            super.onVideoTaken(result)
            listener.onVideoTaken(result)
            disableFlashTorch()
        }

        override fun onPictureTaken(result: PictureResult) {
            super.onPictureTaken(result)
            listener.onPictureTaken(result)
        }

        override fun onCameraError(exception: CameraException) {
            super.onCameraError(exception)
            disableFlashTorch()
        }
    }

    private fun disableFlashTorch() {
        if (hasFlashFeatureOnCamera() && isFlashOn()) {
            cameraView.flash = activeFlash
        }
    }

    override fun release() {
        exceptionHandler {
            cameraView.destroy()
        }
    }

    interface Listener {
        fun onCameraOpened(options: CameraOptions)
        fun onVideoRecordingStart()
        fun onVideoRecordingEnd()
        fun onVideoTaken(result: VideoResult)
        fun onPictureTaken(result: PictureResult)
    }

    companion object {
        private const val SQUARE_RATIO = "1:1"
    }

}
