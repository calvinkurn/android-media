package com.tokopedia.analyticsdebugger.cassava

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.tokopedia.analyticsdebugger.cassava.validator.core.JsonMap
import java.lang.reflect.Type
import java.math.BigDecimal
import java.net.URLDecoder
import javax.inject.Inject

class AnalyticsMapParser @Inject constructor() {

    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(object : TypeToken<Double>() {}.type, BigDoubleAdapter())
        .disableHtmlEscaping()
        .setPrettyPrinting()
        .create()

    fun parse(data: JsonMap): String {
        return URLDecoder.decode(
            gson.toJson(data)
                .replace("%(?![0-9a-fA-F]{2})".toRegex(), "%25")
                .replace("\\+".toRegex(), "%2B"), "UTF-8"
        )
    }

    fun toJsonMap(str: String): JsonMap {
        val jsonType = object : TypeToken<Map<String, Any>>() {}.type
        return try {
            gson.fromJson(str, jsonType)
        } catch (e: JsonSyntaxException) {
            emptyMap()
        }
    }

    class BigDoubleAdapter : JsonSerializer<Double> {
        override fun serialize(
            src: Double?,
            typeOfSrc: Type?,
            context: JsonSerializationContext?
        ): JsonElement {
            var bigDecimal = BigDecimal.valueOf(src ?: 0.0)
            try {
                bigDecimal = BigDecimal(bigDecimal.toBigIntegerExact())
            } catch (e: ArithmeticException) {
                // ignore
            }
            return JsonPrimitive(bigDecimal)
        }
    }

}