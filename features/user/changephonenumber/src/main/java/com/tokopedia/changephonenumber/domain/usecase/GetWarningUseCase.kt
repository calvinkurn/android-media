package com.tokopedia.changephonenumber.domain.usecase

import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.coroutines.usecase.RestRequestUseCase
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.changephonenumber.data.GetWarningDataModel
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import java.lang.reflect.Type
import javax.inject.Inject

class GetWarningUseCase @Inject constructor(
    private val restRepository: RestRepository,
    val userSession: UserSessionInterface
) : RestRequestUseCase(restRepository) {

    private val url = BASE_URL + GET_WARNING
    private val params = mapOf(
        PARAM_OS_TYPE to OS_TYPE_ANDROID
    )

    private val headers = mapOf(
        KEY_AUTHORIZATION to BEARER + " " + userSession.accessToken
    )

    override suspend fun executeOnBackground(): Map<Type, RestResponse?> {
        val restRequest = RestRequest.Builder(url, GetWarningDataModel::class.java)
            .setRequestType(RequestType.GET)
            .setQueryParams(params)
            .setHeaders(headers)
            .build()
        restRequestList.clear()
        restRequestList.add(restRequest)
        return restRepository.getResponses(restRequestList)
    }

    companion object {
        private var BASE_URL = TokopediaUrl.getInstance().ACCOUNTS
        private const val GET_WARNING = "api/v1/change-msisdn/get-warning"
        private const val PARAM_OS_TYPE = "theme"
        private const val OS_TYPE_ANDROID = "mobile"

        private const val KEY_AUTHORIZATION = "Authorization"
        private const val BEARER = "Bearer"
    }
}