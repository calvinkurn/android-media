package com.tokopedia.picker.ui.fragment.camera

import android.content.Context
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
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.common.component.uiComponent
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.picker.R
import com.tokopedia.picker.databinding.FragmentCameraBinding
import com.tokopedia.picker.di.DaggerPickerComponent
import com.tokopedia.picker.di.module.PickerModule
import com.tokopedia.picker.ui.PickerUiConfig
import com.tokopedia.picker.ui.activity.main.PickerActivity
import com.tokopedia.picker.ui.activity.main.PickerActivityListener
import com.tokopedia.picker.ui.fragment.camera.component.CameraControllerComponent
import com.tokopedia.picker.ui.fragment.camera.component.CameraPreviewComponent
import com.tokopedia.picker.utils.exceptionHandler
import com.tokopedia.picker.utils.generateFile
import com.tokopedia.picker.utils.wrapper.FlingGestureWrapper
import com.tokopedia.utils.view.binding.viewBinding
import java.io.File
import javax.inject.Inject

open class CameraFragment : BaseDaggerFragment()
    , CameraControllerComponent.Listener
    , CameraPreviewComponent.Listener {

    @Inject lateinit var factory: ViewModelProvider.Factory

    private val binding: FragmentCameraBinding? by viewBinding()
    private var listener: PickerActivityListener? = null

    private val param = PickerUiConfig.pickerParam()

    private var isTakingPictureMode = true
    private var isFlashOn = false

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            factory
        )[CameraViewModel::class.java]
    }

    private val preview by uiComponent { CameraPreviewComponent(param, this, it) }
    private val controller by uiComponent { CameraControllerComponent(param, this, it) }

    val gestureDetector by lazy {
        GestureDetector(requireContext(), FlingGestureWrapper(
            swipeLeftToRight = {
                controller.scrollToVideoMode()
            },
            swipeRightToLeft = {
                controller.scrollToPhotoMode()
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
        preview.setupView(viewLifecycleOwner)
        controller.setupView()
    }

    override fun onResume() {
        super.onResume()
        preview.open()
    }

    override fun onPause() {
        super.onPause()
        preview.close()
    }

    override fun onCameraModeChanged(mode: Int) {
        isTakingPictureMode = mode == CameraControllerComponent.PHOTO_MODE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        exceptionHandler {
            listener = null
            preview.release()
            controller.release()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = (context as PickerActivity)
    }

    override fun isFacingCameraIsFront(): Boolean {
        return preview.isFacingCameraIsFront()
    }

    override fun isCameraFlashOn(): Boolean {
        return isFlashOn
    }

    override fun onFlashClicked() {
        if (isFlashOn) {
            isFlashOn = false
            controller.setFlashMode(isFlashOn)
        } else {
            isFlashOn = true
            controller.setFlashMode(isFlashOn)
        }
    }

    override fun onFlipClicked() {
        if (preview.isTakingPicture() || preview.isTakingVideo()) return
        preview.toggleFacing()
    }

    override fun onTakeMediaClicked() {
        if (preview.isVideoMode() && preview.isTakingVideo()) {
            preview.stopVideo()
            return
        }

        showShutterEffect {
            preview.enableFlashTorch()

            if (isTakingPictureMode) {
                preview.onStartTakePicture()
            } else {
                preview.onStartTakeVideo()
                controller.onVideoDurationChanged {
                    requireActivity().runOnUiThread {
                        controller.setVideoDurationLabel(it)
                    }
                }
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

    override fun onCameraOpened(options: CameraOptions) {
        controller.isFlashSupported(preview.hasFlashFeatureOnCamera())
        controller.hasFrontCamera(preview.hasFrontCamera())
    }

    override fun onVideoRecordingStart() {
        onStartRecordVideo()
    }

    override fun onVideoRecordingEnd() {
        onStopRecordVideo()
    }

    override fun onVideoTaken(result: VideoResult) {
        onRenderThumbnailCameraCaptured(result.file)
    }

    override fun onPictureTaken(result: PictureResult) {
        generateFile(preview.pictureSize(), result.data) {
            onRenderThumbnailCameraCaptured(it)
        }
    }

    private fun initObservable() {
//        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
//            viewModel.uiEvent.collect {
//                when (it) {
//                    is EventState.SelectionChanged -> {
//                        val lastItemOfDrawer = File(it.data.last().path)
//                        cameraController.setThumbnailPreview(lastItemOfDrawer)
//                    }
//                    is EventState.SelectionRemoved -> {
//                        cameraController.removeThumbnailPreview()
//                    }
//                    else -> {}
//                }
//            }
//        }
    }

    private fun onStartRecordVideo() {
        controller.startRecording()
        listener?.tabVisibility(false)
    }

    private fun onStopRecordVideo() {
        controller.stopRecording()
        listener?.tabVisibility(true)
    }

    private fun onRenderThumbnailCameraCaptured(file: File?) {
        if (file == null) return

        controller.setThumbnailPreview(file)
//        EventBusFactory.emit(EventState.CameraCaptured(file.createFoCameraCaptured()))
    }

    private fun showShutterEffect(action: () -> Unit) {
        binding?.containerBlink?.show()

        Handler(Looper.getMainLooper()).postDelayed({
            binding?.containerBlink?.hide()
            action()
        }, OVERLAY_SHUTTER_DELAY)
    }

    override fun initInjector() {
        DaggerPickerComponent.builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .pickerModule(PickerModule())
            .build()
            .inject(this)
    }

    override fun getScreenName() = "Camera"

    companion object {
        private const val OVERLAY_SHUTTER_DELAY = 200L
    }

}