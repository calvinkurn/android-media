package com.tokopedia.common.network.coroutines.usecase

import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.common.network.domain.RestRequestSupportInterceptorUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.UseCase
import org.xml.sax.ErrorHandler
import java.io.IOException

import java.lang.reflect.Type

/**
 * Rest api call UseCase
 *
 * @See [RestRequestSupportInterceptorUseCase] class for add custom interceptor
 */
open class RestRequestUseCase(private val restRepository: RestRepository):
        UseCase<Map<Type, RestResponse?>>() {
    var restRequestList = mutableListOf<RestRequest>()

    override suspend fun executeOnBackground(): Map<Type, RestResponse?> {
        if (restRequestList.isEmpty()){
            throw RuntimeException("Please set valid rest request query parameter " +
                    "before executing the use-case")
        }

        return restRepository.getResponses(restRequestList) ?: throw IOException()
    }

}
