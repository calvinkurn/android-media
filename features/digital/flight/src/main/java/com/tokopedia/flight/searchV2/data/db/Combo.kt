package com.tokopedia.flight.searchV2.data.db

import android.arch.persistence.room.Entity
import com.google.gson.annotations.SerializedName

/**
 * Created by Rizky on 21/09/18.
 */
@Entity
data class Combo(
        val comboId: String,
        val journeyId: String,
        val adultPrice: String,
        val childPrice: String,
        val infantPrice: String,
        val adultPriceNumeric: Int,
        val childPriceNumeric: Int,
        val infantPriceNumeric: Int
)