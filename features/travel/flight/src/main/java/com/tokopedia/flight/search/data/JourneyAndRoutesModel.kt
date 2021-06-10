package com.tokopedia.flight.search.data

import com.tokopedia.flight.search.data.cache.db.JourneyAndRoutes

/**
 * @author by jessica on 06/03/19
 */

data class JourneyAndRoutesModel (
        val journeyAndRoutes: List<JourneyAndRoutes>,
        val specialTag: String
)