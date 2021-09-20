package com.tokopedia.common.topupbills.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import javax.inject.Inject

class TopupBillsSavedNumberViewModel @Inject constructor(
    val dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.io) {

    private val _searchKeyword = MutableLiveData<String>()
    val searchKeyword: LiveData<String>
        get() = _searchKeyword

    private val _clueVisibility = MutableLiveData<Boolean>()
    val clueVisibility: LiveData<Boolean>
        get() = _clueVisibility

    fun setSearchKeyword(keyword: String) {
        _searchKeyword.postValue(keyword)
    }

    fun setClueVisibility(isVisible: Boolean) {
        _clueVisibility.postValue(isVisible)
    }
}