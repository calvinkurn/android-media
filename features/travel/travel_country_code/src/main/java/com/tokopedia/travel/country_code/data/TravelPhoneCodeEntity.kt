package com.tokopedia.travel.country_code.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 23/12/2019
 */

class TravelPhoneCodeEntity(
        @SerializedName("countries")
        @Expose
        val countries: List<TravelPhoneCodeCountry> = arrayListOf()
) {
    class Response(
            @SerializedName("TravelGetAllCountries")
            @Expose
            val travelGetAllCountries: TravelPhoneCodeEntity = TravelPhoneCodeEntity()
    )
}

class TravelPhoneCodeCountry(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("attributes")
        @Expose
        val attributes: TravelPhoneCodeAttribute = TravelPhoneCodeAttribute()
)

class TravelPhoneCodeAttribute(
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("currency")
        @Expose
        val currency: String = "",
        @SerializedName("phoneCode")
        @Expose
        val phoneCode: Int = 0
)