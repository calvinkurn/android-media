package com.tokopedia.accountprofile.changephonenumber.usecase

import com.tokopedia.accountprofile.changephonenumber.data.GetWarningDataModel
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.coroutines.usecase.RestRequestUseCase
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import java.lang.reflect.Type
import javax.inject.Inject

class GetWarningUseCase @Inject constructor(
    private val restRepository: RestRepository,
    val userSession: UserSessionInterface
) : RestRequestUseCase(restRepository) {

    private val url = GET_WARNING
    private val params = mapOf(
        PARAM_OS_TYPE to OS_TYPE_ANDROID
    )

    override suspend fun executeOnBackground(): Map<Type, RestResponse?> {
        val restRequest = RestRequest.Builder(url, GetWarningDataModel::class.java)
            .setRequestType(RequestType.GET)
            .setQueryParams(params)
            .build()
        restRequestList.clear()
        restRequestList.add(restRequest)
        return restRepository.getResponses(restRequestList)
    }

    companion object {
        private val GET_WARNING = "${TokopediaUrl.getInstance().ACCOUNTS}api/v1/change-msisdn/get-warning"
        private const val PARAM_OS_TYPE = "theme"
        private const val OS_TYPE_ANDROID = "mobile"
    }
}
