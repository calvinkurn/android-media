package com.rahullohra.fakeresponse.domain.usecases

import com.rahullohra.fakeresponse.ResponseListData
import com.rahullohra.fakeresponse.domain.repository.LocalRepository

class ShowGqlUseCase constructor(val repository: LocalRepository) :
    BaseUseCase<LocalRepository>(repository) {

    suspend fun getAllQueries(): List<ResponseListData> {
        return repository.getAllGql()
            .map { ResponseListData(it.id!!, it.gqlOperationName, it.enabled) }
    }

    suspend fun deleteAllRecords() {
        repository.deleteAllRecords()
    }
}