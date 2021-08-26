package com.tokopedia.analyticsdebugger.cassava

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import java.lang.ArithmeticException
import java.lang.reflect.Type
import java.math.BigDecimal
import java.net.URLDecoder

class AnalyticsMapParser {

    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(object : TypeToken<Double>() {}.type, object : JsonSerializer<Double> {
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
        }).disableHtmlEscaping().setPrettyPrinting().create()

    fun parse(data: Map<String, Any>): String {
        return URLDecoder.decode(
            gson.toJson(data)
                .replace("%(?![0-9a-fA-F]{2})".toRegex(), "%25")
                .replace("\\+".toRegex(), "%2B"), "UTF-8"
        )
    }

}