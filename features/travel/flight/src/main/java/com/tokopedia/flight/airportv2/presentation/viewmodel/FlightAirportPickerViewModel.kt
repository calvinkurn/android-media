package com.tokopedia.flight.airportv2.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.flight.airportv2.domain.FlightAirportPopularCityUseCase
import com.tokopedia.flight.airportv2.domain.FlightAirportSuggestionUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * @author by furqan on 19/05/2020
 */
class FlightAirportPickerViewModel @Inject constructor(
        private val flightPopularAirportUseCase: FlightAirportPopularCityUseCase,
        private val flightAirportSuggestionUseCase: FlightAirportSuggestionUseCase,
        private val dispatcherProvider: CoroutineDispatchers)
    : BaseViewModel(dispatcherProvider.io) {

    private val mutableAirportList = MutableLiveData<Result<List<Visitable<*>>>>()
    val airportList: LiveData<Result<List<Visitable<*>>>>
        get() = mutableAirportList

    fun fetchAirport(keyword: String = "") {
        if (keyword.isEmpty()) {
            fetchPopularCity()
        } else {
            fetchSuggestionAirport(keyword)
        }
    }

    private fun fetchPopularCity() {
        launchCatchError(context = dispatcherProvider.main, block = {
            mutableAirportList.postValue(Success(flightPopularAirportUseCase.fetchAirportPopularCity()))
        }) {
            mutableAirportList.postValue(Fail(it))
            it.printStackTrace()
        }
    }

    private fun fetchSuggestionAirport(keyword: String) {
        launchCatchError(context = dispatcherProvider.main, block = {
            mutableAirportList.postValue(Success(flightAirportSuggestionUseCase.fetchAirportSuggestion(keyword)))
        }) {
            mutableAirportList.postValue(Fail(it))
            it.printStackTrace()
        }
    }

}