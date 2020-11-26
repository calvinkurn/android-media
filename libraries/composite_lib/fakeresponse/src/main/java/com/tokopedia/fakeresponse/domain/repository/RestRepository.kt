package com.tokopedia.fakeresponse.domain.repository

import androidx.sqlite.db.SimpleSQLiteQuery
import com.tokopedia.fakeresponse.db.dao.RestDao
import com.tokopedia.fakeresponse.db.entities.RestRecord

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

    fun delete(id:Int) {
        return dao.delete(id)
    }

    fun toggleRestRecord(restRecord: Int, enable: Boolean) {
        return dao.toggle(restRecord, enable)
    }

    fun getResponse(url: String, method: String, enable: Boolean): RestRecord? {
        return dao.getRestResponse(url, method, enable)
    }

    fun updateResponse(restRecord: RestRecord) {
        return dao.update(restRecord)
    }

    fun getResponse(id: Int): RestRecord {
        return dao.getRestResponse(id)
    }

    fun getRestRecords(ids: List<Int>): List<RestRecord> {
        return dao.getRestResponse(ids)
    }

    fun getLastId(): Int {
        return dao.getLastId()
    }

    fun search(url: String?, tag: String? = null, response: String? = null): List<RestRecord> {

        val items = arrayListOf<String>()

        if (!tag.isNullOrEmpty()) {
            items.add(" customTag LIKE '%$tag%' ")
        }
        if (!url.isNullOrEmpty()) {
            items.add(" url LIKE '%$tag%' ")
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

        val query = SimpleSQLiteQuery("SELECT * FROM RestRecord WHERE $queryBuilder ORDER BY updatedAt DESC")
        return dao.searchRecords(query = query)
    }

}