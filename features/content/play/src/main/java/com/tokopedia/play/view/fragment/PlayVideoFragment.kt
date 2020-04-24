package com.tokopedia.play.view.fragment

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
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.di.DaggerPlayComponent
import com.tokopedia.play.di.PlayModule
import com.tokopedia.play.extensions.isAnyHidden
import com.tokopedia.play.extensions.isAnyShown
import com.tokopedia.play.ui.loading.VideoLoadingComponent
import com.tokopedia.play.ui.onetap.OneTapComponent
import com.tokopedia.play.ui.overlayvideo.OverlayVideoComponent
import com.tokopedia.play.ui.video.VideoComponent
import com.tokopedia.play.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play.util.event.EventObserver
import com.tokopedia.play.view.custom.RoundedConstraintLayout
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.layout.video.PlayVideoLayoutManager
import com.tokopedia.play.view.layout.video.PlayVideoLayoutManagerImpl
import com.tokopedia.play.view.layout.video.PlayVideoViewInitializer
import com.tokopedia.play.view.type.PlayRoomEvent
import com.tokopedia.play.view.uimodel.EventUiModel
import com.tokopedia.play.view.uimodel.VideoPropertyUiModel
import com.tokopedia.play.view.uimodel.VideoStreamUiModel
import com.tokopedia.play.view.viewmodel.PlayVideoViewModel
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.unifycomponents.dpToPx
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by jegul on 29/11/19
 */
class PlayVideoFragment :
        BaseDaggerFragment(),
        PlayVideoViewInitializer,
        CoroutineScope {

    companion object {

        fun newInstance(channelId: String): PlayVideoFragment {
            return PlayVideoFragment().apply {
                val bundle = Bundle()
                bundle.putString(PLAY_KEY_CHANNEL_ID, channelId)
                arguments = bundle
            }
        }
    }

    private val job: Job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = job + dispatchers.main

    private val cornerRadius = 16f.dpToPx()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var dispatchers: CoroutineDispatcherProvider

    private lateinit var playViewModel: PlayViewModel
    private lateinit var viewModel: PlayVideoViewModel

    private lateinit var layoutManager: PlayVideoLayoutManager

    private var channelId: String = ""

    private lateinit var containerVideo: RoundedConstraintLayout

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
        channelId  = arguments?.getString(PLAY_KEY_CHANNEL_ID).orEmpty()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_play_video, container, false)
        containerVideo = view.findViewById(R.id.container_video)

        initComponents(view as ViewGroup)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeVOD()
        observeVideoProperty()
        observeOneTapOnboarding()
        observeBottomInsetsState()
        observeEventUserInfo()
        observeVideoStream()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        layoutManager.onDestroy()
        job.cancel()
    }

    //region observe
    private fun observeVOD() {
        playViewModel.observableVOD.observe(viewLifecycleOwner, Observer {
            launch {
                EventBusFactory.get(viewLifecycleOwner)
                        .emit(
                                ScreenStateEvent::class.java,
                                ScreenStateEvent.SetVideo(it)
                        )
            }
        })
    }

    private fun observeVideoProperty() {
        playViewModel.observableVideoProperty.observe(viewLifecycleOwner, Observer(::delegateVideoProperty))
    }

    private fun observeOneTapOnboarding() {
        viewModel.observableOneTapOnboarding.observe(viewLifecycleOwner, EventObserver { showOneTapOnboarding() })
    }

    private fun observeBottomInsetsState() {
        playViewModel.observableBottomInsetsState.observe(viewLifecycleOwner, Observer {
            if (::containerVideo.isInitialized) {
                if (it.isAnyShown) containerVideo.setCornerRadius(cornerRadius)
                else containerVideo.setCornerRadius(0f)
            }

            launch {
                EventBusFactory.get(viewLifecycleOwner)
                        .emit(
                                ScreenStateEvent::class.java,
                                ScreenStateEvent.BottomInsetsChanged(it, it.isAnyShown, it.isAnyHidden, playViewModel.stateHelper)
                        )
            }
        })
    }

    private fun observeEventUserInfo() {
        playViewModel.observableEvent.observe(viewLifecycleOwner, Observer {
            launch {
                if (it.isBanned) sendEventBanned(it)
                else if(it.isFreeze) sendEventFreeze(it)
            }
        })
    }

    private fun observeVideoStream() {
        playViewModel.observableVideoStream.observe(viewLifecycleOwner, Observer(::setVideoStream))
    }
    //endregion

    //region Component Initialization
    private fun initComponents(container: ViewGroup) {
        layoutManager = PlayVideoLayoutManagerImpl(
                container = container,
                orientation = playViewModel.screenOrientation,
                videoOrientation = playViewModel.videoOrientation,
                viewInitializer = this
        )

        sendInitState()

        layoutManager.layoutView(container)
    }

    override fun onInitVideo(container: ViewGroup): Int {
        return VideoComponent(container, EventBusFactory.get(viewLifecycleOwner), this, dispatchers)
                .also(viewLifecycleOwner.lifecycle::addObserver)
                .getContainerId()
    }

    override fun onInitVideoLoading(container: ViewGroup): Int {
        return VideoLoadingComponent(container, EventBusFactory.get(viewLifecycleOwner), this, dispatchers)
                .getContainerId()
    }

    override fun onInitOneTap(container: ViewGroup): Int {
        return OneTapComponent(container, EventBusFactory.get(viewLifecycleOwner), this, dispatchers)
                .getContainerId()
    }

    override fun onInitOverlayVideo(container: ViewGroup): Int {
        return OverlayVideoComponent(container, EventBusFactory.get(viewLifecycleOwner), this, dispatchers)
                .getContainerId()
    }
    //endregion

    private fun sendInitState() {
        launch(dispatchers.immediate) {
            EventBusFactory.get(viewLifecycleOwner).emit(
                    ScreenStateEvent::class.java,
                    ScreenStateEvent.Init(playViewModel.screenOrientation, playViewModel.stateHelper)
            )
        }
    }

    private fun delegateVideoProperty(prop: VideoPropertyUiModel) {
        launch {
            EventBusFactory.get(viewLifecycleOwner)
                    .emit(
                            ScreenStateEvent::class.java,
                            ScreenStateEvent.VideoPropertyChanged(prop, playViewModel.stateHelper)
                    )
        }
    }

    private fun showOneTapOnboarding() {
        launch {
            EventBusFactory.get(viewLifecycleOwner)
                    .emit(
                            ScreenStateEvent::class.java,
                            ScreenStateEvent.ShowOneTapOnboarding
                    )
        }
    }

    private fun sendEventBanned(eventUiModel: EventUiModel) {
        launch {
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
        launch {
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
        launch {
            EventBusFactory.get(viewLifecycleOwner)
                    .emit(
                            ScreenStateEvent::class.java,
                            ScreenStateEvent.VideoStreamChanged(videoStream, playViewModel.stateHelper)
                    )
        }
    }
}