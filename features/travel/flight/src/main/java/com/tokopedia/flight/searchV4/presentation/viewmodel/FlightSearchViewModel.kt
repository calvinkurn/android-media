package com.tokopedia.flight.searchV4.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.constant.TravelSortOption
import com.tokopedia.common.travel.utils.TravelDispatcherProvider
import com.tokopedia.flight.airport.view.model.FlightAirportModel
import com.tokopedia.flight.common.util.FlightRequestUtil
import com.tokopedia.flight.search.presentation.model.FlightAirportCombineModel
import com.tokopedia.flight.search.presentation.model.FlightSearchMetaModel
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataModel
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel
import com.tokopedia.flight.searchV4.data.cloud.single.FlightSearchRequestModel
import com.tokopedia.flight.searchV4.domain.FlightSearchUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author by furqan on 09/04/2020
 */
open class FlightSearchViewModel @Inject constructor(
        private val flightSearchUseCase: FlightSearchUseCase,
        private val dispatcherProvider: TravelDispatcherProvider)
    : BaseViewModel(dispatcherProvider.io()) {

    lateinit var flightSearchPassData: FlightSearchPassDataModel
    lateinit var flightAirportCombine: FlightAirportCombineModel
    lateinit var filterModel: FlightFilterModel
    var selectedSortOption: Int = TravelSortOption.CHEAPEST

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

            val data = flightSearchUseCase.execute(requestModel,
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

    open fun getDepartureAirport(): FlightAirportModel = flightSearchPassData.departureAirport

    open fun getArrivalAirport(): FlightAirportModel = flightSearchPassData.arrivalAirport

    open fun buildFilterModel(filterModel: FlightFilterModel): FlightFilterModel =
            filterModel

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
    }

}