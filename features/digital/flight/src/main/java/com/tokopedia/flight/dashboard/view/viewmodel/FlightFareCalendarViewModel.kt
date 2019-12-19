package com.tokopedia.flight.dashboard.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.flight.dashboard.view.model.FlightFareAttributes
import com.tokopedia.flight.dashboard.view.model.FlightFareData
import com.tokopedia.flight.dashboard.view.widget.FlightCalendarOneWayWidget
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.travelcalendar.TRAVEL_CAL_YYYY
import com.tokopedia.travelcalendar.dateToString
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class FlightFareCalendarViewModel @Inject constructor(dispatcher: CoroutineDispatcher,
                                                      private val gqlRepository: GraphqlRepository)
    : BaseViewModel(dispatcher) {

    val fareFlightCalendarData = MutableLiveData<List<FlightFareAttributes>>()

    fun getFareFlightCalendar(rawQuery: String,
                              mapParam: HashMap<String, Any>,
                              minDate: Date, maxDate: Date) {
        launchCatchError(block = {
            val attributes = ArrayList<FlightFareAttributes>()
            val minCalendar = Calendar.getInstance()
            minCalendar.time = minDate
            val minYear = minCalendar.get(Calendar.YEAR)

            val maxCalendar = Calendar.getInstance()
            maxCalendar.time = maxDate
            val maxYear = maxCalendar.get(Calendar.YEAR)

            val diffYear = (maxYear - minYear)

            for (i in 0..diffYear) {
                val data = withContext(Dispatchers.Default) {
                    val graphqlRequest = GraphqlRequest(rawQuery, FlightFareData::class.java, mapParam)
                    gqlRepository.getReseponse(listOf(graphqlRequest),
                            GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                                    .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * 10).build())
                }.getSuccessData<FlightFareData>()
                attributes.addAll(data.flightFare.attributesList)

                minCalendar.add(Calendar.YEAR, 1)
                val minDateUpdate = minCalendar.time
                mapParam[FlightCalendarOneWayWidget.PARAM_YEAR] = minDateUpdate.dateToString(TRAVEL_CAL_YYYY)
            }

            fareFlightCalendarData.value = attributes

        }) {
            fareFlightCalendarData.value = arrayListOf()
        }

    }

}