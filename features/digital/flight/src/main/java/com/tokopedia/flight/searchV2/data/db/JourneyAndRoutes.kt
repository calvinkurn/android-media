package com.tokopedia.flight.searchV2.data.db

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

/**
 * Created by Rizky on 26/09/18.
 */
class JourneyAndRoutes {

    @Embedded
    var journey: Journey? = null

    @Relation(parentColumn = "id",
            entityColumn = "journeyId")
    var routes: List<Route> = ArrayList()

}