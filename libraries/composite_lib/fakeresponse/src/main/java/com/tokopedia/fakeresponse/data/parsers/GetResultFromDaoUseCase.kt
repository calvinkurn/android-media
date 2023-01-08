package com.tokopedia.fakeresponse.data.parsers

import com.tokopedia.fakeresponse.db.entities.GqlRecord
import com.tokopedia.fakeresponse.domain.repository.GqlRepository

class GetResultFromDaoUseCase(val repository: GqlRepository) {

    fun getResponseFromDao(gqlOperationName: String): GqlRecord? {
        val gqlRecord = repository.getGqlQueryResponse(gqlOperationName, true)
        return gqlRecord
    }
}