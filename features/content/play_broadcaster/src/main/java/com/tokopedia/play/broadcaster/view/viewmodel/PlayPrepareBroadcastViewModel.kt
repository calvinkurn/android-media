package com.tokopedia.play.broadcaster.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.play.broadcaster.dispatcher.PlayBroadcastDispatcher
import com.tokopedia.play.broadcaster.mocker.PlayBroadcastMocker
import com.tokopedia.play.broadcaster.view.uimodel.FollowerUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by jegul on 20/05/20
 */
class PlayPrepareBroadcastViewModel @Inject constructor(
        @Named(PlayBroadcastDispatcher.MAIN) dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val job: Job = SupervisorJob()
    private val scope = CoroutineScope(job + dispatcher)

    val observableFollowers: LiveData<List<FollowerUiModel>>
        get() = _observableFollowers
    private val _observableFollowers = MutableLiveData<List<FollowerUiModel>>()

    init {
        _observableFollowers.value = PlayBroadcastMocker.getMockUnknownFollower()
    }
}