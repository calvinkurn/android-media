package com.tokopedia.play.broadcaster.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.play.broadcaster.dispatcher.PlayBroadcastDispatcher
import com.tokopedia.play.broadcaster.mocker.PlayBroadcastMocker
import com.tokopedia.play.broadcaster.view.uimodel.PlayEtalaseUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by jegul on 26/05/20
 */
class PlayEtalasePickerViewModel @Inject constructor(
        @Named(PlayBroadcastDispatcher.MAIN) dispatcher: CoroutineDispatcher
): ViewModel() {

    private val job: Job = SupervisorJob()
    private val scope = CoroutineScope(job + dispatcher)

    val observableEtalase: LiveData<List<PlayEtalaseUiModel>>
        get() = _observableEtalase
    private val _observableEtalase = MutableLiveData<List<PlayEtalaseUiModel>>()

    init {
        _observableEtalase.value = PlayBroadcastMocker.getMockEtalaseList()
    }
}