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
import com.tokopedia.reviewcommon.feature.media.player.video.di.component.DaggerReviewVideoPlayerComponent
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
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var dispatcher: CoroutineDispatchers

    @Inject
    lateinit var videoPlayer: ReviewVideoPlayer

    private var binding by viewBinding(FragmentReviewMediaGalleryVideoPlayerBinding::bind)
    private var videoPlayerUiStateCollector: Job? = null
    private var videoPlaybackUiStateCollector: Job? = null
    private var videoErrorUiStateCollector: Job? = null
    private var videoThumbnailUiStateCollector: Job? = null

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(requireActivity(), viewModelFactory).get(
            getVideoUri(),
            ReviewVideoPlayerViewModel::class.java
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
    }

    override fun onPause() {
        super.onPause()
        videoPlayerUiStateCollector?.cancel()
        if (activity?.isChangingConfigurations != true) {
            updateCurrentFrameBitmap()
            viewModel.setPlaybackStateToInactive(videoPlayer.getCurrentPosition())
            viewModel.resetVideoPlayerState()
            videoPlayer.pause()
        } else {
            viewModel.setVideoPlayerStateToChangingConfiguration()
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
        viewModel.saveUiState(outState)
    }

    override fun getScreenName(): String {
        return ReviewVideoPlayerFragment::class.java.simpleName.orEmpty()
    }

    override fun initInjector() {
        DaggerReviewVideoPlayerComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .reviewMediaGalleryComponent(ReviewMediaGalleryComponentInstance.getInstance(requireContext()))
            .build()
            .inject(this)
    }

    override fun onReviewVideoPlayerIsPlaying() {
        viewModel.setPlaybackStateToPlaying(videoPlayer.getCurrentPosition())
    }

    override fun onReviewVideoPlayerIsBuffering() {
        viewModel.setPlaybackStateToBuffering(videoPlayer.getCurrentPosition())
    }

    override fun onReviewVideoPlayerIsPaused() {
        viewModel.setPlaybackStateToPaused(videoPlayer.getCurrentPosition())
    }

    override fun onReviewVideoPlayerIsPreloading() {
        viewModel.setPlaybackStateToPreloading(videoPlayer.getCurrentPosition())
    }

    override fun onReviewVideoPlayerIsEnded() {
        viewModel.setPlaybackStateToEnded(videoPlayer.getCurrentPosition())
    }

    override fun onReviewVideoPlayerError() {
        viewModel.setPlaybackStateToError(videoPlayer.getCurrentPosition())
    }

    private fun getVideoUri(): String {
        return arguments?.getString(ARG_VIDEO_URI).orEmpty()
    }

    private fun collectVideoPlayerUiState() {
        videoPlayerUiStateCollector = videoPlayerUiStateCollector?.takeIf { !it.isCompleted } ?: launch {
            viewModel.videoPlayerUiState.collectLatest {
                when (it) {
                    is ReviewVideoPlayerUiState.Initial -> {
                        videoPlayer.initializeVideoPlayer(it.videoUri, true)
                        viewModel.setVideoPlayerStateToRestoring()
                    }
                    is ReviewVideoPlayerUiState.ChangingConfiguration -> {
                        videoPlayer.initializeVideoPlayer(it.videoUri, false)
                        viewModel.setVideoPlayerStateToReadyToPlay()
                    }
                    is ReviewVideoPlayerUiState.RestoringState -> {
                        videoPlayer.restorePlaybackState(it.presentationTimeMs, it.playWhenReady)
                        viewModel.setVideoPlayerStateToReadyToPlay()
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

    private fun collectVideoPlaybackUiState() {
        videoPlaybackUiStateCollector = videoPlaybackUiStateCollector?.takeIf { !it.isCompleted } ?: launch {
            viewModel.videoPlaybackUiState.collectLatest {
                when (it) {
                    is ReviewVideoPlaybackUiState.Inactive -> {
                        viewModel.showVideoThumbnail()
                        viewModel.hideVideoError()
                    }
                    is ReviewVideoPlaybackUiState.Error -> {
                        viewModel.showVideoThumbnail()
                        viewModel.showVideoError()
                    }
                    else -> {
                        viewModel.hideVideoThumbnail()
                        viewModel.hideVideoError()
                    }
                }
            }
        }
    }

    private fun collectVideoErrorUiState() {
        videoErrorUiStateCollector = videoErrorUiStateCollector?.takeIf {
            !it.isCompleted
        } ?: launch {
            viewModel.videoErrorUiState.collectLatest {
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
            viewModel.videoThumbnailUiState.collectLatest {
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
            viewModel.setVideoUri(getVideoUri())
        } else {
            viewModel.restoreUiState(savedInstanceState)
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
            viewModel.updateVideoThumbnail(this)
        }
    }

    private fun getCurrentFrameBitmap(): Bitmap? {
        return (binding?.playerViewReviewVideoPlayer?.videoSurfaceView as? TextureView)?.bitmap
    }
}
