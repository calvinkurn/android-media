package com.tokopedia.settingnotif.usersetting.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.settingnotif.usersetting.data.pojo.setusersetting.SetUserSettingResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

open class SetUserSettingUseCase @Inject constructor(
        private val repository: GraphqlRepository,
        private val graphQuery: String
) : UseCase<SetUserSettingResponse>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): SetUserSettingResponse {
        require(params.paramsAllValueInString.isNotEmpty())

        val request = GraphqlRequest(graphQuery, SetUserSettingResponse::class.java, params.parameters)
        val response = repository.getReseponse(listOf(request))
        val error = response.getError(SetUserSettingResponse::class.java)

        if (error == null || error.isEmpty()) {
            return response.getData(
                    SetUserSettingResponse::class.java
            ) as SetUserSettingResponse
        } else {
            throw MessageErrorException(
                    error.mapNotNull { it.message }.joinToString(separator = ", ")
            )
        }
    }

    companion object {
        private const val PARAM_TYPE = "type"
        private const val PARAM_DATA = "data"

        fun params(
                notificationType: String,
                updatedSettingIds: List<Map<String, Any>>
        ): RequestParams {
            return RequestParams.create().apply {
                putString(PARAM_TYPE, notificationType)
                putObject(PARAM_DATA, updatedSettingIds)
            }
        }
    }

}