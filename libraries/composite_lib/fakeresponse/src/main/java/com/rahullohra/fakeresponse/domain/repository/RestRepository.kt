package com.rahullohra.fakeresponse.domain.repository

import com.rahullohra.fakeresponse.db.dao.RestDao
import com.rahullohra.fakeresponse.db.entities.RestRecord

class RestRepository(val dao: RestDao) : BaseRepository {

    fun addToDb(restRecord: RestRecord): Long {
        return dao.insert(restRecord)
    }

    fun getAll(): List<RestRecord> {
        return dao.getAll()
    }

    fun deleteAllRecords() {
        return dao.deleteAll()
    }

    fun toggleRestRecord(restRecord: Int, enable: Boolean) {
        return dao.toggle(restRecord, enable)
    }

    fun getResponse(url: String, method:String, enable: Boolean): RestRecord {
        return dao.getRestResponse(url, method, enable)
    }

    fun updateResponse(restRecord: RestRecord) {
        return dao.update(restRecord)
    }

    fun getResponse(id: Int): RestRecord {
        return dao.getRestResponse(id)
    }

}