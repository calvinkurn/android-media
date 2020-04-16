package com.rahullohra.fakeresponse.domain.usecases

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rahullohra.fakeresponse.Extensions
import com.rahullohra.fakeresponse.GQL_RECORD
import com.rahullohra.fakeresponse.REST_RECORD
import com.rahullohra.fakeresponse.TRANSACTION
import com.rahullohra.fakeresponse.chuck.TransactionEntity
import com.rahullohra.fakeresponse.data.models.Either
import com.rahullohra.fakeresponse.data.parsers.ParserRuleProvider
import com.rahullohra.fakeresponse.db.entities.GqlRecord
import com.rahullohra.fakeresponse.db.entities.RestRecord
import com.rahullohra.fakeresponse.db.entities.toGqlRecord
import com.rahullohra.fakeresponse.db.entities.toRestRecord
import com.rahullohra.fakeresponse.domain.repository.GqlRepository
import com.rahullohra.fakeresponse.domain.repository.RestRepository
import com.rahullohra.fakeresponse.presentation.viewmodels.data.AddGqlData
import com.rahullohra.fakeresponse.presentation.viewmodels.data.AddRestData
import com.rahullohra.fakeresponse.presentation.viewmodels.data.toGqlRecord
import com.rahullohra.fakeresponse.presentation.viewmodels.data.toRestRecord
import org.json.JSONObject

class ImportUseCase(val restRepository: RestRepository, val gqlRepository: GqlRepository) {

    @Throws(Exception::class)
    fun importText(string: String) {
        val either = Extensions.isJSONValid(string)
        if (either is Either.Left) {
            throw either.a
        }

        val json = JSONObject(string)
        val gson = Gson()

        if (json.has(GQL_RECORD)) {
            val gqlJson = json.getJSONArray(GQL_RECORD)
            val gqlRecordType = object : TypeToken<List<GqlRecord>>() {}.type
            val gqlRecordList = gson.fromJson<List<GqlRecord>>(gqlJson.toString(), gqlRecordType)

            gqlRecordList.forEach { record ->
                gqlRepository.addToDb(record.toGqlRecord())
            }
        }

        if (json.has(REST_RECORD)) {
            val restJson = json.getJSONArray(REST_RECORD)

            val restRecordType = object : TypeToken<List<RestRecord>>() {}.type
            val restRecordList = gson.fromJson<List<RestRecord>>(restJson.toString(), restRecordType)

            restRecordList.forEach { record ->
                restRepository.addToDb(record.toRestRecord())
            }
        }

        if (json.has(TRANSACTION)) {

            val parserRuleProvider = ParserRuleProvider()
            val restRecordLastId = restRepository.getLastId()
            val gqlRecordLastId = gqlRepository.getLastId()

            val transactionEntityType = object : TypeToken<List<TransactionEntity>>() {}.type
            val transactionJson = json.getJSONArray(TRANSACTION)
            val transactionList = gson.fromJson<List<TransactionEntity>>(transactionJson.toString(), transactionEntityType)
            transactionList.forEachIndexed { index, transactionEntity ->

                if (transactionEntity.isGql) {
                    val customName = gqlRecordLastId + index
                    transactionEntityToAddGqlData(parserRuleProvider, transactionEntity, customName.toString())?.let { addGqlData ->
                        gqlRepository.addToDb(addGqlData.toGqlRecord())
                    }
                } else {
                    val customName = restRecordLastId + index
                    val addRestData = transactionEntityToAddRestData(transactionEntity, customName.toString())
                    restRepository.addToDb(addRestData.toRestRecord())
                }
            }
        }


    }

    fun transactionEntityToAddRestData(transactionEntity: TransactionEntity, customName: String): AddRestData {
        val url = transactionEntity.url ?: ""
        val method = transactionEntity.method ?: ""
        val responseBody = transactionEntity.responseBody ?: ""
        return AddRestData(url, method, responseBody, customTag = customName)
    }

    fun transactionEntityToAddGqlData(parserRuleProvider: ParserRuleProvider, transactionEntity: TransactionEntity, customName: String): AddGqlData? {

        val gqlName = parserRuleProvider.parse(transactionEntity.requestBody ?: "")
        val response = transactionEntity.responseBody ?: ""
        if (gqlName.isNotEmpty()) {
            return AddGqlData(gqlQueryName = gqlName, response = response, customTag = customName)
        }
        return null
    }
}