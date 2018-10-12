package com.tokopedia.flight.searchV2.data

import com.tokopedia.flight.search.data.cloud.model.response.FlightDataResponse
import com.tokopedia.flight.search.data.cloud.model.response.FlightSearchData
import com.tokopedia.flight.search.data.cloud.model.response.Meta
import com.tokopedia.flight.searchV2.data.db.JourneyAndRoutes

/**
 * Created by Rizky on 01/10/18.
 */
class ResponseJourneysAndMetaWrapper(val response: FlightDataResponse<List<FlightSearchData>>, val meta: Meta)