package com.tokopedia.travel.passenger.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author by furqan on 02/01/2020
 */
@Entity
class TravelPassengerTable(
        @PrimaryKey
        var idPassenger: String = "",
        var id: String = "",
        var userId: Int = 0,
        var title: Int = 0,
        var idNumber: String = "",
        @ColumnInfo(name = "name")
        var namePassenger: String = "",
        var firstName: String = "",
        var lastName: String = "",
        var birthDate: String = "",
        var nationality: String = "",
        var passportNo: String = "",
        var passportCountry: String = "",
        var passportExpiry: String = "",
        var isBuyer: Int = 0,
        var paxType: Int = 0,
        var travelId: Int = 0,
        var selected: Int = 0
)