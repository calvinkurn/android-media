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

    private val _currentSelectedPage = MutableLiveData<PageFragment>()
    private val _toolbarTitle = MutableLiveData<String>()

    /**
     * this live data used for switching menu/page
     * */
    val currentSelectedPage: LiveData<PageFragment>
        get() = _currentSelectedPage

    val toolbarTitle: LiveData<String>
        get() = _toolbarTitle

    fun setCurrentSelectedPage(page: PageFragment) {
        _currentSelectedPage.value = page
    }

    fun setToolbarTitle(title: String) {
        _toolbarTitle.value = title
    }
}