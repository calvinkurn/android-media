package com.tokopedia.flight.search.data.cache.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Furqan on 05/11/20.
 */
@Entity
class FlightComboTable(@PrimaryKey val comboId: String = "",
                       val onwardJourneyId: String = "",
                       val onwardAdultPrice: String = "",
                       val onwardChildPrice: String = "",
                       val onwardInfantPrice: String = "",
                       val onwardAdultPriceNumeric: Int = 0,
                       val onwardChildPriceNumeric: Int = 0,
                       val onwardInfantPriceNumeric: Int = 0,
                       val returnJourneyId: String = "",
                       val returnAdultPrice: String = "",
                       val returnChildPrice: String = "",
                       val returnInfantPrice: String = "",
                       val returnAdultPriceNumeric: Int = 0,
                       val returnChildPriceNumeric: Int = 0,
                       val returnInfantPriceNumeric: Int = 0,
                       val isBestPairing: Boolean = false)