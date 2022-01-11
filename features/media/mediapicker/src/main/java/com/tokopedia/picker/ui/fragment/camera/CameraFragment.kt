package com.tokopedia.picker.ui.fragment.camera

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.picker.R
import com.tokopedia.picker.databinding.FragmentCameraBinding
import com.tokopedia.picker.ui.PickerUiConfig
import com.tokopedia.picker.ui.fragment.camera.recyclers.adapter.CameraSliderAdapter
import com.tokopedia.picker.ui.fragment.camera.recyclers.managers.SliderLayoutManager
import com.tokopedia.picker.utils.exceptionHandler
import com.tokopedia.unifycomponents.dpToPx
import com.tokopedia.utils.view.binding.viewBinding

open class CameraFragment : BaseDaggerFragment() {

    private val param = PickerUiConfig.getFileLoaderParam()

    private val binding: FragmentCameraBinding? by viewBinding()
    private val cameraView by lazy { binding?.cameraView }

    private var flashList = arrayListOf<Flash>()
    private var flashIndex = 0

    private var isPhotoMode = true

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
        initView()
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

    override fun onDestroyView() {
        super.onDestroyView()
        exceptionHandler {
            cameraView?.destroy()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(CACHE_FLASH_INDEX, flashIndex)
    }

    private fun initView() {
        setupCameraView()
        horizontalPicker()

        binding?.btnFlip?.setOnClickListener(::switchCameraClicked)
        binding?.btnFlash?.setOnClickListener(::cameraFlashClicked)

        binding?.btnTakeCamera?.setOnClickListener {
            binding?.containerBlink?.show()
            Handler(Looper.getMainLooper()).postDelayed({
                binding?.containerBlink?.hide()
            }, 500)
        }
    }

    private fun horizontalPicker() {
        binding?.lstCameraMode?.showWithCondition(param.isIncludeVideo)

        binding?.lstCameraMode?.also { recyclerView ->
            // Camera mode element
            val camerasMode = listOf("PHOTO", "VIDEO")

            // Setting the padding such that the items will appear in the middle of the screen
            val padding = (getScreenWidth() / 2 - 40f.dpToPx()).toInt()
            recyclerView.setPadding(padding, 0, padding, 0)

            // Layout manager
            recyclerView.layoutManager = SliderLayoutManager(
                requireContext(),
                object : SliderLayoutManager.Listener {
                    override fun onItemSelected(index: Int) {
                        isPhotoMode = camerasMode[index] == "PHOTO"
                    }
                }
            )

            recyclerView.adapter = CameraSliderAdapter(
                camerasMode,
                object : CameraSliderAdapter.Listener {
                    override fun onItemClicked(view: View) {
                        recyclerView.smoothScrollToPosition(
                            recyclerView.getChildLayoutPosition(view)
                        )
                    }
                }
            )
        }
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
        }

        override fun onPictureTaken(result: PictureResult) {
            super.onPictureTaken(result)
        }
    }

    private fun switchCameraClicked(view: View) {
        if (cameraView?.isTakingPicture == true || cameraView?.isTakingVideo == true) return
        cameraView?.toggleFacing()
    }

    private fun cameraFlashClicked(view: View) {
        if (flashList.size > 0) {
            flashIndex = (flashIndex + 1) % flashList.size

            setCameraFlash()
        }
    }

    private fun initCameraFlash() {
        if (cameraView == null || cameraView?.cameraOptions == null) return

        val supportedFlashes = cameraView?.cameraOptions?.supportedFlash!!

        for (flash: Flash in supportedFlashes) {
            if (flash != Flash.TORCH) {
                flashList.add(flash)
            }
        }

        binding?.btnFlash?.shouldShowWithAction(flashList.isNotEmpty()) {
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
        setCameraFlashUIState(flash.ordinal)
    }

    private fun setCameraFlashUIState(enum: Int) {
        val colorWhite = ContextCompat.getColor(
            requireContext(),
            com.tokopedia.unifyprinciples.R.color.Unify_Static_White
        )

        when (enum) {
            Flash.AUTO.ordinal -> binding?.btnFlash?.setImageDrawable(
                MethodChecker.getDrawable(activity, R.drawable.ic_picker_camera_flash)
            )
            Flash.ON.ordinal -> binding?.btnFlash?.setImage(
                IconUnify.FLASH_ON,
                colorWhite,
                colorWhite,
                colorWhite,
                colorWhite
            )
            Flash.OFF.ordinal -> binding?.btnFlash?.setImage(
                IconUnify.FLASH_OFF,
                colorWhite,
                colorWhite,
                colorWhite,
                colorWhite
            )
        }
    }

    override fun initInjector() {}

    override fun getScreenName() = "Camera"

    companion object {
        private const val CACHE_FLASH_INDEX = "flash_index"
    }

}