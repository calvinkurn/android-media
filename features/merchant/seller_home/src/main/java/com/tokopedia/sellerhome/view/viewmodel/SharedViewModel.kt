package com.tokopedia.sellerhome.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.sellerhome.common.PageFragment
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 2020-03-05
 */

class SharedViewModel @Inject constructor() : ViewModel() {

    /**
     * this live data used for switching menu/page on it's value changed
     * */
    private val _currentSelectedPage = MutableLiveData<PageFragment>()
    val currentSelectedPage: LiveData<PageFragment>
        get() = _currentSelectedPage

    fun setCurrentSelectedPage(page: PageFragment) {
        _currentSelectedPage.value = page
    }
}