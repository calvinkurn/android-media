package com.tokopedia.play.view.fragment

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
import com.tokopedia.play.ui.loading.LoadingComponent
import com.tokopedia.play.ui.video.VideoComponent
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

        fun newInstance(): PlayVideoFragment {
            return PlayVideoFragment()
        }
    }

    private val job: Job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var playViewModel: PlayViewModel
    private lateinit var viewModel: PlayVideoViewModel

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        playViewModel = ViewModelProvider(parentFragment!!, viewModelFactory).get(PlayViewModel::class.java)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PlayVideoViewModel::class.java)
        return inflater.inflate(R.layout.fragment_play_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initComponents(view as ViewGroup)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        playViewModel.observableVOD.observe(this, Observer {
            launch {
                EventBusFactory.get(viewLifecycleOwner)
                        .emit(
                                ScreenStateEvent::class.java,
                                ScreenStateEvent.SetVideo(it)
                        )
            }
        })
        playViewModel.observableVideoProperty.observe(this, Observer {
            if (it.state is TokopediaPlayVideoState.Error)
                view?.let { fragmentView ->
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

    private fun initComponents(container: ViewGroup) {
        VideoComponent(container, EventBusFactory.get(viewLifecycleOwner), this)
        LoadingComponent(container, EventBusFactory.get(viewLifecycleOwner), this)
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
}