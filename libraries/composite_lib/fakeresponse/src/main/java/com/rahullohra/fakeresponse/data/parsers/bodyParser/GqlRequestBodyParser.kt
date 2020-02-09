package com.rahullohra.fakeresponse.data.parsers.bodyParser

import android.util.Log
import com.rahullohra.fakeresponse.data.parsers.GetResultFromDaoUseCase
import com.rahullohra.fakeresponse.data.parsers.ParserRuleProvider
import com.rahullohra.fakeresponse.db.dao.GqlDao
import com.rahullohra.fakeresponse.domain.repository.LocalRepository
import org.json.JSONArray

class GqlRequestBodyParser(gqlDao: GqlDao) : BodyParser {
    val parserRuleProvider = ParserRuleProvider()
    val useCase = GetResultFromDaoUseCase(LocalRepository(gqlDao))

    fun parse(requestBody: String): String? {
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
            Log.e("NooB", ex.message)
        }
        return null
    }

    fun getFakeResponseFromGqlDatabase(operationName: String): String? {
        return useCase.getResponseFromDao(operationName)
    }
}
