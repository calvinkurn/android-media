package com.tokopedia.fakeresponse.domain.usecases

import com.tokopedia.fakeresponse.Preference
import com.tokopedia.fakeresponse.SortBy
import com.tokopedia.fakeresponse.data.models.ResponseListData
import com.tokopedia.fakeresponse.db.entities.GqlRecord
import com.tokopedia.fakeresponse.db.entities.RestRecord
import com.tokopedia.fakeresponse.db.entities.toResponseListData
import com.tokopedia.fakeresponse.domain.repository.GqlRepository
import com.tokopedia.fakeresponse.domain.repository.RestRepository

class ShowRecordsUseCase constructor(val gqlRepository: GqlRepository, val restRepository: RestRepository) :
        BaseUseCase<GqlRepository>(gqlRepository) {

    suspend fun getAllQueries(): List<ResponseListData> {
        val list = mutableListOf<ResponseListData>()

        list.addAll(gqlRepository.getAllGql()
                .mapNotNull { it.toResponseListData() })

        list.addAll(restRepository.getAll()
                .mapNotNull { it.toResponseListData() })

        @SortBy
        val sortOrder = Preference.getSortBy()
        if (sortOrder == SortBy.TIME_DESC) {
            list.sortWith(object : Comparator<ResponseListData> {
                override fun compare(o1: ResponseListData?, o2: ResponseListData?): Int {
                    if (o1 != null && o2 != null)
                        return o2.updatedAt.compareTo(o1.updatedAt)
                    return 0
                }
            })
        }

        return list
    }

    fun search(url: String? = null, tag: String? = null, response: String? = null): List<ResponseListData> {
        val list = mutableListOf<ResponseListData>()
        list.addAll(gqlRepository.search(tag, response)
                .mapNotNull { it.toResponseListData() })
        list.addAll(restRepository.search(url, tag, response)
                .mapNotNull { it.toResponseListData() })
        return list
    }

    fun getGqlRecords(ids: List<Int>): List<GqlRecord> {
        return gqlRepository.getGqlRecords(ids)
    }

    fun getRestRecords(ids: List<Int>): List<RestRecord> {
        return restRepository.getRestRecords(ids)
    }

    suspend fun deleteAllRecords() {
        gqlRepository.deleteAllRecords()
        restRepository.deleteAllRecords()
    }
}