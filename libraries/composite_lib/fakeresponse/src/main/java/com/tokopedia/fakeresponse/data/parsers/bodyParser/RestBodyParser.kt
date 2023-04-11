package com.tokopedia.fakeresponse.data.parsers.bodyParser

import com.tokopedia.fakeresponse.data.parsers.GetResultFromRestDaoUseCase
import com.tokopedia.fakeresponse.db.dao.RestDao
import com.tokopedia.fakeresponse.domain.repository.RestRepository
import okhttp3.HttpUrl

class RestBodyParser(restDao: RestDao) : BodyParser {

    val repository = RestRepository(restDao)
    val useCase = GetResultFromRestDaoUseCase(repository)

    fun getFakeResponse(httpUrl: HttpUrl, method: String): String? {
        val fullUrl = httpUrl.toUri().toString()
        val fakeResponse = getFakeResponseFromDb(fullUrl, method)
        return fakeResponse
    }

    fun getFakeResponseFromDb(url: String, method: String): String? {
        return useCase.getResponseFromDao(url, method, true)
    }

}