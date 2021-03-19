package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.coroutines.usecase.RestRequestUseCase
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.sellerhome.common.constant.RamadhanThematicUrl
import java.lang.reflect.Type
import javax.inject.Inject

class RamadhanKetupatUseCase @Inject constructor(private val repository: RestRepository): RestRequestUseCase(repository) {

    override suspend fun executeOnBackground(): Map<Type, RestResponse?> {
        val restRequest = RestRequest.Builder(RamadhanThematicUrl.KETUPAT_JSON_URL, String::class.java)
                .setRequestType(RequestType.GET)
                .build()
        restRequestList.clear()
        restRequestList.add(restRequest)
        return repository.getResponses(restRequestList)
    }

}