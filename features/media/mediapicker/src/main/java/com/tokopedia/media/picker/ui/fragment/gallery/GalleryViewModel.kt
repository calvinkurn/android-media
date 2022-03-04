package com.tokopedia.media.picker.ui.fragment.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.media.common.PickerParam
import com.tokopedia.media.common.uimodel.MediaUiModel
import com.tokopedia.media.picker.data.mapper.toUiModel
import com.tokopedia.media.picker.data.repository.MediaRepository
import com.tokopedia.media.common.observer.EventFlowFactory
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GalleryViewModel @Inject constructor(
    private val repository: MediaRepository,
    private val dispatchers: CoroutineDispatchers
) : ViewModel() {

    private var _medias = MediatorLiveData<List<MediaUiModel>>()
    val medias: LiveData<List<MediaUiModel>> get() = _medias

    val uiEvent = EventFlowFactory
        .subscriber(viewModelScope)
        .flowOn(dispatchers.computation)

    fun fetch(bucketId: Long, param: PickerParam) {
        viewModelScope.launch(dispatchers.io) {
            val result = repository(bucketId, param)

            withContext(dispatchers.main) {
                _medias.value = result.toUiModel()
            }
        }
    }

}