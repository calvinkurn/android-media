package com.tokopedia.picker.ui.fragment.gallery

import androidx.lifecycle.*
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.picker.data.entity.Media
import com.tokopedia.picker.data.repository.MediaRepository
import com.tokopedia.picker.ui.PickerParam
import com.tokopedia.picker.utils.EventBusFactory
import com.tokopedia.picker.utils.EventState
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GalleryViewModel @Inject constructor(
    private val repository: MediaRepository,
    private val dispatchers: CoroutineDispatchers
) : ViewModel(), LifecycleObserver {

    private var _mediaFiles = MutableLiveData<List<Media>>()
    val mediaFiles: LiveData<List<Media>> get() = _mediaFiles

    private var _mediaRemoved = MutableLiveData<Media?>()
    val mediaRemoved: LiveData<Media?> get() = _mediaRemoved

    fun fetch(bucketId: Long, param: PickerParam) {
        viewModelScope.launch(dispatchers.io) {
            val result = repository(bucketId, param)

            withContext(dispatchers.main) {
                _mediaFiles.value = result
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onMediaRemovedFromDrawer() {
        viewModelScope.launch {
            EventBusFactory.consumer {
                if (it is EventState.SelectionRemoved) {
                    _mediaRemoved.value = it.media
                }
            }
        }
    }

    fun publishSelectionDataChanged(data: List<Media>) {
        EventBusFactory.send(EventState.SelectionChanged(data))
    }

}