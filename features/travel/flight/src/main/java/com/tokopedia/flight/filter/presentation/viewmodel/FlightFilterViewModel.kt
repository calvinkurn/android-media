package com.tokopedia.flight.filter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.utils.TravelDispatcherProvider
import com.tokopedia.flight.search.domain.FlightSearchCountUseCase
import com.tokopedia.flight.search.domain.FlightSearchStatisticsUseCase
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel
import com.tokopedia.flight.search.presentation.model.resultstatistics.FlightSearchStatisticModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by furqan on 19/02/2020
 */
class FlightFilterViewModel @Inject constructor(
        private val flightSearchCountUseCase: FlightSearchCountUseCase,
        private val flightSearchStatisticUseCase: FlightSearchStatisticsUseCase,
        private val dispatcherProvider: TravelDispatcherProvider)
    : BaseViewModel(dispatcherProvider.io()) {

    private var isReturn = false

    private val mutableFilterModel = MutableLiveData<FlightFilterModel>()
    val filterModel: LiveData<FlightFilterModel>
        get() = mutableFilterModel

    private val mutableStatisticModel = MutableLiveData<FlightSearchStatisticModel>()
    val statisticModel: LiveData<FlightSearchStatisticModel>
        get() = mutableStatisticModel

    private val mutableFlightCount = MutableLiveData<Int>()
    val flightCount: LiveData<Int>
        get() = mutableFlightCount

    fun init(filterModel: FlightFilterModel, isReturn: Boolean) {
        mutableFilterModel.value = filterModel
        this.isReturn = isReturn

        launch(dispatcherProvider.ui()) {
            getStatistics()
            getFlightCount()
        }
    }

    suspend fun getStatistics() {
        mutableStatisticModel.postValue(withContext(dispatcherProvider.ui()) {
            filterModel.value?.run {
                flightSearchStatisticUseCase.executeCoroutine(flightSearchStatisticUseCase
                        .createRequestParams(filterModel.value!!))
            }
        })
    }

    suspend fun getFlightCount() {
        mutableFlightCount.postValue(withContext(dispatcherProvider.ui()) {
            filterModel.value?.run {
                flightSearchCountUseCase.executeCoroutine(flightSearchCountUseCase
                        .createRequestParams(filterModel.value!!))
            }
        })
    }

    fun resetFilter() {
        // TODO: Reset Filter Value
    }
}