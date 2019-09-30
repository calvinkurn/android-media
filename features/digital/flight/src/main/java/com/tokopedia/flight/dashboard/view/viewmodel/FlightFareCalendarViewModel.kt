package com.tokopedia.flight.dashboard.view.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.flight.dashboard.view.model.FlightFareAttributes
import com.tokopedia.flight.dashboard.view.model.FlightFareData
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FlightFareCalendarViewModel @Inject constructor(val dispatcher: CoroutineDispatcher,
                                                      private val gqlRepository: GraphqlRepository)
    : BaseViewModel(dispatcher) {

    val fareFlightCalendarData = MutableLiveData<List<FlightFareAttributes>>()

    fun getFareFlightCalendar(rawQuery: String,
                              mapParam: HashMap<String, kotlin.Any>) {
        launchCatchError(block = {
            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, FlightFareData::class.java, mapParam)
                gqlRepository.getReseponse(listOf(graphqlRequest),
                        GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                                .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * 10).build())
            }.getSuccessData<FlightFareData>()
            fareFlightCalendarData.value = data.flightFare.attributesList
        }) {
            fareFlightCalendarData.value = arrayListOf()
        }
    }

}