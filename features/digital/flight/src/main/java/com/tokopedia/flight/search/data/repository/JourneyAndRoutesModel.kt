package com.tokopedia.flight.search.data.repository

import com.tokopedia.flight.search.data.db.JourneyAndRoutes

/**
 * @author by jessica on 06/03/19
 */

data class JourneyAndRoutesModel (
        val journeyAndRoutes: List<JourneyAndRoutes>,
        val specialTag: String
)