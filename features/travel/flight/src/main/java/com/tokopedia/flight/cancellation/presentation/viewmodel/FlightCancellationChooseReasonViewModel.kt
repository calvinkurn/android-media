package com.tokopedia.flight.cancellation.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.flight.cancellation.data.FlightCancellationPassengerEntity
import com.tokopedia.flight.cancellation.data.FlightCancellationReasonDataCacheSource
import javax.inject.Inject

/**
 * @author by furqan on 20/07/2020
 */
class FlightCancellationChooseReasonViewModel @Inject constructor(
        private val reasonDataCacheSource: FlightCancellationReasonDataCacheSource,
        dispatcherProvider: CoroutineDispatchers)
    : BaseViewModel(dispatcherProvider.io) {

    var selectedReason: FlightCancellationPassengerEntity.Reason? = null

    private val mutableReasonList = MutableLiveData<List<FlightCancellationPassengerEntity.Reason>>()
    val reasonList: LiveData<List<FlightCancellationPassengerEntity.Reason>>
        get() = mutableReasonList

    fun loadReasonList() {
        val reasons = reasonDataCacheSource.getCache()
        mutableReasonList.postValue(reasons)
    }

    fun isReasonChecked(reason: FlightCancellationPassengerEntity.Reason): Boolean =
            selectedReason?.title.equals(reason.title, true)

}