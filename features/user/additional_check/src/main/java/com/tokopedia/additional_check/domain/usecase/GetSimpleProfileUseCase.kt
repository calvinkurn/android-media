package com.tokopedia.additional_check.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.additional_check.data.GetSimpleProfileResponse
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

class GetSimpleProfileUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Unit, GetSimpleProfileResponse>(dispatcher.io) {

    override suspend fun execute(params: Unit): GetSimpleProfileResponse {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String = """
        query userProfile {
          profile {
            full_name
            profilePicture
            phone
            email
          }
        } """.trimIndent()
}