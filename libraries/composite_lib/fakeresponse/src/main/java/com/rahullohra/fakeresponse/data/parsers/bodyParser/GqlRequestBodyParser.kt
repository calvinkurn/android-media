package com.rahullohra.fakeresponse.data.parsers.bodyParser

import android.text.TextUtils
import android.util.Log
import com.rahullohra.fakeresponse.data.parsers.GetResultFromDaoUseCase
import com.rahullohra.fakeresponse.data.parsers.ParserFactory
import com.rahullohra.fakeresponse.data.parsers.rules.GqlParserRule
import com.rahullohra.fakeresponse.db.dao.GqlDao
import com.rahullohra.fakeresponse.domain.repository.LocalRepository
import org.json.JSONArray

class GqlRequestBodyParser(gqlDao: GqlDao) : BodyParser {
    val parserFactory = ParserFactory()
    val useCase =
        GetResultFromDaoUseCase(LocalRepository(gqlDao))

    fun parse(requestBody: String, responseBody: String?):String? {
        try {
            val jsonArray = JSONArray(requestBody)
            for (i in 0 until jsonArray.length()) {
                responseBody
                val item = jsonArray.getJSONObject(i)
                val operationName = item.optString("operationName")
                val query = item.optString("query")

                val formattedOperationName = getFormattedOperationNameNew(parserFactory, query)
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

    fun getFormattedOperationName(parserFactory: ParserFactory, rawQuery: String): String {
        return parserFactory.getSimpleParser().parse(rawQuery)
    }



    fun getFormattedOperationNameNew(parserFactory: ParserFactory, rawQuery: String): String {
        var operationName = parserFactory.getMappedParser().parse(rawQuery)
        if(!TextUtils.isEmpty(operationName)){
            return operationName
        }

        operationName = parserFactory.getSimpleParser().parse(rawQuery)
        if(!TextUtils.isEmpty(operationName)){
            return operationName
        }

        operationName = parserFactory.getNestedQueryParser().parse(rawQuery)
        if(!TextUtils.isEmpty(operationName)){
            return operationName
        }
        return ""
    }

    val authentication = fun(next: GqlParserRule) =
        fun(request: String): String {
            if (!true) {
                throw IllegalArgumentException()
            }
            return next.parse(request)
        }

    val basicAuthentication = fun(next: GqlParserRule) =
        fun(request: String): String {
            if (!true) {
                throw IllegalArgumentException()
            }
            return next.parse(request)
        }

}
