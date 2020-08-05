package com.tokopedia.play.view.fragment

import android.content.res.Configuration
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.R
import com.tokopedia.play.analytic.VideoAnalyticHelper
import com.tokopedia.play.extensions.isAnyShown
import com.tokopedia.play.util.event.DistinctEventObserver
import com.tokopedia.play.util.observer.DistinctObserver
import com.tokopedia.play.util.video.PlayVideoUtil
import com.tokopedia.play.view.contract.PlayFragmentContract
import com.tokopedia.play.view.custom.RoundedConstraintLayout
import com.tokopedia.play.view.type.ScreenOrientation
import com.tokopedia.play.view.uimodel.General
import com.tokopedia.play.view.viewcomponent.EmptyViewComponent
import com.tokopedia.play.view.viewcomponent.OneTapViewComponent
import com.tokopedia.play.view.viewcomponent.VideoViewComponent
import com.tokopedia.play.view.viewmodel.PlayVideoViewModel
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play_common.state.PlayVideoState
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.unifycomponents.dpToPx
import javax.inject.Inject

/**
 * Created by jegul on 29/11/19
 */
class PlayVideoFragment @Inject constructor(
        private val viewModelFactory: ViewModelProvider.Factory,
        private val playVideoUtil: PlayVideoUtil
): TkpdBaseV4Fragment(), PlayFragmentContract {

    private val videoView by viewComponent { VideoViewComponent(it, R.id.view_video) }
    private val videoLoadingView by viewComponent { EmptyViewComponent(it, R.id.view_video_loading) }
    private val oneTapView by viewComponent { OneTapViewComponent(it, R.id.iv_one_tap_finger) }
    private val overlayVideoView by viewComponent { EmptyViewComponent(it, R.id.v_play_overlay_video) }

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
        val view = inflater.inflate(R.layout.fragment_play_video, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAnalytic()
        initView(view)
        setupView()
        setupObserve()
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

    override fun onInterceptSystemUiVisibilityChanged(): Boolean {
        return false
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val orientation = ScreenOrientation.getByInt(newConfig.orientation)
        videoView.setOrientation(orientation, playViewModel.videoOrientation)
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
        videoView.setOrientation(orientation, playViewModel.videoOrientation)
    }

    private fun setupObserve() {
        observeVideoPlayer()
        observeVideoProperty()
        observeOneTapOnboarding()
        observeBottomInsetsState()
        observeEventUserInfo()
        observeVideoStream()
    }

    //region observe
    private fun observeVideoPlayer() {
        playViewModel.observableVideoPlayer.observe(viewLifecycleOwner, Observer {
            if (it is General) videoView.setPlayer(it.exoPlayer)
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
                PlayVideoState.Buffering -> videoLoadingView.show()
                PlayVideoState.Playing, PlayVideoState.Ended, PlayVideoState.NoMedia, PlayVideoState.Pause -> videoLoadingView.hide()
            }

            if (!playViewModel.channelType.isVod) {
                overlayVideoView.hide()
                return@DistinctObserver
            }
            when (it.state) {
                PlayVideoState.Ended -> overlayVideoView.show()
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
                videoView.hide()
                videoView.setPlayer(null)

                oneTapView.hide()
            }
        })
    }

    private fun observeVideoStream() {
        playViewModel.observableVideoStream.observe(viewLifecycleOwner, DistinctObserver {
            videoView.setOrientation(orientation, it.orientation)
        })
    }
    //endregion

    private fun handleVideoStateChanged(state: PlayVideoState) {
        when (state) {
            PlayVideoState.Playing, PlayVideoState.Pause -> {
                videoView.showThumbnail(null)
            }
            PlayVideoState.Ended -> {
                videoView.getCurrentBitmap()?.let { saveEndImage(it) }
            }
        }
    }

    private fun saveEndImage(bitmap: Bitmap) {
        playVideoUtil.saveEndImage(bitmap)
    }
}