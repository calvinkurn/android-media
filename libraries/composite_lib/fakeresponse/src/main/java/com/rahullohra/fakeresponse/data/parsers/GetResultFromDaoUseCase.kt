package com.rahullohra.fakeresponse.data.parsers

import com.rahullohra.fakeresponse.domain.repository.GqlRepository

class GetResultFromDaoUseCase(val repository: GqlRepository) {

    fun getResponseFromDao(gqlOperationName: String): String? {
        return repository.getGqlQueryResponse(gqlOperationName, true).response
    }
}