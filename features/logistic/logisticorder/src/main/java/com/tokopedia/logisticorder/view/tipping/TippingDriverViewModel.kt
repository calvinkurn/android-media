package com.tokopedia.logisticorder.view.tipping

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.logisticorder.mapper.DriverTipMapper
import com.tokopedia.logisticorder.uimodel.LogisticDriverModel
import com.tokopedia.logisticorder.usecase.GetDriverTipUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import javax.inject.Inject

class TippingDriverViewModel @Inject constructor(
    private val getDriverTipUseCase: GetDriverTipUseCase,
    private val driverTipMapper: DriverTipMapper
) : ViewModel() {

    private val _driverTipsData = MutableLiveData<Result<LogisticDriverModel>>()
    val driverTipData: LiveData<Result<LogisticDriverModel>>
        get() = _driverTipsData

    fun getDriverTipsData(orderId: String?) {
        viewModelScope.launch {
            try {
                val driverTipData = getDriverTipUseCase(orderId)
                _driverTipsData.value = Success(driverTipMapper.mapDriverTipData(driverTipData))
            } catch (e: Throwable) {
                _driverTipsData.value = Fail(e)
            }
        }
    }
}
