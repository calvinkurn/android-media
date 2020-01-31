package com.rahullohra.fakeresponse.domain.repository

import com.rahullohra.fakeresponse.db.dao.RestDao
import com.rahullohra.fakeresponse.db.entities.RestResponse

class RestRepository(val dao: RestDao) : BaseRepository {

    fun addToDb(restResponse: RestResponse): Long {
        return dao.insert(restResponse)
    }

    fun getAll(): List<RestResponse> {
        return dao.getAll()
    }

    fun deleteAllRecords() {
        return dao.deleteAll()
    }

    fun toggleRestRecord(restRecord: Int, enable: Boolean) {
        return dao.toggle(restRecord, enable)
    }

    fun getResponse(url: String, method:String, enable: Boolean): RestResponse {
        return dao.getRestResponse(url, method, enable)
    }

}