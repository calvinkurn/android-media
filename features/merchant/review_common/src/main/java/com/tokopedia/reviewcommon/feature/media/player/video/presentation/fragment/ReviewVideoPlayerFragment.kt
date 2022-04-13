package com.tokopedia.reviewcommon.feature.media.player.video.presentation.fragment

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.reviewcommon.R
import com.tokopedia.reviewcommon.databinding.FragmentReviewMediaGalleryVideoPlayerBinding
import com.tokopedia.reviewcommon.feature.media.gallery.base.di.ReviewMediaGalleryComponentInstance
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.di.DetailedReviewMediaGalleryComponentInstance
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.di.qualifier.DetailedReviewMediaGalleryViewModelFactory
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.viewmodel.SharedReviewMediaGalleryViewModel
import com.tokopedia.reviewcommon.feature.media.player.video.di.component.DaggerReviewVideoPlayerComponent
import com.tokopedia.reviewcommon.feature.media.player.video.di.qualifier.ReviewVideoPlayerViewModelFactory
import com.tokopedia.reviewcommon.feature.media.player.video.presentation.uistate.ReviewVideoErrorUiState
import com.tokopedia.reviewcommon.feature.media.player.video.presentation.uistate.ReviewVideoPlaybackUiState
import com.tokopedia.reviewcommon.feature.media.player.video.presentation.uistate.ReviewVideoPlayerUiState
import com.tokopedia.reviewcommon.feature.media.player.video.presentation.uistate.ReviewVideoThumbnailUiState
import com.tokopedia.reviewcommon.feature.media.player.video.presentation.viewmodel.ReviewVideoPlayerViewModel
import com.tokopedia.reviewcommon.feature.media.player.video.presentation.widget.ReviewVideoPlayer
import com.tokopedia.reviewcommon.feature.media.player.video.presentation.widget.ReviewVideoPlayerListener
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ReviewVideoPlayerFragment : BaseDaggerFragment(), CoroutineScope, ReviewVideoPlayerListener {
    companion object {
        private const val ARG_VIDEO_URI = "argVideoUri"

        fun createInstance(videoUri: String): ReviewVideoPlayerFragment {
            return ReviewVideoPlayerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_VIDEO_URI, videoUri)
                }
            }
        }
    }

    @Inject
    @ReviewVideoPlayerViewModelFactory
    lateinit var reviewVideoPlayerViewModelFactory: ViewModelProvider.Factory

    @Inject
    @DetailedReviewMediaGalleryViewModelFactory
    lateinit var detailedReviewMediaGalleryViewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var dispatcher: CoroutineDispatchers

    @Inject
    lateinit var videoPlayer: ReviewVideoPlayer

    private var binding by viewBinding(FragmentReviewMediaGalleryVideoPlayerBinding::bind)
    private var videoPlayerUiStateCollector: Job? = null
    private var videoPlaybackUiStateCollector: Job? = null
    private var videoErrorUiStateCollector: Job? = null
    private var videoThumbnailUiStateCollector: Job? = null
    private var wifiConnectivityStatusCollector: Job? = null

    private val reviewVideoPlayerViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(requireActivity(), reviewVideoPlayerViewModelFactory).get(
            getVideoUri(),
            ReviewVideoPlayerViewModel::class.java
        )
    }

    private val sharedReviewMediaGalleryViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(requireActivity(), detailedReviewMediaGalleryViewModelFactory).get(
            SharedReviewMediaGalleryViewModel::class.java
        )
    }

    override val coroutineContext: CoroutineContext
        get() = dispatcher.main + SupervisorJob()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initUiState(savedInstanceState)
        return FragmentReviewMediaGalleryVideoPlayerBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectVideoThumbnailUiState()
    }

    override fun onResume() {
        super.onResume()
        collectVideoPlayerUiState()
        collectWifiConnectivityStatus()
    }

    override fun onPause() {
        super.onPause()
        wifiConnectivityStatusCollector?.cancel()
        videoPlayerUiStateCollector?.cancel()
        if (activity?.isChangingConfigurations != true) {
            updateCurrentFrameBitmap()
            reviewVideoPlayerViewModel.setPlaybackStateToInactive(videoPlayer.getCurrentPosition())
            reviewVideoPlayerViewModel.resetVideoPlayerState()
            videoPlayer.pause()
        } else {
            reviewVideoPlayerViewModel.setVideoPlayerStateToChangingConfiguration()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        videoPlaybackUiStateCollector?.cancel()
        videoThumbnailUiStateCollector?.cancel()
        videoErrorUiStateCollector?.cancel()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        reviewVideoPlayerViewModel.saveUiState(outState)
    }

    override fun getScreenName(): String {
        return ReviewVideoPlayerFragment::class.java.simpleName.orEmpty()
    }

    override fun initInjector() {
        DaggerReviewVideoPlayerComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .reviewMediaGalleryComponent(ReviewMediaGalleryComponentInstance.getInstance(requireContext()))
            .detailedReviewMediaGalleryComponent(DetailedReviewMediaGalleryComponentInstance.getInstance(requireContext()))
            .build()
            .inject(this)
    }

    override fun onReviewVideoPlayerIsPlaying() {
        reviewVideoPlayerViewModel.setPlaybackStateToPlaying(videoPlayer.getCurrentPosition())
    }

    override fun onReviewVideoPlayerIsBuffering() {
        reviewVideoPlayerViewModel.setPlaybackStateToBuffering(videoPlayer.getCurrentPosition())
    }

    override fun onReviewVideoPlayerIsPaused() {
        reviewVideoPlayerViewModel.setPlaybackStateToPaused(videoPlayer.getCurrentPosition())
    }

    override fun onReviewVideoPlayerIsPreloading() {
        reviewVideoPlayerViewModel.setPlaybackStateToPreloading(videoPlayer.getCurrentPosition())
    }

    override fun onReviewVideoPlayerIsEnded() {
        reviewVideoPlayerViewModel.setPlaybackStateToEnded(videoPlayer.getCurrentPosition())
    }

    override fun onReviewVideoPlayerError() {
        reviewVideoPlayerViewModel.setPlaybackStateToError(videoPlayer.getCurrentPosition())
    }

    private fun getVideoUri(): String {
        return arguments?.getString(ARG_VIDEO_URI).orEmpty()
    }

    private fun collectVideoPlayerUiState() {
        videoPlayerUiStateCollector = videoPlayerUiStateCollector?.takeIf { !it.isCompleted } ?: launch {
            reviewVideoPlayerViewModel.videoPlayerUiState.collectLatest {
                when (it) {
                    is ReviewVideoPlayerUiState.Initial -> {
                        videoPlayer.initializeVideoPlayer(it.videoUri, true)
                        reviewVideoPlayerViewModel.setVideoPlayerStateToRestoring()
                    }
                    is ReviewVideoPlayerUiState.ChangingConfiguration -> {
                        videoPlayer.initializeVideoPlayer(it.videoUri, false)
                        reviewVideoPlayerViewModel.setVideoPlayerStateToReadyToPlay()
                    }
                    is ReviewVideoPlayerUiState.RestoringState -> {
                        videoPlayer.restorePlaybackState(it.presentationTimeMs, it.playWhenReady)
                        reviewVideoPlayerViewModel.setVideoPlayerStateToReadyToPlay()
                    }
                    else -> {
                        collectVideoPlaybackUiState()
                        collectVideoErrorUiState()
                        collectVideoThumbnailUiState()
                    }
                }
            }
        }
    }

    private fun collectWifiConnectivityStatus() {
        wifiConnectivityStatusCollector = wifiConnectivityStatusCollector?.takeIf {
            !it.isCompleted
        } ?: launch {
            sharedReviewMediaGalleryViewModel.connectedToWifi.collectLatest {
                reviewVideoPlayerViewModel.updateWifiConnectivityStatus(it)
            }
        }
    }

    private fun collectVideoPlaybackUiState() {
        videoPlaybackUiStateCollector = videoPlaybackUiStateCollector?.takeIf { !it.isCompleted } ?: launch {
            reviewVideoPlayerViewModel.videoPlaybackUiState.collectLatest {
                when (it) {
                    is ReviewVideoPlaybackUiState.Inactive -> {
                        binding?.loaderReviewVideoPlayer?.gone()
                        binding?.overlayReviewVideoPlayerLoading?.gone()
                        reviewVideoPlayerViewModel.showVideoThumbnail()
                        reviewVideoPlayerViewModel.hideVideoError()
                    }
                    is ReviewVideoPlaybackUiState.Error -> {
                        binding?.loaderReviewVideoPlayer?.gone()
                        binding?.overlayReviewVideoPlayerLoading?.gone()
                        reviewVideoPlayerViewModel.showVideoThumbnail()
                        reviewVideoPlayerViewModel.showVideoError()
                    }
                    is ReviewVideoPlaybackUiState.Buffering,
                    is ReviewVideoPlaybackUiState.Preloading -> {
                        binding?.loaderReviewVideoPlayer?.show()
                        binding?.overlayReviewVideoPlayerLoading?.show()
                        reviewVideoPlayerViewModel.hideVideoThumbnail()
                        reviewVideoPlayerViewModel.hideVideoError()
                    }
                    else -> {
                        binding?.loaderReviewVideoPlayer?.gone()
                        binding?.overlayReviewVideoPlayerLoading?.gone()
                        reviewVideoPlayerViewModel.hideVideoThumbnail()
                        reviewVideoPlayerViewModel.hideVideoError()
                    }
                }
            }
        }
    }

    private fun collectVideoErrorUiState() {
        videoErrorUiStateCollector = videoErrorUiStateCollector?.takeIf {
            !it.isCompleted
        } ?: launch {
            reviewVideoPlayerViewModel.videoErrorUiState.collectLatest {
                binding?.overlayReviewVideoPlayerError?.showWithCondition(it is ReviewVideoErrorUiState.Showing)
                binding?.icReviewVideoPlayerError?.showWithCondition(it is ReviewVideoErrorUiState.Showing)
                binding?.tvReviewVideoPlayerError?.showWithCondition(it is ReviewVideoErrorUiState.Showing)
                if (it is ReviewVideoErrorUiState.Showing) {
                    showToasterError(
                        getString(R.string.review_video_player_error_message),
                        getString(R.string.review_video_player_error_action_text)
                    )
                }
            }
        }
    }

    private fun showToasterError(message: String, actionText: String) {
        binding?.root?.let { view ->
            Toaster.build(view, message, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR, actionText).show()
        }
    }

    private fun collectVideoThumbnailUiState() {
        videoThumbnailUiStateCollector = videoThumbnailUiStateCollector?.takeIf { !it.isCompleted } ?: launch {
            reviewVideoPlayerViewModel.videoThumbnailUiState.collectLatest {
                when (it) {
                    is ReviewVideoThumbnailUiState.Showed -> {
                        binding?.ivReviewVideoPlayerFramePreview?.run {
                            setImageBitmap(it.videoThumbnail)
                            show()
                        }
                    }
                    is ReviewVideoThumbnailUiState.Hidden -> {
                        binding?.ivReviewVideoPlayerFramePreview?.gone()
                    }
                }
            }
        }
    }

    private fun initUiState(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            reviewVideoPlayerViewModel.setVideoUri(getVideoUri())
        } else {
            reviewVideoPlayerViewModel.restoreUiState(savedInstanceState)
        }
    }

    private fun ReviewVideoPlayer.initializeVideoPlayer(videoUri: String, shouldPrepare: Boolean) {
        val playerView = binding?.playerViewReviewVideoPlayer ?: return
        initializeVideoPlayer(
            uri = videoUri,
            newPlayerView = playerView,
            newListener = this@ReviewVideoPlayerFragment,
            shouldPrepare = shouldPrepare
        )
    }

    private fun updateCurrentFrameBitmap() {
        getCurrentFrameBitmap()?.run {
            reviewVideoPlayerViewModel.updateVideoThumbnail(this)
        }
    }

    private fun getCurrentFrameBitmap(): Bitmap? {
        return (binding?.playerViewReviewVideoPlayer?.videoSurfaceView as? TextureView)?.bitmap
    }
}
