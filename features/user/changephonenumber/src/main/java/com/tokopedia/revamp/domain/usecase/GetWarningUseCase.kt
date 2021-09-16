package com.tokopedia.revamp.domain.usecase

import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.coroutines.usecase.RestRequestUseCase
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.revamp.data.GetWarningDataModel
import java.lang.reflect.Type
import javax.inject.Inject

class GetWarningUseCase @Inject constructor(
    private val restRepository: RestRepository
) : RestRequestUseCase(restRepository) {

    private val url = ""
    private val params = mutableListOf(
        PARAM_OS_TYPE to OS_TYPE_ANDROID
    )

    override suspend fun executeOnBackground(): Map<Type, RestResponse?> {
        val restRequest = RestRequest.Builder(url, GetWarningDataModel::class.java)
            .setRequestType(RequestType.POST)
            .setBody(params)
            .build()
        restRequestList.clear()
        restRequestList.add(restRequest)
        return restRepository.getResponses(restRequestList)
    }

    companion object {
        private const val PARAM_OS_TYPE = "theme"
        private const val OS_TYPE_ANDROID = "mobile"
    }
}