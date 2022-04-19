package com.tokopedia.flight.search.data.cloud.combine

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.flight.search.data.cloud.single.FlightSearchErrorEntity
import com.tokopedia.flight.search.data.cloud.single.FlightSearchMetaEntity

/**
 * @author by furqan on 06/04/2020
 */
class FlightSearchCombineEntity(
        @SerializedName("data")
        @Expose
        val data: FlightCombineData = FlightCombineData(),
        @SerializedName("meta")
        @Expose
        val meta: FlightSearchMetaEntity = FlightSearchMetaEntity(),
        @SerializedName("error")
        @Expose
        val error: List<FlightSearchErrorEntity> = arrayListOf()
) {
    class Response(
            @SerializedName("flightSearchCombineV3")
            @Expose
            val flightSearchCombine: FlightSearchCombineEntity = FlightSearchCombineEntity()
    )
}

class FlightCombineData(
        @SerializedName("journeys")
        @Expose
        val journeys: List<List<FlightCombineJourney>> = arrayListOf(),
        @SerializedName("combos")
        @Expose
        val combos: List<FlightCombineCombo> = arrayListOf()
)

class FlightCombineJourney(
        @SerializedName("journeyID")
        @Expose
        val journeyId: String = ""
)

class FlightCombineCombo(
        @SerializedName("fares")
        @Expose
        val fares: List<FlightCombineFare> = arrayListOf(),
        @SerializedName("combination")
        @Expose
        val combination: String = "",
        @SerializedName("comboKey")
        @Expose
        val comboKey: String = "",
        @SerializedName("isBestPairing")
        @Expose
        val isBestPairing: Boolean = false,
        @SerializedName("total")
        @Expose
        val totalPrice: String = "",
        @SerializedName("totalNumeric")
        @Expose
        val totalPriceNumeric: Int = 0
)

class FlightCombineFare(
        @SerializedName("adultPrice")
        @Expose
        val adultPrice: String = "",
        @SerializedName("childPrice")
        @Expose
        val childPrice: String = "",
        @SerializedName("infantPrice")
        @Expose
        val infantPrice: String = "",
        @SerializedName("adultPriceNumeric")
        @Expose
        val adultPriceNumeric: Int = 0,
        @SerializedName("childPriceNumeric")
        @Expose
        val childPriceNumeric: Int = 0,
        @SerializedName("infantPriceNumeric")
        @Expose
        val infantPriceNumeric: Int = 0
)