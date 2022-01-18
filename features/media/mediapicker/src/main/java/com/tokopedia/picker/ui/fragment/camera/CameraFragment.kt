package com.tokopedia.picker.ui.fragment.camera

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.otaliastudios.cameraview.*
import com.otaliastudios.cameraview.controls.Audio
import com.otaliastudios.cameraview.controls.Facing
import com.otaliastudios.cameraview.controls.Flash
import com.otaliastudios.cameraview.controls.Mode
import com.otaliastudios.cameraview.gesture.Gesture
import com.otaliastudios.cameraview.gesture.GestureAction
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.common.component.uiComponent
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.picker.R
import com.tokopedia.picker.data.entity.Media.Companion.createFoCameraCaptured
import com.tokopedia.picker.databinding.FragmentCameraBinding
import com.tokopedia.picker.ui.PickerUiConfig
import com.tokopedia.picker.ui.fragment.camera.component.CameraControllerComponent
import com.tokopedia.picker.utils.EventBusFactory
import com.tokopedia.picker.utils.EventState
import com.tokopedia.picker.utils.MediaFileUtils
import com.tokopedia.picker.utils.exceptionHandler
import com.tokopedia.picker.utils.wrapper.FlingGestureWrapper
import com.tokopedia.utils.view.binding.viewBinding
import java.io.File

open class CameraFragment : BaseDaggerFragment(), CameraControllerComponent.Listener {

    private val binding: FragmentCameraBinding? by viewBinding()
    private val param = PickerUiConfig.pickerParam()

    private var isPhotoMode = true
    private var isFlashOn = false

    private val viewModel by lazy {
        ViewModelProvider(
            this,
        )[CameraViewModel::class.java]
    }

    private val cameraController by uiComponent {
        CameraControllerComponent(param, this, it)
    }

    private val cameraView by lazy {
        binding?.cameraView
    }

    val gestureDetector by lazy {
        GestureDetector(requireContext(), FlingGestureWrapper(
            swipeLeftToRight = {
                cameraController.scrollToVideoMode()
            },
            swipeRightToLeft = {
                cameraController.scrollToPhotoMode()
            }
        ))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_camera,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservable()
        setupCameraView()
        cameraController.setupView()
    }

    override fun onResume() {
        super.onResume()
        exceptionHandler {
            cameraView?.open()
        }
    }

    override fun onPause() {
        super.onPause()
        exceptionHandler {
            cameraView?.close()
        }
    }

    override fun onCameraModeChanged(mode: Int) {
        isPhotoMode = mode == CameraControllerComponent.PHOTO_MODE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        exceptionHandler {
            cameraView?.destroy()
            cameraController.release()
        }
    }

    override fun isFrontFacingCamera(): Boolean {
        return cameraView?.facing == Facing.FRONT
    }

    override fun onFlashClicked() {
        if (isFlashOn) {
            isFlashOn = false
            cameraController.setFlashToggleUiState(isFlashOn)
        } else {
            isFlashOn = true
            cameraController.setFlashToggleUiState(isFlashOn)
        }
    }

    override fun onFlipClicked() {
        if (isTakingPicture() || isTakingVideo()) return
        cameraView?.toggleFacing()
    }

    override fun onTakeMediaClicked() {
        if (isVideoMode() && isTakingVideo()) {
            onStopRecordVideo()
            return
        }

        showShutterEffect {
            enableFlashTorch()

            if (isPhotoMode) {
                onCapturePhoto()
            } else {
                cameraController.startRecording()
                onRecordVideo()
            }
        }
    }

    override fun hasReachedLimit() {
        Toast.makeText(
            requireContext(),
            getString(R.string.picker_selection_limit_message, param.limit),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun hasVideoAddedOnMediaSelection() {
        Toast.makeText(
            requireContext(),
            getString(R.string.picker_selection_limit_video),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun onCapturePhoto() {
        cameraView?.set(Mode.PICTURE)
        cameraView?.takePicture()
    }

    private fun onRecordVideo() {
        cameraView?.set(Mode.VIDEO)
        cameraView?.set(Audio.ON)

        cameraController.onObserveVideoDuration { countDown ->
            activity?.runOnUiThread {
                cameraController.onChangeVideoDuration(countDown)
            }
        }

        cameraView?.takeVideoSnapshot(
            MediaFileUtils.createMediaFile(false),
            param.maxVideoDuration
        )
    }

    private fun onStopRecordVideo() {
        disableFlashTorch()
        cameraView?.stopVideo()
        cameraController.stopRecording()
    }

    private fun initObservable() {
        lifecycle.addObserver(viewModel)

        viewModel.selectedMedia.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                val lastItemOfDrawer = File(it.last().path)
                cameraController.setThumbnailPreview(lastItemOfDrawer)
            }
        })

        viewModel.mediaRemoved.observe(viewLifecycleOwner, {
            cameraController.removeThumbnailPreview()
        })
    }

    private fun setupCameraView() {
        binding?.cameraView?.apply {
            clearCameraListeners()
            setLifecycleOwner(viewLifecycleOwner)
            addCameraListener(cameraViewListener())
            mapGesture(Gesture.TAP, GestureAction.AUTO_FOCUS)
        }
    }

    private fun cameraViewListener() = object : CameraListener() {
        override fun onCameraOpened(options: CameraOptions) {
            super.onCameraOpened(options)
            cameraController.isCameraFlashSupported(hasFlashFeatureOnCamera())
            cameraController.isCameraHasFrontCamera(hasFrontCamera())
        }

        override fun onVideoTaken(result: VideoResult) {
            super.onVideoTaken(result)
            disableFlashTorch()

            cameraController.setThumbnailPreview(result.file)
            EventBusFactory.send(EventState.CameraCaptured(result.file.createFoCameraCaptured()))
        }

        override fun onPictureTaken(result: PictureResult) {
            super.onPictureTaken(result)
            disableFlashTorch()
        }

        override fun onCameraError(exception: CameraException) {
            super.onCameraError(exception)
            disableFlashTorch()
        }
    }

    private fun showShutterEffect(action: () -> Unit) {
        binding?.containerBlink?.show()

        Handler(Looper.getMainLooper()).postDelayed({
            binding?.containerBlink?.hide()
            action()
        }, OVERLAY_SHUTTER_DELAY)
    }

    private fun hasFrontCamera() = cameraView
        ?.cameraOptions
        ?.supportedFacing
        ?.isNotEmpty() == true

    private fun hasFlashFeatureOnCamera() = cameraView
            ?.cameraOptions
            ?.supportedFlash
            ?.contains(Flash.TORCH) == true

    private fun enableFlashTorch() {
        if (hasFlashFeatureOnCamera() && isFlashOn) {
            cameraView?.flash = Flash.TORCH
        }
    }

    private fun disableFlashTorch() {
        if (hasFlashFeatureOnCamera() && isFlashOn) {
            cameraView?.flash = Flash.OFF
        }
    }

    private fun isVideoMode() = cameraView?.mode == Mode.VIDEO

    private fun isTakingPicture() = cameraView?.isTakingPicture == true

    private fun isTakingVideo() = cameraView?.isTakingVideo == true

    override fun initInjector() {}

    override fun getScreenName() = "Camera"

    companion object {
        private const val OVERLAY_SHUTTER_DELAY = 200L
    }

}