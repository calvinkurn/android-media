package com.tokopedia.logisticorder.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.logisticorder.mapper.TrackingPageMapperNew
import com.tokopedia.logisticorder.uimodel.TrackingDataModel
import com.tokopedia.logisticorder.usecase.GetTrackingUseCase
import com.tokopedia.logisticorder.usecase.SetRetryAvailabilityUseCase
import com.tokopedia.logisticorder.usecase.SetRetryBookingUseCase
import com.tokopedia.logisticorder.usecase.entity.RetryAvailabilityResponse
import com.tokopedia.logisticorder.usecase.entity.RetryBookingResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import javax.inject.Inject

class TrackingPageViewModel @Inject constructor(
    private val trackingUseCase: GetTrackingUseCase,
    private val setRetryBookingUseCase: SetRetryBookingUseCase,
    private val setRetryAvailabilityUseCase: SetRetryAvailabilityUseCase,
    private val mapper: TrackingPageMapperNew
) : ViewModel() {

    private val _trackingData = MutableLiveData<Result<TrackingDataModel>>()
    val trackingData: LiveData<Result<TrackingDataModel>>
        get() = _trackingData

    private val _retryBooking = MutableLiveData<Result<RetryBookingResponse>>()
    val retryBooking: LiveData<Result<RetryBookingResponse>>
        get() = _retryBooking

    private val _retryAvailability = MutableLiveData<Result<RetryAvailabilityResponse>>()
    val retryAvailability: LiveData<Result<RetryAvailabilityResponse>>
        get() = _retryAvailability

    fun getTrackingData(orderId: String) {
        viewModelScope.launch {
            try {
                val trackingParam = trackingUseCase.getParam(orderId, "")
                val getTrackingData = trackingUseCase(trackingParam)
                _trackingData.value = Success(mapper.mapTrackingData(getTrackingData))
            } catch (e: Throwable) {
                _trackingData.value = Fail(e)
            }
        }
    }

    fun retryBooking(orderId: String) {
        viewModelScope.launch {
            try {
                val retryBooking = setRetryBookingUseCase(orderId)
                _retryBooking.value = Success(retryBooking)
            } catch (e: Throwable) {
                _retryBooking.value = Fail(e)
            }
        }
    }

    fun retryAvailability(orderId: String) {
        viewModelScope.launch {
            try {
                val retryAvailability = setRetryAvailabilityUseCase(orderId)
                _retryAvailability.value = Success(retryAvailability)
            } catch (e: Throwable) {
                _retryAvailability.value = Fail(e)
            }
        }
    }

}
