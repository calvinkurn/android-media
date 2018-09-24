package com.tokopedia.flight.searchV2.data.db

import android.arch.persistence.room.Entity
import com.google.gson.annotations.SerializedName

/**
 * Created by Rizky on 21/09/18.
 */
@Entity(
        primaryKeys = ["journeyId"]
)
data class Journey(
        val journeyId: String,
        @field:SerializedName("name")
        val name: String,
        @field:SerializedName("email")
        val email: String
)