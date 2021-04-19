package com.tokopedia.play.view.fragment

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.ui.PlayerView
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.floatingwindow.FloatingWindowAdapter
import com.tokopedia.floatingwindow.exception.FloatingWindowException
import com.tokopedia.floatingwindow.permission.FloatingWindowPermissionManager
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.R
import com.tokopedia.play.analytic.PlayAnalytic
import com.tokopedia.play.analytic.PlayPiPAnalytic
import com.tokopedia.play.analytic.VideoAnalyticHelper
import com.tokopedia.play.extensions.isAnyBottomSheetsShown
import com.tokopedia.play.extensions.isAnyShown
import com.tokopedia.play.util.PlayViewerPiPCoordinator
import com.tokopedia.play.util.observer.DistinctEventObserver
import com.tokopedia.play.util.observer.DistinctObserver
import com.tokopedia.play.util.video.state.BufferSource
import com.tokopedia.play.util.video.state.PlayViewerVideoState
import com.tokopedia.play.view.activity.PlayActivity
import com.tokopedia.play.view.contract.PlayFragmentContract
import com.tokopedia.play.view.contract.PlayPiPCoordinator
import com.tokopedia.play.view.pip.PlayViewerPiPView
import com.tokopedia.play.view.storage.PiPSessionStorage
import com.tokopedia.play.view.type.PiPMode
import com.tokopedia.play.view.type.PiPState
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.type.ScreenOrientation
import com.tokopedia.play.view.uimodel.OpenApplinkUiModel
import com.tokopedia.play.view.uimodel.PiPInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayVideoPlayerUiModel
import com.tokopedia.play.view.uimodel.recom.isYouTube
import com.tokopedia.play.view.viewcomponent.EmptyViewComponent
import com.tokopedia.play.view.viewcomponent.OnboardingViewComponent
import com.tokopedia.play.view.viewcomponent.VideoLoadingComponent
import com.tokopedia.play.view.viewcomponent.VideoViewComponent
import com.tokopedia.play.view.viewmodel.PlayParentViewModel
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play_common.lifecycle.lifecycleBound
import com.tokopedia.play_common.lifecycle.whenLifecycle
import com.tokopedia.play_common.util.blur.ImageBlurUtil
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play_common.view.RoundedConstraintLayout
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.play_common.viewcomponent.viewComponentOrNull
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
        dispatchers: CoroutineDispatchers,
        private val pipAnalytic: PlayPiPAnalytic,
        private val analytic: PlayAnalytic,
        private val pipSessionStorage: PiPSessionStorage
) : TkpdBaseV4Fragment(), PlayFragmentContract, VideoViewComponent.DataSource {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(dispatchers.immediate + job)

    private val videoView by viewComponent { VideoViewComponent(it, R.id.view_video, this) }
    private val videoLoadingView by viewComponent { VideoLoadingComponent(it, R.id.view_video_loading) }
    private val overlayVideoView by viewComponent { EmptyViewComponent(it, R.id.v_play_overlay_video) }
    private val onboardingView by viewComponentOrNull { OnboardingViewComponent(it, R.id.iv_onboarding) }

    private val blurUtil: ImageBlurUtil by lifecycleBound (
            creator = { ImageBlurUtil(it.requireContext()) },
            onLifecycle = whenLifecycle {
                onDestroy { it.close() }
            }
    )

    private val pipAdapter: FloatingWindowAdapter by lifecycleBound(
            creator = { FloatingWindowAdapter(this@PlayVideoFragment) }
    )

    private val playPiPCoordinator: PlayPiPCoordinator
        get() = requireActivity() as PlayPiPCoordinator

    private var isEnterPiPAfterPermission: Boolean = false

    private val playViewerPiPCoordinatorListener = object : PlayViewerPiPCoordinator.Listener {

        private fun openDialog(requestHandler: () -> Unit, cancelHandler: () -> Unit, dismissHandler: () -> Unit) {
            DialogUnify(requireContext(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
                    .apply {
                        setTitle(getString(R.string.play_pip_permission_rationale_title))
                        setDescription(getString(R.string.play_pip_permission_rationale_desc))
                        setPrimaryCTAText(getString(R.string.play_pip_activate))
                        setPrimaryCTAClickListener {
                            requestHandler()
                            dismiss()
                        }
                        setSecondaryCTAText(getString(R.string.play_pip_cancel))
                        setSecondaryCTAClickListener {
                            cancelHandler()
                            dismiss()
                        }
                        dialogOverlay.setOnClickListener {
                            dismissHandler()
                        }
                    }.show()
        }

        override fun onShouldRequestPermission(pipMode: PiPMode, requestPermissionFlow: FloatingWindowPermissionManager.RequestPermissionFlow) {
            val dialogHandler = { openDialog(
                    requestHandler = {
                        requestPermissionFlow.requestPermission()
                        if (pipMode is PiPMode.BrowsingOtherPage) {
                            pipSessionStorage.setHasRequestedPiPBrowsing(true)
                        }
                     },
                    cancelHandler = {
                        requestPermissionFlow.cancel()
                        if (pipMode is PiPMode.BrowsingOtherPage) {
                            pipSessionStorage.setHasRequestedPiPBrowsing(true)
                            onShouldOpenApplink(pipMode.applinkModel)
                        }
                    },
                    dismissHandler = {
                        requestPermissionFlow.cancel()
                    }
            ) }

            when (pipMode) {
                PiPMode.WatchInPiP -> dialogHandler()
                is PiPMode.BrowsingOtherPage -> {
                    if (!pipSessionStorage.hasRequestedPiPBrowsing()) {
                        dialogHandler()
                    } else {
                        onShouldOpenApplink(pipMode.applinkModel)
                        requestPermissionFlow.cancel()
                    }
                }
            }
        }

        override fun onFailedEnterPiPMode(error: FloatingWindowException) {
        }

        override fun onSucceededEnterPiPMode(view: PlayViewerPiPView) {
            playViewModel.goPiP()

            isEnterPiPAfterPermission = true

            val videoPlayer = playViewModel.videoPlayer as? PlayVideoPlayerUiModel.General.Complete ?: return
            PlayerView.switchTargetView(videoPlayer.exoPlayer, videoView.getPlayerView(), view.getPlayerView())

            if (playViewModel.pipState.mode == PiPMode.WatchInPiP) {
                playPiPCoordinator.onEnterPiPMode()
                pipAnalytic.enterPiP(
                        channelId = channelId,
                        shopId = playViewModel.partnerId,
                        channelType = playViewModel.channelType
                )
            }
        }

        override fun onShouldOpenApplink(applinkModel: OpenApplinkUiModel) {
            openApplink(
                    applink = applinkModel.applink,
                    requestCode = applinkModel.requestCode,
                    shouldFinish = applinkModel.shouldFinish,
                    params = applinkModel.params.toTypedArray(),
            )
        }
    }

    private lateinit var pipCoordinator: PlayViewerPiPCoordinator

    private val cornerRadius = 16f.dpToPx()

    private lateinit var playParentViewModel: PlayParentViewModel
    private lateinit var playViewModel: PlayViewModel

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

        val theActivity = requireActivity()
        if (theActivity is PlayActivity) {
            playParentViewModel = ViewModelProvider(theActivity, theActivity.getViewModelFactory()).get(PlayParentViewModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play_video, container, false)
    }

    override fun onResume() {
        super.onResume()
        if (!isEnterPiPAfterPermission) {
            if (::pipCoordinator.isInitialized) {
                pipCoordinator.view.setPauseOnDetached(false)
            }
            playViewModel.stopPiP()
        }
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
        isEnterPiPAfterPermission = false
        if (!isYouTube) videoAnalyticHelper.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!isYouTube) videoAnalyticHelper.sendLeaveRoomAnalytic()
    }

    override fun onInterceptOrientationChangedEvent(newOrientation: ScreenOrientation): Boolean {
        return false
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val orientation = ScreenOrientation.getByInt(newConfig.orientation)
        videoView.setOrientation(orientation, playViewModel.videoOrientation)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        pipAdapter.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onEnterPiPState(pipState: PiPState) {
        val videoMeta = playViewModel.observableVideoMeta.value ?: return
        if (videoMeta.videoPlayer !is PlayVideoPlayerUiModel.General) return

        pipCoordinator = PlayViewerPiPCoordinator(
                context = requireContext(),
                videoPlayer = playViewModel.getVideoPlayer(),
                videoOrientation = playViewModel.videoOrientation,
                pipInfoUiModel = PiPInfoUiModel(
                        channelId = channelId,
                        source = playParentViewModel.source,
                        partnerId = playViewModel.partnerId,
                        channelType = playViewModel.channelType,
                        videoPlayer = videoMeta.videoPlayer,
                        videoStream = videoMeta.videoStream,
                        stopOnClose = pipState.mode == PiPMode.WatchInPiP,
                        pipMode = pipState.mode!!
                ),
                pipAdapter = pipAdapter,
                listener = playViewerPiPCoordinatorListener
        )
        pipCoordinator.startPip()
    }

    /**
     * Video View Component DataSource
     */
    override fun isInPiPMode(): Boolean {
        return playViewModel.pipState.isInPiP
    }

    private fun initAnalytic() {
        videoAnalyticHelper = VideoAnalyticHelper(requireContext(), analytic)
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
        observeVideoMeta()
        observeVideoProperty()
        observeBottomInsetsState()
        observeStatusInfo()
        observePiPEvent()
        observeOnboarding()
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

    private fun removePiP() {
        pipAdapter.removeByKey(FLOATING_WINDOW_KEY)
        videoViewOnStateChanged()
    }

    //region observe
    private fun observeVideoMeta() {
        playViewModel.observableVideoMeta.observe(viewLifecycleOwner, Observer { meta ->
            videoView.setOrientation(orientation, meta.videoStream.orientation)

            videoViewOnStateChanged(videoPlayer = meta.videoPlayer)
        })
    }

    private fun observeVideoProperty() {
        playViewModel.observableVideoProperty.observe(viewLifecycleOwner, DistinctObserver {
            videoAnalyticHelper.onNewVideoState(it.state)

            videoLoadingViewOnStateChanged(state = it.state)
            videoViewOnStateChanged(state = it.state)
            overlayVideoViewOnStateChanged(state = it.state)
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

    private fun observeStatusInfo() {
        playViewModel.observableStatusInfo.observe(viewLifecycleOwner, DistinctObserver {
            val isFreezeOrBanned = it.statusType.isFreeze || it.statusType.isBanned

            videoViewOnStateChanged(isFreezeOrBanned = isFreezeOrBanned)
            videoLoadingViewOnStateChanged(isFreezeOrBanned = isFreezeOrBanned)
        })
    }

    private fun observePiPEvent() {
        playViewModel.observableEventPiPState.observe(viewLifecycleOwner, Observer {
            if (it.peekContent() == PiPState.Stop) removePiP()
        })
    }

    private fun observeOnboarding() {
        playViewModel.observableOnboarding.observe(viewLifecycleOwner, DistinctEventObserver {
            if (!orientation.isLandscape) onboardingView?.showAnimated()
        })
    }
    //endregion

    private fun openApplink(applink: String, vararg params: String, requestCode: Int? = null, shouldFinish: Boolean = false) {
        if (requestCode == null) {
            RouteManager.route(context, applink, *params)
        } else {
            val intent = RouteManager.getIntent(context, applink, *params)
            startActivityForResult(intent, requestCode)
        }

        activity?.overridePendingTransition(R.anim.anim_play_enter_page, R.anim.anim_play_exit_page)

        if (shouldFinish) activity?.finish()
    }

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
            pipState: PiPState = playViewModel.pipState,
            state: PlayViewerVideoState = playViewModel.viewerVideoState,
            videoPlayer: PlayVideoPlayerUiModel = playViewModel.videoPlayer,
            isFreezeOrBanned: Boolean = playViewModel.isFreezeOrBanned,
    ) {
        if (isFreezeOrBanned && !playViewModel.bottomInsets.isAnyBottomSheetsShown) {
            videoView.setPlayer(null)
            videoView.hide()
            return
        }

        if (pipState is PiPState.InPiP) return

        when (videoPlayer) {
            is PlayVideoPlayerUiModel.YouTube, PlayVideoPlayerUiModel.Unknown -> videoView.hide()
            is PlayVideoPlayerUiModel.General -> {
                if (videoPlayer is PlayVideoPlayerUiModel.General.Complete) videoView.setPlayer(videoPlayer.exoPlayer)

                videoAnalyticHelper.onNewVideoState(state)
                videoView.show()
                handleVideoStateChanged(state)
            }
        }
    }

    private fun videoLoadingViewOnStateChanged(
            state: PlayViewerVideoState = playViewModel.viewerVideoState,
            isFreezeOrBanned: Boolean = playViewModel.isFreezeOrBanned
    ) {
        if (isFreezeOrBanned) {
            videoLoadingView.hide()
            return
        }

        when (state) {
            PlayViewerVideoState.Waiting -> videoLoadingView.showWaitingState()
            is PlayViewerVideoState.Buffer -> videoLoadingView.show(source = state.bufferSource)
            PlayViewerVideoState.Play, PlayViewerVideoState.End, PlayViewerVideoState.Pause -> videoLoadingView.hide()
        }
    }

    private fun overlayVideoViewOnStateChanged(
            state: PlayViewerVideoState = playViewModel.viewerVideoState,
            channelType: PlayChannelType = playViewModel.channelType,
    ) {
        if (!channelType.isVod) {
            overlayVideoView.hide()
            return
        }

        when (state) {
            PlayViewerVideoState.End -> overlayVideoView.show()
            else -> overlayVideoView.hide()
        }
    }
    //endregion

    companion object {
        private const val BLUR_RADIUS = 25f

        const val FLOATING_WINDOW_KEY = "PLAY_VIEWER_PIP"
    }
}