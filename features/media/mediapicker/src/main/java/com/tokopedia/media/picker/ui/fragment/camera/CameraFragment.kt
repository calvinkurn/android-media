package com.tokopedia.media.picker.ui.fragment.camera

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.otaliastudios.cameraview.CameraOptions
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.VideoResult
import com.otaliastudios.cameraview.controls.Flash
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.R
import com.tokopedia.picker.common.cache.PickerCacheManager
import com.tokopedia.media.databinding.FragmentCameraBinding
import com.tokopedia.media.picker.analytics.*
import com.tokopedia.media.picker.analytics.camera.CameraAnalytics
import com.tokopedia.media.picker.ui.activity.picker.PickerActivity
import com.tokopedia.media.picker.ui.activity.picker.PickerActivityContract
import com.tokopedia.media.picker.ui.activity.picker.PickerViewModel
import com.tokopedia.media.picker.ui.component.CameraControllerComponent
import com.tokopedia.media.picker.ui.component.CameraViewUiComponent
import com.tokopedia.media.picker.ui.observer.observe
import com.tokopedia.media.picker.ui.observer.stateOnCameraCapturePublished
import com.tokopedia.media.picker.ui.widget.LoaderDialogWidget
import com.tokopedia.media.picker.utils.exceptionHandler
import com.tokopedia.media.picker.utils.generateKey
import com.tokopedia.media.picker.utils.wrapper.FlingGestureWrapper
import com.tokopedia.picker.common.basecomponent.uiComponent
import com.tokopedia.picker.common.uimodel.MediaUiModel
import com.tokopedia.picker.common.uimodel.MediaUiModel.Companion.cameraToUiModel
import com.tokopedia.picker.common.uimodel.MediaUiModel.Companion.safeRemove
import com.tokopedia.picker.common.utils.wrapper.PickerFile.Companion.asPickerFile
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

