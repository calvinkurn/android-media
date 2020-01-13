package com.rahullohra.fakeresponse.domain.repository

import com.rahullohra.fakeresponse.db.dao.GqlDao
import com.rahullohra.fakeresponse.db.entities.FakeGql

class LocalRepository (val dao: GqlDao) :BaseRepository{

    fun addToDb(fakeGql: FakeGql): Long {
        return dao.insertGql(fakeGql)
    }

    fun getAllGql(): List<FakeGql> {
        return dao.getAll()
    }

    fun deleteAllRecords() {
        return dao.deleteAll()
    }

    fun toggleGqlRecord(gqlRecord: Int, enable: Boolean) {
        return dao.toggleGql(gqlRecord, enable)
    }

    fun getGqlQueryResponse(gqlQuery:String, enable: Boolean) :FakeGql{
        return dao.getRecordFromGqlQuery(gqlQuery, enable)
    }
}