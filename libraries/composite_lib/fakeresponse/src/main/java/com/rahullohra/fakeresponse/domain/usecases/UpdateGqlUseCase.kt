package com.rahullohra.fakeresponse.domain.usecases

import com.rahullohra.fakeresponse.domain.repository.LocalRepository

class UpdateGqlUseCase constructor(val repository: LocalRepository) : BaseUseCase<LocalRepository>(repository)  {

    suspend fun toggleGql(gqlRecord: Int, enable: Boolean) {
        repository.toggleGqlRecord(gqlRecord, enable)
    }

    suspend fun deleteAllRecords() {
        repository.deleteAllRecords()
    }
}