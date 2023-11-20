package com.tokopedia.review.feature.media.player.video.presentation.fragment

import android.graphics.Bitmap
import android.os.Bundle
import android.util.LruCache
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
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.R
import com.tokopedia.review.common.extension.collectLatestWhenResumed
import com.tokopedia.review.databinding.FragmentReviewMediaGalleryVideoPlayerBinding
import com.tokopedia.review.feature.media.gallery.detailed.di.DetailedReviewMediaGalleryComponentInstance
import com.tokopedia.review.feature.media.gallery.detailed.di.qualifier.DetailedReviewMediaGalleryViewModelFactory
import com.tokopedia.review.feature.media.gallery.detailed.presentation.viewmodel.SharedReviewMediaGalleryViewModel
import com.tokopedia.review.feature.media.player.video.di.component.DaggerReviewVideoPlayerComponent
import com.tokopedia.review.feature.media.player.video.di.qualifier.ReviewVideoPlayerViewModelFactory
import com.tokopedia.review.feature.media.player.video.presentation.uistate.ReviewVideoErrorUiState
import com.tokopedia.review.feature.media.player.video.presentation.uistate.ReviewVideoPlaybackUiState
import com.tokopedia.review.feature.media.player.video.presentation.uistate.ReviewVideoPlayerUiState
import com.tokopedia.review.feature.media.player.video.presentation.uistate.ReviewVideoThumbnailUiState
import com.tokopedia.review.feature.media.player.video.presentation.viewmodel.ReviewVideoPlayerViewModel
import com.tokopedia.reviewcommon.extension.isMoreThanZero
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
import kotlin.math.ceil

class ReviewVideoPlayerFragment : BaseDaggerFragment(), CoroutineScope, ReviewVideoPlayerListener {
    companion object {
        private const val ARG_VIDEO_URI = "argVideoUri"
        private const val VIDEO_FRAME_SCALE = 0.5

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

    @Inject
    lateinit var bitmapCache: LruCache<String, Bitmap>

    private var binding by viewBinding(FragmentReviewMediaGalleryVideoPlayerBinding::bind)
    private var listener: Listener? = null
    private var videoPlaybackUiStateCollector: Job? = null
    private var videoErrorUiStateCollector: Job? = null
    private var videoThumbnailUiStateCollector: Job? = null

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
        collectVideoPlayerUiState()
        collectWifiConnectivityStatus()
    }

