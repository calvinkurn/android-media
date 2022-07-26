package com.tokopedia.profilecompletion.addpin.data.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.profilecompletion.addpin.data.CreatePinV2Param
import com.tokopedia.profilecompletion.changepin.data.model.CreatePinV2Response
import javax.inject.Inject

class CreatePinV2UseCase @Inject constructor(
    @ApplicationContext val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<CreatePinV2Param, CreatePinV2Response>(dispatcher.io) {

    override suspend fun execute(params: CreatePinV2Param): CreatePinV2Response {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String {
        return """
	    mutation createPinV2(${'$'}pin_token: String!, ${'$'}validate_token: String!) {
            create_pin_v2(input: {
                pin_token: ${'$'}pin_token,
                validate_token: ${'$'}validate_token
            }) {
                success
                errors {
                  name
                  message
                }
	      }
	    }
	""".trimIndent()
    }
}