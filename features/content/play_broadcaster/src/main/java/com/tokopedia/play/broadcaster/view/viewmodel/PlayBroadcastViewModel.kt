package com.tokopedia.play.broadcaster.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.play.broadcaster.dispatcher.PlayBroadcastDispatcher
import com.tokopedia.play.broadcaster.mocker.PlayBroadcastMocker
import com.tokopedia.play.broadcaster.pusher.PlayPusher
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherNetworkState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import javax.inject.Named


/**
 * Created by mzennis on 24/05/20.
 */
class PlayBroadcastViewModel  @Inject constructor(
        private val playPusher: PlayPusher,
        @Named(PlayBroadcastDispatcher.MAIN) dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val job: Job = SupervisorJob()
    private val scope = CoroutineScope(job + dispatcher)

    val observablePage: LiveData<Boolean>
        get() = _observablePage
    private val _observablePage = MutableLiveData<Boolean>()

    init {
        playPusher.create()
    }

    fun getConfiguration() {
        val configuration = PlayBroadcastMocker.getMockConfiguration()
        if (configuration.isHaveOnGoingLive) {
            getChannel(configuration.channelId)
        } else {
            // TODO("temporary")
            _observablePage.value = true
        }
    }

    fun getChannel(channelId: String) {

    }

    fun getPlayPusher(): PlayPusher {
        return playPusher
    }
}