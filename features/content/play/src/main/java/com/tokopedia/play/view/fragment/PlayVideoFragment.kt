package com.tokopedia.play.view.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.ui.PlayerView
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.floatingwindow.FloatingWindowAdapter
import com.tokopedia.floatingwindow.view.FloatingWindowView
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.R
import com.tokopedia.play.analytic.VideoAnalyticHelper
import com.tokopedia.play.extensions.isAnyShown
import com.tokopedia.play.util.blur.ImageBlurUtil
import com.tokopedia.play.util.observer.DistinctEventObserver
import com.tokopedia.play.util.observer.DistinctObserver
import com.tokopedia.play.util.video.state.BufferSource
import com.tokopedia.play.util.video.state.PlayViewerVideoState
import com.tokopedia.play.view.contract.PlayFragmentContract
import com.tokopedia.play.view.type.ScreenOrientation
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.General
import com.tokopedia.play.view.uimodel.VideoPlayerUiModel
import com.tokopedia.play.view.viewcomponent.EmptyViewComponent
import com.tokopedia.play.view.viewcomponent.OneTapViewComponent
import com.tokopedia.play.view.viewcomponent.VideoLoadingComponent
import com.tokopedia.play.view.viewcomponent.VideoViewComponent
import com.tokopedia.play.view.viewmodel.PlayVideoViewModel
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play_common.lifecycle.lifecycleBound
import com.tokopedia.play_common.lifecycle.whenLifecycle
import com.tokopedia.play_common.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play_common.view.RoundedConstraintLayout
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.unifycomponents.dpToPx
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by jegul on 29/11/19
 */
