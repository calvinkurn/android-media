package com.tokopedia.flight.searchV4.data

import com.tokopedia.common.travel.constant.TravelSortOption
import com.tokopedia.flight.searchV4.data.cache.FlightSearchCombineDataDbSource
import com.tokopedia.flight.searchV4.data.cache.FlightSearchDataCacheSource
import com.tokopedia.flight.searchV4.data.cache.FlightSearchSingleDataDbSource
import com.tokopedia.flight.searchV4.data.cache.db.FlightComboTable
import com.tokopedia.flight.searchV4.data.cache.db.FlightJourneyTable
import com.tokopedia.flight.searchV4.data.cache.db.JourneyAndRoutes
import com.tokopedia.flight.searchV4.data.cloud.FlightSearchDataCloudSource
import com.tokopedia.flight.searchV4.data.cloud.combine.FlightCombineRequestModel
import com.tokopedia.flight.searchV4.data.cloud.single.*
import com.tokopedia.flight.searchV4.domain.FlightSearchMapper.Companion.createCompleteJourneyAndRoutes
import com.tokopedia.flight.searchV4.domain.FlightSearchMapper.Companion.createJourneyWithCombo
import com.tokopedia.flight.searchV4.domain.FlightSearchMapper.Companion.getAirlineById
import com.tokopedia.flight.searchV4.domain.FlightSearchMapper.Companion.getAirlines
import com.tokopedia.flight.searchV4.domain.FlightSearchMapper.Companion.getAirports
import com.tokopedia.flight.searchV4.presentation.model.FlightAirlineModel
import com.tokopedia.flight.searchV4.presentation.model.FlightAirportModel
import com.tokopedia.flight.searchV4.presentation.model.filter.FlightFilterModel
import javax.inject.Inject

/**
 * @author by furqan on 08/04/2020
 */
