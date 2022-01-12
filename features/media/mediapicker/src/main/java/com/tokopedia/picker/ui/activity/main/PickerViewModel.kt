package com.tokopedia.picker.ui.activity.main

import androidx.lifecycle.*
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.picker.data.entity.Media
import com.tokopedia.picker.utils.EventBusFactory
import com.tokopedia.picker.utils.EventState
import kotlinx.coroutines.launch
import javax.inject.Inject

class PickerViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers
) : ViewModel(), LifecycleObserver {

    private var _finishButtonState = MediatorLiveData<Boolean>()
    val finishButtonState: LiveData<Boolean> get() = _finishButtonState

    private var _selectedMedia = MediatorLiveData<List<Media>>()
    val selectedMedia: LiveData<List<Media>> get() = _selectedMedia

    init {
        _finishButtonState.addSource(_selectedMedia) {
            finishButtonState(it.isNotEmpty())
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun getSelectedMedia() {
        viewModelScope.launch {
            EventBusFactory.consumer(dispatchers.computation) {
                if (it is EventState.MediaSelection) {
                    _selectedMedia.value = it.data
                }
            }
        }
    }

    fun publishMediaSelectedChanged(data: List<Media>) {
        EventBusFactory.send(EventState.MediaSelection(data))
    }

    private fun finishButtonState(value: Boolean) {
        _finishButtonState.value = value
    }

}