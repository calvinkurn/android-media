package com.tokopedia.picker.ui.activity.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PickerViewModel : ViewModel() {

    private var _finishButtonState = MutableLiveData<Boolean>()
    val finishButtonState: LiveData<Boolean> get() = _finishButtonState

    fun setFinishButtonState(value: Boolean) {
        _finishButtonState.value = value
    }

}