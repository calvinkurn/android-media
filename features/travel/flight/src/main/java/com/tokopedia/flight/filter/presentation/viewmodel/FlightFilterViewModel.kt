package com.tokopedia.flight.filter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.constant.TravelSortOption
import com.tokopedia.common.travel.utils.TravelDispatcherProvider
import com.tokopedia.flight.filter.presentation.model.BaseFilterSortModel
import com.tokopedia.flight.filter.presentation.model.FlightSortModel
import com.tokopedia.flight.filter.presentation.model.PriceRangeModel
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

    private val mutableFilterViewData = MutableLiveData<List<BaseFilterSortModel>>()
    val filterViewData: LiveData<List<BaseFilterSortModel>>
        get() = mutableFilterViewData

    fun init(selectedSort: Int, filterModel: FlightFilterModel, isReturn: Boolean) {
        mutableSelectedSort.postValue(selectedSort)
        mutableFilterModel.value = filterModel
        this.isReturn = isReturn

        getStatistics()
        getFlightCount()
    }

    private fun getStatistics() {
        launch(dispatcherProvider.ui()) {
            mutableStatisticModel.postValue(withContext(dispatcherProvider.ui()) {
                filterModel.value?.run {
                    flightSearchStatisticUseCase.executeCoroutine(flightSearchStatisticUseCase
                            .createRequestParams(filterModel.value!!))
                }
            })

            mapStatisticToModel(statisticModel.value)
        }
    }
    fun setSelectedSort(selectedId: Int) {
        mutableSelectedSort.postValue(selectedId)
    }

    fun getSelectedSort(): Int = selectedSort.value ?: TravelSortOption.CHEAPEST

    fun getFlightCount() {
        launch(dispatcherProvider.ui()) {
            mutableFlightCount.postValue(withContext(dispatcherProvider.ui()) {
                filterModel.value?.run {
                    flightSearchCountUseCase.executeCoroutine(flightSearchCountUseCase
                            .createRequestParams(filterModel.value!!))
                }
            })
        }
    }

    fun resetFilter() {
        // TODO: Reset Filter Value
    }

    private fun mapStatisticToModel(statistic: FlightSearchStatisticModel?) {
        val items = arrayListOf<BaseFilterSortModel>()

        statistic?.run {
            // Sort
            items.add(SORT_ORDER, FlightSortModel())

            // Transit
            items.add(TRANSIT_ORDER, PriceRangeModel())

            // Departure Time
            items.add(DEPARTURE_TIME_ORDER, PriceRangeModel())

            // Arrival Time
            items.add(ARRIVAL_TIME_ORDER, PriceRangeModel())

            // Airline
            items.add(AIRLINE_ORDER, PriceRangeModel())

            // Facility
            items.add(FACILITY_ORDER, PriceRangeModel())

            // Price
            items.add(PRICE_ORDER, PriceRangeModel(
                    initialStartValue = statistic.minPrice,
                    initialEndValue = statistic.maxPrice,
                    selectedStartValue = filterModel.value?.priceMin ?: statistic.minPrice,
                    selectedEndValue = filterModel.value?.priceMax ?: statistic.maxPrice
            ))
        }

        mutableFilterViewData.postValue(items)
    }

    companion object {
        const val SORT_ORDER = 0
        const val TRANSIT_ORDER = 1
        const val DEPARTURE_TIME_ORDER = 2
        const val ARRIVAL_TIME_ORDER = 3
        const val AIRLINE_ORDER = 4
        const val FACILITY_ORDER = 5
        const val PRICE_ORDER = 6
    }
}