    override fun onPause() {
        super.onPause()
        updateCurrentFrameBitmap()
        reviewVideoPlayerViewModel.setPlaybackStateToInactive(videoPlayer.getCurrentPositionMillis())
        if (activity?.isChangingConfigurations != true) {
            reviewVideoPlayerViewModel.resetVideoPlayerState()
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
            .detailedReviewMediaGalleryComponent(DetailedReviewMediaGalleryComponentInstance.getInstance(requireContext()))
            .build()
            .inject(this)
    }

    override fun onReviewVideoPlayerIsPlaying() {
        reviewVideoPlayerViewModel.setPlaybackStateToPlaying(videoPlayer.getCurrentPositionMillis())
        sharedReviewMediaGalleryViewModel.updateVideoDurationMillis(videoPlayer.getDurationMillis())
    }

    override fun onReviewVideoPlayerIsBuffering() {
        reviewVideoPlayerViewModel.setPlaybackStateToBuffering(videoPlayer.getCurrentPositionMillis())
    }

    override fun onReviewVideoPlayerIsPaused() {
        reviewVideoPlayerViewModel.setPlaybackStateToPaused(videoPlayer.getCurrentPositionMillis())
        sharedReviewMediaGalleryViewModel.updateVideoDurationMillis(videoPlayer.getDurationMillis())
    }

    override fun onReviewVideoPlayerIsPreloading() {
        reviewVideoPlayerViewModel.setPlaybackStateToPreloading(videoPlayer.getCurrentPositionMillis())
    }

    override fun onReviewVideoPlayerIsEnded() {
        reviewVideoPlayerViewModel.setPlaybackStateToEnded(videoPlayer.getCurrentPositionMillis())
    }

    override fun onReviewVideoPlayerError(errorCode: String) {
        reviewVideoPlayerViewModel.setPlaybackStateToError(videoPlayer.getCurrentPositionMillis(), errorCode)
    }

    private fun getVideoUri(): String {
        return arguments?.getString(ARG_VIDEO_URI).orEmpty()
    }

    private fun collectVideoPlayerUiState() {
        viewLifecycleOwner.collectLatestWhenResumed(reviewVideoPlayerViewModel.videoPlayerUiState) {
            when (it) {
                is ReviewVideoPlayerUiState.Initial -> {
                    videoPlayer.initializeVideoPlayer(it.videoUri)
                    reviewVideoPlayerViewModel.setVideoPlayerStateToRestoring()
                }
                is ReviewVideoPlayerUiState.ChangingConfiguration -> {
                    videoPlayer.initializeVideoPlayer(it.videoUri)
                    reviewVideoPlayerViewModel.setVideoPlayerStateToRestoring()
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

    private fun trackImpression() {
        val videoDurationSecond = videoPlayer.getVideoDurationSecond()
        if (listener != null && !reviewVideoPlayerViewModel.getImpressHolder().isInvoke && videoDurationSecond.isMoreThanZero()) {
            reviewVideoPlayerViewModel.getImpressHolder().invoke()
            listener?.onVideoImpressed(getVideoUri(), videoDurationSecond)
        }
    }

    private fun trackPlaying() {
        val videoDurationSecond = videoPlayer.getVideoDurationSecond()
        if (listener != null && videoDurationSecond.isMoreThanZero()) {
            listener?.onVideoPlaying(getVideoUri(), videoDurationSecond)
        }
    }

    private fun trackStopped(watchingDurationSecond: Long) {
        val videoDurationSecond = videoPlayer.getVideoDurationSecond()
        if (listener != null && watchingDurationSecond.isMoreThanZero() && videoDurationSecond.isMoreThanZero()) {
            listener?.onVideoStopped(getVideoUri(), videoDurationSecond, watchingDurationSecond)
        }
    }

    private fun collectWifiConnectivityStatus() {
        viewLifecycleOwner.collectLatestWhenResumed(sharedReviewMediaGalleryViewModel.connectedToWifi) {
            reviewVideoPlayerViewModel.updateWifiConnectivityStatus(it)
        }
    }

    private fun collectVideoPlaybackUiState() {
        videoPlaybackUiStateCollector = videoPlaybackUiStateCollector?.takeIf { !it.isCompleted } ?: launch {
            reviewVideoPlayerViewModel.videoPlaybackUiState.collectLatest {
                sharedReviewMediaGalleryViewModel.updateIsPlayingVideo(it.isPlaying())
                when (it) {
                    is ReviewVideoPlaybackUiState.Inactive -> {
                        trackStopped(ceil(it.currentPosition / 1000f).toLong())
                        binding?.loaderReviewVideoPlayer?.gone()
                        binding?.overlayReviewVideoPlayerLoading?.gone()
                        reviewVideoPlayerViewModel.hideVideoError()
                    }
                    is ReviewVideoPlaybackUiState.Error -> {
                        binding?.loaderReviewVideoPlayer?.gone()
                        binding?.overlayReviewVideoPlayerLoading?.gone()
                        reviewVideoPlayerViewModel.showVideoError(it.errorCode)
                    }
                    is ReviewVideoPlaybackUiState.Buffering,
                    is ReviewVideoPlaybackUiState.Preloading -> {
                        trackImpression()
                        binding?.loaderReviewVideoPlayer?.show()
                        binding?.overlayReviewVideoPlayerLoading?.show()
                        reviewVideoPlayerViewModel.hideVideoError()
                    }
                    is ReviewVideoPlaybackUiState.Playing -> {
                        trackImpression()
                        trackPlaying()
                        binding?.loaderReviewVideoPlayer?.gone()
                        binding?.overlayReviewVideoPlayerLoading?.gone()
                        reviewVideoPlayerViewModel.hideVideoError()
                    }
                    is ReviewVideoPlaybackUiState.Paused,
                    is ReviewVideoPlaybackUiState.Ended -> {
                        trackImpression()
                        trackStopped(ceil(it.currentPosition / 1000f).toLong())
                        binding?.loaderReviewVideoPlayer?.gone()
                        binding?.overlayReviewVideoPlayerLoading?.gone()
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
                        getString(R.string.review_video_player_error_message, it.errorCode),
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
                        val bitmap = bitmapCache.get(reviewVideoPlayerViewModel.videoPlayerUiState.value.videoUri)
                        if (bitmap == null) {
                            binding?.ivReviewVideoPlayerFramePreview?.gone()
                        } else {
                            binding?.ivReviewVideoPlayerFramePreview?.loadImage(bitmap)
                            binding?.ivReviewVideoPlayerFramePreview?.show()
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

    private fun ReviewVideoPlayer.initializeVideoPlayer(videoUri: String) {
        val playerView = binding?.playerViewReviewVideoPlayer ?: return
        initializeVideoPlayer(
            uri = videoUri,
            newPlayerView = playerView,
            newListener = this@ReviewVideoPlayerFragment,
            shouldPrepare = true
        )
    }

    private fun updateCurrentFrameBitmap() {
        val bitmap = getCurrentFrameBitmap() ?: return
        bitmapCache.put(reviewVideoPlayerViewModel.videoPlayerUiState.value.videoUri, bitmap)
    }

    private fun getCurrentFrameBitmap(): Bitmap? {
        val textureView = binding?.playerViewReviewVideoPlayer?.videoSurfaceView as? TextureView
        return if (textureView == null) {
            null
        } else {
            val textureViewWidth = (textureView.width * VIDEO_FRAME_SCALE).toInt()
            val textureViewHeight = (textureView.height * VIDEO_FRAME_SCALE).toInt()
            val bitmap = bitmapCache.get(
                reviewVideoPlayerViewModel.videoPlayerUiState.value.videoUri
            )?.takeIf { cachedBitmap ->
                val cachedBitmapWidth = (cachedBitmap.width * VIDEO_FRAME_SCALE).toInt()
                val cachedBitmapHeight = (cachedBitmap.height * VIDEO_FRAME_SCALE).toInt()
                cachedBitmapWidth == textureViewWidth && cachedBitmapHeight == textureViewHeight
            } ?: Bitmap.createBitmap(textureViewWidth, textureViewHeight, Bitmap.Config.RGB_565)
            textureView.getBitmap(bitmap)
        }
    }

    fun setListener(newListener: Listener) {
        listener = newListener
    }

    interface Listener {
        fun onVideoImpressed(videoUri: String, videoDurationSecond: Long)
        fun onVideoPlaying(videoUri: String, videoDurationSecond: Long)
        fun onVideoStopped(videoUri: String, videoDurationSecond: Long, watchingDurationSecond: Long)
    }
}
