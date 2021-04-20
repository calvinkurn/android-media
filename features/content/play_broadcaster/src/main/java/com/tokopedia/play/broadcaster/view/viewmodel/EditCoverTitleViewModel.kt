package com.tokopedia.play.broadcaster.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.model.result.map
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by jegul on 01/07/20
 */
class EditCoverTitleViewModel @Inject constructor(
        private val hydraConfigStore: HydraConfigStore,
        private val dispatcher: CoroutineDispatchers,
        private val setupDataStore: PlayBroadcastSetupDataStore
) : ViewModel() {

    private val channelId: String
        get() = hydraConfigStore.getChannelId()

    val maxTitleChars: Int
        get() = hydraConfigStore.getMaxTitleChars()

    private val job = SupervisorJob()
    private val scope = CoroutineScope(dispatcher.main + job)

    val observableCurrentTitle: LiveData<String> = Transformations.map(setupDataStore.getObservableSelectedCover()) {
        it.title
    }

    val observableUpdateTitle: LiveData<NetworkResult<Unit>>
        get() = _observableUpdateTitle
    private val _observableUpdateTitle = MutableLiveData<NetworkResult<Unit>>()

    fun isValidCoverTitle(coverTitle: String): Boolean {
        return coverTitle.isNotBlank() && coverTitle.length <= maxTitleChars
    }

    fun editTitle(title: String) {
        setupDataStore.updateCoverTitle(title)

        scope.launch {
            _observableUpdateTitle.value = NetworkResult.Loading
            val value = setupDataStore.uploadCoverTitle(channelId).map { Unit }
            _observableUpdateTitle.value = value
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}