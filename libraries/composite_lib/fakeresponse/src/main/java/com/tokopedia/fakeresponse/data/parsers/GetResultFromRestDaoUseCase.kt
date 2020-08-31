package com.tokopedia.fakeresponse.data.parsers

import com.tokopedia.fakeresponse.domain.repository.RestRepository

class GetResultFromRestDaoUseCase(val repository: RestRepository) {

    fun getResponseFromDao(formattedUrl: String, method:String, enabled: Boolean): String? {
        return repository.getResponse(formattedUrl,method, enabled).response
    }
}