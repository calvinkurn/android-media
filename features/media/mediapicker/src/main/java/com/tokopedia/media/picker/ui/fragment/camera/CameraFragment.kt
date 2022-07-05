package com.tokopedia.media.picker.ui.fragment.camera

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.otaliastudios.cameraview.CameraOptions
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.VideoResult
import com.otaliastudios.cameraview.controls.Flash
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.R
import com.tokopedia.media.common.utils.ParamCacheManager
import com.tokopedia.media.databinding.FragmentCameraBinding
import com.tokopedia.media.picker.analytics.CAMERA_BACK_STRING
import com.tokopedia.media.picker.analytics.CAMERA_FRONT_STRING
import com.tokopedia.media.picker.analytics.FLASH_AUTO_STRING
import com.tokopedia.media.picker.analytics.FLASH_OFF_STRING
import com.tokopedia.media.picker.analytics.FLASH_ON_STRING
import com.tokopedia.media.picker.analytics.camera.CameraAnalytics
import com.tokopedia.media.picker.di.DaggerPickerComponent
import com.tokopedia.media.picker.ui.activity.picker.PickerActivity
import com.tokopedia.media.picker.ui.activity.picker.PickerActivityContract
import com.tokopedia.media.picker.ui.component.CameraControllerComponent
import com.tokopedia.media.picker.ui.component.CameraViewComponent
import com.tokopedia.media.picker.ui.observer.observe
import com.tokopedia.media.picker.ui.observer.stateOnCameraCapturePublished
import com.tokopedia.media.picker.ui.widget.LoaderDialogWidget
import com.tokopedia.media.picker.utils.exceptionHandler
import com.tokopedia.media.picker.utils.wrapper.FlingGestureWrapper
import com.tokopedia.picker.common.basecomponent.uiComponent
import com.tokopedia.picker.common.uimodel.MediaUiModel
import com.tokopedia.picker.common.uimodel.MediaUiModel.Companion.cameraToUiModel
import com.tokopedia.picker.common.uimodel.MediaUiModel.Companion.safeRemove
import com.tokopedia.picker.common.utils.FileCamera
import com.tokopedia.picker.common.utils.wrapper.PickerFile.Companion.asPickerFile
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

open class CameraFragment : BaseDaggerFragment()
    , CameraControllerComponent.Listener
    , CameraViewComponent.Listener {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var param: ParamCacheManager

    @Inject
    lateinit var cameraAnalytics: CameraAnalytics

    private val binding: FragmentCameraBinding? by viewBinding()
    private var contract: PickerActivityContract? = null

    private var loaderDialog: LoaderDialogWidget? = null

    private val cameraView by uiComponent {
        CameraViewComponent(
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

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            factory
        )[CameraViewModel::class.java]
    }

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
                cameraView.onStartTakeVideo()
                controller.onVideoDurationChanged()
                cameraAnalytics.clickRecord()
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
        val fileToModel = result.file
            .asPickerFile()
            .cameraToUiModel()

        if (contract?.isMinVideoDuration(fileToModel) == true) {
            contract?.onShowVideoMinDurationToast()
            fileToModel.file?.safeDelete()
            return
        }

        if (contract?.isMinStorageThreshold() == true) {
            contract?.onShowFailToVideoRecordToast()
            return
        }

        onShowLoaderDialog()
        onShowMediaThumbnail(fileToModel)
    }

    override fun onPictureTaken(result: PictureResult) {
        onShowLoaderDialog()
        FileCamera.createPhoto(cameraView.pictureSize(), result.data) {
            if (it == null) return@createPhoto
            val fileToModel = it
                .asPickerFile()
                .cameraToUiModel()

            onShowMediaThumbnail(fileToModel)
        }
    }

    private fun initObservable() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.uiEvent.observe(
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
        stateOnCameraCapturePublished(element)
        loaderDialog?.dismiss()
    }

    private fun onShowLoaderDialog() {
        loaderDialog = LoaderDialogWidget(requireContext())
        loaderDialog?.show()
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

    override fun initInjector() {
        DaggerPickerComponent.builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    override fun getScreenName() = "Camera"

    /**
     * on thumbnail finish load callback for testing purpose
     */
    override fun onThumbnailLoaded() {}

    companion object {
        private const val OVERLAY_SHUTTER_DELAY = 100L
    }

}