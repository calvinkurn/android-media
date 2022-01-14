package com.tokopedia.picker.ui.activity.main

import androidx.lifecycle.*
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.picker.data.entity.Media
import com.tokopedia.picker.utils.EventBusFactory
import com.tokopedia.picker.utils.EventState
import kotlinx.coroutines.launch
import javax.inject.Inject

class PickerViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io), LifecycleObserver {

    private var _continueActionState = MediatorLiveData<Boolean>()
    val continueActionState: LiveData<Boolean> get() = _continueActionState

    private var _selectedMedia = MediatorLiveData<List<Media>>()
    val selectedMedia: LiveData<List<Media>> get() = _selectedMedia

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun continueActionState() {
        _continueActionState.addSource(_selectedMedia) {
            _continueActionState.value = it.isNotEmpty()
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

}