package com.tokopedia.flight.filter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.constant.TravelSortOption
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.flight.filter.presentation.FlightFilterFacilityEnum
import com.tokopedia.flight.filter.presentation.model.*
import com.tokopedia.flight.searchV4.domain.FlightSearchCountUseCase
import com.tokopedia.flight.searchV4.domain.FlightSearchStatisticsUseCase
import com.tokopedia.flight.searchV4.presentation.model.filter.DepartureTimeEnum
import com.tokopedia.flight.searchV4.presentation.model.filter.FlightFilterModel
import com.tokopedia.flight.searchV4.presentation.model.filter.TransitEnum
import com.tokopedia.flight.searchV4.presentation.model.statistics.AirlineStat
import com.tokopedia.flight.searchV4.presentation.model.statistics.FlightSearchStatisticModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by furqan on 19/02/2020
 */
class FlightFilterViewModel @Inject constructor(
        private val flightSearchCountUseCase: FlightSearchCountUseCase,
        private val flightSearchStatisticUseCase: FlightSearchStatisticsUseCase,
        private val dispatcherProvider: CoroutineDispatchers)
    : BaseViewModel(dispatcherProvider.io) {

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
        mutableSelectedSort.postValue(selectedSort)
        mutableFilterModel.postValue(filterModel)

        getStatistics()
        getFlightCount()
    }

    fun setSelectedSort(selectedId: Int) {
        mutableSelectedSort.postValue(selectedId)
    }

    fun filterTransit(selectedTransits: List<TransitEnum>) {
        val updatedFilterModel = (filterModel.value as FlightFilterModel)
        updatedFilterModel.transitTypeList = selectedTransits.toMutableList()
        mutableFilterModel.postValue(updatedFilterModel)
    }

    fun filterDepartureTime(selectedDepartureTimes: List<DepartureTimeEnum>) {
        val updatedFilterModel = (filterModel.value as FlightFilterModel)
        updatedFilterModel.departureTimeList = selectedDepartureTimes.toMutableList()
        mutableFilterModel.postValue(updatedFilterModel)
    }

    fun filterArrivalTime(selectedArrivalTimes: List<DepartureTimeEnum>) {
        val updatedFilterModel = (filterModel.value as FlightFilterModel)
        updatedFilterModel.arrivalTimeList = selectedArrivalTimes.toMutableList()
        mutableFilterModel.postValue(updatedFilterModel)
    }

    fun filterAirlines(selectedArlines: List<String>) {
        val updatedFilterModel = (filterModel.value as FlightFilterModel)
        updatedFilterModel.airlineList = selectedArlines.toMutableList()
        mutableFilterModel.postValue(updatedFilterModel)
    }

    fun filterFacilities(selectedFacilities: List<FlightFilterFacilityEnum>) {
        val updatedFilterModel = (filterModel.value as FlightFilterModel)
        updatedFilterModel.facilityList = selectedFacilities.toMutableList()
        mutableFilterModel.postValue(updatedFilterModel)
    }

    fun filterPrices(minPrice: Int, maxPrice: Int) {
        val updateFilterModel = (filterModel.value as FlightFilterModel)
        updateFilterModel.priceMin = minPrice
        updateFilterModel.priceMax = maxPrice
        mutableFilterModel.postValue(updateFilterModel)
    }

    fun resetFilter() {
        mutableSelectedSort.postValue(SORT_DEFAULT_VALUE)
        mutableFilterModel.postValue(resetFilterModel())
    }

    fun getAirlineList(): List<AirlineStat> = statisticModel.value?.airlineStatList ?: arrayListOf()

    fun setStatistics(statistics: FlightSearchStatisticModel) {
        mutableStatisticModel.postValue(statistics)
    }

    private fun getStatistics() {
        launch(dispatcherProvider.main) {
            filterModel.value?.let {
                mutableStatisticModel.postValue(flightSearchStatisticUseCase.execute(it))
            }
        }
    }

    private fun getFlightCount() {
        launch(dispatcherProvider.main) {
            filterModel.value?.run {
                mediatorFlightCount.postValue(flightSearchCountUseCase.execute(filterModel.value!!))
            }
        }
    }

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
            val selectedMinPrice = filterModel.value?.priceMin ?: 0
            val selectedMaxPrice = filterModel.value?.priceMax ?: Integer.MAX_VALUE
            items.add(PRICE_ORDER, PriceRangeModel(
                    initialStartValue = it.minPrice,
                    initialEndValue = it.maxPrice,
                    selectedStartValue = if (selectedMinPrice > it.minPrice) selectedMinPrice else it.minPrice,
                    selectedEndValue = if (selectedMaxPrice < it.maxPrice) selectedMaxPrice else it.maxPrice
            ))

        }

        filterViewData.postValue(items)
    }

    private fun resetFilterModel(): FlightFilterModel {
        val filterModel = mutableFilterModel.value?.copy() ?: FlightFilterModel()
        filterModel.isHasFilter = false
        filterModel.isSpecialPrice = false
        filterModel.priceMin = mutableStatisticModel.value?.minPrice ?: 0
        filterModel.priceMax = mutableStatisticModel.value?.maxPrice ?: Integer.MAX_VALUE
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