package com.tokopedia.analyticsdebugger.cassava

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.tokopedia.analyticsdebugger.AnalyticsSource
import com.tokopedia.analyticsdebugger.cassava.validator.core.JsonMap
import java.lang.reflect.Type
import java.math.BigDecimal
import java.net.URLDecoder
import javax.inject.Inject

class AnalyticsParser @Inject constructor() {

    private val gson: Gson = GsonBuilder()
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

    fun inferName(data: Map<String, Any>, @AnalyticsSource source: String): String {
       return runCatching {
           when(source) {
               AnalyticsSource.GTM -> data["event"].toString()
               AnalyticsSource.BRANCH_IO -> data["eventName"].toString()
               AnalyticsSource.ERROR -> "ERROR GTM V5"
               else -> ""
           }
       }.getOrElse { "" }
    }

    fun toJsonMap(str: String): JsonMap {
        val jsonType = object : TypeToken<Map<String, Any>>() {}.type
        return try {
            gson.fromJson(str, jsonType)
        } catch (e: JsonSyntaxException) {
            emptyMap()
        }
    }

    /**
     * This adapter will converts big decimal such as 2000000000.0 to 2000000000 instead of 2.0E9
     * Currently, not being used
     * */
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