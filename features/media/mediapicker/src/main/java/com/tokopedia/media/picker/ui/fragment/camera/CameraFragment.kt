package com.tokopedia.media.picker.ui.fragment.camera

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
import androidx.lifecycle.lifecycleScope
import com.otaliastudios.cameraview.CameraOptions
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.VideoResult
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.R
import com.tokopedia.media.common.basecomponent.uiComponent
import com.tokopedia.media.common.uimodel.MediaUiModel
import com.tokopedia.media.common.uimodel.MediaUiModel.Companion.cameraToUiModel
import com.tokopedia.media.databinding.FragmentCameraBinding
import com.tokopedia.media.picker.di.DaggerPickerComponent
import com.tokopedia.media.picker.di.module.PickerModule
import com.tokopedia.media.picker.ui.PickerUiConfig
import com.tokopedia.media.picker.ui.activity.main.PickerActivity
import com.tokopedia.media.picker.ui.activity.main.PickerActivityListener
import com.tokopedia.media.picker.ui.fragment.camera.component.CameraControllerComponent
import com.tokopedia.media.picker.ui.fragment.camera.component.CameraPreviewComponent
import com.tokopedia.media.picker.ui.observer.observe
import com.tokopedia.media.picker.ui.observer.stateOnCameraCapturePublished
import com.tokopedia.media.picker.ui.uimodel.containByName
import com.tokopedia.media.picker.ui.uimodel.hasVideoBy
import com.tokopedia.media.picker.ui.uimodel.safeRemove
import com.tokopedia.media.picker.utils.files.FileGenerator
import com.tokopedia.media.picker.utils.exceptionHandler
import com.tokopedia.media.picker.utils.wrapper.FlingGestureWrapper
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

open class CameraFragment : BaseDaggerFragment()
    , CameraControllerComponent.Listener
    , CameraPreviewComponent.Listener {

    @Inject lateinit var factory: ViewModelProvider.Factory

    private val binding: FragmentCameraBinding? by viewBinding()
    private var listener: PickerActivityListener? = null

    private val param by lazy { PickerUiConfig.pickerParam() }
    private val medias = mutableListOf<MediaUiModel>()

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

        preview.enableFlashTorch()

        showShutterEffect {
            if (isTakingPictureMode) {
                preview.onStartTakePicture()
            } else {
                preview.onStartTakeVideo()
//                controller.onVideoDurationChanged()
            }
        }
    }

    override fun hasReachedLimit(): Boolean {
        return listener?.mediaSelected()?.size == param.limitOfMedia()
    }

    override fun hasVideoAddedOnMediaSelection(): Boolean {
        return listener?.mediaSelected()
            ?.hasVideoBy(param.maxVideoCount())
            ?: false
    }

    override fun onShowToastMediaLimit() {
        Toast.makeText(
            requireContext(),
            getString(R.string.picker_selection_limit_message, param.limitOfMedia()),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onShowToastVideoLimit() {
        Toast.makeText(
            requireContext(),
            getString(
                R.string.picker_selection_limit_video,
                param.maxVideoCount()
            ),
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
        val fileToModel = result.file.cameraToUiModel()

        if (!fileToModel.isVideoDurationValid(requireContext())) {
            Toast.makeText(
                requireContext(),
                getString(R.string.picker_video_duration_min_limit),
                Toast.LENGTH_SHORT
            ).show()

            // delete unused video file
            if (result.file.exists()) {
                result.file.delete()
            }

            return
        }

        onShowMediaThumbnail(fileToModel)
    }

    override fun onPictureTaken(result: PictureResult) {
        FileGenerator.createFileCameraCapture(preview.pictureSize(), result.data) {
            if (it == null) return@createFileCameraCapture
            val fileToModel = it.cameraToUiModel()

            onShowMediaThumbnail(fileToModel)
        }
    }

    private fun initObservable() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.uiEvent.observe(
                onRemoved = {
                    if (medias.containByName(it)) {
                        medias.safeRemove(it)
                    }
                },
                onAdded = {
                    if (!medias.containByName(it)) {
                        medias.add(it)
                    }
                }
            ) {
                // update the thumbnail
                if (medias.isNotEmpty()) {
                    val lastMedia = medias.last()
                    controller.setThumbnailPreview(lastMedia)
                } else {
                    controller.removeThumbnailPreview()
                }
            }
        }
    }

    private fun onStartRecordVideo() {
        controller.startRecording()
        listener?.tabVisibility(false)
    }

    private fun onStopRecordVideo() {
        controller.stopRecording()
        listener?.tabVisibility(true)
    }

    private fun onShowMediaThumbnail(element: MediaUiModel?) {
        if (element == null) return
        stateOnCameraCapturePublished(element)
    }

    private fun showShutterEffect(action: () -> Unit) {
        binding?.containerBlink?.show()

        Looper.myLooper()?.let {
            Handler(it).postDelayed({
                binding?.containerBlink?.hide()
                action()
            }, OVERLAY_SHUTTER_DELAY)
        }
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
        private const val OVERLAY_SHUTTER_DELAY = 100L
    }

}