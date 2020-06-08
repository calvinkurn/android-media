package com.tokopedia.fakeresponse.data.parsers

import com.tokopedia.fakeresponse.domain.repository.GqlRepository

class GetResultFromDaoUseCase(val repository: GqlRepository) {

    fun getResponseFromDao(gqlOperationName: String): String? {
        return repository.getGqlQueryResponse(gqlOperationName, true).response
    }
}