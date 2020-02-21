package com.tokopedia.flight.filter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.constant.TravelSortOption
import com.tokopedia.common.travel.utils.TravelDispatcherProvider
import com.tokopedia.flight.filter.presentation.model.*
import com.tokopedia.flight.search.domain.FlightSearchCountUseCase
import com.tokopedia.flight.search.domain.FlightSearchStatisticsUseCase
import com.tokopedia.flight.search.presentation.model.filter.DepartureTimeEnum
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel
import com.tokopedia.flight.search.presentation.model.filter.TransitEnum
import com.tokopedia.flight.search.presentation.model.resultstatistics.FlightSearchStatisticModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

    private val mutableSelectedSort = MutableLiveData<Int>()
    val selectedSort: LiveData<Int>
        get() = mutableSelectedSort

    private val mutableFilterModel = MutableLiveData<FlightFilterModel>()
    val filterModel: LiveData<FlightFilterModel>
        get() = mutableFilterModel

    private val mutableStatisticModel = MutableLiveData<FlightSearchStatisticModel>()
    val statisticModel: LiveData<FlightSearchStatisticModel>
        get() = mutableStatisticModel

    private val mutableFlightCount = MutableLiveData<Int>()
    val flightCount: LiveData<Int>
        get() = mutableFlightCount

    val filterViewData = MutableLiveData<List<BaseFilterSortModel>>()

    fun init(selectedSort: Int, filterModel: FlightFilterModel, isReturn: Boolean) {
        mutableSelectedSort.postValue(selectedSort)
        mutableFilterModel.value = filterModel
        this.isReturn = isReturn

        getStatistics()
        getFlightCount()
    }

    private fun getStatistics() {
        launch(dispatcherProvider.ui()) {
            filterModel.value?.let {
                mutableStatisticModel.postValue(flightSearchStatisticUseCase.executeCoroutine(
                        flightSearchStatisticUseCase.createRequestParams(filterModel.value!!)))
            }

            delay(DELAY_VALUE)

            mapStatisticToModel(statisticModel.value)
        }
    }

    fun setSelectedSort(selectedId: Int) {
        mutableSelectedSort.postValue(selectedId)
    }

    fun filterTransit(selectedTransits: List<TransitEnum>) {
        val updatedFilterModel = (filterModel.value as FlightFilterModel)
        updatedFilterModel.transitTypeList = selectedTransits
        mutableFilterModel.postValue(updatedFilterModel)
    }

    fun filterDepartureTime(selectedDepartureTimes: List<DepartureTimeEnum>) {
        val updatedFilterModel = (filterModel.value as FlightFilterModel)
        updatedFilterModel.departureTimeList = selectedDepartureTimes
        mutableFilterModel.postValue(updatedFilterModel)
    }

    fun filterArrivalTime(selectedArrivalTimes: List<DepartureTimeEnum>) {
        val updatedFilterModel = (filterModel.value as FlightFilterModel)
        updatedFilterModel.arrivalTimeList = selectedArrivalTimes
        mutableFilterModel.postValue(updatedFilterModel)
    }

    fun getSelectedSort(): Int = selectedSort.value ?: TravelSortOption.CHEAPEST

    fun getFlightCount() {
        launch(dispatcherProvider.ui()) {
            filterModel.value?.run {
                mutableFlightCount.postValue(flightSearchCountUseCase.executeCoroutine(flightSearchCountUseCase
                        .createRequestParams(filterModel.value!!)))
            }
        }
    }

    fun resetFilter() {
        // TODO: Reset Filter Value
    }

    private fun mapStatisticToModel(statistic: FlightSearchStatisticModel?) {
        val items = arrayListOf<BaseFilterSortModel>()

        statistic?.let {
            // Sort
            items.add(SORT_ORDER, FlightSortModel())

            // Transit
            items.add(TRANSIT_ORDER, TransitModel(TransitEnum.DIRECT))

            // Departure Time
            items.add(DEPARTURE_TIME_ORDER, DepartureTimeModel(DepartureTimeEnum._00))

            // Arrival Time
            items.add(ARRIVAL_TIME_ORDER, ArrivalTimeModel(DepartureTimeEnum._00))

            // Airline
            items.add(AIRLINE_ORDER, PriceRangeModel())

            // Facility
            items.add(FACILITY_ORDER, PriceRangeModel())

            // Price
            items.add(PRICE_ORDER, PriceRangeModel(
                    initialStartValue = it.minPrice,
                    initialEndValue = it.maxPrice,
                    selectedStartValue = filterModel.value?.priceMin ?: it.minPrice,
                    selectedEndValue = filterModel.value?.priceMax ?: it.maxPrice
            ))
        }

        filterViewData.postValue(items)
    }

    companion object {
        const val SORT_ORDER = 0
        const val TRANSIT_ORDER = 1
        const val DEPARTURE_TIME_ORDER = 2
        const val ARRIVAL_TIME_ORDER = 3
        const val AIRLINE_ORDER = 4
        const val FACILITY_ORDER = 5
        const val PRICE_ORDER = 6

        const val DELAY_VALUE : Long = 2000
    }
}