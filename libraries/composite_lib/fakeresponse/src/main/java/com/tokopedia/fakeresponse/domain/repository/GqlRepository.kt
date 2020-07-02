package com.tokopedia.fakeresponse.domain.repository

import androidx.sqlite.db.SimpleSQLiteQuery
import com.tokopedia.fakeresponse.db.dao.GqlDao
import com.tokopedia.fakeresponse.db.entities.GqlRecord

class GqlRepository(val dao: GqlDao) : BaseRepository {

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

    fun getGqlQueryResponse(gqlQuery: String, enable: Boolean): GqlRecord {
        return dao.getRecordFromGqlQuery(gqlQuery, enable)
    }

    fun getGqlRecord(id: Int): GqlRecord {
        return dao.getRecordFromGqlQuery(id)
    }

    fun getGqlRecords(ids:List<Int>):List<GqlRecord>{
        return dao.getRecordFromGqlQuery(ids)
    }

    fun updateResponse(gqlRecord: GqlRecord) {
        return dao.updateGql(gqlRecord)
    }

    fun getLastId(): Int {
        return dao.getLastId()
    }

    fun search(tag: String? = null, response: String? = null): List<GqlRecord> {

        val items = arrayListOf<String>()

        if (!tag.isNullOrEmpty()) {
            items.add(" customTag LIKE '%$tag%' ")
        }
        if (!response.isNullOrEmpty()) {
            items.add(" response LIKE '%$response%' ")
        }

        val queryBuilder = StringBuilder()
        items.forEach {
            if (!queryBuilder.isEmpty()) {
                queryBuilder.append(" OR ")
            }
            queryBuilder.append(it)
        }

        if (queryBuilder.isEmpty()) {
            return emptyList()
        }

        val query = SimpleSQLiteQuery("SELECT * FROM GqlRecord WHERE $queryBuilder ORDER BY updatedAt DESC")
        return dao.searchRecords(query = query)
    }
}