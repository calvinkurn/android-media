package com.tokopedia.play.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
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
import com.tokopedia.play.util.CoroutineDispatcherProvider
import com.tokopedia.play.util.event.EventObserver
import com.tokopedia.play.view.custom.RoundedConstraintLayout
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.type.PlayRoomEvent
import com.tokopedia.play.view.uimodel.EventUiModel
import com.tokopedia.play.view.uimodel.VideoPropertyUiModel
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
class PlayVideoFragment : BaseDaggerFragment(), CoroutineScope {

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
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
    //endregion

    //region Component Initialization
    private fun initComponents(container: ViewGroup) {
        val videoComponent = initVideoComponent(container)
        val videoLoadingComponent = initVideoLoadingComponent(container)
        val oneTapComponent = initOneTapComponent(container)
        val overlayVideoComponent = initOverlayVideoComponent(container)

        sendInitState()

        layoutView(
                container = container,
                videoComponentId = videoComponent.getContainerId(),
                videoLoadingComponentId = videoLoadingComponent.getContainerId(),
                oneTapComponentId = oneTapComponent.getContainerId(),
                overlayVideoComponentId = overlayVideoComponent.getContainerId()
        )
    }

    private fun initVideoComponent(container: ViewGroup): UIComponent<Unit> {
        return VideoComponent(container, EventBusFactory.get(viewLifecycleOwner), this, dispatchers)
                .also(viewLifecycleOwner.lifecycle::addObserver)
    }

    private fun initVideoLoadingComponent(container: ViewGroup): UIComponent<Unit> {
        return VideoLoadingComponent(container, EventBusFactory.get(viewLifecycleOwner), this, dispatchers)
    }

    private fun initOneTapComponent(container: ViewGroup): UIComponent<Unit> {
        return OneTapComponent(container, EventBusFactory.get(viewLifecycleOwner), this, dispatchers)
    }

    private fun initOverlayVideoComponent(container: ViewGroup): UIComponent<Unit> {
        return OverlayVideoComponent(container, EventBusFactory.get(viewLifecycleOwner), this, dispatchers)
    }
    //endregion

    private fun sendInitState() {
        launch(dispatchers.immediate) {
            EventBusFactory.get(viewLifecycleOwner).emit(
                    ScreenStateEvent::class.java,
                    ScreenStateEvent.Init
            )
        }
    }

    //region layouting
    private fun layoutView(
            container: ViewGroup,
            @IdRes videoComponentId: Int,
            @IdRes videoLoadingComponentId: Int,
            @IdRes oneTapComponentId: Int,
            @IdRes overlayVideoComponentId: Int
    ) {

        fun layoutVideo(container: ViewGroup, @IdRes id: Int) {
            val constraintSet = ConstraintSet()

            constraintSet.clone(container as ConstraintLayout)

            constraintSet.apply {
                connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
                connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            }

            constraintSet.applyTo(container)
        }

        fun layoutVideoLoading(container: ViewGroup, @IdRes id: Int) {
            val constraintSet = ConstraintSet()

            constraintSet.clone(container as ConstraintLayout)

            constraintSet.apply {
                connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
                connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            }

            constraintSet.applyTo(container)
        }

        fun layoutOneTap(container: ViewGroup, @IdRes id: Int) {
            val constraintSet = ConstraintSet()

            constraintSet.clone(container as ConstraintLayout)

            constraintSet.apply {
                connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                connect(id, ConstraintSet.TOP, R.id.gl_one_tap_post, ConstraintSet.BOTTOM)
            }

            constraintSet.applyTo(container)
        }

        fun layoutOverlayVideo(container: ViewGroup, @IdRes id: Int) {
            val constraintSet = ConstraintSet()

            constraintSet.clone(container as ConstraintLayout)

            constraintSet.apply {
                connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
                connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            }

            constraintSet.applyTo(container)
        }

        layoutVideo(container, videoComponentId)
        layoutVideoLoading(container, videoLoadingComponentId)
        layoutOneTap(container , oneTapComponentId)
        layoutOverlayVideo(container, overlayVideoComponentId)
    }
    //endregion

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
}