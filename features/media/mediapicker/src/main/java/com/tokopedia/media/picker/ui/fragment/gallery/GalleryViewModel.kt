package com.tokopedia.media.picker.ui.fragment.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.media.picker.data.mapper.toUiModel
import com.tokopedia.media.picker.data.repository.MediaRepository
import com.tokopedia.picker.common.observer.EventFlowFactory
import com.tokopedia.picker.common.uimodel.MediaUiModel
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

    fun fetch(bucketId: Long) {
        viewModelScope.launch {
            val result = repository(bucketId)

            withContext(dispatchers.main) {
                _medias.value = result.toUiModel()
            }
        }
    }

}