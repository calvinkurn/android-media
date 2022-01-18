package com.tokopedia.picker.ui.activity.main

import androidx.lifecycle.*
import com.tokopedia.picker.data.entity.Media
import com.tokopedia.picker.utils.EventBusFactory
import com.tokopedia.picker.utils.EventState
import kotlinx.coroutines.launch

class PickerViewModel : ViewModel(), LifecycleObserver {

    private var _finishButtonState = MediatorLiveData<Boolean>()
    val finishButtonState: LiveData<Boolean> get() = _finishButtonState

    private var _selectedMedia = MediatorLiveData<List<Media>>()
    val selectedMedia: LiveData<List<Media>> get() = _selectedMedia

    private var _cameraCaptured = MediatorLiveData<Media>()
    val cameraCaptured: LiveData<Media> get() = _cameraCaptured

    init {
        _finishButtonState.addSource(_selectedMedia) {
            _finishButtonState.value = it.isNotEmpty()
        }

        _finishButtonState.addSource(_cameraCaptured) {
            _finishButtonState.value = it != null
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onMediaChangedFromDrawer() {
        viewModelScope.launch {
            EventBusFactory.consumer {
                if (it is EventState.SelectionChanged) {
                    _selectedMedia.value = it.data
                }
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onMediaAddedFromCamera() {
        viewModelScope.launch {
            EventBusFactory.consumer {
                if (it is EventState.CameraCaptured) {
                    _cameraCaptured.value = it.data
                }
            }
        }
    }

    fun publishSelectionRemovedChanged(data: Media, newData: List<Media>) {
        EventBusFactory.send(EventState.SelectionRemoved(data, newData))
    }

    fun publishSelectionDataChanged(data: List<Media>) {
        EventBusFactory.send(EventState.SelectionChanged(data))
    }

}