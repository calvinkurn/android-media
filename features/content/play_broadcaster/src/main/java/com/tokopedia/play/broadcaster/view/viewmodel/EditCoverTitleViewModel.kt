package com.tokopedia.play.broadcaster.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.ui.model.result.NetworkResult
import com.tokopedia.play.broadcaster.util.coroutine.CoroutineDispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by jegul on 01/07/20
 */
class EditCoverTitleViewModel @Inject constructor(
        private val dispatcher: CoroutineDispatcherProvider,
        private val setupDataStore: PlayBroadcastSetupDataStore
) : ViewModel() {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(dispatcher.main + job)

    val observableUpdateTitle: LiveData<NetworkResult<Unit>>
        get() = _observableUpdateTitle
    private val _observableUpdateTitle = MutableLiveData<NetworkResult<Unit>>()

    fun editTitle(title: String, channelId: String) {
        setupDataStore.updateCoverTitle(title)

        scope.launch(dispatcher.io) {
            setupDataStore.uploadCoverTitle(channelId)
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}