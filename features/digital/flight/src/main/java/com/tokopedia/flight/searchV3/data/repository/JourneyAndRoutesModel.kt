package com.tokopedia.flight.searchV3.data.repository

import com.tokopedia.flight.searchV3.data.db.JourneyAndRoutes

/**
 * @author by jessica on 06/03/19
 */

data class JourneyAndRoutesModel (
        val journeyAndRoutes: List<JourneyAndRoutes>,
        val specialTag: String
)