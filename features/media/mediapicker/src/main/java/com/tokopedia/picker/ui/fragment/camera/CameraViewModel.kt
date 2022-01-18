package com.tokopedia.picker.ui.fragment.camera

import androidx.lifecycle.*
import com.tokopedia.picker.data.entity.Media
import com.tokopedia.picker.utils.EventBusFactory
import com.tokopedia.picker.utils.EventState
import kotlinx.coroutines.launch

class CameraViewModel : ViewModel(), LifecycleObserver {

    private var _selectedMedia = MediatorLiveData<List<Media>>()
    val selectedMedia: LiveData<List<Media>> get() = _selectedMedia

    private var _mediaRemoved = MutableLiveData<Media?>()
    val mediaRemoved: LiveData<Media?> get() = _mediaRemoved

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onMediaChangedAndRemovedFromDrawer() {
        viewModelScope.launch {
            EventBusFactory.consumer {
                if (it is EventState.SelectionChanged) {
                    _selectedMedia.value = it.data
                }
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onMediaRemovedFromDrawer() {
        viewModelScope.launch {
            EventBusFactory.consumer {
                if (it is EventState.SelectionRemoved) {
                    _mediaRemoved.value = it.media
                    _selectedMedia.value = it.data
                }
            }
        }
    }

}