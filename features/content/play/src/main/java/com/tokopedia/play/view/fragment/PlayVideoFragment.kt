package com.tokopedia.play.view.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.R
import com.tokopedia.play.analytic.VideoAnalyticHelper
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.di.DaggerPlayComponent
import com.tokopedia.play.di.PlayModule
import com.tokopedia.play.extensions.isAnyHidden
import com.tokopedia.play.extensions.isAnyShown
import com.tokopedia.play.ui.loading.VideoLoadingComponent
import com.tokopedia.play.ui.onetap.OneTapComponent
import com.tokopedia.play.ui.overlayvideo.OverlayVideoComponent
import com.tokopedia.play.ui.video.VideoComponent
import com.tokopedia.play.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play.util.event.DistinctEventObserver
import com.tokopedia.play.util.observer.DistinctObserver
import com.tokopedia.play.util.video.PlayVideoUtil
import com.tokopedia.play.view.contract.PlayFragmentContract
import com.tokopedia.play.view.custom.RoundedConstraintLayout
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.layout.video.PlayVideoLayoutManager
import com.tokopedia.play.view.layout.video.PlayVideoLayoutManagerImpl
import com.tokopedia.play.view.layout.video.PlayVideoViewInitializer
import com.tokopedia.play.view.type.PlayRoomEvent
import com.tokopedia.play.view.type.ScreenOrientation
import com.tokopedia.play.view.uimodel.EventUiModel
import com.tokopedia.play.view.uimodel.VideoPropertyUiModel
import com.tokopedia.play.view.uimodel.VideoStreamUiModel
import com.tokopedia.play.view.viewmodel.PlayVideoViewModel
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.unifycomponents.dpToPx
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by jegul on 29/11/19
 */
class PlayVideoFragment : BaseDaggerFragment(), PlayVideoViewInitializer, PlayFragmentContract {

    companion object {

        fun newInstance(channelId: String): PlayVideoFragment {
            return PlayVideoFragment().apply {
                val bundle = Bundle()
                bundle.putString(PLAY_KEY_CHANNEL_ID, channelId)
                arguments = bundle
            }
        }
    }

