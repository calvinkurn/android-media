package com.rahullohra.fakeresponse.domain.repository

import com.rahullohra.fakeresponse.db.dao.GqlDao
import com.rahullohra.fakeresponse.db.entities.GqlRecord

class LocalRepository (val dao: GqlDao) :BaseRepository{

    fun addToDb(gqlRecord: GqlRecord): Long {
        return dao.insertGql(gqlRecord)
    }

    fun getAllGql(): List<GqlRecord> {
        return dao.getAll()
    }

    fun deleteAllRecords() {
        return dao.deleteAll()
    }

    fun toggleGqlRecord(gqlRecord: Int, enable: Boolean) {
        return dao.toggleGql(gqlRecord, enable)
    }

    fun getGqlQueryResponse(gqlQuery:String, enable: Boolean) :GqlRecord{
        return dao.getRecordFromGqlQuery(gqlQuery, enable)
    }

    fun getGqlRecord(id: Int): GqlRecord {
        return dao.getRecordFromGqlQuery(id)
    }

    fun updateResponse(gqlRecord: GqlRecord) {
        return dao.updateGql(gqlRecord)
    }
}