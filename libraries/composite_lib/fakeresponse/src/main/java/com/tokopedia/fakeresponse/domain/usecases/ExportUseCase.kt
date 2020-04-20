package com.tokopedia.fakeresponse.domain.usecases

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.tokopedia.fakeresponse.GQL_RECORD
import com.tokopedia.fakeresponse.REST_RECORD
import com.tokopedia.fakeresponse.data.models.ResponseItemType
import com.tokopedia.fakeresponse.db.entities.GqlRecord
import com.tokopedia.fakeresponse.db.entities.RestRecord
import com.tokopedia.fakeresponse.domain.repository.GqlRepository
import com.tokopedia.fakeresponse.domain.repository.RestRepository

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