package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.data.model.*
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhome.common.constant.RamadhanThematicUrl
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class RamadhanKetupatUseCase @Inject constructor(private val repository: RestRepository): UseCase<String>() {

    override suspend fun executeOnBackground(): String{
        val cacheStrategy = RestCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        val restRequest = RestRequest.Builder(RamadhanThematicUrl.KETUPAT_JSON_URL, Any::class.java)
                .setRequestType(RequestType.GET)
                .setCacheStrategy(cacheStrategy)
                .build()
        return getJsonString(repository.getResponse(restRequest))
    }

    private fun getJsonString(response: RestResponse): String {
        if (response.isError) {
            throw MessageErrorException(response.errorBody)
        }
        return response.getData<Any>().toString()
    }

}