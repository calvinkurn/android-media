package com.tokopedia.common.travel.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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
