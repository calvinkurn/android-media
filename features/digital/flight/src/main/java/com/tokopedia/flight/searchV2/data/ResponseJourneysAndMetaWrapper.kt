package com.tokopedia.flight.searchV2.data

import com.tokopedia.flight.search.data.cloud.model.response.Meta
import com.tokopedia.flight.searchV2.data.db.JourneyAndRoutesJava

/**
 * Created by Rizky on 01/10/18.
 */
class ResponseJourneysAndMetaWrapper(val flightJourneys: List<JourneyAndRoutesJava>, val meta: Meta)