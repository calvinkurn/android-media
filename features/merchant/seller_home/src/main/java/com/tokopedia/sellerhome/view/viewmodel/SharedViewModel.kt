package com.tokopedia.sellerhome.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 2020-03-05
 */

class SharedViewModel @Inject constructor() : ViewModel() {

    private val _currentSelectedMenuIndex = MutableLiveData<Int>()

    /**
     * this live data used for switching
     * */
    val currentSelectedMenuIndex: LiveData<Int>
        get() = _currentSelectedMenuIndex

    fun setCurrentSelectedMenu(position: Int) {
        _currentSelectedMenuIndex.value = position
    }
}