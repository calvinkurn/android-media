package com.tokopedia.flight.searchV2.data.db

import android.arch.persistence.room.Entity

/**
 * Created by Rizky on 21/09/18.
 */
@Entity(
        primaryKeys = ["id"]
)
data class Journey(
        val id: String,
        val term: String,
        val departureAirport: String,
        val arrivalAirport: String,
        val departureTime: String,
        val arrivalTime: String,
        val totalTransit: Int,
        val totalStop: Int,
        val addDayArrival: Int,
        val duration: String,
        val durationMinute: Int,
        val total: String,
        val totalNumeric: Int,
        val beforeTotal: String,
        val routes: List<Route>
)