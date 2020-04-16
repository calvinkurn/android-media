package com.rahullohra.fakeresponse.domain.usecases

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.rahullohra.fakeresponse.GQL_RECORD
import com.rahullohra.fakeresponse.REST_RECORD
import com.rahullohra.fakeresponse.data.models.ResponseItemType
import com.rahullohra.fakeresponse.db.entities.GqlRecord
import com.rahullohra.fakeresponse.db.entities.RestRecord
import com.rahullohra.fakeresponse.domain.repository.GqlRepository
import com.rahullohra.fakeresponse.domain.repository.RestRepository

class ExportUseCase(val restRepository: RestRepository, val gqlRepository: GqlRepository) {

    fun export(id: Int, responseItemType: ResponseItemType): String {

        val gqlRecordList = arrayListOf<GqlRecord>()
        val restRecordList = arrayListOf<RestRecord>()

        if (responseItemType == ResponseItemType.REST) {
            restRecordList.add(restRepository.getResponse(id))
        } else {
            gqlRecordList.add(gqlRepository.getGqlRecord(id))
        }

        val gson = Gson()
        val json = JsonObject()

        json.add(GQL_RECORD, gson.toJsonTree(gqlRecordList))
        json.add(REST_RECORD, gson.toJsonTree(restRecordList))
        return json.toString()
    }
}