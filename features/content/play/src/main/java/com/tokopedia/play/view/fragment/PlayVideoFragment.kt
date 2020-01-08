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
import com.tokopedia.play.analytic.PlayAnalytics
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.di.DaggerPlayComponent
import com.tokopedia.play.ui.loading.VideoLoadingComponent
import com.tokopedia.play.ui.onetap.OneTapComponent
import com.tokopedia.play.ui.video.VideoComponent
import com.tokopedia.play.util.event.EventObserver
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.uimodel.VideoPropertyUiModel
import com.tokopedia.play.view.viewmodel.PlayVideoViewModel
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play_common.state.TokopediaPlayVideoState
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.*
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
        get() = job + Dispatchers.Main

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var playViewModel: PlayViewModel
    private lateinit var viewModel: PlayVideoViewModel

    private var channelId: String = ""

    override fun getScreenName(): String = "Play Video"

    override fun initInjector() {
        DaggerPlayComponent
                .builder()
                .baseAppComponent(
                        (requireContext().applicationContext as BaseMainApplication).baseAppComponent
                )
                .build()
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playViewModel = ViewModelProvider(parentFragment!!, viewModelFactory).get(PlayViewModel::class.java)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PlayVideoViewModel::class.java)
        channelId  = arguments?.getString(PLAY_KEY_CHANNEL_ID).orEmpty()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_play_video, container, false)
        initComponents(view as ViewGroup)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeVOD()
        observeVideoProperty()
        observeOneTapOnboarding()
        observeKeyboardState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job.cancel()
    }

    //region observe
    private fun observeVOD() {
        playViewModel.observableVOD.observe(this, Observer {
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
        playViewModel.observableVideoProperty.observe(this, Observer {
            if (it.state is TokopediaPlayVideoState.Error)
                view?.let { fragmentView ->
                    PlayAnalytics.errorState(channelId, it.state.error.localizedMessage, playViewModel.isLive)
                    Toaster.make(
                            fragmentView,
                            it.state.error.localizedMessage,
                            type = Toaster.TYPE_ERROR,
                            actionText = getString(R.string.play_try_again),
                            clickListener = View.OnClickListener {
                                //TODO("Maybe retry video")
                            }
                    )
                }
            else delegateVideoProperty(it)
        })
    }

    private fun observeOneTapOnboarding() {
        viewModel.observableOneTapOnboarding.observe(this, EventObserver { showOneTapOnboarding() })
    }

    private fun observeKeyboardState() {
        playViewModel.observableKeyboardState.observe(this, Observer {
            launch {
                EventBusFactory.get(viewLifecycleOwner)
                        .emit(ScreenStateEvent::class.java, ScreenStateEvent.KeyboardStateChanged(it.isShown))
            }
        })
    }
    //endregion

    //region Component Initialization
    private fun initComponents(container: ViewGroup) {
        val videoComponent = initVideoComponent(container)
        val videoLoadingComponent = initVideoLoadingComponent(container)
        val oneTapComponent = initOneTapComponent(container)

        layoutView(
                container = container,
                videoComponentId = videoComponent.getContainerId(),
                videoLoadingComponentId = videoLoadingComponent.getContainerId(),
                oneTapComponentId = oneTapComponent.getContainerId()
        )
    }

    private fun initVideoComponent(container: ViewGroup): UIComponent<Unit> {
        return VideoComponent(container, EventBusFactory.get(viewLifecycleOwner), this)
                .also(viewLifecycleOwner.lifecycle::addObserver)
    }

    private fun initVideoLoadingComponent(container: ViewGroup): UIComponent<Unit> {
        return VideoLoadingComponent(container, EventBusFactory.get(viewLifecycleOwner), this)
    }

    private fun initOneTapComponent(container: ViewGroup): UIComponent<Unit> {
        return OneTapComponent(container, EventBusFactory.get(viewLifecycleOwner), this)
    }
    //endregion

    //region layouting
    private fun layoutView(
            container: ViewGroup,
            @IdRes videoComponentId: Int,
            @IdRes videoLoadingComponentId: Int,
            @IdRes oneTapComponentId: Int
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
                connect(id, ConstraintSet.TOP, R.id.gl_quarter, ConstraintSet.BOTTOM)
            }

            constraintSet.applyTo(container)
        }

        layoutVideo(container, videoComponentId)
        layoutVideoLoading(container, videoLoadingComponentId)
        layoutOneTap(container , oneTapComponentId)
    }

    private fun delegateVideoProperty(prop: VideoPropertyUiModel) {
        launch {
            EventBusFactory.get(viewLifecycleOwner)
                    .emit(
                            ScreenStateEvent::class.java,
                            ScreenStateEvent.VideoPropertyChanged(prop)
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
}