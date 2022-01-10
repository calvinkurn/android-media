package com.tokopedia.picker.ui.activity.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.picker.data.entity.Media

class PickerViewModel : ViewModel() {

    private var _finishButtonState = MediatorLiveData<Boolean>()
    val finishButtonState: LiveData<Boolean> get() = _finishButtonState

    private var _selectedMedia = MediatorLiveData<List<Media>>()
    val selectedMedia: LiveData<List<Media>> get() = _selectedMedia

    init {
        _finishButtonState.addSource(_selectedMedia) {
            setFinishButtonState(it.isNotEmpty())
        }
    }

    fun setSelectedMedia(value: List<Media>) {
        _selectedMedia.value = value
    }

    private fun setFinishButtonState(value: Boolean) {
        _finishButtonState.value = value
    }

}