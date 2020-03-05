package com.tokopedia.sellerhome.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 2020-03-05
 */

class SharedViewModel @Inject constructor() : ViewModel() {

    private val _currentSelectedMenu = MutableLiveData<Int>()
    private val _toolbarTitle = MutableLiveData<String>()

    /**
     * this live data used for switching menu/page
     * */
    val currentSelectedMenu: LiveData<Int>
        get() = _currentSelectedMenu

    val toolbarTitle: LiveData<String>
        get() = _toolbarTitle

    fun setCurrentSelectedMenu(position: Int) {
        _currentSelectedMenu.value = position
    }

    fun setToolbarTitle(title: String) {
        _toolbarTitle.value = title
    }
}