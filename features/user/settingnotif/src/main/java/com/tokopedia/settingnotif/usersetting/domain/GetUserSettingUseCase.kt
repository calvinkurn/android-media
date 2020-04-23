package com.tokopedia.settingnotif.usersetting.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.settingnotif.usersetting.data.pojo.UserNotificationResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

open class GetUserSettingUseCase @Inject constructor(
        private val repository: GraphqlRepository,
        private val graphQuery: String
) : UseCase<UserNotificationResponse>() {

    override suspend fun executeOnBackground(): UserNotificationResponse {
        val request = GraphqlRequest(graphQuery, UserNotificationResponse::class.java)
        val response = repository.getReseponse(listOf(request))
        val error = response.getError(UserNotificationResponse::class.java)

        if (error == null || error.isEmpty()) {
            return response.getData(
                    UserNotificationResponse::class.java
            ) as UserNotificationResponse
        } else {
            throw MessageErrorException(
                    error.mapNotNull { it.message }.joinToString(separator = ", ")
            )
        }
    }

}