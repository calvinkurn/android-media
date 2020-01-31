package com.tokopedia.flight.search.data.api.single

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.tokopedia.flight.search.data.api.single.response.*
import java.lang.reflect.Type
import javax.inject.Inject

/**
 * Created by Rizky on 29/10/18.
 */
class FlightSearchJsonDeserializer @Inject constructor(val gson: Gson) : JsonDeserializer<FlightDataResponse<List<FlightSearchData>>> {

    private val KEY_DATA = "data"
    private val KEY_META = "meta"
    private val KEY_RELATIONSHIPS = "relationships"
    private val KEY_INCLUDED = "included"
    private val KEY_TYPE = "type"
    private val KEY_TYPE_AIRLINE = "airline"
    private val KEY_TYPE_AIRPORT = "airport"
    private val KEY_ATTRIBUTES = "attributes"
    private val KEY_ID = "id"

    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): FlightDataResponse<List<FlightSearchData>> {
        val flightDataResponse = FlightDataResponse<List<FlightSearchData>>()
        val rootJsonObject = json.asJsonObject
        if (rootJsonObject.has(KEY_DATA)) {
            val listType = object : TypeToken<List<FlightSearchData>>() {}.type
            val data: List<FlightSearchData> = gson.fromJson(rootJsonObject.get(KEY_DATA).toString(), listType)
            flightDataResponse.data = data
        }
        if (rootJsonObject.has(KEY_META)) {
            val meta = gson.fromJson(rootJsonObject.get(KEY_META).toString(), Meta::class.java)
            flightDataResponse.meta = meta
        }
        if (rootJsonObject.has(KEY_INCLUDED)) {
            val includes = rootJsonObject.getAsJsonArray(KEY_INCLUDED)
            val includedEntities = arrayListOf<Included<AttributesInc>>()
            for (include in includes) {
                val includeJsonObject = include.asJsonObject
                val type = object : TypeToken<Included<AttributesAirline>>() {}.type
                val includedEntity: Included<AttributesInc> = gson.fromJson(includeJsonObject, type)
                if (includeJsonObject.get(KEY_TYPE).asString.equals(KEY_TYPE_AIRLINE, ignoreCase = true)) {
                    // airline
                    val attributesAirlineEntity = gson.fromJson(includeJsonObject.getAsJsonObject(KEY_ATTRIBUTES), AttributesAirline::class.java)
                    includedEntity.attributes = attributesAirlineEntity
                } else if (includeJsonObject.get(KEY_TYPE).asString.equals(KEY_TYPE_AIRPORT, ignoreCase = true)) {
                    // airport
                    val attributesAirportEntity = gson.fromJson(includeJsonObject.getAsJsonObject(KEY_ATTRIBUTES), AttributesAirport::class.java)
                    includedEntity.attributes = attributesAirportEntity
                }
                includedEntities.add(includedEntity)
            }
            flightDataResponse.included = includedEntities
        }
        return flightDataResponse
    }

}