package com.tokopedia.play.view.fragment

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.ui.PlayerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.play.R
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.ui.video.VideoComponent
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play_common.service.TokopediaPlayService
import kotlinx.coroutines.*
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

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {

        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            if (service != null && service is TokopediaPlayService.LocalBinder) {
                val serviceInstance = service.service
                serviceInstance.playVideoWithString("http://www.exit109.com/~dnn/clips/RW20seconds_2.mp4")
                launch {
                    EventBusFactory.get(viewLifecycleOwner)
                            .emit(
                                    ScreenStateEvent::class.java,
                                    ScreenStateEvent.Play(serviceInstance.videoPlayer)
                            )
                }
            }
        }
    }

    override fun getScreenName(): String = "Play Video"

    override fun initInjector() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initComponents(view as ViewGroup)
        initVideo()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        requireContext().unbindService(serviceConnection)
    }

    private fun initComponents(container: ViewGroup) {
        VideoComponent(container, EventBusFactory.get(viewLifecycleOwner), this)
    }

    private fun initVideo() {
        val playServiceIntent = Intent(context, TokopediaPlayService::class.java)
//        requireContext().startService(playServiceIntent)
        requireContext().bindService(
                playServiceIntent,
                serviceConnection,
                Service.BIND_AUTO_CREATE
        )
    }
}