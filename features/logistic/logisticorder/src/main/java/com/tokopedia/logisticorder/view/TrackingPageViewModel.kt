package com.tokopedia.logisticorder.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.logisticorder.domain.response.TrackOrder
import com.tokopedia.logisticorder.mapper.TrackingPageMapperNew
import com.tokopedia.logisticorder.uimodel.TrackingDataModel
import com.tokopedia.logisticorder.usecase.GetDeliveryImageUseCase
import com.tokopedia.logisticorder.usecase.TrackingPageRepository
import com.tokopedia.logisticorder.usecase.entity.RetryAvailabilityResponse
import com.tokopedia.logisticorder.usecase.entity.RetryBookingResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class TrackingPageViewModel @Inject constructor(
        private val repo: TrackingPageRepository,
        private val mapper: TrackingPageMapperNew,
        private val getDeliveryImageUseCase: GetDeliveryImageUseCase) : ViewModel() {

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
        viewModelScope.launch(onErrorGetTrackingData) {
            val getTrackingData = repo.getTrackingPage(orderId, " ")
            _trackingData.value = Success(mapper.mapTrackingData(getTrackingData))
        }
    }

    fun retryBooking(orderId: String) {
        viewModelScope.launch(onErrorRetryBooking) {
            val retryBooking = repo.retryBooking(orderId)
            _retryBooking.value = Success(retryBooking)
        }
    }

    fun retryAvailability(orderId: String) {
        viewModelScope.launch(onErrorRetryAvailability) {
            val retryAvailability = repo.retryAvailability(orderId)
            _retryAvailability.value = Success(retryAvailability)
        }
    }

    fun getDeliveryImage(orderId: String, )


    private val onErrorGetTrackingData = CoroutineExceptionHandler { _, throwable ->
        _trackingData.value = Fail(throwable)
    }

    private val onErrorRetryBooking = CoroutineExceptionHandler { _, throwable ->
        _retryBooking.value = Fail(throwable)
    }

    private val onErrorRetryAvailability = CoroutineExceptionHandler { _, throwable ->
        _retryAvailability.value = Fail(throwable)
    }
}