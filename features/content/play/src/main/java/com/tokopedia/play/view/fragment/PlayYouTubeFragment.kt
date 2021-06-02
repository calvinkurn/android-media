package com.tokopedia.play.view.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.youtube.player.YouTubeInitializationResult
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.floatingwindow.FloatingWindowAdapter
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.R
import com.tokopedia.play.analytic.PlayAnalytic
import com.tokopedia.play.analytic.VideoAnalyticHelper
import com.tokopedia.play.extensions.isAnyShown
import com.tokopedia.play.util.observer.DistinctObserver
import com.tokopedia.play.util.video.state.PlayViewerVideoState
import com.tokopedia.play.view.contract.PlayFragmentContract
import com.tokopedia.play.view.contract.PlayOrientationListener
import com.tokopedia.play.view.type.PiPState
import com.tokopedia.play.view.type.ScreenOrientation
import com.tokopedia.play.view.uimodel.recom.PlayVideoPlayerUiModel
import com.tokopedia.play.view.uimodel.recom.isYouTube
import com.tokopedia.play.view.viewcomponent.YouTubeViewComponent
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play_common.lifecycle.lifecycleBound
import com.tokopedia.play_common.view.RoundedConstraintLayout
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.unifycomponents.dpToPx
import javax.inject.Inject

/**
 * Created by jegul on 28/04/20
 */
class PlayYouTubeFragment @Inject constructor(
        private val viewModelFactory: ViewModelProvider.Factory,
        private val analytic: PlayAnalytic
): TkpdBaseV4Fragment(), PlayFragmentContract, YouTubeViewComponent.Listener, YouTubeViewComponent.DataSource {

    private lateinit var containerYouTube: RoundedConstraintLayout
    private val youtubeView by viewComponent { YouTubeViewComponent(it, R.id.fl_youtube_player, childFragmentManager, this, this) }

    private lateinit var playViewModel: PlayViewModel

    private lateinit var videoAnalyticHelper: VideoAnalyticHelper

    private val cornerRadius = 16f.dpToPx()

    private val channelId: String
        get() = arguments?.getString(PLAY_KEY_CHANNEL_ID).orEmpty()

    private val orientationListener: PlayOrientationListener
        get() = requireActivity() as PlayOrientationListener

    private val orientation: ScreenOrientation
        get() = ScreenOrientation.getByInt(resources.configuration.orientation)

    private val isYouTube: Boolean
        get() = playViewModel.videoPlayer.isYouTube

    private val pipAdapter: FloatingWindowAdapter by lifecycleBound(
            creator = { FloatingWindowAdapter(this@PlayYouTubeFragment) }
    )

    override fun getScreenName(): String = "Play YouTube"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playViewModel = ViewModelProvider(requireParentFragment(), viewModelFactory).get(PlayViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play_youtube, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)
        initAnalytic()
        setupObserve()
    }

    override fun onResume() {
        super.onResume()
        playViewModel.stopPiP()
    }

    override fun onPause() {
        super.onPause()
        if (isYouTube) videoAnalyticHelper.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isYouTube) videoAnalyticHelper.sendLeaveRoomAnalytic(channelId)
    }

    override fun onInterceptOrientationChangedEvent(newOrientation: ScreenOrientation): Boolean {
        return false
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val orientation = ScreenOrientation.getByInt(newConfig.orientation)
        youtubeView.setIsFullScreen(orientation.isLandscape)
    }

    /**
     * YouTube View Component Data Source
     */
    override fun isEligibleToPlay(view: YouTubeViewComponent): Boolean {
        return lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)
    }

    /**
     * YouTube View Component Listener
     */
    override fun onInitFailure(view: YouTubeViewComponent, result: YouTubeInitializationResult) { }

    override fun onEnterFullscreen(view: YouTubeViewComponent) {
        analytic.clickCtaFullScreenFromPortraitToLandscape()
        enterFullscreen()
    }

    override fun onExitFullscreen(view: YouTubeViewComponent) {
        exitFullscreen()
    }

    override fun onVideoStateChanged(view: YouTubeViewComponent, state: PlayViewerVideoState) {
        handleYouTubeVideoState(state)
    }

    /**
     * Private methods
     */
    private fun initAnalytic() {
        videoAnalyticHelper = VideoAnalyticHelper(requireContext(), analytic)
    }

    private fun initView(view: View) {
        with (view) {
            containerYouTube = findViewById(R.id.container_youtube)
        }
    }

    private fun enterFullscreen() {
        if (!orientation.isLandscape)
            orientationListener.onOrientationChanged(ScreenOrientation.Landscape, isTilting = false)
    }

    private fun exitFullscreen() {
        if (!orientation.isPortrait)
            orientationListener.onOrientationChanged(ScreenOrientation.Portrait, isTilting = false)
    }

    private fun handleYouTubeVideoState(state: PlayViewerVideoState) {
        if (isYouTube) videoAnalyticHelper.onNewVideoState(state)
    }

    private fun setupObserve() {
        observeVideoMeta()
        observeBottomInsetsState()
        observeEventUserInfo()
        observePiPEvent()
    }

    //region observe
    /**
     * Observe
     */
    private fun observeVideoMeta() {
        playViewModel.observableVideoMeta.observe(viewLifecycleOwner, Observer {
            youtubeViewOnStateChanged(videoPlayer = it.videoPlayer)
        })
    }

    private fun observeBottomInsetsState() {
        playViewModel.observableBottomInsetsState.observe(viewLifecycleOwner, DistinctObserver {
            if (::containerYouTube.isInitialized) {
                if (it.isAnyShown) containerYouTube.setCornerRadius(cornerRadius)
                else containerYouTube.setCornerRadius(0f)
            }
        })
    }

    private fun observeEventUserInfo() {
        playViewModel.observableStatusInfo.observe(viewLifecycleOwner, DistinctObserver {
            youtubeViewOnStateChanged(isFreezeOrBanned = it.statusType.isFreeze || it.statusType.isBanned)
        })
    }

    private fun observePiPEvent() {
        playViewModel.observableEventPiPState.observe(viewLifecycleOwner, Observer {
            if (it.peekContent() == PiPState.Stop) removePiP()
        })
    }

    private fun removePiP() {
        pipAdapter.removeByKey(PlayVideoFragment.FLOATING_WINDOW_KEY)
    }
    //endregion

    //region OnStateChanged
    private fun youtubeViewOnStateChanged(
            videoPlayer: PlayVideoPlayerUiModel = playViewModel.videoPlayer,
            isFreezeOrBanned: Boolean = playViewModel.isFreezeOrBanned
    ) {
        when {
            isFreezeOrBanned -> {
                youtubeView.safeRelease()
                youtubeView.hide()
            }
            videoPlayer is PlayVideoPlayerUiModel.YouTube -> {
                youtubeView.setYouTubeId(videoPlayer.youtubeId)
                youtubeView.show()
            }
            else -> {
                youtubeView.release()
                youtubeView.hide()
            }
        }
    }
    //endregion
}