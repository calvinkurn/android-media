package com.tokopedia.flight.searchV2.domain

import android.text.TextUtils
import android.util.SparseIntArray
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.flight.search.constant.FlightSortOption
import com.tokopedia.flight.search.data.cloud.model.response.Amenity
import com.tokopedia.flight.search.data.cloud.model.response.Info
import com.tokopedia.flight.search.data.cloud.model.response.Route
import com.tokopedia.flight.search.data.cloud.model.response.StopDetailEntity
import com.tokopedia.flight.search.view.model.filter.DepartureTimeEnum
import com.tokopedia.flight.search.view.model.filter.TransitEnum
import com.tokopedia.flight.search.view.model.resultstatistics.*
import com.tokopedia.flight.searchV2.data.db.JourneyAndRoutes
import com.tokopedia.flight.searchV2.data.repository.FlightSearchRepository
import com.tokopedia.flight.searchV2.presentation.model.FlightFareViewModel
import com.tokopedia.flight.searchV2.presentation.model.FlightJourneyViewModel
import com.tokopedia.flight.searchV2.presentation.model.filter.FlightFilterModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import java.util.*
import javax.inject.Inject

/**
 * Created by Rizky on 15/10/18.
 */
class FlightSearchStatisticsUseCase @Inject constructor(
        private val flightSearchRepository: FlightSearchRepository) : UseCase<FlightSearchStatisticModel>() {

    private val PARAM_FILTER_MODEL = "PARAM_FILTER_MODEL"

    override fun createObservable(requestParams: RequestParams): Observable<FlightSearchStatisticModel> {
        val filterModel = requestParams.getObject(PARAM_FILTER_MODEL) as FlightFilterModel

        return flightSearchRepository.getSearchFilter(FlightSortOption.CHEAPEST, filterModel)
                .map {
                    val flightJourneyViewModel = map(it)
                    mapToStatisticsViewModel(flightJourneyViewModel)
                }
    }

    private fun mapToStatisticsViewModel(flightSearchViewModelList: List<FlightJourneyViewModel>):
            FlightSearchStatisticModel {
        var minPrice = Integer.MAX_VALUE
        var maxPrice = Integer.MIN_VALUE
        var minDuration = Integer.MAX_VALUE
        var maxDuration = Integer.MIN_VALUE
        val transitTypeStatList = ArrayList<TransitStat>()
        val airlineStatList = ArrayList<AirlineStat>()
        val departureTimeStatList = ArrayList<DepartureStat>()
        val refundableTypeStatList = ArrayList<RefundableStat>()

        val transitIDTrackArray = SparseIntArray()
        val airlineIDTrackArray = HashMap<String, Int>()
        val departureIDTrackArray = SparseIntArray()
        val refundableTrackArray = SparseIntArray()

        var isHaveSpecialPrice = false

        for (flightSearchViewModel in flightSearchViewModelList) {
            val price = flightSearchViewModel.fare.adultNumeric
            val priceString = flightSearchViewModel.fare.adult
            if (price < minPrice) {
                minPrice = price
            }
            if (price > maxPrice) {
                maxPrice = price
            }
            val duration = flightSearchViewModel.durationMinute
            if (duration < minDuration) {
                minDuration = duration
            }
            if (duration > maxDuration) {
                maxDuration = duration
            }
            // populate total transit and minprice per each transit
            val transitTypeDef = when (flightSearchViewModel.totalTransit) {
                0 -> TransitEnum.DIRECT
                1 -> TransitEnum.ONE
                2 -> TransitEnum.TWO
                else -> TransitEnum.THREE_OR_MORE
            }
            if (transitIDTrackArray.get(transitTypeDef.id, -1) == -1) {
                transitTypeStatList.add(TransitStat(transitTypeDef, price, priceString))
                transitIDTrackArray.put(transitTypeDef.id, transitTypeStatList.size - 1)
            } else {
                val index = transitIDTrackArray.get(transitTypeDef.id)
                val prevTransitStat = transitTypeStatList[index]
                if (price < prevTransitStat.minPrice) {
                    prevTransitStat.minPrice = price
                    prevTransitStat.minPriceString = priceString
                }
            }

            // populate airline and minprice per each airline
            val airlineData = flightSearchViewModel.airlineDataList
            if (airlineData != null) {
                for (flightAirlineDB in airlineData) {
                    val airlineID = flightAirlineDB.id

                    if (!airlineIDTrackArray.containsKey(airlineID)) {
                        airlineStatList.add(AirlineStat(flightAirlineDB, price, priceString))
                        airlineIDTrackArray[airlineID] = airlineStatList.size - 1
                    } else {
                        val index = airlineIDTrackArray[airlineID]
                        val prevAirlineStat = airlineStatList[index!!]
                        if (price < prevAirlineStat.minPrice) {
                            prevAirlineStat.minPrice = price
                            prevAirlineStat.minPriceString = priceString
                        }
                    }
                }
            }

            // populate departureTime and minprice per each time
            val departureTimeDef = when (flightSearchViewModel.departureTimeInt) {
                in 0..559 -> DepartureTimeEnum._00
                in 600..1159 -> DepartureTimeEnum._06
                in 1200..1759 -> DepartureTimeEnum._12
                else -> DepartureTimeEnum._18
            }

            if (departureIDTrackArray.get(departureTimeDef.id, -1) == -1) {
                departureTimeStatList.add(DepartureStat(departureTimeDef, price, priceString))
                departureIDTrackArray.put(departureTimeDef.id, departureTimeStatList.size - 1)
            } else {
                val index = departureIDTrackArray.get(departureTimeDef.id)
                val prevDepartureStat = departureTimeStatList[index]
                if (price < prevDepartureStat.minPrice) {
                    prevDepartureStat.minPrice = price
                    prevDepartureStat.minPriceString = priceString
                }
            }

            // populate distinct refundable
            val refundable = flightSearchViewModel.isRefundable
            if (refundableTrackArray.get(refundable.id, -1) == -1) {
                refundableTypeStatList.add(RefundableStat(refundable, price, priceString))
                refundableTrackArray.put(refundable.id, refundableTypeStatList.size - 1)
            } else {
                val index = refundableTrackArray.get(refundable.id)
                val prevRefundableStat = refundableTypeStatList[index]
                if (price < prevRefundableStat.minPrice) {
                    prevRefundableStat.minPrice = price
                    prevRefundableStat.minPriceString = priceString
                }
            }
            if (!TextUtils.isEmpty(flightSearchViewModel.beforeTotal)) {
                isHaveSpecialPrice = true
            }
        }

        //sort array
        transitTypeStatList.sortWith(Comparator { o1, o2 -> o1.transitType.id - o2.transitType.id })
        airlineStatList.sortWith(Comparator { o1, o2 -> o1.airlineDB.name.compareTo(o2.airlineDB.name) })
        departureTimeStatList.sortWith(Comparator { o1, o2 -> o1.departureTime.id - o2.departureTime.id })
        refundableTypeStatList.sortWith(Comparator { o1, o2 -> o1.refundableEnum.id - o2.refundableEnum.id })

        return FlightSearchStatisticModel(minPrice, maxPrice, minDuration, maxDuration, transitTypeStatList,
                airlineStatList, departureTimeStatList, refundableTypeStatList, isHaveSpecialPrice)
    }

    private fun map(it: List<JourneyAndRoutes>): List<FlightJourneyViewModel> {
        val gson = Gson()
        return it.map { journeyAndRoutes ->
            val routes = journeyAndRoutes.routes.map {
                val stopDetailJsonString = it.stopDetail
                val stopDetailType = object : TypeToken<List<StopDetailEntity>>() {}.type
                val stopDetails = gson.fromJson<List<StopDetailEntity>>(stopDetailJsonString, stopDetailType)

                val amenitiesJsonString = it.amenities
                val amenitiesType = object : TypeToken<List<Amenity>>() {}.type
                val amenities = gson.fromJson<List<Amenity>>(amenitiesJsonString, amenitiesType)

                val infosJsonString = it.infos
                val infosType = object : TypeToken<List<Info>>() {}.type
                val infos = gson.fromJson<List<Info>>(infosJsonString, infosType)

                Route(
                        it.airline,
                        it.departureAirport,
                        it.departureTimestamp,
                        it.arrivalAirport,
                        it.arrivalTimestamp,
                        it.duration,
                        it.layover,
                        infos,
                        it.flightNumber,
                        it.isRefundable,
                        amenities,
                        it.stops,
                        stopDetails,
                        it.airlineName,
                        it.airlineLogo,
                        it.departureAirportName,
                        it.departureAirportCity,
                        it.arrivalAirportName,
                        it.arrivalAirportCity
                )
            }
            with(journeyAndRoutes.flightJourneyTable) {
                val fare = FlightFareViewModel(
                        adult,
                        adultCombo,
                        child,
                        childCombo,
                        infant,
                        infantCombo,
                        adultNumeric,
                        adultNumericCombo,
                        childNumeric,
                        childNumericCombo,
                        infantNumeric,
                        infantNumericCombo
                )
                FlightJourneyViewModel(
                        term,
                        id,
                        departureAirport,
                        departureAirportName,
                        departureAirportCity,
                        departureTime,
                        departureTimeInt,
                        arrivalAirport,
                        arrivalTime,
                        arrivalAirportName,
                        arrivalAirportCity,
                        arrivalTimeInt,
                        totalTransit,
                        addDayArrival,
                        duration,
                        durationMinute,
                        total,
                        totalNumeric,
                        totalCombo,
                        0,
                        isBestPairing,
                        beforeTotal,
                        isRefundable,
                        isReturn,
                        fare,
                        routes,
                        flightAirlineDBS
                )
            }
        }
    }

    fun createRequestParams(flightFilterModel: FlightFilterModel): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putObject(PARAM_FILTER_MODEL, flightFilterModel)
        return requestParams
    }

}