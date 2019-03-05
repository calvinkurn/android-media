package com.tokopedia.flight.bookingV2.data

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.tokopedia.flight.booking.data.cloud.entity.InsuranceEntity
import com.tokopedia.flight.bookingV2.data.entity.GetCartEntity
import com.tokopedia.flight.search.data.api.single.response.AttributesAirport
import com.tokopedia.flight.search.data.api.single.response.Meta
import java.lang.reflect.Type
import javax.inject.Inject

/**
 * @author by furqan on 04/03/19
 */
class FlightCartJsonDeserializer @Inject constructor(private val gson: Gson) : JsonDeserializer<GetCartEntity> {

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): GetCartEntity {
        var getCartEntity = GetCartEntity()
        if (json != null) {
            val rootJsonObject = json.asJsonObject
            if (rootJsonObject.has(KEY_DATA)) {
                // data
                val dataObject = rootJsonObject.getAsJsonObject(KEY_DATA)
                getCartEntity = gson.fromJson(rootJsonObject.get(KEY_DATA).toString(), GetCartEntity::class.java)
                if (dataObject.has(KEY_RELATIONSHIPS)) {
                    val insuranceEntities = arrayListOf<InsuranceEntity>()
                    val airportEntities = hashMapOf<String, AttributesAirport>()
                    if (rootJsonObject.has(KEY_INCLUDED)) {
                        val includes = rootJsonObject.getAsJsonArray(KEY_INCLUDED)
                        for (item in includes) {
                            val jsonObj = item.asJsonObject
                            if (jsonObj.get(KEY_TYPE).asString.equals(KEY_INSURANCE, true)) {
                                // insurance
                                val insuranceEntity = gson.fromJson(jsonObj.get(KEY_ATTRIBUTES).toString(), InsuranceEntity::class.java)
                                insuranceEntity.id = jsonObj.get(KEY_ID).asString
                                insuranceEntities.add(insuranceEntity)
                            }
                            if (jsonObj.get(KEY_TYPE).asString.equals(KEY_AIRPORT, true)) {
                                // airport
                                val airportEntity = gson.fromJson(jsonObj.get(KEY_ATTRIBUTES).toString(), AttributesAirport::class.java)
                                airportEntities[jsonObj.get(KEY_ID).asString] = airportEntity
                            }
                        }
                        getCartEntity.attributes.insurances = insuranceEntities

                        for (item in getCartEntity.attributes.flight.journeys) {
                            if (airportEntities.containsKey(item.departureId)) {
                                item.departureAirportName = airportEntities[item.departureId]?.name ?: ""
                            }
                            if (airportEntities.containsKey(item.arrivalId)) {
                                item.arrivalAirportName = airportEntities[item.arrivalId]?.name ?: ""
                            }
                        }
                    }
                }
            }
            if (rootJsonObject.has(KEY_META)) {
                // meta
                getCartEntity.meta = gson.fromJson(rootJsonObject.get(KEY_META).toString(), Meta::class.java)
            }
        }
        return getCartEntity
    }

    companion object {
        private val KEY_DATA = "data"
        private val KEY_RELATIONSHIPS = "relationships"
        private val KEY_INCLUDED = "included"
        private val KEY_TYPE = "type"
        private val KEY_INSURANCE = "insurance_benefit"
        private val KEY_ATTRIBUTES = "attributes"
        private val KEY_ID = "id"
        private val KEY_AIRPORT = "airport"
        private val KEY_META = "meta"
    }
}