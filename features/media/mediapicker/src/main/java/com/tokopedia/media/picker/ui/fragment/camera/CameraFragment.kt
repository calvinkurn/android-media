package com.tokopedia.media.picker.ui.fragment.camera

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
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
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.R
import com.tokopedia.media.databinding.FragmentCameraBinding
import com.tokopedia.media.picker.di.DaggerPickerComponent
import com.tokopedia.media.picker.di.module.PickerModule
import com.tokopedia.media.picker.ui.activity.main.PickerActivity
import com.tokopedia.media.picker.ui.activity.main.PickerActivityListener
import com.tokopedia.media.picker.ui.fragment.camera.component.CameraControllerComponent
import com.tokopedia.media.picker.ui.fragment.camera.component.CameraPreviewComponent
import com.tokopedia.media.picker.ui.observer.observe
import com.tokopedia.media.picker.ui.observer.stateOnCameraCapturePublished
import com.tokopedia.media.picker.ui.uimodel.safeRemove
import com.tokopedia.media.picker.utils.exceptionHandler
import com.tokopedia.media.picker.utils.wrapper.FlingGestureWrapper
import com.tokopedia.picker.common.ParamCacheManager
import com.tokopedia.picker.common.basecomponent.uiComponent
import com.tokopedia.picker.common.uimodel.MediaUiModel
import com.tokopedia.picker.common.uimodel.MediaUiModel.Companion.cameraToUiModel
import com.tokopedia.picker.common.utils.FileGenerator
import com.tokopedia.picker.common.utils.videoFormat
import com.tokopedia.utils.view.binding.viewBinding
import java.io.File
import javax.inject.Inject

open class CameraFragment : BaseDaggerFragment()
    , CameraControllerComponent.Listener
    , CameraPreviewComponent.Listener {

    @Inject lateinit var factory: ViewModelProvider.Factory
    @Inject lateinit var param: ParamCacheManager

    private val binding: FragmentCameraBinding? by viewBinding()
    private var listener: PickerActivityListener? = null

    private val preview by uiComponent { CameraPreviewComponent(param.get(), this, it) }
    private val controller by uiComponent { CameraControllerComponent(param.get(), this, it) }

    private val medias = mutableListOf<MediaUiModel>()

    private var videoDurationTimer: CountDownTimer? = null
    private var isTakingPictureMode = true
    private var isFlashOn = false

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

    override fun onImageThumbnailClicked() {
        listener?.onPreviewItemSelected(medias)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        resetVideoDuration()
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
                onVideoDurationChanged()
            }
        }
    }

    override fun hasMediaLimitReached(): Boolean {
        return listener?.hasMediaLimitReached()?: false
    }

    override fun hasVideoLimitReached(): Boolean {
        return listener?.hasVideoLimitReached()?: false
    }

    override fun onShowToastMediaLimit() {
        listener?.onShowMediaLimitReachedToast()
    }

    override fun onShowToastVideoLimit() {
        listener?.onShowVideoLimitReachedToast()
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
        if (isMinVideoDuration(fileToModel)) return

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
        listener?.tabVisibility(false)
    }

    private fun onStopRecordVideo() {
        resetVideoDuration()
        controller.stopRecording()
        listener?.tabVisibility(true)
    }

    private fun onShowMediaThumbnail(element: MediaUiModel?) {
        if (element == null) return
        stateOnCameraCapturePublished(element)
    }

    private fun isMinVideoDuration(model: MediaUiModel): Boolean {
        if (listener?.isMinVideoDuration(model) == true) {
            listener?.onShowVideoMinDurationToast()
            deleteFile(model.path)
            return true
        }

        return false
    }

    private fun deleteFile(path: String) {
        val file = File(path)

        if (file.exists()) {
            file.delete()
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

    private fun onVideoDurationChanged() {
        resetVideoDuration()

        val maxDuration = param.get().maxVideoDuration()

        videoDurationTimer = object : CountDownTimer(
            param.get().maxVideoDuration(),
            COUNTDOWN_INTERVAL
        ) {
            override fun onTick(milis: Long) {
                val time = maxDuration - milis
                controller.setVideoDuration(time.videoFormat())
            }

            override fun onFinish() {
                onStopRecordVideo()
            }
        }

        videoDurationTimer?.start()
    }

    private fun resetVideoDuration() {
        try {
            videoDurationTimer?.cancel()
            videoDurationTimer = null
        } catch (t: Throwable) {}
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
        private const val COUNTDOWN_INTERVAL = 1000L
    }

}