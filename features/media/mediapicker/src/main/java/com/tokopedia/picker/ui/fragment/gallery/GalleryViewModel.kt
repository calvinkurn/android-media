package com.tokopedia.picker.ui.fragment.gallery

import androidx.lifecycle.*
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.picker.data.mapper.toUiModel
import com.tokopedia.picker.data.repository.MediaRepository
import com.tokopedia.picker.ui.PickerParam
import com.tokopedia.picker.ui.uimodel.MediaUiModel
import com.tokopedia.picker.utils.EventBusFactory
import com.tokopedia.picker.utils.EventState
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GalleryViewModel @Inject constructor(
    private val repository: MediaRepository,
    private val dispatchers: CoroutineDispatchers
) : ViewModel(), LifecycleObserver {

    private var _mediaFiles = MediatorLiveData<List<MediaUiModel>>()
    val mediaFiles: LiveData<List<MediaUiModel>> get() = _mediaFiles

    val uiEvent = EventBusFactory
        .subscriber(viewModelScope)
        .flowOn(dispatchers.computation)

    fun fetch(bucketId: Long, param: PickerParam) {
        viewModelScope.launch(dispatchers.io) {
            val result = repository(bucketId, param)

            withContext(dispatchers.main) {
                _mediaFiles.value = result.toUiModel()
            }
        }
    }

    fun send(eventState: EventState) {
        EventBusFactory.emit(eventState)
    }

}