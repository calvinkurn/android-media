package com.rahullohra.fakeresponse.domain.usecases

import com.rahullohra.fakeresponse.ResponseItemType
import com.rahullohra.fakeresponse.domain.repository.LocalRepository
import com.rahullohra.fakeresponse.domain.repository.RestRepository

class UpdateGqlUseCase constructor(
    val repository: LocalRepository,
    val restRepository: RestRepository
) : BaseUseCase<LocalRepository>(repository) {

    suspend fun toggle(recordId: Int, enable: Boolean, responseItemType: ResponseItemType) {
        when (responseItemType) {
            ResponseItemType.REST -> restRepository.toggleRestRecord(recordId, enable)
            else -> repository.toggleGqlRecord(recordId, enable)
        }
    }

    suspend fun deleteAllRecords() {
        repository.deleteAllRecords()
    }
}