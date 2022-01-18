package com.tokopedia.picker.ui.fragment.camera

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraOptions
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.VideoResult
import com.otaliastudios.cameraview.controls.Audio
import com.otaliastudios.cameraview.controls.Flash
import com.otaliastudios.cameraview.controls.Mode
import com.otaliastudios.cameraview.gesture.Gesture
import com.otaliastudios.cameraview.gesture.GestureAction
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.common.component.uiComponent
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.picker.R
import com.tokopedia.picker.databinding.FragmentCameraBinding
import com.tokopedia.picker.ui.PickerUiConfig
import com.tokopedia.picker.ui.fragment.camera.component.CameraControllerComponent
import com.tokopedia.picker.utils.exceptionHandler
import com.tokopedia.picker.utils.wrapper.FlingGestureWrapper
import com.tokopedia.utils.view.binding.viewBinding

open class CameraFragment : BaseDaggerFragment(), CameraControllerComponent.Listener {

    private val param = PickerUiConfig.pickerParam()

    private val binding: FragmentCameraBinding? by viewBinding()
    private val cameraView by lazy { binding?.cameraView }

    private var flashList = arrayListOf<Flash>()
    private var flashIndex = 0

    private var isPhotoMode = true

    private val cameraController by uiComponent {
        CameraControllerComponent(param, this, it)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let {
            flashIndex = it.getInt(CACHE_FLASH_INDEX, 0)
        }
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(CACHE_FLASH_INDEX, flashIndex)
    }

    private fun setupCameraView() {
        binding?.cameraView?.apply {
            mode = Mode.VIDEO
            audio = Audio.ON

            clearCameraListeners()
            addCameraListener(initCameraViewListener())
            mapGesture(Gesture.TAP, GestureAction.AUTO_FOCUS)
        }
    }

    private fun initCameraViewListener() = object : CameraListener() {
        override fun onCameraOpened(options: CameraOptions) {
            super.onCameraOpened(options)
            initCameraFlash()
        }

        override fun onVideoTaken(result: VideoResult) {
            super.onVideoTaken(result)
            cameraController.setThumbnailPreview(
                result.file
            )
        }

        override fun onPictureTaken(result: PictureResult) {
            super.onPictureTaken(result)

        }
    }

    override fun onFlashClicked() {
        if (flashList.size > 0) {
            flashIndex = (flashIndex + 1) % flashList.size

            setCameraFlash()
        }
    }

    override fun onFlipClicked() {
        if (cameraView?.isTakingPicture == true || cameraView?.isTakingVideo == true) return
        cameraView?.toggleFacing()
    }

    override fun onTakeMediaClicked() {
        binding?.containerBlink?.show()

        Handler(Looper.getMainLooper()).postDelayed({
            binding?.containerBlink?.hide()
            cameraController.startRecording()

            Handler(Looper.getMainLooper()).postDelayed({
                cameraController.stopRecording()
            }, 3000)
        }, 200)
    }

    private fun initCameraFlash() {
        if (cameraView == null || cameraView?.cameraOptions == null) return

        val supportedFlashes = cameraView?.cameraOptions?.supportedFlash!!

        for (flash: Flash in supportedFlashes) {
            if (flash != Flash.TORCH) {
                flashList.add(flash)
            }
        }

        cameraController.setupFlashButtonState(flashList.isNotEmpty()) {
            setCameraFlash()
        }
    }

    private fun setCameraFlash() {
        if (flashIndex < 0 || flashList.size <= flashIndex) return

        var flash = flashList[flashIndex]

        if (flash.ordinal == Flash.TORCH.ordinal) {
            flashIndex = (flashIndex + 1) % flashList.size
            flash = flashList[flashIndex]
        }

        cameraView?.set(flash)

        cameraController.setCameraFlashUIState(
            flash.ordinal
        )
    }

    override fun initInjector() {}

    override fun getScreenName() = "Camera"

    companion object {
        private const val CACHE_FLASH_INDEX = "flash_index"
    }

}