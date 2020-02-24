package com.tokopedia.flight.filter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.constant.TravelSortOption
import com.tokopedia.common.travel.utils.TravelDispatcherProvider
import com.tokopedia.flight.filter.presentation.FlightFilterFacilityEnum
import com.tokopedia.flight.filter.presentation.model.*
import com.tokopedia.flight.search.domain.FlightSearchCountUseCase
import com.tokopedia.flight.search.domain.FlightSearchStatisticsUseCase
import com.tokopedia.flight.search.presentation.model.filter.DepartureTimeEnum
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel
import com.tokopedia.flight.search.presentation.model.filter.TransitEnum
import com.tokopedia.flight.search.presentation.model.resultstatistics.AirlineStat
import com.tokopedia.flight.search.presentation.model.resultstatistics.FlightSearchStatisticModel
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

    private val mutableSelectedSort = MutableLiveData<Int>()
    val selectedSort: LiveData<Int>
        get() = mutableSelectedSort

    private val mutableFilterModel = MutableLiveData<FlightFilterModel>()
    val filterModel: LiveData<FlightFilterModel>
        get() = mutableFilterModel

    private val mutableStatisticModel = MutableLiveData<FlightSearchStatisticModel>()
    val statisticModel: LiveData<FlightSearchStatisticModel>
        get() = mutableStatisticModel

    private val mediatorFlightCount = MediatorLiveData<Int>()
    val flightCount: LiveData<Int>
        get() = mediatorFlightCount

    val filterViewData = MediatorLiveData<List<BaseFilterSortModel>>()

    init {
        mediatorFlightCount.addSource(mutableFilterModel) {
            getFlightCount()
        }

        filterViewData.addSource(mutableStatisticModel) {
            mapStatisticToModel()
        }
    }

    fun init(selectedSort: Int, filterModel: FlightFilterModel) {
        mutableSelectedSort.value = selectedSort
        mutableFilterModel.value = filterModel

        getStatistics()
        getFlightCount()
    }

    private fun getStatistics() {
        launch(dispatcherProvider.ui()) {
            filterModel.value?.let {
                mutableStatisticModel.postValue(flightSearchStatisticUseCase.executeCoroutine(
                        flightSearchStatisticUseCase.createRequestParams(filterModel.value!!)))
            }
        }
    }

    fun setSelectedSort(selectedId: Int) {
        mutableSelectedSort.value = selectedId
    }

    fun filterTransit(selectedTransits: List<TransitEnum>) {
        val updatedFilterModel = (filterModel.value as FlightFilterModel)
        updatedFilterModel.transitTypeList = selectedTransits
        mutableFilterModel.value = updatedFilterModel
    }

    fun filterDepartureTime(selectedDepartureTimes: List<DepartureTimeEnum>) {
        val updatedFilterModel = (filterModel.value as FlightFilterModel)
        updatedFilterModel.departureTimeList = selectedDepartureTimes
        mutableFilterModel.value = updatedFilterModel
    }

    fun filterArrivalTime(selectedArrivalTimes: List<DepartureTimeEnum>) {
        val updatedFilterModel = (filterModel.value as FlightFilterModel)
        updatedFilterModel.arrivalTimeList = selectedArrivalTimes
        mutableFilterModel.value = updatedFilterModel
    }

    fun filterAirlines(selectedArlines: List<String>) {
        val updatedFilterModel = (filterModel.value as FlightFilterModel)
        updatedFilterModel.airlineList = selectedArlines
        mutableFilterModel.value = updatedFilterModel
    }

    fun filterFacilities(selectedFacilities: List<FlightFilterFacilityEnum>) {
        val updatedFilterModel = (filterModel.value as FlightFilterModel)
        updatedFilterModel.facilityList = selectedFacilities
        mutableFilterModel.value = updatedFilterModel
    }

    fun filterPrices(minPrice: Int, maxPrice: Int) {
        val updateFilterModel = (filterModel.value as FlightFilterModel)
        updateFilterModel.priceMin = minPrice
        updateFilterModel.priceMax = maxPrice
        mutableFilterModel.value = updateFilterModel
    }

    private fun getFlightCount() {
        launch(dispatcherProvider.ui()) {
            filterModel.value?.run {
                mediatorFlightCount.postValue(flightSearchCountUseCase.executeCoroutine(flightSearchCountUseCase
                        .createRequestParams(filterModel.value!!)))
            }
        }
    }

    fun resetFilter() {
        mutableSelectedSort.value = SORT_DEFAULT_VALUE
        mutableFilterModel.value = resetFilterModel()
    }

    fun getAirlineList(): List<AirlineStat> = statisticModel.value?.airlineStatList ?: arrayListOf()

    private fun mapStatisticToModel() {
        val items = arrayListOf<BaseFilterSortModel>()

        statisticModel.value?.let {
            // Sort
            items.add(SORT_ORDER, FlightSortModel())

            // Transit
            items.add(TRANSIT_ORDER, TransitModel(TransitEnum.DIRECT))

            // Departure Time
            items.add(DEPARTURE_TIME_ORDER, DepartureTimeModel(DepartureTimeEnum._00))

            // Arrival Time
            items.add(ARRIVAL_TIME_ORDER, ArrivalTimeModel(DepartureTimeEnum._00))

            // Airline
            items.add(AIRLINE_ORDER, FlightFilterAirlineModel())

            // Facility
            items.add(FACILITY_ORDER, FlightFilterFacilityModel())

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

    private fun resetFilterModel(): FlightFilterModel {
        val filterModel = mutableFilterModel.value?.copy() ?: FlightFilterModel()
        filterModel.setHasFilter(false)
        filterModel.isSpecialPrice = false
        filterModel.priceMin = Integer.MIN_VALUE
        filterModel.priceMax = Integer.MAX_VALUE
        filterModel.transitTypeList = arrayListOf()
        filterModel.airlineList = arrayListOf()
        filterModel.departureTimeList = arrayListOf()
        filterModel.arrivalTimeList = arrayListOf()
        filterModel.refundableTypeList = arrayListOf()
        filterModel.facilityList = arrayListOf()
        return filterModel
    }

    companion object {
        const val SORT_ORDER = 0
        const val TRANSIT_ORDER = 1
        const val DEPARTURE_TIME_ORDER = 2
        const val ARRIVAL_TIME_ORDER = 3
        const val AIRLINE_ORDER = 4
        const val FACILITY_ORDER = 5
        const val PRICE_ORDER = 6

        const val SORT_DEFAULT_VALUE = TravelSortOption.CHEAPEST
    }
}