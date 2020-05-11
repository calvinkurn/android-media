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
import com.tokopedia.play.R
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.di.DaggerPlayComponent
import com.tokopedia.play.di.PlayModule
import com.tokopedia.play.extensions.isAnyHidden
import com.tokopedia.play.extensions.isAnyShown
import com.tokopedia.play.ui.youtube.YouTubeComponent
import com.tokopedia.play.ui.youtube.interaction.YouTubeInteractionEvent
import com.tokopedia.play.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play.util.observer.DistinctObserver
import com.tokopedia.play.view.contract.PlayFragmentContract
import com.tokopedia.play.view.contract.PlayOrientationListener
import com.tokopedia.play.view.custom.RoundedConstraintLayout
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.layout.youtube.PlayYouTubeLayoutManager
import com.tokopedia.play.view.layout.youtube.PlayYouTubeLayoutManagerImpl
import com.tokopedia.play.view.layout.youtube.PlayYouTubeViewInitializer
import com.tokopedia.play.view.type.ScreenOrientation
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.unifycomponents.dpToPx
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by jegul on 28/04/20
 */
class PlayYouTubeFragment : BaseDaggerFragment(), PlayYouTubeViewInitializer, PlayFragmentContract {

    companion object {

        fun newInstance(): PlayYouTubeFragment {
            return PlayYouTubeFragment()
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

    private lateinit var playViewModel: PlayViewModel

    private lateinit var layoutManager: PlayYouTubeLayoutManager

    private lateinit var containerYouTube: RoundedConstraintLayout

    private val orientationListener: PlayOrientationListener
        get() = requireParentFragment() as PlayOrientationListener

    private val orientation: ScreenOrientation
        get() = ScreenOrientation.getByInt(resources.configuration.orientation)

    override fun getScreenName(): String = "Play YouTube"

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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_play_youtube, container, false)
        containerYouTube = view.findViewById(R.id.container_youtube)

        initComponents(view as ViewGroup)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeVideoPlayer()
        observeBottomInsetsState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        layoutManager.onDestroy()
        job.cancel()
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
        sendOrientationChangedEvent(orientation)
    }

    //region observe
    private fun observeVideoPlayer() {
        playViewModel.observableVideoPlayer.observe(viewLifecycleOwner, DistinctObserver {
            scope.launch {
                EventBusFactory.get(viewLifecycleOwner)
                        .emit(
                                ScreenStateEvent::class.java,
                                ScreenStateEvent.SetVideo(it)
                        )
            }
        })
    }

    private fun observeBottomInsetsState() {
        playViewModel.observableBottomInsetsState.observe(viewLifecycleOwner, Observer {
            if (::containerYouTube.isInitialized) {
                if (it.isAnyShown) containerYouTube.setCornerRadius(cornerRadius)
                else containerYouTube.setCornerRadius(0f)
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
    //endregion

    //region Component Initialization
    private fun initComponents(container: ViewGroup) {
        layoutManager = PlayYouTubeLayoutManagerImpl(
                container = container,
                viewInitializer = this
        )

        sendInitState()

        layoutManager.layoutView(container)
    }

    override fun onInitYouTube(container: ViewGroup): Int {
        val youTubeComponent = YouTubeComponent(container, childFragmentManager, EventBusFactory.get(viewLifecycleOwner), scope, dispatchers)
                .also(viewLifecycleOwner.lifecycle::addObserver)

        scope.launch {
            youTubeComponent.getUserInteractionEvents()
                    .collect {
                        when (it) {
                            YouTubeInteractionEvent.EnterFullscreenClicked -> enterFullscreen()
                            YouTubeInteractionEvent.ExitFullscreenClicked -> exitFullscreen()
                        }
                    }
        }

        return youTubeComponent.getContainerId()
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

    private fun enterFullscreen() {
        if (!orientation.isLandscape)
            orientationListener.onOrientationChanged(ScreenOrientation.Landscape, isTilting = false)
    }

    private fun exitFullscreen() {
        if (!orientation.isPortrait)
            orientationListener.onOrientationChanged(ScreenOrientation.Portrait, isTilting = false)
    }

    private fun sendOrientationChangedEvent(orientation: ScreenOrientation) {
        scope.launch {
            EventBusFactory.get(viewLifecycleOwner)
                    .emit(ScreenStateEvent::class.java, ScreenStateEvent.OrientationChanged(orientation, playViewModel.getStateHelper(orientation)))
        }
    }
}