package com.tokopedia.flight.search.domain

import android.text.TextUtils
import android.util.SparseIntArray
import com.tokopedia.common.travel.constant.TravelSortOption
import com.tokopedia.flight.search.data.FlightSearchRepository
import com.tokopedia.flight.search.data.cache.db.JourneyAndRoutes
import com.tokopedia.flight.search.presentation.model.filter.DepartureTimeEnum
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel
import com.tokopedia.flight.search.presentation.model.filter.TransitEnum
import com.tokopedia.flight.search.presentation.model.statistics.*
import java.util.*
import javax.inject.Inject

/**
 * Created by Rizky on 15/10/18.
 */
class FlightSearchStatisticsUseCase @Inject constructor(
        private val flightSearchRepository: FlightSearchRepository) {

    suspend fun execute(flightFilterModel: FlightFilterModel): FlightSearchStatisticModel? {
        return mapToFlightSearchStatisticsModel(flightSearchRepository.getSearchFilterStatistic(
                TravelSortOption.CHEAPEST, flightFilterModel).journeyAndRoutes)
    }

    private fun mapToFlightSearchStatisticsModel(journeyAndRoutesList: List<JourneyAndRoutes>):
            FlightSearchStatisticModel {
        var minPrice = Integer.MAX_VALUE
        var maxPrice = Integer.MIN_VALUE
        var minDuration = Integer.MAX_VALUE
        var maxDuration = Integer.MIN_VALUE
        val transitTypeStatList = ArrayList<TransitStat>()
        val airlineStatList = ArrayList<AirlineStat>()
        val departureTimeStatList = ArrayList<DepartureStat>()
        val arrivalTimeStatList = ArrayList<DepartureStat>()
        val refundableTypeStatList = ArrayList<RefundableStat>()

        val transitIDTrackArray = SparseIntArray()
        val airlineIDTrackArray = HashMap<String, Int>()
        val departureIDTrackArray = SparseIntArray()
        val arrivalIDTrackArray = SparseIntArray()
        val refundableTrackArray = SparseIntArray()

        var isHaveSpecialPrice = false
        var isHaveBaggage = false
        var isHaveInFlightMeal = false
        var isHasFreeRapidTest = false
        var isSeatDistancing = false

        for (journeyAndRoutes in journeyAndRoutesList) {
            val price = journeyAndRoutes.flightJourneyTable.sortPriceNumeric
            val priceString = journeyAndRoutes.flightJourneyTable.sortPrice
            if (price < minPrice) {
                minPrice = price
            }
            if (price > maxPrice) {
                maxPrice = price
            }

            val duration = journeyAndRoutes.flightJourneyTable.durationMinute
            if (duration < minDuration) {
                minDuration = duration
            }
            if (duration > maxDuration) {
                maxDuration = duration
            }

            // populate total transit and minprice per each transit
            val transitTypeDef = when (journeyAndRoutes.flightJourneyTable.totalTransit) {
                0 -> TransitEnum.DIRECT
                1 -> TransitEnum.ONE
                else -> TransitEnum.TWO
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
            val airlineData = journeyAndRoutes.flightJourneyTable.flightAirlineDBS
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
            val departureTimeDef = when (journeyAndRoutes.flightJourneyTable.departureTimeInt) {
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

            // populate arrivalTime and minprice per each time
            val arrivalTimeDef = when (journeyAndRoutes.flightJourneyTable.arrivalTimeInt) {
                in 0..559 -> DepartureTimeEnum._00
                in 600..1159 -> DepartureTimeEnum._06
                in 1200..1759 -> DepartureTimeEnum._12
                else -> DepartureTimeEnum._18
            }

            if (arrivalIDTrackArray.get(arrivalTimeDef.id, -1) == -1) {
                arrivalTimeStatList.add(DepartureStat(arrivalTimeDef, price, priceString))
                arrivalIDTrackArray.put(arrivalTimeDef.id, arrivalTimeStatList.size - 1)
            } else {
                val index = arrivalIDTrackArray.get(arrivalTimeDef.id)
                val prevArrivalStat = arrivalTimeStatList[index]
                if (price < prevArrivalStat.minPrice) {
                    prevArrivalStat.minPrice = price
                    prevArrivalStat.minPriceString = priceString
                }
            }

            // populate distinct refundable
            val refundable = journeyAndRoutes.flightJourneyTable.isRefundable
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
            if (!TextUtils.isEmpty(journeyAndRoutes.flightJourneyTable.beforeTotal)) {
                isHaveSpecialPrice = true
            }

            if (!isHaveBaggage || !isHaveInFlightMeal) {
                for (route in journeyAndRoutes.routes) {
                    if (route.amenities.contains("baggage")) {
                        isHaveBaggage = true
                    }
                    if (route.amenities.contains("meal")) {
                        isHaveInFlightMeal = true
                    }

                    if (isHaveBaggage && isHaveInFlightMeal) break
                }
            }

            if (!isHasFreeRapidTest && journeyAndRoutes.flightJourneyTable.hasFreeRapidTest) {
                isHasFreeRapidTest = true
            }

            if (!isSeatDistancing && journeyAndRoutes.flightJourneyTable.isSeatDistancing) {
                isSeatDistancing = true
            }
        }

        //sort array
        transitTypeStatList.sortWith(Comparator { o1, o2 -> o1.transitType.id - o2.transitType.id })
        airlineStatList.sortWith(Comparator { o1, o2 -> o1.airlineDB.name.compareTo(o2.airlineDB.name) })
        departureTimeStatList.sortWith(Comparator { o1, o2 -> o1.departureTime.id - o2.departureTime.id })
        arrivalTimeStatList.sortWith(Comparator { o1, o2 -> o1.departureTime.id - o2.departureTime.id })
        refundableTypeStatList.sortWith(Comparator { o1, o2 -> o1.refundableEnum.id - o2.refundableEnum.id })

        return FlightSearchStatisticModel(minPrice, maxPrice, minDuration, maxDuration, transitTypeStatList,
                airlineStatList, departureTimeStatList, arrivalTimeStatList, refundableTypeStatList,
                isHaveSpecialPrice, isHaveBaggage, isHaveInFlightMeal, isHasFreeRapidTest, isSeatDistancing)
    }

}