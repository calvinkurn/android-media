package com.rahullohra.fakeresponse.domain.usecases

import com.rahullohra.fakeresponse.ResponseListData
import com.rahullohra.fakeresponse.db.entities.toResponseListData
import com.rahullohra.fakeresponse.domain.repository.LocalRepository
import com.rahullohra.fakeresponse.domain.repository.RestRepository

class ShowRecordsUseCase constructor(val repository: LocalRepository, val restRepository: RestRepository) :
        BaseUseCase<LocalRepository>(repository) {

    suspend fun getAllQueries(): List<ResponseListData> {
        val list = mutableListOf<ResponseListData>()

        list.addAll(repository.getAllGql()
                .mapNotNull { it.toResponseListData() })

        list.addAll(restRepository.getAll()
                .mapNotNull { it.toResponseListData() })

        return list
    }

    suspend fun deleteAllRecords() {
        repository.deleteAllRecords()
        restRepository.deleteAllRecords()
    }
}