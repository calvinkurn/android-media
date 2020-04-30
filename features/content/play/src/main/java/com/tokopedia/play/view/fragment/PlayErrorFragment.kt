package com.tokopedia.play.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.ERR_STATE_GLOBAL
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.R
import com.tokopedia.play.analytic.PlayAnalytics
import com.tokopedia.play.di.DaggerPlayComponent
import com.tokopedia.play.di.PlayModule
import com.tokopedia.play.util.CoroutineDispatcherProvider
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play.view.wrapper.GlobalErrorCodeWrapper
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


/**
 * Created by mzennis on 2020-01-10.
 */
class PlayErrorFragment: BaseDaggerFragment(), CoroutineScope {

    companion object {
        fun newInstance(channelId: String?): PlayErrorFragment {
            return PlayErrorFragment().apply {
                val args = Bundle()
                args.putString(PLAY_KEY_CHANNEL_ID, channelId)
                arguments = args
            }
        }
    }

    private val job: Job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = job + dispatchers.main

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var dispatchers: CoroutineDispatcherProvider

    private lateinit var playViewModel: PlayViewModel
    private lateinit var container: View
    private lateinit var globalError: GlobalError

    private var channelId: String = ""

    override fun getScreenName() = "Play Video"

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
        channelId  = arguments?.getString(PLAY_KEY_CHANNEL_ID).orEmpty()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_play_error, container, false)
        initComponent(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeErrorChannel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job.cancel()
    }

    private fun initComponent(view: View) {
        container = view.findViewById(R.id.container_global_error)
        globalError = view.findViewById(R.id.global_error)
        context?.let {
            globalError.errorTitle.setTextColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Neutral_N0))
            globalError.errorDescription.setTextColor(ContextCompat.getColor(it, R.color.play_error_text_color))
        }

        val imgBack = view.findViewById<AppCompatImageView>(R.id.img_back)
        imgBack.setOnClickListener { activity?.onBackPressed() }
    }

    private fun observeErrorChannel() {
        playViewModel.observableGetChannelInfo.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Fail -> {
                    showGlobalError(it.throwable)
                }
                is Success -> {
                    container.gone()
                }
            }
        })
    }

    private fun showGlobalError(throwable: Throwable) {
        throwable.message?.let {
            when(GlobalErrorCodeWrapper.wrap(it)) {
                is GlobalErrorCodeWrapper.NotFound -> {
                    globalError.setType(GlobalError.PAGE_NOT_FOUND)
                    globalError.setActionClickListener {
                        activity?.let { activity ->
                            RouteManager.route(activity, ApplinkConst.HOME)
                        }
                    }
                }
                is GlobalErrorCodeWrapper.PageFull -> {
                    globalError.setType(GlobalError.PAGE_FULL)
                    globalError.setActionClickListener {
                        playViewModel.getChannelInfo(channelId)
                    }
                }
                is GlobalErrorCodeWrapper.ServerError,
                is GlobalErrorCodeWrapper.Unknown -> {
                    globalError.setType(GlobalError.SERVER_ERROR)
                    globalError.setActionClickListener {
                        playViewModel.getChannelInfo(channelId)
                    }
                }
            }
            PlayAnalytics.errorState(channelId, "$ERR_STATE_GLOBAL: ${globalError.errorDescription.text}", playViewModel.channelType)
            container.visible()
        }
    }
}