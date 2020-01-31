package com.rahullohra.fakeresponse.domain.usecases

import com.rahullohra.fakeresponse.ResponseItemType
import com.rahullohra.fakeresponse.ResponseListData
import com.rahullohra.fakeresponse.domain.repository.LocalRepository
import com.rahullohra.fakeresponse.domain.repository.RestRepository

class ShowRecordsUseCase constructor(val repository: LocalRepository, val restRepository: RestRepository) :
    BaseUseCase<LocalRepository>(repository) {

    suspend fun getAllQueries(): List<ResponseListData> {
        val list = mutableListOf<ResponseListData>()

        list.addAll(repository.getAllGql()
            .map { ResponseListData(it.id!!, it.gqlOperationName, it.enabled, responseType = ResponseItemType.GQL) })

        list.addAll(restRepository.getAll()
            .map { ResponseListData(it.id!!, it.url, it.enabled, responseType = ResponseItemType.REST) })

        return list
    }

    suspend fun deleteAllRecords() {
        repository.deleteAllRecords()
        restRepository.deleteAllRecords()
    }
}