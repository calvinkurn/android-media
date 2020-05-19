package com.tokopedia.fakeresponse.chuck.domain.usecase

import com.tokopedia.fakeresponse.chuck.TransactionEntity
import com.tokopedia.fakeresponse.chuck.domain.repository.ChuckRepository

class ChuckSearchUseCase(val repository: ChuckRepository) {

    fun performSearch(responseBody: String? = null,
                      url: String? = null,
                      requestBody: String? = null,
                      id: Long? = null): List<TransactionEntity> {
        return repository.search(responseBody, url, requestBody, id)
    }
}