package com.tokopedia.flight.searchV2.data.db

import android.arch.persistence.room.Query

/**
 * Created by Rizky on 21/09/18.
 */
interface RouteDao {

    @Query("SELECT * FROM Route WHERE Route.journeyId = :journeyId")
    fun getRoutesByJourneyId(journeyId: String): List<Route>

}