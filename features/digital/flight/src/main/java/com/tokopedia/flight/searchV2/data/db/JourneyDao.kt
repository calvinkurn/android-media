package com.tokopedia.flight.searchV2.data.db

import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction

/**
 * Created by Rizky on 21/09/18.
 */
interface JourneyDao {

    @Query("SELECT * FROM Journey")
    fun getAllJourneys()

    @Transaction
    @Query("SELECT * FROM Journey")
    fun getJourneys(): List<JourneyAndRoutes>

}