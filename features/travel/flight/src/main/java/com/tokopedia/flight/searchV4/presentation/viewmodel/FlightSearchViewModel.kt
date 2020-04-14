package com.tokopedia.flight.searchV4.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.constant.TravelSortOption
import com.tokopedia.common.travel.utils.TravelDispatcherProvider
import com.tokopedia.flight.airport.view.model.FlightAirportModel
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.common.util.FlightRequestUtil
import com.tokopedia.flight.search.presentation.model.FlightAirportCombineModel
import com.tokopedia.flight.search.presentation.model.FlightJourneyModel
import com.tokopedia.flight.search.presentation.model.FlightSearchMetaModel
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataModel
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel
import com.tokopedia.flight.searchV4.data.cloud.single.FlightSearchRequestModel
import com.tokopedia.flight.searchV4.domain.FlightSearchSingleUseCase
import com.tokopedia.flight.searchV4.domain.FlightSortAndFilterUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author by furqan on 09/04/2020
 */
open class FlightSearchViewModel @Inject constructor(
        private val flightSearchSingleUseCase: FlightSearchSingleUseCase,
        private val flightSortAndFilterUseCase: FlightSortAndFilterUseCase,
        private val flightAnalytics: FlightAnalytics,
        private val dispatcherProvider: TravelDispatcherProvider)
    : BaseViewModel(dispatcherProvider.io()) {

    lateinit var flightSearchPassData: FlightSearchPassDataModel
    lateinit var flightAirportCombine: FlightAirportCombineModel
    lateinit var filterModel: FlightFilterModel
    var selectedSortOption: Int = TravelSortOption.CHEAPEST
    val isInFilterMode: Boolean
        get() {
            if (::filterModel.isInitialized) {
                return filterModel.isHasFilter
            }
            return false
        }

    var priceFilterStatistic: Pair<Int, Int> = Pair(0, Int.MAX_VALUE)

    private val mutableJourneyList = MutableLiveData<List<FlightJourneyModel>>()
    val journeyList: LiveData<List<FlightJourneyModel>>
        get() = mutableJourneyList

    val progress = MutableLiveData<Int>()

    init {
        progress.value = DEFAULT_PROGRESS_VALUE
    }

    fun initialize(needDeleteData: Boolean, isReturnTrip: Boolean) {
        if (needDeleteData) {
            if (isReturnTrip) {
//                deleteFlightReturnSearch()
            } else {
//                deleteAllSearchData()
            }
        }
    }

    fun fetchSearchDataCloud(isReturnTrip: Boolean, delayInSeconds: Long = -1) {
        val date: String = flightSearchPassData.getDate(isReturnTrip)
        val adult = flightSearchPassData.flightPassengerViewModel.adult
        val child = flightSearchPassData.flightPassengerViewModel.children
        val infant = flightSearchPassData.flightPassengerViewModel.infant
        val classId = flightSearchPassData.flightClass.id
        val searchRequestId = flightSearchPassData.searchRequestId

        val requestModel = FlightSearchRequestModel(
                flightAirportCombine.depAirport,
                flightAirportCombine.arrAirport,
                date, adult, child, infant, classId,
                flightAirportCombine.airlines,
                FlightRequestUtil.getLocalIpAddress(),
                searchRequestId)

        launchCatchError(dispatcherProvider.ui(), {
            if (delayInSeconds > -1) {
                delay(TimeUnit.SECONDS.toMillis(delayInSeconds))
            }

            val data = flightSearchSingleUseCase.execute(requestModel,
                    isReturnTrip,
                    !flightSearchPassData.isOneWay,
                    filterModel.journeyId)

            onGetSearchMeta(data, isReturnTrip)
        }) {
            it.printStackTrace()
        }
    }

    fun buildAirportCombineModel(): FlightAirportCombineModel {
        val departureAirport = if (getDepartureAirport().airportCode == null || getDepartureAirport().airportCode.isEmpty()) {
            getDepartureAirport().cityCode
        } else {
            getDepartureAirport().airportCode
        }

        val arrivalAirport = if (getArrivalAirport().airportCode == null || getArrivalAirport().airportCode.isEmpty()) {
            getArrivalAirport().cityCode
        } else {
            getArrivalAirport().airportCode
        }

        return FlightAirportCombineModel(departureAirport, arrivalAirport)
    }

    fun fetchSortAndFilter() {
        launchCatchError(context = dispatcherProvider.ui(), block = {
            flightSortAndFilterUseCase.execute(selectedSortOption, filterModel).let {
                flightAnalytics.eventSearchView(flightSearchPassData, true)
                mutableJourneyList.postValue(it)
            }
        }) {
            it.printStackTrace()
        }
    }

    fun isDoneLoadData(): Boolean {
        progress.value?.let {
            return it >= MAX_PROGRESS
        }
        return false
    }

    fun isFilterModelInitialized(): Boolean = ::filterModel.isInitialized

    fun isOneWay(): Boolean = flightSearchPassData.isOneWay

    fun recountFilterCounter(): Int {
        var counter = 0

        counter += filterModel.transitTypeList.size
        counter += filterModel.airlineList.size
        counter += filterModel.departureTimeList.size
        counter += filterModel.arrivalTimeList.size
        counter += filterModel.refundableTypeList.size
        counter += filterModel.facilityList.size

        if (filterModel.priceMin > priceFilterStatistic.first ||
                filterModel.priceMax < priceFilterStatistic.second) {
            counter++
        }

        return counter
    }

    fun sendQuickFilterTrack(filterName: String) {
        flightAnalytics.eventQuickFilterClick(filterName)
    }

    open fun getDepartureAirport(): FlightAirportModel = flightSearchPassData.departureAirport

    open fun getArrivalAirport(): FlightAirportModel = flightSearchPassData.arrivalAirport

    open fun buildFilterModel(filterModel: FlightFilterModel) {
        this.filterModel = filterModel
    }

    private fun onGetSearchMeta(flightSearchMeta: FlightSearchMetaModel, returnTrip: Boolean) {
        if (!returnTrip) {
            flightSearchPassData.searchRequestId = flightSearchMeta.searchRequestId
        }

        if (flightAirportCombine.isNeedRefresh) {
            if (flightSearchMeta.isNeedRefresh) {
                flightAirportCombine.noOfRetry++

                // already reach max retry limit, end retry
                if (flightAirportCombine.noOfRetry > flightSearchMeta.maxRetry) {
                    flightAirportCombine.isNeedRefresh = false
                } else {
                    // retry
                    fetchSearchDataCloud(returnTrip, flightSearchMeta.refreshTime.toLong())
                }
            } else {
                flightAirportCombine.isNeedRefresh = false
            }
        }

        progress.postValue(
                if (flightAirportCombine.isNeedRefresh) ((flightAirportCombine.noOfRetry.toDouble()) / (flightSearchMeta.maxRetry.toDouble()) * MAX_PROGRESS).toInt()
                else MAX_PROGRESS)

        fetchSortAndFilter()
    }

    companion object {
        private const val DEFAULT_PROGRESS_VALUE = 0
        private const val MAX_PROGRESS = 100
    }

}