class PlayVideoFragment @Inject constructor(
        private val viewModelFactory: ViewModelProvider.Factory,
        dispatchers: CoroutineDispatcherProvider
) : TkpdBaseV4Fragment(), PlayFragmentContract {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(dispatchers.immediate + job)

    private val videoView by viewComponent { VideoViewComponent(it, R.id.view_video) }
    private val videoLoadingView by viewComponent { VideoLoadingComponent(it, R.id.view_video_loading) }
    private val oneTapView by viewComponent { OneTapViewComponent(it, R.id.iv_one_tap_finger) }
    private val overlayVideoView by viewComponent { EmptyViewComponent(it, R.id.v_play_overlay_video) }

    private val blurUtil: ImageBlurUtil by lifecycleBound (
            creator = { ImageBlurUtil(it.requireContext()) },
            onLifecycle = whenLifecycle {
                onDestroy { it.close() }
            }
    )

    private val pipAdapter: FloatingWindowAdapter by lifecycleBound(
            creator = { FloatingWindowAdapter(this@PlayVideoFragment) }
    )

    private val cornerRadius = 16f.dpToPx()

    private lateinit var playViewModel: PlayViewModel
    private lateinit var viewModel: PlayVideoViewModel

    private lateinit var videoAnalyticHelper: VideoAnalyticHelper

    private lateinit var containerVideo: RoundedConstraintLayout

    private val orientation: ScreenOrientation
        get() = ScreenOrientation.getByInt(resources.configuration.orientation)

    private val isYouTube: Boolean
        get() = playViewModel.videoPlayer.isYouTube

    private val channelId: String
        get() = arguments?.getString(PLAY_KEY_CHANNEL_ID).orEmpty()

    override fun getScreenName(): String = "Play Video"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playViewModel = ViewModelProvider(requireParentFragment(), viewModelFactory).get(PlayViewModel::class.java)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PlayVideoViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAnalytic()
        initView(view)
        setupView()
        setupObserve()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job.cancelChildren()
    }

    override fun onPause() {
        super.onPause()
        if (!isYouTube) videoAnalyticHelper.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!isYouTube) videoAnalyticHelper.sendLeaveRoomAnalytic(playViewModel.channelType)
    }

    override fun onInterceptOrientationChangedEvent(newOrientation: ScreenOrientation): Boolean {
        return false
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val orientation = ScreenOrientation.getByInt(newConfig.orientation)
        videoView.setOrientation(orientation, playViewModel.videoOrientation)
    }

    override fun onEnterPiPMode() {
        val videoMeta = playViewModel.observableVideoMeta.value ?: return
        val videoPlayer: General = videoMeta.videoPlayer as? General ?: return

        val scaleFactor = 0.4f

        val screenWidth = getScreenWidth()
        val screenHeight = getScreenHeight()
        val (width, height) = if (playViewModel.videoOrientation is VideoOrientation.Horizontal) {
            screenWidth to (9f/16 * screenWidth).toInt()
        } else {
            screenWidth to screenHeight
        }

        val view = LayoutInflater.from(requireContext()).inflate(R.layout.view_floating_video, null).also {
            val playerView = it.findViewById<PlayerView>(R.id.pv_video)
            PlayerView.switchTargetView(videoPlayer.exoPlayer, videoView.getPlayerView(), playerView)
        }

        val scaledWidth = (scaleFactor * width).toInt()
        val scaledHeight = (scaleFactor * height).toInt()

        pipAdapter.addView(
                floatingView = FloatingWindowView.Builder(
                        key = FLOATING_WINDOW_KEY,
                        view = view,
                        width = scaledWidth,
                        height = scaledHeight,
                ).setX(screenWidth - scaledWidth - 16)
                        .setY(screenHeight - scaledHeight - 16)
                        .build(),
                overwrite = true
        )
    }

    private fun initAnalytic() {
        videoAnalyticHelper = VideoAnalyticHelper(requireContext(), channelId)
    }

    private fun initView(view: View) {
        with (view) {
            containerVideo = findViewById(R.id.container_video)
        }
    }

    private fun setupView() {
        pipAdapter.removeByKey(FLOATING_WINDOW_KEY)
        videoView.setOrientation(orientation, playViewModel.videoOrientation)
    }

    private fun setupObserve() {
        observeVideoMeta()
        observeVideoProperty()
        observeOneTapOnboarding()
        observeBottomInsetsState()
        observeEventUserInfo()
    }

    private fun showVideoThumbnail() {
        val currentThumbnail = videoView.getCurrentBitmap()
        currentThumbnail?.let {
            scope.launch {
                videoView.showThumbnail(
                        blurUtil.blurImage(it, radius = BLUR_RADIUS)
                )
            }
        }
    }

    //region observe
    private fun observeVideoMeta() {
        playViewModel.observableVideoMeta.observe(viewLifecycleOwner, Observer { meta ->
            meta.videoStream?.let {
                videoView.setOrientation(orientation, it.orientation)
            }

            videoViewOnStateChanged(videoPlayer = meta.videoPlayer)
        })
    }

    private fun observeVideoProperty() {
        playViewModel.observableVideoProperty.observe(viewLifecycleOwner, DistinctObserver {
            if (!isYouTube) videoAnalyticHelper.onNewVideoState(playViewModel.userId, playViewModel.channelType, it.state)
            if (playViewModel.videoPlayer.isYouTube) videoView.hide()
            else {
                videoView.show()
                handleVideoStateChanged(it.state)
            }

            when (it.state) {
                PlayViewerVideoState.Waiting -> videoLoadingView.showWaitingState()
                is PlayViewerVideoState.Buffer -> videoLoadingView.show(source = it.state.bufferSource)
                PlayViewerVideoState.Play, PlayViewerVideoState.End, PlayViewerVideoState.Pause -> videoLoadingView.hide()
            }

            if (!playViewModel.channelType.isVod) {
                overlayVideoView.hide()
                return@DistinctObserver
            }
            when (it.state) {
                PlayViewerVideoState.End -> overlayVideoView.show()
                else -> overlayVideoView.hide()
            }
        })
    }

    private fun observeOneTapOnboarding() {
        viewModel.observableOneTapOnboarding.observe(viewLifecycleOwner, DistinctEventObserver {
            if (!orientation.isLandscape && !playViewModel.videoOrientation.isHorizontal) oneTapView.showAnimated()
        })
    }

    private fun observeBottomInsetsState() {
        playViewModel.observableBottomInsetsState.observe(viewLifecycleOwner, DistinctObserver {
            if (::containerVideo.isInitialized) {
                if (it.isAnyShown) containerVideo.setCornerRadius(cornerRadius)
                else containerVideo.setCornerRadius(0f)
            }
        })
    }

    private fun observeEventUserInfo() {
        playViewModel.observableEvent.observe(viewLifecycleOwner, DistinctObserver {
            if (it.isFreeze || it.isBanned) {
                oneTapView.hide()
            }

            videoViewOnStateChanged(isFreezeOrBanned = it.isFreeze || it.isBanned)
        })
    }
    //endregion

    private fun handleVideoStateChanged(state: PlayViewerVideoState) {
        when (state) {
            is PlayViewerVideoState.Buffer -> {
                if (state.bufferSource == BufferSource.Broadcaster) showVideoThumbnail()
                else videoView.hideThumbnail()
            }
            PlayViewerVideoState.Play,
            PlayViewerVideoState.Pause,
            PlayViewerVideoState.Waiting,
            PlayViewerVideoState.End -> {
                videoView.hideThumbnail()
            }
        }
    }

    //region OnStateChanged
    private fun videoViewOnStateChanged(
            videoPlayer: VideoPlayerUiModel = playViewModel.videoPlayer,
            isFreezeOrBanned: Boolean = playViewModel.isFreezeOrBanned
    ) {
        if (isFreezeOrBanned) {
            videoView.setPlayer(null)
            videoView.hide()
        } else if (videoPlayer is General) videoView.setPlayer(videoPlayer.exoPlayer)
    }
    //endregion

    companion object {
        private const val BLUR_RADIUS = 25f

        private const val FLOATING_WINDOW_KEY = "PLAY_VIEWER_PIP"
    }
}