package com.tokopedia.flight.searchV2.data.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import com.google.gson.annotations.SerializedName

/**
 * Created by Rizky on 21/09/18.
 */
@Entity(foreignKeys = [
        ForeignKey(
                entity = Journey::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("journeyId"),
                onDelete = CASCADE
        )])
data class Route(
        val journeyId: String,
        val airline: String,
        val departureAirport: String,
        val arrivalAirport: String,
        val departureTimestamp: String,
        val arrivalTimestamp: String,
        val duration: String,
        val layover: String,
        val flightNumber: String,
        val isRefundable: Boolean,
        val stops: Int
)