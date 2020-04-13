package com.rahullohra.fakeresponse.domain.usecases

import com.rahullohra.fakeresponse.data.models.ResponseItemType
import com.rahullohra.fakeresponse.domain.repository.GqlRepository
import com.rahullohra.fakeresponse.domain.repository.RestRepository

class UpdateGqlUseCase constructor(
        val repository: GqlRepository,
        val restRepository: RestRepository
) : BaseUseCase<GqlRepository>(repository) {

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