package com.tokopedia.logisticseller.ui.findingnewdriver.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.logisticseller.data.param.NewDriverParam
import com.tokopedia.logisticseller.domain.mapper.FindingNewDriverMapper
import com.tokopedia.logisticseller.domain.usecase.NewDriverAvailabilityUseCase
import com.tokopedia.logisticseller.domain.usecase.NewDriverBookingUseCase
import com.tokopedia.logisticseller.ui.findingnewdriver.uimodel.NewDriverAvailabilityState
import com.tokopedia.logisticseller.ui.findingnewdriver.uimodel.NewDriverBookingState
import kotlinx.coroutines.launch
import javax.inject.Inject

class FindingNewDriverViewModel @Inject constructor(
    dispatcher: CoroutineDispatchers,
    private val newDriverAvailabilityUseCase: NewDriverAvailabilityUseCase,
    private val newDriverBookingUseCase: NewDriverBookingUseCase,
    private val findingNewDriverMapper: FindingNewDriverMapper,
) : BaseViewModel(dispatcher.main) {

    private val _newDriverAvailability =
        MutableLiveData<NewDriverAvailabilityState>()
    val newDriverAvailability: LiveData<NewDriverAvailabilityState>
        get() = _newDriverAvailability

    private val _newDriverBooking = MutableLiveData<NewDriverBookingState>()
    val newDriverBooking: LiveData<NewDriverBookingState>
        get() = _newDriverBooking

    fun getNewDriverAvailability(orderId: String) {
        viewModelScope.launch {
            try {
                _newDriverAvailability.value =
                    NewDriverAvailabilityState.Loading
                val response = newDriverAvailabilityUseCase(
                    NewDriverParam(
                        orderId = orderId
                    )
                )

                response.data?.apply {
                    findingNewDriverMapper.map(this).apply {
                        _newDriverAvailability.value = NewDriverAvailabilityState.Success(this)
                    }
                } ?: run {
                    _newDriverAvailability.value = NewDriverAvailabilityState.Fail(null)
                }
            } catch (e: Exception) {
                _newDriverAvailability.value = NewDriverAvailabilityState.Fail(e.message)
            }
        }
    }

    fun getNewDriverBooking(orderId: String) {
        viewModelScope.launch {
            try {
                _newDriverBooking.value = NewDriverBookingState.Loading(isShowLoading = true)
                val response = newDriverBookingUseCase(
                    NewDriverParam(
                        orderId = orderId
                    )
                )

                _newDriverBooking.value = NewDriverBookingState.Loading(isShowLoading = false)

                response.data?.apply {
                    _newDriverBooking.value = NewDriverBookingState.Success(message)
                } ?: run {
                    _newDriverBooking.value = NewDriverBookingState.Fail(null)
                }
            } catch (e: Exception) {
                _newDriverBooking.value = NewDriverBookingState.Loading(isShowLoading = false)
                _newDriverBooking.value = NewDriverBookingState.Fail(e.message)
            }
        }
    }
}
