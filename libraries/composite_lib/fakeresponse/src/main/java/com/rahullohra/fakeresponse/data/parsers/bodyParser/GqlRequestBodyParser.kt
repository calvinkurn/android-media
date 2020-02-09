package com.rahullohra.fakeresponse.data.parsers.bodyParser

import android.text.TextUtils
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

                val formattedOperationName = getFormattedOperationNameNew(parserRuleProvider, query)
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

    fun getFormattedOperationName(parserRuleProvider: ParserRuleProvider, rawQuery: String): String {
        return parserRuleProvider.getSimpleParser().parse(rawQuery)
    }


    fun getFormattedOperationNameNew(parserRuleProvider: ParserRuleProvider, rawQuery: String): String {
        var operationName = parserRuleProvider.getMappedParser().parse(rawQuery)
        if (!TextUtils.isEmpty(operationName)) {
            return operationName
        }

        operationName = parserRuleProvider.getSimpleParser().parse(rawQuery)
        if (!TextUtils.isEmpty(operationName)) {
            return operationName
        }

        operationName = parserRuleProvider.getNestedQueryParser().parse(rawQuery)
        if (!TextUtils.isEmpty(operationName)) {
            return operationName
        }
        return ""
    }

}
