package com.tokopedia.mvc.presentation.bottomsheet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import javax.inject.Inject

class VoucherEditPeriodViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main){

    companion object {

    }

    private val _startDateLiveData = MutableLiveData<String>()
    val startDateLiveData: LiveData<String>
        get() = _startDateLiveData

    private val _endDateLiveData = MutableLiveData<String>()
    val endDateLiveData: LiveData<String>
        get() = _endDateLiveData

    fun setStartDate(startDate: String){
        _startDateLiveData.value = startDate
    }

    fun setEndDate(endDate: String){
        _endDateLiveData.value = endDate
    }


}
