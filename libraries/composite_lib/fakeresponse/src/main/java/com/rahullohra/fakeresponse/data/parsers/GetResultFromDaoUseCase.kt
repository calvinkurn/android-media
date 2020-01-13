package com.rahullohra.fakeresponse.data.parsers

import com.rahullohra.fakeresponse.domain.repository.LocalRepository

class GetResultFromDaoUseCase(val repository: LocalRepository) {

    fun getResponseFromDao(gqlOperationName: String): String? {
        return repository.getGqlQueryResponse(gqlOperationName, true).response
    }
}