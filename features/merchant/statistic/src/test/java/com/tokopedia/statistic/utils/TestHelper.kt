package com.tokopedia.statistic.utils

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import java.io.File
import java.lang.reflect.Type

/**
 * Created By @ilhamsuaib on 12/08/20
 */

object TestHelper {

    const val JSON_KEY_DATA = "data"

    fun getJsonFromFile(path: String): String {
        val uri = ClassLoader.getSystemClassLoader().getResource(path)
        val file = File(uri.path)
        return String(file.readBytes())
    }

    inline fun <reified T> parseJson(json: String): T {
        return Gson().fromJson(json, T::class.java)
    }

    inline fun<reified T: Any> createSuccessResponse(jsonPath: String): GraphqlResponse {
        val mockJsonResponse = getJsonFromFile(jsonPath)
        val response: JsonObject = parseJson(mockJsonResponse)
        val jsonData = response.get(JSON_KEY_DATA).toString()
        val data: T = parseJson(jsonData)
        val result = mapOf<Type, Any>(T::class.java to data)
        val error = mapOf<Type, List<GraphqlError>>(T::class.java to emptyList())
        return GraphqlResponse(result, error, false)
    }

    inline fun<reified T: Any> createErrorResponse(): GraphqlResponse {
        val errorResponse = GraphqlError().apply {
            message = "error message"
        }
        val result = mapOf<Type, Any?>(T::class.java to null)
        val error = mapOf<Type, List<GraphqlError>>(T::class.java to listOf(errorResponse))
        return GraphqlResponse(result, error, false)
    }
}