package com.tokopedia.flight.country.database

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by nabillasabbaha on 12/03/19.
 */
@Entity
class CountryPhoneCodeTable(
        @PrimaryKey
        @ColumnInfo(name = "country_id")
        var countryId: String = "",

        @ColumnInfo(name = "country_name")
        var countryName: String = "",

        @ColumnInfo(name = "phone_code")
        var phoneCode: Long = 0)
