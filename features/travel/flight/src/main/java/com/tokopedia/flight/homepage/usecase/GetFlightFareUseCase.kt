package com.tokopedia.flight.homepage.usecase

import com.tokopedia.flight.homepage.data.cache.FlightDashboardGQLQueries.FLIGHT_FARE_CALENDAR_QUERY
import com.tokopedia.flight.homepage.presentation.model.FlightFareAttributes
import com.tokopedia.flight.homepage.presentation.model.FlightFareData
import com.tokopedia.flight.homepage.presentation.widget.FlightCalendarOneWayWidget
import com.tokopedia.flight.homepage.presentation.widget.FlightCalendarRoundTripWidget
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.travelcalendar.TRAVEL_CAL_YYYY
import com.tokopedia.travelcalendar.dateToString
import com.tokopedia.usecase.coroutines.UseCase
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * @author by jessica on 22/10/20
 */

class GetFlightFareUseCase @Inject constructor(private val gqlRepository: GraphqlRepository)
    : UseCase<ArrayList<FlightFareAttributes>>(){

    var params: HashMap<String, Any> = hashMapOf()
    var minDate: Date = Date()
    var maxDate: Date = Date()

    override suspend fun executeOnBackground(): ArrayList<FlightFareAttributes> {
        val attributes = arrayListOf<FlightFareAttributes>()

        val minCalendar = Calendar.getInstance()
        minCalendar.time = minDate
        val minYear = minCalendar.get(Calendar.YEAR)

        val maxCalendar = Calendar.getInstance()
        maxCalendar.time = maxDate
        val maxYear = maxCalendar.get(Calendar.YEAR)

        val diffYear = (maxYear - minYear)
        for (i in 0..diffYear) {
            val graphqlRequest = GraphqlRequest(FLIGHT_FARE_CALENDAR_QUERY, FlightFareData::class.java, params)
            val gqlResponse = gqlRepository.getReseponse(listOf(graphqlRequest),
                    GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                            .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * 10).build())

            val errors = gqlResponse.getError(FlightFareData::class.java)
            if (!errors.isNullOrEmpty()) {
                throw MessageErrorException(errors[0].message)
            } else {
                val data = gqlResponse.getData<FlightFareData>(FlightFareData::class.java)
                attributes.addAll(data.flightFare.attributesList)
            }

            minCalendar.add(Calendar.YEAR, 1)
            val minDateUpdate = minCalendar.time
            params[FlightCalendarOneWayWidget.PARAM_YEAR] = minDateUpdate.dateToString(TRAVEL_CAL_YYYY)
        }
        return attributes
    }

    fun createReturnParam(mapParam: HashMap<String, Any>) {
        val minCalendar = Calendar.getInstance()
        minCalendar.time = minDate
        val mapReturnFareParam = mapParam.clone() as HashMap<String, Any>
        mapReturnFareParam[FlightCalendarRoundTripWidget.PARAM_DEPARTURE_CODE] = mapParam[FlightCalendarRoundTripWidget.PARAM_ARRIVAL_CODE].toString()
        mapReturnFareParam[FlightCalendarRoundTripWidget.PARAM_ARRIVAL_CODE] = mapParam[FlightCalendarRoundTripWidget.PARAM_DEPARTURE_CODE].toString()
        mapReturnFareParam[FlightCalendarRoundTripWidget.PARAM_YEAR] = minCalendar.time.dateToString(TRAVEL_CAL_YYYY)
        params = mapReturnFareParam
    }
}