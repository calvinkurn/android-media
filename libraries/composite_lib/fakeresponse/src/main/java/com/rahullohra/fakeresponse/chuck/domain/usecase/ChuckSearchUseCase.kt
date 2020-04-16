package com.rahullohra.fakeresponse.chuck.domain.usecase

import com.rahullohra.fakeresponse.chuck.TransactionEntity
import com.rahullohra.fakeresponse.chuck.domain.repository.ChuckRepository

class ChuckSearchUseCase(val repository: ChuckRepository) {

    fun performSearch(responseBody: String? = null,
                      url: String? = null,
                      requestBody: String? = null,
                      id: Long? = null): List<TransactionEntity> {
        return repository.search(responseBody, url, requestBody, id)
    }
}