    private val scope = object : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = job + dispatchers.main
    }
    private val job: Job = SupervisorJob()

    private val cornerRadius = 16f.dpToPx()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var dispatchers: CoroutineDispatcherProvider

    @Inject
    lateinit var playVideoUtil: PlayVideoUtil

    private var channelId = ""

    private lateinit var playViewModel: PlayViewModel
    private lateinit var viewModel: PlayVideoViewModel

    private lateinit var layoutManager: PlayVideoLayoutManager
    private lateinit var videoAnalyticHelper: VideoAnalyticHelper

    private lateinit var containerVideo: RoundedConstraintLayout

    private val orientation: ScreenOrientation
        get() = ScreenOrientation.getByInt(resources.configuration.orientation)

    private val isYouTube: Boolean
        get() = playViewModel.videoPlayer.isYouTube

    override fun getScreenName(): String = "Play Video"

    override fun initInjector() {
        DaggerPlayComponent
                .builder()
                .baseAppComponent(
                        (requireContext().applicationContext as BaseMainApplication).baseAppComponent
                )
                .playModule(PlayModule(requireContext()))
                .build()
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playViewModel = ViewModelProvider(requireParentFragment(), viewModelFactory).get(PlayViewModel::class.java)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PlayVideoViewModel::class.java)
        channelId = arguments?.getString(PLAY_KEY_CHANNEL_ID).orEmpty()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_play_video, container, false)
        containerVideo = view.findViewById(R.id.container_video)

        initComponents(view as ViewGroup)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAnalytic()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeVideoPlayer()
        observeVideoProperty()
        observeOneTapOnboarding()
        observeBottomInsetsState()
        observeEventUserInfo()
        observeVideoStream()
    }

    override fun onPause() {
        super.onPause()
        if (!isYouTube) videoAnalyticHelper.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        layoutManager.onDestroy()
        job.cancelChildren()
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
        sendOrientationChangedEvent(ScreenOrientation.getByInt(newConfig.orientation))
    }

    private fun initAnalytic() {
        videoAnalyticHelper = VideoAnalyticHelper(requireContext(), channelId)
    }

    //region observe
    private fun observeVideoPlayer() {
        playViewModel.observableVideoPlayer.observe(viewLifecycleOwner, Observer {
            scope.launch {
                EventBusFactory.get(viewLifecycleOwner)
                        .emit(
                                ScreenStateEvent::class.java,
                                ScreenStateEvent.SetVideo(it)
                        )
            }
        })
    }

    private fun observeVideoProperty() {
        playViewModel.observableVideoProperty.observe(viewLifecycleOwner, DistinctObserver {
            if (!isYouTube) videoAnalyticHelper.onNewVideoState(playViewModel.userId, playViewModel.channelType, it.state)
            delegateVideoProperty(it)
        })
    }

    private fun observeOneTapOnboarding() {
        viewModel.observableOneTapOnboarding.observe(viewLifecycleOwner, DistinctEventObserver { showOneTapOnboarding() })
    }

    private fun observeBottomInsetsState() {
        playViewModel.observableBottomInsetsState.observe(viewLifecycleOwner, DistinctObserver {
            if (::containerVideo.isInitialized) {
                if (it.isAnyShown) containerVideo.setCornerRadius(cornerRadius)
                else containerVideo.setCornerRadius(0f)
            }

            scope.launch {
                EventBusFactory.get(viewLifecycleOwner)
                        .emit(
                                ScreenStateEvent::class.java,
                                ScreenStateEvent.BottomInsetsChanged(it, it.isAnyShown, it.isAnyHidden, playViewModel.getStateHelper(orientation))
                        )
            }
        })
    }

    private fun observeEventUserInfo() {
        playViewModel.observableEvent.observe(viewLifecycleOwner, DistinctObserver {
            scope.launch {
                if (it.isBanned) sendEventBanned(it)
                else if(it.isFreeze) sendEventFreeze(it)
            }
        })
    }

    private fun observeVideoStream() {
        playViewModel.observableVideoStream.observe(viewLifecycleOwner, DistinctObserver(::setVideoStream))
    }
    //endregion

    //region Component Initialization
    private fun initComponents(container: ViewGroup) {
        layoutManager = PlayVideoLayoutManagerImpl(
                container = container,
                videoOrientation = playViewModel.videoOrientation,
                viewInitializer = this
        )

        sendInitState()

        layoutManager.layoutView(container)
    }

    override fun onInitVideo(container: ViewGroup): Int {
        return VideoComponent(container, EventBusFactory.get(viewLifecycleOwner), scope, dispatchers, playVideoUtil)
                .also(viewLifecycleOwner.lifecycle::addObserver)
                .getContainerId()
    }

    override fun onInitVideoLoading(container: ViewGroup): Int {
        return VideoLoadingComponent(container, EventBusFactory.get(viewLifecycleOwner), scope, dispatchers)
                .getContainerId()
    }

    override fun onInitOneTap(container: ViewGroup): Int {
        return OneTapComponent(container, EventBusFactory.get(viewLifecycleOwner), scope, dispatchers)
                .getContainerId()
    }

    override fun onInitOverlayVideo(container: ViewGroup): Int {
        return OverlayVideoComponent(container, EventBusFactory.get(viewLifecycleOwner), scope, dispatchers)
                .getContainerId()
    }
    //endregion

    private fun sendInitState() {
        scope.launch(dispatchers.immediate) {
            EventBusFactory.get(viewLifecycleOwner).emit(
                    ScreenStateEvent::class.java,
                    ScreenStateEvent.Init(orientation, playViewModel.getStateHelper(orientation))
            )
        }
    }

    private fun sendOrientationChangedEvent(orientation: ScreenOrientation) {
        scope.launch {
            EventBusFactory.get(viewLifecycleOwner).emit(
                    ScreenStateEvent::class.java,
                    ScreenStateEvent.OrientationChanged(orientation, playViewModel.getStateHelper(orientation))
            )
        }
    }

    private fun delegateVideoProperty(prop: VideoPropertyUiModel) {
        scope.launch {
            EventBusFactory.get(viewLifecycleOwner)
                    .emit(
                            ScreenStateEvent::class.java,
                            ScreenStateEvent.VideoPropertyChanged(prop, playViewModel.getStateHelper(orientation))
                    )
        }
    }

    private fun showOneTapOnboarding() {
        scope.launch {
            EventBusFactory.get(viewLifecycleOwner)
                    .emit(
                            ScreenStateEvent::class.java,
                            ScreenStateEvent.ShowOneTapOnboarding(playViewModel.getStateHelper(orientation))
                    )
        }
    }

    private fun sendEventBanned(eventUiModel: EventUiModel) {
        scope.launch {
            EventBusFactory.get(viewLifecycleOwner)
                    .emit(
                            ScreenStateEvent::class.java,
                            ScreenStateEvent.OnNewPlayRoomEvent(
                                    PlayRoomEvent.Banned(
                                            title = eventUiModel.bannedTitle,
                                            message = eventUiModel.bannedMessage,
                                            btnTitle = eventUiModel.bannedButtonTitle
                                    )
                            )
                    )
        }
    }

    private fun sendEventFreeze(eventUiModel: EventUiModel) {
        scope.launch {
            EventBusFactory.get(viewLifecycleOwner)
                    .emit(
                            ScreenStateEvent::class.java,
                            ScreenStateEvent.OnNewPlayRoomEvent(
                                    PlayRoomEvent.Freeze(
                                            title = eventUiModel.freezeTitle,
                                            message = eventUiModel.freezeMessage,
                                            btnTitle = eventUiModel.freezeButtonTitle,
                                            btnUrl = eventUiModel.freezeButtonUrl
                                    )
                            )
                    )
        }
    }

    private fun setVideoStream(videoStream: VideoStreamUiModel) {
        scope.launch {
            EventBusFactory.get(viewLifecycleOwner)
                    .emit(
                            ScreenStateEvent::class.java,
                            ScreenStateEvent.VideoStreamChanged(videoStream, playViewModel.getStateHelper(orientation))
                    )
        }
    }
}