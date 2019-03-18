package com.tokopedia.flight.passenger.data.db

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by nabillasabbaha on 12/03/19.
 */
@Entity
class FlightPassengerTable(
        @PrimaryKey
        @ColumnInfo(name = "id")
        var passengerId: String,

        @ColumnInfo(name = "first_name")
        var firstName: String? = null,

        @ColumnInfo(name = "last_name")
        var lastName: String? = null,

        @ColumnInfo(name = "birthdate")
        var birthdate: String? = null,

        @ColumnInfo(name = "title_id")
        var titleId: Int = 0,

        @ColumnInfo(name = "is_selected")
        var isSelected: Int = 0,

        @ColumnInfo(name = "nationality")
        var passportNationality: String? = null,

        @ColumnInfo(name = "passport_country")
        var passportCountry: String? = null,

        @ColumnInfo(name = "passport_expiry")
        var passportExpiry: String? = null,

        @ColumnInfo(name = "passport_no")
        var passportNo: String? = null)
