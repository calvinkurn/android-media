package com.tokopedia.fakeresponse.data.parsers.bodyParser

import android.util.Log
import com.tokopedia.fakeresponse.data.parsers.GetResultFromDaoUseCase
import com.tokopedia.fakeresponse.data.parsers.ParserRuleProvider
import com.tokopedia.fakeresponse.db.dao.GqlDao
import com.tokopedia.fakeresponse.db.entities.GqlRecord
import com.tokopedia.fakeresponse.domain.repository.GqlRepository
import org.json.JSONArray

class GqlRequestBodyParser(gqlDao: GqlDao) : BodyParser {
    val parserRuleProvider = ParserRuleProvider()
    val useCase = GetResultFromDaoUseCase(GqlRepository(gqlDao))

    fun parse(requestBody: String): GqlRecord? {
        try {
            val jsonArray = JSONArray(requestBody)
            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)
                val operationName = item.optString("operationName")
                val query = item.optString("query")

                val formattedOperationName = parserRuleProvider.parse(query)
                val fakeResponse = getFakeResponseFromGqlDatabase(formattedOperationName)
                return fakeResponse
            }
        } catch (ex: Exception) {
            Log.e("NooB", ex?.message ?: "")
        }
        return null
    }

    fun getFakeResponseFromGqlDatabase(operationName: String): GqlRecord? {
        return useCase.getResponseFromDao(operationName)
    }
}
