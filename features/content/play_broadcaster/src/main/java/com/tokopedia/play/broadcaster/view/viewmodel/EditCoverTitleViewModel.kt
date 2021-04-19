package com.tokopedia.play.broadcaster.view.viewmodel

import androidx.lifecycle.*
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.ui.model.title.PlayTitleUiModel
import com.tokopedia.play.broadcaster.ui.validator.title.TitleSetupValidator
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.model.result.map
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by jegul on 01/07/20
 */
class EditCoverTitleViewModel @Inject constructor(
        private val hydraConfigStore: HydraConfigStore,
        private val dispatcher: CoroutineDispatchers,
        private val setupDataStore: PlayBroadcastSetupDataStore,
) : ViewModel(), TitleSetupValidator {

    private val channelId: String
        get() = hydraConfigStore.getChannelId()

    val maxTitleChars: Int
        get() = hydraConfigStore.getMaxTitleChars()

    private val job = SupervisorJob()
    private val scope = CoroutineScope(dispatcher.main + job)

    val observableTitle: LiveData<PlayTitleUiModel.HasTitle>
        get() = setupDataStore.getObservableTitle()
                .filterIsInstance<PlayTitleUiModel.HasTitle>()
                .asLiveData(viewModelScope.coroutineContext + dispatcher.computation)

    val observableUpdateTitle: LiveData<NetworkResult<Unit>>
        get() = _observableUpdateTitle
    private val _observableUpdateTitle = MutableLiveData<NetworkResult<Unit>>()

    override fun isTitleValid(title: String): Boolean {
        return title.isNotBlank() && title.length <= maxTitleChars
    }

    fun editTitle(title: String) {
        setupDataStore.setTitle(title)

        scope.launch {
            _observableUpdateTitle.value = NetworkResult.Loading
            val value = setupDataStore.uploadTitle(channelId).map { Unit }
            _observableUpdateTitle.value = value
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}