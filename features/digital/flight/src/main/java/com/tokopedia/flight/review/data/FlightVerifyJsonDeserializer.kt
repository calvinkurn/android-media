package com.tokopedia.flight.review.data

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.tokopedia.flight.review.domain.verifybooking.model.response.DataResponseVerify
import com.tokopedia.flight.search.data.api.single.response.Meta
import java.lang.reflect.Type
import javax.inject.Inject

/**
 * @author by furqan on 15/03/19
 */
class FlightVerifyJsonDeserializer @Inject constructor(private val gson: Gson) : JsonDeserializer<DataResponseVerify> {

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): DataResponseVerify {
        var dataResponseVerify = DataResponseVerify()

        if (json != null) {
            val rootJsonObject = json.asJsonObject
            if (rootJsonObject.has(KEY_DATA)) {
                val dataObject = rootJsonObject.getAsJsonObject(KEY_DATA)
                dataResponseVerify = gson.fromJson(rootJsonObject.get(KEY_DATA).toString(), DataResponseVerify::class.java)

            }
            if (rootJsonObject.has(KEY_META)) {
                val meta = rootJsonObject.getAsJsonObject(KEY_META)
                dataResponseVerify.meta = gson.fromJson(rootJsonObject.get(KEY_META).toString(), Meta::class.java)
            }
        }

        return dataResponseVerify
    }

    companion object {
        private val KEY_DATA = "data"
        private val KEY_META = "meta"
    }
}