open class CameraFragment @Inject constructor(
    private var viewModelFactory: ViewModelProvider.Factory,
    private var param: PickerCacheManager,
    private var cameraAnalytics: CameraAnalytics,
) : BaseDaggerFragment()
    , CameraControllerComponent.Listener
    , CameraViewUiComponent.Listener {

    private val pickerViewModel: PickerViewModel by activityViewModels { viewModelFactory }

    private val cameraViewModel: CameraViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        )[CameraViewModel::class.java]
    }

    private val binding: FragmentCameraBinding? by viewBinding()
    private var contract: PickerActivityContract? = null

    private var loaderDialog: LoaderDialogWidget? = null

    private val cameraView by uiComponent {
        CameraViewUiComponent(
            param = param.get(),
            listener = this,
            parent = it
        )
    }

    private val controller by uiComponent {
        CameraControllerComponent(
            param = param.get(),
            activityContract = contract,
            controllerListener = this,
            parent = it
        )
    }

    private val medias = mutableListOf<MediaUiModel>()

    // this is needed to determine the shutter button action
    private var isCameraPhotoMode = true

    // flag for camera flash initialization
    private var isInitFlashState = false

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
        cameraView.setupView(viewLifecycleOwner)
        controller.setupView()
    }

    override fun onResume() {
        super.onResume()
        cameraView.open()
    }

    override fun onPause() {
        super.onPause()
        cameraView.close()
    }

    override fun onCameraModeChanged(mode: CameraMode) {
        if (mode == CameraMode.Photo) {
            cameraView.onPictureMode()
            isCameraPhotoMode = true
        } else if (mode == CameraMode.Video) {
            cameraView.onVideoMode()
            isCameraPhotoMode = false
        }
    }

    override fun onCameraThumbnailClicked() {
        if (medias.isEmpty()) return

        contract?.onCameraThumbnailClicked()
        cameraAnalytics.clickThumbnail()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        exceptionHandler {
            contract = null
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contract = (context as PickerActivity)
    }

    override fun isFrontCamera(): Boolean {
        return cameraView.isFacingCameraIsFront()
    }

    override fun onFlashClicked() {
        cameraView.setCameraFlashIndex()
        setCameraFlashState()

        val flashStateString = when(cameraView.cameraFlash()){
            Flash.AUTO -> FLASH_AUTO_STRING
            Flash.ON -> FLASH_ON_STRING
            else -> FLASH_OFF_STRING
        }
        cameraAnalytics.clickFlash(flashStateString)
    }

    override fun onFlipClicked() {
        if (cameraView.isTakingPicture() || cameraView.isTakingVideo()) return
        cameraView.toggleFacing()

        val cameraState = if (cameraView.isFacingCameraIsFront()) {
            CAMERA_FRONT_STRING
        } else {
            CAMERA_BACK_STRING
        }

        cameraAnalytics.clickFlip(cameraState)
    }

    override fun onTakeMediaClicked() {
        if (controller.isVideoMode() && cameraView.isTakingVideo()) {
            cameraView.stopVideo()
            return
        }

        showShutterEffect {
            if (isCameraPhotoMode) {
                cameraView.onStartTakePicture()
                cameraAnalytics.clickShutter()
            } else {
                cameraView.enableFlashTorch()
                controller.onVideoDurationChanged()
                cameraAnalytics.clickRecord()
                cameraViewModel.onVideoTaken()
            }
        }
    }

    override fun onCameraOpened(options: CameraOptions) {
        controller.hasFrontCamera(cameraView.hasFrontCamera())
        cameraView.initCameraFlash()

        // isInitFlashState to make sure we init the flash only once
        if (!isInitFlashState && cameraView.hasFlashFeatureOnCamera()) {
            setCameraFlashState()
            isInitFlashState = true
        }
    }

    override fun onVideoRecordingStart() {
        onStartRecordVideo()
    }

    override fun onVideoRecordingEnd() {
        onStopRecordVideo()
    }

    override fun onVideoTaken(result: VideoResult) {
        val videoFile = result.file
            .asPickerFile()
            .cameraToUiModel()

        if (contract?.isMinVideoDuration(videoFile) == true) {
            contract?.onShowVideoMinDurationToast()
            videoFile.file?.safeDelete()
            return
        }

        if (contract?.isMinStorageThreshold() == true) {
            contract?.onShowFailToVideoRecordToast()
            return
        }

        onShowMediaThumbnail(videoFile)
    }

    override fun onPictureTaken(result: PictureResult) {
        cameraViewModel.onPictureTaken(cameraView.pictureSize(), result.data)
    }

    private fun initObservable() {
        cameraViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                loaderDialog = LoaderDialogWidget(requireContext())
                loaderDialog?.show()
            } else {
                loaderDialog?.dismiss()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            cameraViewModel.pictureTaken.collect {
                if (it == null) return@collect

                val file = it
                    .asPickerFile()
                    .cameraToUiModel()

                onShowMediaThumbnail(file)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            cameraViewModel.videoTaken.collect {
                cameraView.onStartTakeVideo(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            pickerViewModel.uiEvent.observe(
                onChanged = {
                    medias.clear()
                    medias.addAll(it)
                },
                onRemoved = {
                    if (medias.contains(it)) {
                        medias.safeRemove(it)
                    }
                },
                onAdded = {
                    if (!medias.contains(it)) {
                        medias.add(it)
                    }
                }
            ) {
                onShowThumbnailPreview()
            }
        }
    }

    private fun onShowThumbnailPreview() {
        if (medias.isNotEmpty()) {
            val lastMedia = medias.last()
            controller.setThumbnailPreview(lastMedia)
        } else {
            controller.removeThumbnailPreview()
        }
    }

    private fun onStartRecordVideo() {
        controller.startRecording()
        contract?.parentTabIsShownAs(false)
    }

    private fun onStopRecordVideo() {
        controller.stopRecording()
        contract?.parentTabIsShownAs(true)
    }

    private fun onShowMediaThumbnail(element: MediaUiModel?) {
        if (element == null) return
        stateOnCameraCapturePublished(element, param.get().generateKey())
    }

    private fun setCameraFlashState() {
        val activeFlash = cameraView.cameraFlash()

        if (!isFrontCamera()) {
            controller.isFlashSupported(activeFlash != null)
        }

        if (activeFlash != null) {
            controller.setFlashMode(activeFlash.ordinal)
        }
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

    override fun initInjector() {}

    override fun getScreenName() = "Camera"

    /**
     * on thumbnail finish load callback for testing purpose
     */
    override fun onThumbnailLoaded() {}

    companion object {
        private const val OVERLAY_SHUTTER_DELAY = 100L
    }

}