class FlightSearchRepository @Inject constructor(
        private val flightSearchSingleDataDbSource: FlightSearchSingleDataDbSource,
        private val flightSearchDataCloudSource: FlightSearchDataCloudSource,
        private val flightSearchDataCacheSource: FlightSearchDataCacheSource,
        private val flightSearchCombineDataDbSource: FlightSearchCombineDataDbSource) {

    suspend fun getSearchCombined(combineParam: FlightCombineRequestModel): FlightSearchMetaEntity {
        with(flightSearchDataCloudSource.getSearchCombineData(combineParam)) {
            data.combos.map {
                val journeyIndexs = it.combination.split(",")
                val onwardJourneyId = data.journeys[ONWARD_JOURNEY_INDEX][journeyIndexs[ONWARD_JOURNEY_INDEX].toInt()].journeyId
                val returnJourneyId = data.journeys[RETURN_JOURNEY_INDEX][journeyIndexs[RETURN_JOURNEY_INDEX].toInt()].journeyId

                val comboTable = FlightComboTable(
                        it.comboKey,
                        onwardJourneyId,
                        it.fares[ONWARD_JOURNEY_INDEX].adultPrice,
                        it.fares[ONWARD_JOURNEY_INDEX].childPrice,
                        it.fares[ONWARD_JOURNEY_INDEX].infantPrice,
                        it.fares[ONWARD_JOURNEY_INDEX].adultPriceNumeric,
                        it.fares[ONWARD_JOURNEY_INDEX].childPriceNumeric,
                        it.fares[ONWARD_JOURNEY_INDEX].infantPriceNumeric,
                        returnJourneyId,
                        it.fares[RETURN_JOURNEY_INDEX].adultPrice,
                        it.fares[RETURN_JOURNEY_INDEX].childPrice,
                        it.fares[RETURN_JOURNEY_INDEX].infantPrice,
                        it.fares[RETURN_JOURNEY_INDEX].adultPriceNumeric,
                        it.fares[RETURN_JOURNEY_INDEX].childPriceNumeric,
                        it.fares[RETURN_JOURNEY_INDEX].infantPriceNumeric,
                        it.isBestPairing
                )

                //update journey data
                val latestJourneyList = arrayListOf<FlightJourneyTable>()
                val onwardJourneyTable = flightSearchSingleDataDbSource
                        .getJourneyRouteById(onwardJourneyId)?.flightJourneyTable
                val returnJourneyTable = flightSearchSingleDataDbSource
                        .getJourneyRouteById(returnJourneyId)?.flightJourneyTable

                if (onwardJourneyTable != null && isJourneyNeedToUpdate(onwardJourneyTable, it.fares[ONWARD_JOURNEY_INDEX].adultPriceNumeric)) {
                    latestJourneyList.add(createJourneyWithCombo(onwardJourneyTable, comboTable))
                }
                if (returnJourneyTable != null && isJourneyNeedToUpdate(returnJourneyTable, it.fares[RETURN_JOURNEY_INDEX].adultPriceNumeric)) {
                    latestJourneyList.add(createJourneyWithCombo(returnJourneyTable, comboTable))
                }
                flightSearchSingleDataDbSource.updateJourneyDataList(latestJourneyList)

                comboTable
            }.toList().let {
                flightSearchCombineDataDbSource.insert(it)
                return meta
            }
        }
    }

    suspend fun getSearchSingle(searchParam: FlightSearchRequestModel, isReturnTrip: Boolean): FlightSearchMetaEntity {
        val searchData = flightSearchDataCloudSource.getSearchSingleData(searchParam)
        val journeyAndRouteList = searchData.data.map {
            generateJourneyAndRoutes(it, searchData.included, isReturnTrip)
        }.toList()
        flightSearchSingleDataDbSource.insertList(journeyAndRouteList)
        val meta = searchData.meta
        meta.airlineList = getAirlines(journeyAndRouteList)
        return meta
    }

    // call search single api and then combine the result with combo, airport and airline db
    suspend fun getSearchSingleCombined(searchParam: FlightSearchRequestModel, isReturnTrip: Boolean): FlightSearchMetaEntity {
        val searchData = flightSearchDataCloudSource.getSearchSingleData(searchParam)
        val journeyAndRouteList = searchData.data.map { data ->
            generateJourneyAndRoutes(data, searchData.included, isReturnTrip).let { journeyAndRoutes ->
                flightSearchCombineDataDbSource.getSearchOnwardCombined(data.id).let { combos ->
                    if (combos.isNotEmpty()) {
                        val comboBestPairing = combos.find { it.isBestPairing }
                        val journeyTable = journeyAndRoutes.flightJourneyTable
                        journeyAndRoutes.flightJourneyTable = comboBestPairing?.let {
                            createJourneyWithCombo(journeyTable, it)
                        } ?: createJourneyWithCombo(journeyTable, combos[0])
                    }

                    journeyAndRoutes
                }
            }
        }.toList()
        flightSearchSingleDataDbSource.insertList(journeyAndRouteList)
        return searchData.meta.apply {
            airlineList = getAirlines(journeyAndRouteList)
        }
    }

    suspend fun getSearchCombinedReturn(searchParams: FlightSearchRequestModel,
                                        onwardJourneyId: String, isReturnTrip: Boolean)
            : FlightSearchMetaEntity {
        val searchData = flightSearchDataCloudSource.getSearchSingleData(searchParams)
        val journeyAndRouteList = searchData.data.map { data ->
            generateJourneyAndRoutes(data, searchData.included, isReturnTrip).let { journeyAndRoutes ->
                flightSearchCombineDataDbSource.getSearchReturnCombined(data.id).let { combos ->
                    if (combos.isNotEmpty()) {
                        val combo = combos.firstOrNull { it.onwardJourneyId == onwardJourneyId }
                        val journeyTable = journeyAndRoutes.flightJourneyTable
                        journeyAndRoutes.flightJourneyTable = combo?.let {
                            createJourneyWithCombo(journeyTable, it)
                        } ?: journeyTable
                    }

                    journeyAndRoutes
                }
            }
        }.toList()
        flightSearchSingleDataDbSource.insertList(journeyAndRouteList)
        return searchData.meta.apply {
            airlineList = getAirlines(journeyAndRouteList)
        }
    }

    suspend fun getJourneyById(journeyId: String): JourneyAndRoutes =
            flightSearchSingleDataDbSource.getJourneyById(journeyId)

    suspend fun getSearchFilter(flightSortOption: Int, flightFilterModel: FlightFilterModel): JourneyAndRoutesModel {
        val filteredJourney = flightSearchSingleDataDbSource.getFilteredJourneys(flightFilterModel, flightSortOption)
        val specialTag = flightSearchDataCacheSource.cacheCoroutine
        return JourneyAndRoutesModel(filteredJourney, specialTag)
    }

    suspend fun getComboKey(onwardJourneyId: String, returnJourneyId: String): String =
            flightSearchCombineDataDbSource.getComboData(onwardJourneyId, returnJourneyId).let {
                if (it.isNotEmpty()) {
                    it[0].comboId
                } else {
                    ""
                }
            }

    suspend fun deleteAllFlightSearchData() {
        flightSearchSingleDataDbSource.deleteAllSearchData()
        flightSearchCombineDataDbSource.deleteAllSearchCombinedData()
    }

    suspend fun deleteFlightSearchReturnData() {
        val filterModel = FlightFilterModel()
        filterModel.isReturn = true
        flightSearchSingleDataDbSource.getFilteredJourneys(filterModel, TravelSortOption.CHEAPEST).let {
            flightSearchSingleDataDbSource.deleteSearchReturnData(it)
        }
    }

    suspend fun getSearchCount(filterModel: FlightFilterModel): Int {
        return flightSearchSingleDataDbSource.getSearchCount(filterModel)
    }

    suspend fun getSearchFilterStatistic(@TravelSortOption sortOption: Int, filterModel: FlightFilterModel):
            JourneyAndRoutesModel {
        return JourneyAndRoutesModel(flightSearchSingleDataDbSource.getFilteredJourneysStatistic(filterModel, sortOption),
                flightSearchDataCacheSource.cacheCoroutine)
    }

    private fun generateJourneyAndRoutes(journeyResponse: FlightSearchData,
                                         includedList: List<FlightSearchIncluded>,
                                         isReturnTrip: Boolean): JourneyAndRoutes =
            journeyResponse.routes.map {
                getRouteAirlineByIdAndAirports(it, includedList)
            }.toList().let { routeAirlineAndAirports ->
                getAirports(journeyResponse.departureAirportId, journeyResponse.arrivalAirportId, includedList).let { journeyAirports ->
                    val journeyAirlines = arrayListOf<FlightAirlineModel>()
                    for (routeAirline in routeAirlineAndAirports) {
                        if (!journeyAirlines.contains(routeAirline.first.first)) {
                            journeyAirlines.add(routeAirline.first.first)
                        }
                    }
                    createCompleteJourneyAndRoutes(journeyResponse, journeyAirports,
                            journeyAirlines, routeAirlineAndAirports, isReturnTrip)
                }
            }

    private fun getRouteAirlineByIdAndAirports(route: FlightSearchRoute,
                                               includedList: List<FlightSearchIncluded>)
            : Pair<Pair<FlightAirlineModel, FlightAirlineModel>, Pair<FlightAirportModel, FlightAirportModel>> =
            if (route.operatingAirlineId.isNotEmpty()) {
                getAirlineById(route.airlineId, includedList).let { routeAirline ->
                    getAirlineById(route.operatingAirlineId, includedList).let { routeOperatingAirline ->
                        getAirports(route.departureAirportId, route.arrivalAirportId, includedList).let { routeAirportPair ->
                            Pair(Pair(routeAirline, routeOperatingAirline), routeAirportPair)
                        }
                    }
                }
            } else {
                getAirlineById(route.airlineId, includedList).let { routeAirline ->
                    getAirports(route.departureAirportId, route.arrivalAirportId, includedList).let { routeAirportPair ->
                        Pair(Pair(routeAirline, FlightAirlineModel("", "", "", "")), routeAirportPair)
                    }
                }
            }

    private fun isJourneyNeedToUpdate(journeyTable: FlightJourneyTable, comboPrice: Int): Boolean =
            !journeyTable.isBestPairing && (journeyTable.adultNumericCombo == 0 || journeyTable.adultNumericCombo > comboPrice)

    companion object {
        private const val ONWARD_JOURNEY_INDEX = 0
        private const val RETURN_JOURNEY_INDEX = 1
    }
}