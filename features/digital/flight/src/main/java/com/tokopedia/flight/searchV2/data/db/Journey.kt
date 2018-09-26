package com.tokopedia.flight.searchV2.data.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Rizky on 21/09/18.
 */
@Entity
data class Journey(
        @PrimaryKey
        val id: String,
        val term: String,
        val departureAirport: String,
        val departureTime: String,
        val departureTimeInt: Int,
        val arrivalAirport: String,
        val arrivalTime: String,
        val arrivalTimeInt: Int,
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