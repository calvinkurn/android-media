package com.tokopedia.flight.searchV2.data.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

/**
 * Created by Rizky on 24/09/18.
 */
@Dao
interface ComboDao {

    @Insert
    fun insert(combo: Combo)

    @Query("SELECT * FROM Combo WHERE Combo.journeyId = :journeyId")
    fun findCombosByJourneyId(journeyId: String): List<Combo>

}