package com.tokopedia.flight.searchV4.data

import com.tokopedia.flight.searchV4.data.cache.db.JourneyAndRoutes

/**
 * @author by jessica on 06/03/19
 */

data class JourneyAndRoutesModel (
        val journeyAndRoutes: List<JourneyAndRoutes>,
        val specialTag: String
)